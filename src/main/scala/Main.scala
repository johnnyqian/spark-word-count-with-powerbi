import java.io.{IOException, StringReader}

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.hadoop.io.LongWritable
import org.apache.hadoop.io.Text
import org.apache.hadoop.mapred.TextInputFormat
import org.apache.lucene.analysis.TokenStream
import org.apache.lucene.analysis.tokenattributes.TermAttribute
import org.wltea.analyzer.lucene.IKTokenizer

import scala.collection.mutable.ArrayBuffer

object Main extends App {
  val conf = new SparkConf().setMaster("local")
                            .setAppName("WordCount")
                            .set("spark.hadoop.validateOutputSpecs", "false")
  val sc = new SparkContext(conf)

  val input = sc.hadoopFile[LongWritable, Text, TextInputFormat]("src/main/resources/射雕英雄传.txt")
                .mapPartitions(_.map(line => new String(line._2.getBytes, 0, line._2.getLength, "gb2312")))

  val words = input.flatMap(line => tokenization(new IKTokenizer(new StringReader(line), true)))

  val counts = words.map(word => (word, 1))
                    .reduceByKey{ case (x ,y) => x + y }

  counts.filter(item => item._1.length > 1)
        .sortBy { case (_, value) => -value }
        .map { case (key, value) => Array(key, value).mkString(",") }
        .coalesce(1)
        .saveAsTextFile("output")

  Console.println("Done.")

  def tokenization(tokenizer: TokenStream) : ArrayBuffer[String] = {
    val termList = ArrayBuffer[String]()
    try {
      while ( {tokenizer.incrementToken}) {
        val termAtt: TermAttribute = tokenizer.getAttribute (classOf[TermAttribute])
        termList += termAtt.term
      }
    }
    catch {
      case e: IOException => e.printStackTrace
    }
    termList
  }
}
