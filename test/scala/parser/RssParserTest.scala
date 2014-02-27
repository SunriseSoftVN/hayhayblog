package scala.parser

import org.junit.Test
import org.apache.commons.io.IOUtils
import parser.RssParser
import collection.JavaConversions._
import org.rometools.feed.module.mediarss.{MediaEntryModuleImpl, MediaModule}
import org.rometools.feed.module.feedburner.FeedBurner

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
    val input = this.getClass.getClassLoader.getResourceAsStream("rss4.xml")
    val bytes = IOUtils.toByteArray(input)
    parser.parse(bytes, "http://eatingandgettingfattogether.wordpress.com/").map(feed => {
      feed.getEntries.foreach(entry => {
        val media = entry.getModule(MediaModule.URI).asInstanceOf[MediaEntryModuleImpl]
        if (media != null && media.getMetadata != null) {
          media.getMetadata.getThumbnail.foreach(println)
        }

        val feedBunnerModule = entry.getModule(FeedBurner.URI)
        if (feedBunnerModule != null) {
          val origLink = feedBunnerModule.asInstanceOf[FeedBurner].getOrigLink
          println(origLink)
        }
      })
    })
  }

  @Test
  def test2() {
    val input = this.getClass.getClassLoader.getResourceAsStream("rss1.xml")
    val bytes = IOUtils.toByteArray(input)
    parser.parse(bytes, "http://hocthenao.vn/").map(feed => {
      feed.getEntries.foreach(entry => {
        entry.getForeignMarkup.foreach(m => {
          println(m.getName)
          println(m.getNamespacePrefix)
          println(m.getValue)
        })
      })
    })
  }

  @Test
  def test3() {
    val input = this.getClass.getClassLoader.getResourceAsStream("rss3.xml")
    val bytes = IOUtils.toByteArray(input)
    parser.parse(bytes, "http://hocthenao.vn/").map(feed => {
      feed.getEntries.foreach(entry => {
        entry.getForeignMarkup.foreach(m => {
          println(m.getName)
          println(m.getNamespaceURI)
          println(m.getValue)
        })
      })
    })
  }

  @Test
  def test4() {
    val input = this.getClass.getClassLoader.getResourceAsStream("rss4.xml")
    val bytes = IOUtils.toByteArray(input)
    parser.parse(bytes, "http://hocthenao.vn/").map(feed => {
      feed.getEntries.foreach(entry => {
        println(entry.getContents)
      })
    })
  }

}
