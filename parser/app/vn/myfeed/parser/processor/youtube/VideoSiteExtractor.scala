package vn.myfeed.parser.processor.youtube

import vn.myfeed.parser.processor.Processor
import vn.myfeed.parser.model.{ArticleElement, MediaElement, TextElement, Article}
import scala.collection.JavaConversions._
import org.apache.commons.lang3.StringUtils
import org.jsoup.Jsoup
import scala.collection.mutable.ListBuffer

/**
 * The Class YoutubeExtractor.
 *
 * @author Nguyen Duc Dung
 * @since 6/19/13 11:00 PM
 *
 */
class VideoSiteExtractor extends Processor {
  def process(implicit article: Article) {
    val doc = article.doc
    val titleEl = doc.select("meta[property=og:title]")
    val descriptionEl = doc.select("meta[property=og:description]")
    val playerEl = doc.select("meta[name=twitter:player]")
    val thumpEl = doc.select("meta[name=twitter:image]")

    var html = ""

    titleEl.headOption.map(_.attr("content")).map(title => {
      article.title = title
    })

    playerEl.headOption.map(_.attr("content")).map(embedUrl => {
      html += "<div class='text-center'>"
      html += s"<iframe width='100%' style='z-index: -1' height='380' src='$embedUrl?feature=oembed' frameborder='0'></iframe>"
      html += "</div>"
    }).getOrElse({
      thumpEl.headOption.map(_.attr("content")).map(url => {
        html += s"<img src='$url'><img>"
      })
    })

    descriptionEl.headOption.map(_.attr("content")).map(description => {
      html += s"<p>$description</p>"
    })

    if (StringUtils.isNotBlank(html)) {
      val elements = new ListBuffer[ArticleElement]
      val newDoc = Jsoup.parseBodyFragment(html, doc.baseUri())
      newDoc.select("iframe, img").foreach(el => {
        val mediaEl = new MediaElement(el)
        mediaEl.isContent = true
        elements += mediaEl
      })
      newDoc.select("p").foreach(el => {
        val textEl = new TextElement(el)
        textEl.isContent = true
        elements += textEl
      })
      article.elements = elements.toList
      article.contentHtml = html
      article.isVideoSite = true
    }
  }
}
