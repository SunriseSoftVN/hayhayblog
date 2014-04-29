/*
 Copyright 2009 David Hall, Daniel Ramage

 Licensed under the Apache License, Version 2.0 (the "License")
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
*/
package vn.myfeed.parser.util

import java.util.Locale
import breeze.text.transform.Transformer
import org.apache.commons.io.IOUtils
import collection.JavaConversions._

/**
 * Filter that removes stop words. Use the two letter code. "en" or "fr"
 * Syntax: words.filter(new RemoveStopwords)
 *
 * @author dramage
 * @author dlwh
 */
case class StopWordFilter(language: String) extends Transformer {
  /**
   *
   * Defaults to locale's language.
   */
  def this(locale: Locale) = this(locale.getLanguage)
  /**
   * Defaults to Default Locale's language.
   */
  def this() = this(Locale.getDefault)

  val words = {
    val strm = try {
      this.getClass.getClassLoader.getResourceAsStream("stopwords/" + language.toLowerCase + ".lst")
    } catch {
      case ex: Exception => throw new IllegalArgumentException("Unavailable language: " + language, ex)
    }
    val src = IOUtils.readLines(strm)

    val ret = src.filter(word => !word.startsWith("#") && !word.isEmpty).map(_.trim)
    strm.close()
    ret.toSet
  }

  override def apply(doc: Iterable[String]) = doc.filter(word => !words.contains(word.toLowerCase))

  def apply(s: String) = !words(s.toLowerCase)

}

object StopWordFilter {
  def apply() = new StopWordFilter()
  def apply(locale: Locale) = new StopWordFilter(locale)
}
