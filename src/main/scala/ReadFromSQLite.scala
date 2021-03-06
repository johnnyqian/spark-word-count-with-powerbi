import java.io.StringReader
import org.apache.spark.sql.{Row, SparkSession}
import org.wltea.analyzer.lucene.IKTokenizer

object ReadFromSQLite extends App {
  val spark = SparkSession
    .builder()
    .master("local")
    .appName("WordCount")
    .config("spark.hadoop.validateOutputSpecs", "false")
    .getOrCreate()

  val df = spark.read.format("jdbc").options(
    Map(
      "driver" -> "org.sqlite.JDBC",
      "url" -> "jdbc:sqlite:src/main/resources/sqlite.db",
      "dbtable" -> "posts")
  ).load()

  val words = df.select("content").rdd.flatMap {
    case Row(content: String) => Main.tokenization(new IKTokenizer(new StringReader(content), true))
  }

  val counts = words.map(word => (word, 1))
    .reduceByKey{ case (x ,y) => x + y }

  counts.filter(item => item._1.length > 1)
    .sortBy { case (_, value) => -value }
    .map { case (key, value) => Array(key, value).mkString(",") }
    .coalesce(1)
    .saveAsTextFile("output")

  Console.println("Done.")
}
