import org.apache.spark.streaming.twitter.TwitterUtils
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.{Minutes, Seconds, StreamingContext}

object TwitterStreaming {
  def main(args: Array[String]): Unit = {

    val apiKey = "8dD3v2yFgEdfIgzYSPKlhL9vW"
    val apiSecret = "quURcrkUNOG6O7xnUlbPzQAfw8gxncYSoIT26v4hy0zhQG7mqQ"
    val accessToken = "317037418-FmxESuhNiEGlG3sUDSim0DJGl73RcOCfaZfGCIbl"
    val accessTokenSecret = "faAl81LdjN58b3cNHIUMCFiKzCMbKpR69SFzzmVydMfDL"

    System.setProperty("twitter4j.oauth.consumerKey", apiKey)
    System.setProperty("twitter4j.oauth.consumerSecret", apiSecret)
    System.setProperty("twitter4j.oauth.accessToken", accessToken)
    System.setProperty("twitter4j.oauth.accessTokenSecret", accessTokenSecret)

    val slideInterval = Seconds(20)
    val windowLength  = Minutes(2)

    val conf = new SparkConf().setMaster("local[*]").setAppName("spark-twitter-streaming")
    val sc = new SparkContext(conf)
    val ssc = new StreamingContext(sc, slideInterval)
    val twitterStream = TwitterUtils.createStream(ssc, None)

    // Split the twitterStream on space and extract hashtags
    val hashTags = twitterStream.flatMap(status => status.getText.split(" ").filter(_.startsWith("#")))

    // Get the top hashtags over the previous 60 sec window
    val topCounts60 = hashTags.map((_, 1)).reduceByKeyAndWindow(_ + _, windowLength)
      .map { case (topic, count) => (count, topic) }
      .transform(_.sortByKey(false))

    // print tweets in the currect DStream
    twitterStream.print()

    // Print popular hashtags
    topCounts60.foreachRDD(rdd => {
      val topList = rdd.take(10)
      println(s"\nPopular topics in last 60 seconds (${rdd.count()} total):")
      topList.foreach { case (count, tag) => println(s"${tag} (${count} tweets)") }
    })

    ssc.start()
    ssc.awaitTerminationOrTimeout(Minutes(1).milliseconds)
  }

}
