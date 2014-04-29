package vn.myfeed.parser.processor

import vn.myfeed.parser.model.Article
import collection.JavaConversions._
import org.jsoup.nodes.{Element, Node, TextNode}
import scala.collection.mutable.ListBuffer
import org.jsoup.parser.Tag
import org.apache.commons.lang3.StringUtils
import vn.myfeed.parser.model.RichSoup._

/**
 * The Class DocumentCleaner.
 *
 * @author Nguyen Duc Dung
 * @since 12/25/12 2:08 PM
 *
 */
class DocumentCleaner extends Processor {
  def process(implicit article: Article) {
    val containerElement = article.containerElement
    val cleanedHtml = containerElement.html.replaceAll("&nbsp;", " ").replaceAll("<!--.*-->", "")
    containerElement.html(cleanedHtml)

    val headerElement = containerElement.select("head")
    headerElement.remove()

    //Remove noscript tag
    val noScriptElement = containerElement.select("noscript")
    noScriptElement.remove()

    val scriptElement = containerElement.select("script")
    scriptElement.remove()

    val buttonElement = containerElement.select("button")
    buttonElement.remove()

    val inputElement = containerElement.select("input")
    inputElement.remove()

    val textareaElement = containerElement.select("textarea")
    textareaElement.remove()

    val selectElement = containerElement.select("select")
    selectElement.remove()

    val iframeElement = containerElement.select("iframe")
    iframeElement.filterNot(el => {
      val src = el.attr("src")
      src.contains("youtube.com")
    }).remove()

    //Try to wrap a inline tag or a text node inside a block tag
    travelAndWrap(article.containerElement, article)
  }

  def travelAndWrap(parent: Element, article: Article) {
    if (parent.tagName != "pre") {
      parent.children.foreach(travelAndWrap(_, article))

      if (parent.tag.isBlock && !parent.isTextBlock) {
        var wrapElement: Option[Element] = None
        val newChildren = new ListBuffer[Node]
        parent.childNodes.foreach(node => {
          node match {
            case WrapElementMatcher(removeNode) if wrapElement.isEmpty => {
              wrapElement = Some(new Element(Tag.valueOf("p"), article.url))
              wrapElement.get.appendChild(removeNode.clone())
              newChildren += wrapElement.get
            }
            case el: Element if (el.tag.isEmpty || el.tag.isBlock) && wrapElement.isDefined => {
              wrapElement = None
              newChildren += node
            }
            case _ => {
              wrapElement.map(el => {
                el.appendChild(node.clone())
              }).getOrElse(
                newChildren += node
              )
            }
          }
        })

        parent.childNodes.toList.foreach(_.remove())
        parent.insertChildren(0, newChildren)
      }
    }
  }
}

object WrapElementMatcher {
  def unapply(node: Node) = node match {
    case textNode: TextNode if StringUtils.isNotBlank(textNode.text) => Some(textNode)
    case el: Element if el.tag.isInline && !el.tag.isEmpty => Some(el)
    case _ => None
  }
}