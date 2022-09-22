package org.example

import org.apache.spark.sql.{SaveMode, SparkSession, functions}
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions.{avg, col, collect_set, explode, row_number, to_timestamp, udf}
import org.apache.spark.sql.types.{DoubleType, LongType}


object App {

  // Convert String dollar to double euro. Ex: $1 -> 0.99
  def dollarToEuro = udf ( (s: String) =>
    s.take(1) match {
      case "$" => s.substring(1).toDouble * 0.9
      case _ => 0
    }
  )

  // Convert String to Double MegaBytes. Ex: 19M -> 19 || 999k -> 0.999
  def sizeToDouble = udf ( (s: String) =>
    s.takeRight(1) match {
      case "M" => s.dropRight(1).toDouble
      case "k" => s.dropRight(1).toDouble * 0.001
      case "G" => s.dropRight(1).toDouble * 1000
      case _ => 0
    }
  )


  def main(args: Array[String]): Unit = {
    // Creating a basic SparkSession.
    val spark = SparkSession
      .builder()
      .appName("Spark Challenge JoaoMacedo")
      .config("spark.master", "local")
      .getOrCreate()

    // Creating a Dataframe with App's and Average_Sentiment_Polarity.
    val df_1 = spark.read.option("header",true)
      .csv("google-play-store-apps/googleplaystore_user_reviews.csv")
      .withColumn("Sentiment_Polarity",col("Sentiment_Polarity").cast(DoubleType))
      .select("App", "Sentiment_Polarity")
      .na.fill(0) // Change default value null to 0 so it can be possible to calculate Sentiment_Polarity average.
      .groupBy("App").avg("Sentiment_Polarity") // Average of the column Sentiment_Polarity grouped by App name
      .withColumnRenamed("avg(Sentiment_Polarity)", "Average_Sentiment_Polarity")

    // Read googleplaystore.csv as a Dataframe and obtain all Apps
    // with a "Rating" greater or equal to 4.0 sorted in descending order.
    val df_2 = spark.read.option("header",true)
      .csv("google-play-store-apps/googleplaystore.csv")
      .withColumn("Rating",col("Rating").cast(DoubleType))
      .na.fill(0)
      .filter(col("Rating")>=4 && col("Rating").isNotNull /*&& col("Rating")<=5*/)
      // There is a row that has one value missing so in rating cloumn appears
      // that it's rating is 19 (it's reviews) and that's wrong.
      .sort(col("Rating").desc)
      .select("App","Rating")
      //Save as a CSV (delimiter: "§") named "best_apps.csv"
      .coalesce(1)
      .write
      .mode(SaveMode.Overwrite)
      .option("header",true)
      .option("delimiter","§")
      .csv("google-play-store-apps/best_apps")

    // Convert Reviews into Long and put 0 as default value
    val df_31 = spark.read.option("header",true)
      .csv("google-play-store-apps/googleplaystore.csv")
      .withColumn("Reviews",col("Reviews").cast(LongType))
      .na.fill(0,Array("Reviews"))

    // Creating a window partition to remove App duplicates
    // The remains App row's will be the ones with more reviews
    val w2 = Window.partitionBy("App").orderBy(col("Reviews").desc)
    val df3 = df_31.withColumn("row",row_number.over(w2)).where(col("row")=== 1)
      .drop("row","Category")

    // Using collect_set to aggregate duplicate Apps and collect all it's categories without duplicates
    // Then, join to previous dataframe in order to have unique Apps with all Categories and the remaining values from the top review row
    val df_3 = df_31.groupBy("App").agg(collect_set("Category").alias("Category"))
      .join(df3,"App")
      .withColumnRenamed("Category","Categories")
      .withColumn("Rating",col("Rating").cast(DoubleType))
      .withColumnRenamed("Content Rating","Content_Rating")
      .withColumnRenamed("Last Updated","Last_Updated")
      .withColumnRenamed("Current Ver","Current_Version")
      .withColumnRenamed("Android Ver","Minimum_Android_Version")
      .withColumn("Size", sizeToDouble(col("Size")))
      .withColumn("Price", dollarToEuro(col("Price")))
      .withColumn("Genres", functions.split(col("Genres"), ";").cast("array<String>"))
      .withColumn("Last_Updated",to_timestamp(col("Last_Updated"),"MMMM d, yyyy"))

    // Join df_3 and df_1 and
    // Save it as a parquet file with gzip compression with the name "googleplaystore_cleaned"
    val df4 = df_3.join(df_1,"App")
      .repartition(1)
      .write
      .mode(SaveMode.Overwrite)
      .option("compression", "gzip")
      .parquet("google-play-store-apps/googleplaystore_cleaned")

    // Transform Genres array in different rows
    // Then join to df_1 because Average_Sentiment_Polarity
    // Calculate average of Rating and Average_Sentiment_Polarity
    val df_4aux1 = df_3.select(col("App"),col("Rating"),explode(col("Genres")).alias("Genres"))
      .join(df_1,"App")
      .groupBy(col("Genres")).agg(avg("Rating"), avg("Average_Sentiment_Polarity"))
      .withColumnRenamed("avg(Rating)","Rating")
      .withColumnRenamed("avg(Average_Sentiment_Polarity)","Average_Sentiment_Polarity")

    // Lastly, use df_3 to do the counting Apps by Genre and join do previews df
    val df_4 = df_3.select(col("App"),col("Rating"),explode(col("Genres")).alias("Genres"))
      .groupBy("Genres").count()
      .join(df_4aux1,"Genres")
      .withColumnRenamed("Genres","Genre")
      .repartition(1)
      .write
      .mode(SaveMode.Overwrite)
      .option("compression", "gzip")
      .parquet("google-play-store-apps/googleplaystore_metrics")

    /* COMMENTS:
            Como já mencionei nos comentário acima, há uma linha que tem em falta um valor o que faz
            com que os restantes valores estejam nas colunas erradas.
            Não sei o que gostariam que eu fizesse com essa linha.
            
            Outra situação é a dos doubles que têm os valores como por exemplo 9.0,
            e nos exemplos esses 0's não existem mas eu não os retirei.
     */


  }
}
