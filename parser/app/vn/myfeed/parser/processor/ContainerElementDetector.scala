package vn.myfeed.parser.processor

import vn.myfeed.parser.model.{TextElement, LinkElement, Article}
import collection.JavaConversions._
import org.jsoup.nodes.Element
import scala.util.control.Breaks._
import vn.myfeed.parser.model.RichSoup._

/**
 * This class will find the lowest element contain all content element.
 *
 * @author Nguyen Duc Dung
 * @since 12/27/12 7:27 PM
 *
 */
class ContainerElementDetector extends Processor {
  def process(implicit article: Article) {
    val elements = article.doc.getAllElements
    val containElements = article.contentElements.map(_.jsoupEl)
    var containerElement: Option[Element] = None

    elements.foreach(el => if (el.isBlock && el.tag.formatAsBlock()) {
      if (el.getAllElements.containsAll(containElements) && el.tagName != "body") containerElement = Some(el)
    })

    containerElement.map(containerEl => {
      article.containerElement = containerEl
      article.titleElement.map(titleEl => {
        //Mark all element inside container element is content begin from titleEl and end at endEl
        breakable {
          article.elements.filter(el => {
            containerEl.getAllElements.contains(el.jsoupEl) && el.index > titleEl.index
          }).foreach(el => if (!el.isEnd) {
            el match {
              case textEl: TextElement => if (textEl.wordCount > 1 || textEl.jsoupEl.isHeading) {
                el.isContent = true
              }
              case _ => el.isContent = true
            }
          } else {
            break()
          })
        }
      })
    })

    //Remove link in the end of content.
    breakable {
      article.contentElements.reverse.foreach(el => {
        if (el.isInstanceOf[LinkElement]) {
          el.isContent = false
        } else {
          break()
        }
      })
    }
  }
}
