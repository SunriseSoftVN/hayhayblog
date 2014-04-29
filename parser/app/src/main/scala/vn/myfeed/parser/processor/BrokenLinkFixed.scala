package vn.myfeed.parser.processor

import vn.myfeed.parser.model.Article
import org.apache.commons.lang3.StringUtils
import org.apache.commons.validator.routines.UrlValidator
import edu.uci.ics.crawler4j.url.URLCanonicalizer
import collection.JavaConversions._
import org.jsoup.nodes.Element

/**
 * The processor is using for fix relative link.
 * Example: /abc => www.example.com/abc
 *
 * @author Nguyen Duc Dung
 * @since 6/13/13 5:22 PM
 *
 */
class BrokenLinkFixed extends Processor {

  val urlValidator = new UrlValidator(Array("http", "https"))

  def process(implicit article: Article) {
    article.contentElements.foreach(element => {
      checkElement(element.jsoupEl)
    })
    article.rssDoc.map(doc => {
      doc.getAllElements.foreach(checkElement)
    })
  }

  private def checkElement(element: Element)(implicit article: Article) {
    element.getAllElements.foreach(implicit jsoupEl => {
      jsoupEl.tagName match {
        case "a" => {
          fix("href")
          jsoupEl.attr("target", "_blank")
        }
        case "img" => {
          val dataSrc = jsoupEl.attr("data-cfsrc")
          if(StringUtils.isNotBlank(dataSrc)) {
            jsoupEl.attr("src", dataSrc)
          }
          fix("src")
        }
        case "embed" => fix("src")
        case "iframe" => fix("src")
        case _ =>
      }
    })
  }

  private def fix(attr: String)(implicit jsoupEl: Element, article: Article) {
    //Fix relative url.
    val url = jsoupEl.attr(attr)
    if (StringUtils.isNotBlank(url) && StringUtils.isNotBlank(article.url)) {
      val fixedUrl = URLCanonicalizer.getCanonicalURL(StringUtils.trim(url), article.url)
      if (StringUtils.isNotBlank(fixedUrl) && urlValidator.isValid(fixedUrl)) {
        jsoupEl.attr(attr, fixedUrl)
      }
    }
  }

}
