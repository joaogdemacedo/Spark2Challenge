# Spark2 Challenge - Scala
Developing a Spark 2 application (in Scala) that processes 2 CSV files.

## Introduction
For this challenge you'll have to develop a Spark 2 application (**in Scala**) that processes 2 CSV files ([google-play-store-apps.zip](https://github.com/bdu-xpand-it/BDU-Recruitment-Challenges/blob/master/google-play-store-apps.zip)).

For this project you should use Maven for dependency management.

>### Important Info
>* When writing code and structuring the project try to produce clean and concise implementations.
>* Produce code that does not need adaptations to run (self-contained and runnable).
>* Bottom line try to produce a well structured and tested project.

In case of any interpretation doubts or detect inconsistencies in the supplied material, you have full autonomy to make any decision you consider necessary as long you clearly detail and present you justification.

You should submit a compilable and executable project solution as well any artifact and documentation that you consider relevant for the correct evaluation of this challenge.

## Datasets
Both CSV file contain scraped data from the Google Play Store.

### googleplaystore.csv
This dataset contains all the information about the the mobile applications registered in the Google Play Store.

**Columns**
* **App** - Application name
* **Category** - Category the app belongs to
* **Rating** - Overall user rating of the app (as when scraped)
* **Reviews** - Number of user reviews for the app (as when scraped)
* **Size** - Size of the app (as when scraped)
* **Installs** - Number of user downloads/installs for the app (as when scraped)
* **Type** - Paid or Free
* **Price** - Price of the app (as when scraped)
* **Content Rating** - Age group the app is targeted at - Children / Mature 21+ / Adult
* **Genres** - An app can belong to multiple genres (apart from its main category). For eg, a musical family game will belong to Music, Game, Family genres.
* **Last Updated** - Date when the app was last updated on Play Store (as when scraped)
* **Current Ver** - Current version of the app available on Play Store (as when scraped)
* **Android Ver** - Min required Android version (as when scraped)

#### Example/Sample 1

App	|	Category	|	Rating	|	Reviews	|	Size	|	Installs	|	Type	|	Price	|	Content&#160;Rating	|	Genres	|	Last Updated	|	Current Ver	|	Android&#160;Ver
--- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- 
Photo Editor	|	ART_AND_DESIGN	|	4.1	|	159	|	19M	|	10,000+	|	Free	|	0	|	Everyone	|	Art & Design	|	January 7, 2018	|	1.0.0	|	4.0.3 and up
Coloring book	|	ART_AND_DESIGN	|	3.9	|	967	|	14M	|	500,000+	|	Free	|	0	|	Everyone	|	Art&#160;&&#160;Design;Pretend&#160;Play	|	January&#160;15,&#160;2018	|	2.0.0	|	4.0.3&#160;and&#160;up
U&#160;Launcher&#160;Lite.	|	ART_AND_DESIGN	|	4.7	|	87510	|	8.7M	|	5,000,000+	|	Free	|	0	|	Everyone	|	Art & Design	|	August 1, 2018	|	1.2.4	|	4.0.3 and up
Sketch	|	ART_AND_DESIGN	|	4.5	|	215644	|	25M	|	50,000,000+	|	Free	|	0	|	Teen	|	Art & Design	|	June 8, 2018	|	Varies&#160;with&#160;device	|	4.2 and up
Pixel Draw	|	ART_AND_DESIGN	|	4.3	|	967	|	2.8M	|	100,000+	|	Free	|	0	|	Everyone	|	Art & Design;Creativity	|	June 20, 2018	|	1.1	|	4.4 and up
Clash of Clans	|	GAME	|	4.6	|	44893888	|	98M	|	100,000,000+	|	Free	|	0	|	Everyone 10+	|	Strategy	|	July 15, 2018	|	10.322.16	|	4.1 and up
Clash of Clans	|	FAMILY	|	4.6	|	44881447	|	98M	|	100,000,000+	|	Free	|	0	|	Everyone&#160;10+	|	Strategy	|	July 15, 2018	|	10.322.16	|	4.1 and up

### googleplaystore_user_reviews.csv
This dataset contains all the information about the users' reviews for the mobiles apps registered in the Google Play Store.

