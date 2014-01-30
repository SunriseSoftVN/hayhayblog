package parser

import java.io.{StringReader, ByteArrayInputStream}
import com.sun.syndication.io.SyndFeedInput
import com.sun.syndication.feed.synd.SyndFeed
import org.jsoup.Jsoup
import org.apache.commons.lang3.StringUtils
import org.jsoup.parser.Parser
import play.api.Logger

/**
 * The Class RssParser.
 *
 * @author Nguyen Duc Dung
 * @since 5/4/13 6:09 AM
 *
 */
class RssParser {

  def parse(content: Array[Byte], url: String): Option[SyndFeed] = {
    val input = new ByteArrayInputStream(content)
    val doc = Jsoup.parse(input, null, url, Parser.xmlParser).normalise()
    input.close()
    val rss = if (!doc.select("rss").isEmpty) {
      doc.select("rss").outerHtml
    } else {
      //atom format
      doc.select("feed").outerHtml
    }
    if (StringUtils.isNotBlank(rss)) {
      val feedInput = new SyndFeedInput
      val feed = feedInput.build(new StringReader(RssFixer.fix(rss)))
      return Some(feed)
    }
    Logger.error("Error parse rss: " + url)
    None
  }

}
