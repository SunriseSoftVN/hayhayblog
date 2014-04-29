package vn.myfeed.parser.util

import scala.io.Source
import org.apache.commons.lang3.StringUtils
import scala.collection.mutable.ListBuffer

/**
 * The Class WhiteListPatten.
 *
 * @author Nguyen Duc Dung
 * @since 6/20/13 4:31 PM
 *
 */
object WhiteListPatten {

  val regexFile = "filter/whitelist.lst"

  lazy val selects = {
    val selects = new ListBuffer[(String, String)]
    val strm = try {
      this.getClass.getClassLoader.getResourceAsStream(regexFile)
    } catch {
      case _: Throwable => throw new IllegalArgumentException(s"Unavailable file $regexFile")
    }
    val src = Source.fromInputStream(strm)

    src.getLines().filter(st => StringUtils.isNotBlank(st)).foreach(st => {
      val tuple = st.split(">")
      if (tuple.length == 2) {
        val key = StringUtils.trim(tuple(0))
        val value = StringUtils.trim(tuple(1))
        selects += key -> value
      }
    })
    strm.close()
    selects.toList
  }

  def get(url: String) = selects.filter(v => url.contains(v._1)).map(_._2)
}