**Columns**
* **App** - Name of app
* **Translated_Review** - User review (Preprocessed and translated to English)
* **Sentiment** - Positive/Negative/Neutral (Preprocessed)
* **Sentiment_Polarity** - Sentiment polarity score
* **Sentiment_Subjectivity** - Sentiment subjectivity score

#### Example/Sample 2
App	|	Translated_Review	|	Sentiment	|	Sentiment_Polarity	|	Sentiment_Subjectivity
--- | --- | :---: | ---: | ---:
10&#160;Best&#160;Foods&#160;for&#160;You	|	This&#160;help&#160;eating&#160;healthy&#160;exercise&#160;regular&#160;basis	|	Positive	|	0.25	|	0.28846153846153844
10 Best Foods for You	|	Best idea us	|	Positive	|	1.0	|	0.3
10 Best Foods for You	|	Amazing	|	Positive	|	0.6000000000000001	|	0.9
10 Best Foods for You	|	|	|	
10 Best Foods for You	|	Looking forward app,	|	Neutral	|	0.0	|	0.0

## Exercise
In this exercise you will be asked to develop a Spark basic application that performs some basic operations on both datasets and stores the final results in other files.

### Part 1
From _googleplaystore_user_reviews.csv_ create a Dataframe (df_1) with the following structure:

Column name	|	Data type	|	Default Value	|	Notes
--- | :---: | --- | --- 
App	|	String	|	|	|	
Average_Sentiment_Polarity	|	Double	|	0 (instead of NULL)	|	Average of the column Sentiment_Polarity grouped by App name

### Part 2
1. Read _googleplaystore.csv_ as a Dataframe and obtain all Apps with a **"Rating" greater or equal to 4.0** sorted in **descending** order.
2. Save that Dataframe as a **CSV** (delimiter: "§") named "best_apps.csv"

### Part 3
1. From _googleplaystore.csv_ create a Dataframe (df_3) with the structure from the table below

**Attention**

* **App** should be a **unique** value;
* In case of App duplicates, the column "Categories" of the resulting row should contain an **array with all the possible categories (without duplicates)** for that app (compare example 1 with 3);
* In case of App duplicates (for all columns except categories), the **remaining columns should have the same values as the ones on the row with the maximum number of reviews** (compare example 1 with 3).

Table with Dataframe's structure that you should do to produce the final Dataframe:

Column name	|	Data type	|	Default&#160;Value	|	**IMPORTANT NOTES**
--- | :---: | :---: | --- 
App	|	String	|	|	Remove duplicates
Categories	|	Array[String]	|	| Rename column
Rating	|	Double	|	null	|	
Reviews	|	Long	|	0	|	
Size	|	Double	|	null	|	Convert from string to double (value in MB). Attention - Not all values end in "M"
Installs	|	String	|	null	|	
Type	|	String	|	null	|	
Price	|	Double	|	null	|	Convert from string to double and present the value in euros (All values are in dollars) (Consider conversion rate: 1$ = 0.9€)
Content_Rating	|	String	|	null	|	Rename column from 'Content Rating'
Genres	|	Array[String]	|	null	|	Convert string to array of strings (delimiter: ";")
Last_Updated	|	Date	|	null	|	Convert string to date. Rename column from 'Last Updated'
Current_Version	|	String	|	null	|	Rename column from 'Current Ver'
Minimum_Android_Version	|	String	|	null	|	Rename column from 'Android Ver'

#### Example/sample 3


