import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming._
import org.apache.spark.streaming.twitter.TwitterUtils

object TwitterStreaming {
  def main(args: Array[String]): Unit = {

    val apiKey = "*******"
    val apiSecret = "*******"
    val accessToken = "*******"
    val accessTokenSecret = "*******"

    System.setProperty("twitter4j.oauth.consumerKey", apiKey)
    System.setProperty("twitter4j.oauth.consumerSecret", apiSecret)
    System.setProperty("twitter4j.oauth.accessToken", accessToken)
    System.setProperty("twitter4j.oauth.accessTokenSecret", accessTokenSecret)

    val slideInterval = Seconds(20)
    val windowLength  = Minutes(2)
    val hashTag       = "#starwars"
    val wordRegex     = "[^\\W\\d_]+".r

    val conf = new SparkConf().setAppName("TwitterStreaming").setMaster("local[*]")
    val sc   = new SparkContext(conf)
    val ssc  = new StreamingContext(sc, slideInterval)
    val twitterStream = TwitterUtils.createStream(ssc, None, Array(hashTag))

    twitterStream.map(_.getText.toLowerCase)
      .window(windowLength, slideInterval)
      .foreachRDD(rdd => {
        println("Count: " + rdd.count)
        rdd.flatMap(wordRegex.findAllIn(_).toList)
          .map((_, 1)).reduceByKey(_ + _)
          .sortBy(_._2, false)
          .take(10).foreach(println)
      })

    ssc.start
    ssc.awaitTermination
  }
}
