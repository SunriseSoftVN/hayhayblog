package scala.parser

import org.junit.Test
import org.apache.commons.io.IOUtils
import parser.RssParser
import collection.JavaConversions._
import org.rometools.feed.module.mediarss.{MediaEntryModuleImpl, MediaModule}

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
  def test1() {
    val input = this.getClass.getClassLoader.getResourceAsStream("rss3.xml")
    val bytes = IOUtils.toByteArray(input)
    parser.parse(bytes, "http://eatingandgettingfattogether.wordpress.com/").map(feed => {
      feed.getEntries.foreach(entry => {
        val syndEntry = entry
        val media = syndEntry.getModule(MediaModule.URI).asInstanceOf[MediaEntryModuleImpl]
        if (media != null && media.getMetadata != null) {
          media.getMetadata.getThumbnail.foreach(println)
        }
      })
    })
  }

  @Test
  def test2() {
    val input = this.getClass.getClassLoader.getResourceAsStream("rss2.xml")
    val bytes = IOUtils.toByteArray(input)
    parser.parse(bytes, "http://hocthenao.vn/").map(feed => {
      feed.getEntries.foreach(entry => {
        val syndEntry = entry
        syndEntry.getContents.foreach(content => println(content.getValue))
      })
    })
  }

}
