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
    val hashTag       = "#starwars"
    val wordRegex     = "[^\\W\\d_]+".r

    val conf = new SparkConf().setAppName("spark-twitter-streaming").setMaster("local[*]")
    val sc   = new SparkContext(conf)
    val ssc  = new StreamingContext(sc, slideInterval)
    val twitterStream = TwitterUtils.createStream(ssc, None, Array(hashTag))

    twitterStream.map(_.getText.toLowerCase)
      .window(windowLength, slideInterval)
      .foreachRDD(rdd => {
        println(s"Count: ${rdd.count}")
        rdd.flatMap(wordRegex.findAllIn(_).toList)
          .map((_, 1)).reduceByKey(_ + _)
          .sortBy(_._2, false)
          .take(10).foreach(println)
      })

    ssc.start
    ssc.awaitTermination
  }
}
