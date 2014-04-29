package vn.myfeed.parser.util

import java.util.regex.Pattern
import org.apache.commons.lang3.StringUtils
import org.apache.commons.io.IOUtils
import collection.JavaConversions._

/**
 * The Class Pattern.
 *
 * @author Nguyen Duc Dung
 * @since 6/15/13 11:08 AM
 *
 */
trait FilePattern {

  val regexFile: String

  val start = ".*"
  val end = ".*"
  val next = "|"

  lazy val regex = {
    val strm = try {
      this.getClass.getClassLoader.getResourceAsStream(regexFile)
    } catch {
      case _: Throwable => throw new IllegalArgumentException(s"Unavailable file $regexFile")
    }

    val src = IOUtils.readLines(strm)

    val sb = new StringBuilder
    src.filter(st => StringUtils.isNotBlank(st) && !st.startsWith("#")).foreach(st => {
      sb.append(start + st.trim + end + next)
    })
    strm.close()
    val regex = sb.toString()

    //Remove last '|'
    regex.substring(0, regex.length - next.length)
  }

  def pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE)

  def matches(st: String) = pattern.matcher(st).matches()

}
