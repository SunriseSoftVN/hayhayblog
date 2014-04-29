package vn.myfeed.parser.model

import org.apache.http.client.utils.URIUtils
import java.net.URI
import scala.slick.driver.H2Driver.simple._
import java.io.{ByteArrayInputStream, InputStream}
import org.jsoup.Jsoup
import org.apache.commons.io.IOUtils

/**
 * The Class Article.
 *
 * @author Nguyen Duc Dung
 * @since 12/23/12 1:00 AM
 *
 */
class Article(input: InputStream, val url: String) {

  val bytes = IOUtils.toByteArray(input)

  val doc = Jsoup.parse(new ByteArrayInputStream(bytes), null, url).normalise()

  /**
   * Default is vi.
   */
  var languageCode = "vi"

  var title = ""

  //This element will contain all text content element. Default is body element.
  var containerElement = doc.body()

  var contentHtml = ""

  //Only for youtube and vimeo and some common video site
  var isVideoSite = false

  /**
   * Article element list without title element.
   */
  var elements: List[ArticleElement] = Nil

  /**
   * Original html
   */
  val html = doc.html()

  var rssHtml: Option[String] = None

  lazy val rssDoc = rssHtml.map(html => Jsoup.parseBodyFragment(html))

  def baseUrl = URIUtils.extractHost(new URI(url)).toURI

  def titleElement = elements.sortBy(!_.isInstanceOf[TextElement]).find(_.isTitle)

  def textElements: List[TextElement] = elements.filter(_.isInstanceOf[TextElement]).map(_.asInstanceOf[TextElement])

  def mediaElements: List[MediaElement] = elements.filter(_.isInstanceOf[MediaElement]).map(_.asInstanceOf[MediaElement])

  def linkElements: List[LinkElement] = elements.filter(_.isInstanceOf[LinkElement]).map(_.asInstanceOf[LinkElement])

  def potentialElements: List[ArticleElement] = elements.filter(_.isPotential)

  def contentElements: List[ArticleElement] = elements.filter(_.isContent)

  def textContentElements: List[ArticleElement] = textElements.filter(_.isContent)

  def jsoupElements = elements.map(_.jsoupEl)

  def images = contentElements.filter(_.tagName == "img").map(_.asInstanceOf[MediaElement])

  def text = {
    val sb = new StringBuilder
    if (!isVideoSite) {
      textContentElements.foreach(element => {
        sb.append(element.text)
        sb.append("\n")
      })
    } else {
      sb.append(Jsoup.parse(contentHtml).text())
    }
    sb.toString()
  }

  override def toString = url
}

object Article extends Table[(String, Array[Byte], String, String, String)]("article") {
  def url = column[String]("url", O.PrimaryKey)

  def html = column[Array[Byte]]("html")

  def title = column[String]("title")

  def text = column[String]("text")

  def contentHtml = column[String]("contentHtml")

  def * = url ~ html ~ title ~ text ~ contentHtml
}