App	|	Categories	|	Rating	|	Reviews	|	Size	|	Installs	|	Type	|	Price	|	Content_Rating	|	Genres	|	Last_Updated	|	Current_Version	|	Minimum_Android_Version
--- |--- | :---: | :---: | :---: | --- | :---: | :---: | --- | --- | :---: | --- | --- 
Photo Editor	|	["ART_AND_DESIGN"]	|	4.1	|	159	|	19	|	10,000+	|	Free	|	0	|	Everyone	|	["Art & Design"]	|	2018-01-07 00:00:00	|	1.0.0	|	4.0.3
Coloring book	|	["ART_AND_DESIGN"]	|	3.9	|	967	|	14	|	500,000+	|	Free	|	0	|	Everyone	|	["Art&#160;&&#160;Design",&#160;"Pretend&#160;Play"]	|	2018-01-15 00:00:00	|	2.0.0	|	4.0.3
U&#160;Launcher&#160;Lite.	|	["ART_AND_DESIGN"]	|	4.7	|	87510	|	8.7	|	5,000,000+	|	Free	|	0	|	Everyone	|	["Art & Design"]	|	2018-08-01 00:00:00	|	1.2.4	|	4.0.3
Sketch	|	["ART_AND_DESIGN"]	|	4.5	|	215644	|	25	|	50,000,000+	|	Free	|	0	|	Teen	|	["Art & Design"]	|	2018-06-08 00:00:00	|	Varies&#160;with&#160;device	|	4.2
Pixel Draw	|	["ART_AND_DESIGN"]	|	4.3	|	967	|	2.8	|	100,000+	|	Free	|	0	|	Everyone	|	["Art & Design", "Creativity"]	|	2018-06-20 00:00:00	|	1.1	|	4.4
Clash of Clans	|	["GAME", "FAMILY"]	|	4.6	|	44893888	|	98	|	100,000,000+	|	Free	|	0	|	Everyone 10+	|	["Strategy"]	|	2018-06-15 00:00:00	|	10.322.16	|	4.1

### Part 4

1. Given the Dataframes produced by Exercise 1 and 3, **produce a Dataframe with all its information plus its 'Average_Sentiment_Polarity'** calculated in Exercise 1
2. Save the final Dataframe as a **parquet** file **with gzip compression** with the name "googleplaystore_cleaned"

#### Example/Sample 4

App	|	Categories	|	Rating	|	Reviews	|	Size	|	Installs	|	Type	|	Price	|	Content_Rating	|	Genres	|	Last_Updated	|	Current_Version	|	Minimum_Android_Version	|	Average_Sentiment_Polarity
--- |--- | :---: | :---: | :---: | --- | :---: | :---: | --- | --- | :---: | --- | --- | --- 
Photo Editor	|	["ART_AND_DESIGN"]	|	4.1	|	159	|	19	|	10,000+	|	Free	|	0	|	Everyone	|	["Art & Design"]	|	2018-01-07 00:00:00	|	1.0.0	|	4.0.3	|	6.83523
Coloring book	|	["ART_AND_DESIGN"]	|	3.9	|	967	|	14	|	500,000+	|	Free	|	0	|	Everyone	|	["Art&#160;&&#160;Design"&#160;,"Pretend&#160;Play"]	|	2018-01-15 00:00:00	|	2.0.0	|	4.0.3	| null
U&#160;Launcher&#160;Lite.	|	["ART_AND_DESIGN"]	|	4.7	|	87510	|	8.7	|	5,000,000+	|	Free	|	0	|	Everyone	|	["Art & Design"]	|	2018-08-01 00:00:00	|	1.2.4	|	4.0.3	|	null
Sketch	|	["ART_AND_DESIGN"]	|	4.5	|	215644	|	25	|	50,000,000+	|	Free	|	0	|	Teen	|	["Art & Design"]	|	2018-06-08 00:00:00	|	Varies&#160;with&#160;device	|	4.2	|	null
Pixel Draw	|	["ART_AND_DESIGN"]	|	4.3	|	967	|	2.8	|	100,000+	|	Free	|	0	|	Everyone	|	["Art & Design", "Creativity"]	|	2018-06-20 00:00:00	|	1.1	|	4.4	|	7.211111
Clash of Clans	|	[ "GAME", "FAMILY" ]	|	4.6	|	44893888	|	98	|	100,000,000+	|	Free	|	0	|	Everyone&#160;10+	|	["Strategy"]	|	2018-06-15 00:00:00	|	10.322.16	|	4.1	|	null

*Note: the Average_Sentiment_Polarity values in this example may not be correct*
### Part 5
1. Using df_3 create a new Dataframe (df_4) containing the **number of applications**, the **average rating** and the **average sentiment polarity by genre** and **save** it as a **parquet file with gzip compression** with the name "googleplaystore_metrics".
#### Example/Sample 5

Genre	|	Count	|	Average_Rating	|	Average_Sentiment_Polarity
--- | :---: | :---: | :---:
Art & Design	|	345	|	2.6	|	-2.1
Pretend Play	|	78	|	3.12222	|	null
Creativity	|	139	|	3.8	|	3.3332
Strategy	|	451	|	4.2	|	8.2