package vn.myfeed.parser.processor

import vn.myfeed.parser.model.Article
import vn.myfeed.parser.model.RichSoup._
import collection.JavaConversions._

/**
 * The Class RemoveHiddenElement.
 *
 * @author Nguyen Duc Dung
 * @since 12/25/12 1:25 PM
 *
 */
class RemoveHiddenElement extends Processor {
  def process(implicit article: Article) {
    val removeElements = article.doc.getAllElements.filter(_.isHidden)
    removeElements.foreach(_.remove())
  }
}
