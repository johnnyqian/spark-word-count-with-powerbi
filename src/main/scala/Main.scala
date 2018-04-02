import java.io.IOException
import org.apache.lucene.analysis.TokenStream
import org.apache.lucene.analysis.tokenattributes.TermAttribute
import scala.collection.mutable.ArrayBuffer

object Main extends App {
  def tokenization(tokenizer: TokenStream) : ArrayBuffer[String] = {
    val termList = ArrayBuffer[String]()
    try {
      while ( {tokenizer.incrementToken}) {
        val termAtt: TermAttribute = tokenizer.getAttribute (classOf[TermAttribute])
        termList += termAtt.term
      }
    }
    catch {
      case e: IOException => e.printStackTrace()
    }
    termList
  }
}
