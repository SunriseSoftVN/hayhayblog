package vn.myfeed.parser.processor

import vn.myfeed.parser.model.Article
import collection.JavaConversions._
import vn.myfeed.parser.model.RichSoup._
import org.jsoup.Jsoup

/**
 * This class will remove all class of element.
 *
 * @author Nguyen Duc Dung
 * @since 6/15/13 12:13 PM
 *
 */
class HtmlFormater extends Processor {

  val default_width = "90%"
  val default_height = "380"

  def process(implicit article: Article) {
    article.contentHtml = format(article.contentHtml)
    article.rssDoc.map(doc => {
      doc.select("img").foreach(element => {
        element.attr("style", s"max-width: $default_width !important;")
      })
      doc.select("style").remove()
      doc.select("script").remove()
    })
  }

  private def format(html: String) = {
    val formatDoc = Jsoup.parseBodyFragment(html)

    formatDoc.getAllElements.foreach(jsoupEl => {

      if (!jsoupEl.isHeading) jsoupEl.removeAttr("class")

      if (!jsoupEl.isAlignCenter) {
        if (jsoupEl.tagName != "pre") {
          jsoupEl.removeAttr("style")
        }
      } else {
        jsoupEl.removeAttr("style")
        jsoupEl.addClass("text-center")
      }

      jsoupEl.removeAttr("skip-parse")
      jsoupEl.removeAttr("face")
      jsoupEl.removeAttr("name")
      jsoupEl.removeAttr("id")
      jsoupEl.removeAttr("width")
      jsoupEl.removeAttr("height")
      jsoupEl.removeAttr("border")

      jsoupEl.tagName match {
        case "img" => {
          val imgSrc = jsoupEl.attr("src")
          val title = jsoupEl.attr("alt")
          //set max width and max height for image
          jsoupEl.attr("style", s"max-width: $default_width !important;")
          jsoupEl.attr("data-src", imgSrc)
          jsoupEl.attr("src", "/assets/img/img-loader.gif")
          if (jsoupEl.parent.tagName != "a") {
            jsoupEl.wrap("<p></p>")
            jsoupEl.wrap(s"<a rel='gal' class='gallery' href='$imgSrc' title='$title' target='_blank'></a>")
          } else {
            jsoupEl.parent.attr("rel", "gal")
            jsoupEl.parent.attr("class", "gallery")
            jsoupEl.parent.attr("title", title)
          }
        }
        case "font" => jsoupEl.tagName("span")
        case "blockquote" => if (jsoupEl.parent != null && jsoupEl.parent.tagName != "pre") {
          jsoupEl.tagName("div")
          jsoupEl.attr("class", "well")
        }
        case "table" => jsoupEl.attr("class", "table table-striped table-bordered")
        case "ul" => jsoupEl.wrap("<div class='well'></div>")
        case "iframe" | "object" | "embed" => {
          jsoupEl.attr("width", default_width)
          jsoupEl.attr("height", default_height)
        }
        case "p" | "div" => {
          if (!jsoupEl.children.isEmpty) {
            val first = jsoupEl.children.first()
            if (first.tagName == "br") first.remove()
            if (jsoupEl.children.size > 1) {
              val last = jsoupEl.children.last()
              if (last.tagName == "br") last.remove()
            }
          }
        }
        case _ => //do nothing
      }

      if (jsoupEl.isHeading) {
        jsoupEl.attr("style", "font-weight: bold")
        jsoupEl.removeAttr("class")
      }

    })

    formatDoc.body.html()
  }
}
