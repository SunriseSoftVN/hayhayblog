package parser

import org.jsoup.Jsoup
import org.apache.commons.lang3.StringUtils
import com.sun.syndication.feed.synd.{SyndContent, SyndEntry}
import scala.collection.JavaConversions._

/**
 * The this class using two parser LinksParse and ArticleParser.
 *
 * @author Nguyen Duc Dung
 * @since 12/27/12 6:15 PM
 *
 */
class NewsParser {

  def extract(item: SyndEntry, url: String): (Option[String], Option[String], List[String]) = {
    getHtml(item).map(html => {
      val htmlExtractor = new HtmlExtractor
      val doc = Jsoup.parse(html, url)
      htmlExtractor.extract(doc)
      (Some(StringUtils.trimToEmpty(htmlExtractor.text)), Some(html), htmlExtractor.images.toList)
    }).getOrElse((None, None, Nil))
  }

  private def getHtml(item: SyndEntry) = {
    var html = ""
    item.getContents.foreach(content => content match {
      case c: SyndContent => {
        if (StringUtils.isNotBlank(c.getValue)) {
          html += c.getValue
        }
      }
    })

    if (StringUtils.isBlank(html) && item.getDescription != null
      && StringUtils.isNotBlank(item.getDescription.getValue)) {
      html = item.getDescription.getValue
    }

    if (StringUtils.isNotBlank(html)) {
      Some(html)
    } else {
      None
    }
  }
}
