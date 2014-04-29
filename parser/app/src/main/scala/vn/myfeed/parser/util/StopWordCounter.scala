package vn.myfeed.parser.util

import breeze.text.tokenize.JavaWordTokenizer
import org.apache.commons.lang3.StringUtils
import org.apache.commons.io.IOUtils
import collection.JavaConversions._

/**
 * The Class StopWordCounter.
 *
 * @author Nguyen Duc Dung
 * @since 12/19/12, 6:10 PM
 *
 */
class StopWordCounter(language: String) {

  val tokenizer = JavaWordTokenizer

  lazy val stopWords = {
    val strm = try {
      this.getClass.getClassLoader.getResourceAsStream("stopwords/" + language.toLowerCase + ".lst")
    } catch {
      case _: Throwable => throw new IllegalArgumentException("Unavailable language: " + language)
    }
    val src = IOUtils.readLines(strm)

    val ret = src.filter(!_.startsWith("#")).map(_.trim)
    strm.close()
    ret.toSeq
  }

  def count(st: String) = {
    var count = 0
    if (StringUtils.isNotBlank(st)) {
      tokenizer(st).foreach(word => if (stopWords.contains(word)) count += 1)
    }
    count
  }

}
