package vn.myfeed.parser.model

import org.jsoup.nodes.Element
import org.apache.commons.lang3.StringUtils

/**
 * The Class ArticleElement.
 *
 * @author Nguyen Duc Dung
 * @since 12/23/12 3:24 AM
 *
 */
abstract class ArticleElement(_jsoupElement: Element)(implicit article: Article) {

  private var _isContent = false

  //getter
  def jsoupEl = _jsoupElement

  /**
   * This property will base on @see EndContentPattern
   */
  var isEnd = false

  /**
   * Index of this element inside an article.
   */
  var index = 0

  /**
   * This attribute mean this element might is a part of content.
   */
  var isPotential = false

  var isTitle = false

  var isDirty = false

  def isContent = _isContent

  def isContent_=(isContent: Boolean) {
    //title and end element should not be a content element
    if(!isTitle && !isEnd && !isDirty) {
      _isContent = isContent
    }
  }

  def id = jsoupEl.id()

  def tag = jsoupEl.tag()

  def tagName = jsoupEl.tagName()

  def className = jsoupEl.className()

  /**
   * Check similarity class name between two elements
   * @param otherElement
   * @return if two element has more than one same class name.
   */
  def isSimilarClassName(otherElement: ArticleElement) = {
    var similar = false

    val otherClassNames = otherElement.className.split(" ")
    val classNames = className.split(" ")

    otherClassNames.foreach(otherName => {
      classNames.foreach(name => {
        if (StringUtils.equalsIgnoreCase(otherName.trim, name.trim)
          && StringUtils.isNotBlank(otherName)) {
          similar = true
        }
      })
    })

    similar
  }

  def parent = jsoupEl.parent()

  def text: String

  def html = jsoupEl.html

  def hasText = StringUtils.isNotBlank(text)

  /**
   * Score to evaluate this element.
   * @return
   */
  def score: Double = 0

  override def equals(obj: Any): Boolean = obj match {
    case element: ArticleElement =>
      val element2 = element.jsoupEl
      jsoupEl.equals(element2)
    case _ =>
      false
  }


  override def hashCode() = jsoupEl.hashCode()

}
