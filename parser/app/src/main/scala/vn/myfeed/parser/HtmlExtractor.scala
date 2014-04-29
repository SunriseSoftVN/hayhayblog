package vn.myfeed.parser

import org.jsoup.nodes.Document
import scala.collection.JavaConversions._
import org.apache.http.client.utils.URIUtils
import java.net.URI
import org.apache.commons.lang3.StringUtils
import edu.uci.ics.crawler4j.url.URLCanonicalizer
import org.apache.commons.validator.routines.UrlValidator
import scala._
import scala.collection.mutable.ListBuffer

/**
 * This extractor using for get image and text from a html fragment.
 * Basically it just use Jsoup parser but for more convenient and more functionally.
 *
 * @author Nguyen Duc Dung
 * @since 6/16/13 1:43 PM
 *
 */
class HtmlExtractor {

  val images = new ListBuffer[String]
  var text: String = ""
  val urlValidator = new UrlValidator(Array("http", "https"))

  def extract(doc: Document) {
    val images = doc.select("img")
    images.foreach(img => {
      val src = img.attr("src")
      if (StringUtils.isNotBlank(src)) {
        if (StringUtils.isNotBlank(doc.baseUri)) {
          val baseUrl = URIUtils.extractHost(new URI(doc.baseUri)).toURI
          if (baseUrl != null) {
            val fixedUrl = URLCanonicalizer.getCanonicalURL(src, baseUrl)
            if (StringUtils.isNotBlank(fixedUrl) && urlValidator.isValid(fixedUrl)) {
              img.attr("src", fixedUrl)
            }
          }
        }
        if (urlValidator.isValid(src)) {
          this.images += src
        }
      }
    })
    this.text = doc.text()
  }
}
