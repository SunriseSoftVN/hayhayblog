package scala.parser

import org.junit.Test
import org.apache.commons.io.IOUtils
import parser.RssParser
import collection.JavaConversions._
import com.sun.syndication.feed.synd.SyndEntry
import com.sun.syndication.feed.module.mediarss.{MediaEntryModuleImpl, MediaModule}

/**
 * The Class RssParser.
 *
 * @author Nguyen Duc Dung
 * @since 2/27/14 8:37 AM
 *
 */
class RssParserTest {

  val parser = new RssParser

  @Test
  def test() {
    val input = this.getClass.getClassLoader.getResourceAsStream("rss1.xml")
    val bytes = IOUtils.toByteArray(input)
    parser.parse(bytes, "http://eatingandgettingfattogether.wordpress.com/").map(feed => {
      feed.getEntries.foreach(entry => {
        val syndEntry = entry.asInstanceOf[SyndEntry]
        val media = syndEntry.getModule(MediaModule.URI).asInstanceOf[MediaEntryModuleImpl]
        println(media.getMetadata.getThumbnail()(0).getUrl)
      })
    })
  }

}
