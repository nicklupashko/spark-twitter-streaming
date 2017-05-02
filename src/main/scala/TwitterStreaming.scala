import org.apache.spark.streaming.twitter.TwitterUtils
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.{Minutes, Seconds, StreamingContext}
import twitter4j.auth.OAuthAuthorization
import twitter4j.conf.ConfigurationBuilder

object TwitterStreaming {
  def main(args: Array[String]): Unit = {

    val consumerKey = "8dD3v2yFgEdfIgzYSPKlhL9vW"
    val consumerSecret = "quURcrkUNOG6O7xnUlbPzQAfw8gxncYSoIT26v4hy0zhQG7mqQ"
    val accessToken = "317037418-FmxESuhNiEGlG3sUDSim0DJGl73RcOCfaZfGCIbl"
    val accessTokenSecret = "faAl81LdjN58b3cNHIUMCFiKzCMbKpR69SFzzmVydMfDL"

    System.setProperty("twitter4j.oauth.consumerKey", consumerKey)
    System.setProperty("twitter4j.oauth.consumerSecret", consumerSecret)
    System.setProperty("twitter4j.oauth.accessToken", accessToken)
    System.setProperty("twitter4j.oauth.accessTokenSecret", accessTokenSecret)

    val outputDirectory = "/twitter"
    val slideInterval = Seconds(20)
    val windowLength  = Minutes(2)
    val timeoutJobLength = 100 * 1000
    val hashTag = "#starwars"

    val config = new SparkConf().setAppName("nicklupashko-spark-streaming")
    val sc   = new SparkContext(config)
    sc.setLogLevel("WARN")
    val ssc  = new StreamingContext(sc, slideInterval)
    val auth = Some(new OAuthAuthorization(new ConfigurationBuilder().build()))
    val twitterStream = TwitterUtils.createStream(ssc, auth)

    val hastTagStream = twitterStream.map(_.getText).filter(_.contains(hashTag))


  }
}
