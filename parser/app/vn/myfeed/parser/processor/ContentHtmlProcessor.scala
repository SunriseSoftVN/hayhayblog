package vn.myfeed.parser.processor

import vn.myfeed.parser.model.Article
import scala.util.control.Breaks._
import org.apache.commons.lang3.StringUtils
import collection.JavaConversions._

/**
 * This processor will remove all element be marked not content.
 *
 * @author Nguyen Duc Dung
 * @since 9/7/13, 10:46 AM
 *
 */
class ContentHtmlProcessor extends Processor {

  val tableElement = List("table", "tbody", "tr", "td", "th", "thead")
  val keepElement = List("code", "pre")

  def process(implicit article: Article) {
    val tableElements = article.containerElement.select(tableElement.mkString(",")).flatMap(_.getAllElements)
    //Keep element inside a table even though it's not a content element
    val removeElements = article.containerElement.getAllElements
      .filterNot(el => tableElements.contains(el))
      .filterNot(el => keepElement.contains(el.tagName))
      .filterNot(el1 => {
      var keep = false
      breakable {
        article.contentElements.flatMap(_.jsoupEl.getAllElements).foreach(el2 => {
          if (el1.getAllElements.contains(el2)) {
            keep = true
            break()
          }
        })
      }
      keep
    })

    removeElements.foreach(_.remove())

    val emptyTables = article.containerElement.select("table").filter(table => StringUtils.isBlank(table.text))
    emptyTables.foreach(_.remove())

    val sb = new StringBuilder

    article.contentElements
      .filter(el => article.doc.getAllElements.indexOf(el.jsoupEl) < article.doc.getAllElements.indexOf(article.containerElement))
      .foreach(el => {
      sb ++= "<div>" + el.jsoupEl.outerHtml + "</div>"
    })

    sb ++= "<div>" + article.containerElement.html + "</div>"

//    //Todo remove this ugly code
//    article.contentElements
//      .filter(el => article.doc.getAllElements.indexOf(el.jsoupEl) > article.doc.getAllElements.indexOf(article.containerElement)
//      && !article.containerElement.getAllElements.contains(el.jsoupEl))
//      .foreach(el => {
//      sb ++= "<div>" + el.jsoupEl.outerHtml + "</div>"
//    })

    article.contentHtml = sb.toString()
  }
}
