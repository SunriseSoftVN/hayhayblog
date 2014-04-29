package vn.myfeed.parser.processor

import vn.myfeed.parser.model.Article
import vn.myfeed.parser.util.EndContentPattern

/**
 * This class will find the end of article base on common words and common class.
 *
 * @author Nguyen Duc Dung
 * @since 6/15/13 11:33 AM
 *
 */
class EndOfContentDetector extends Processor {
  def process(implicit article: Article) {
    article.elements.foreach(element => if (element.tagName != "pre") {
      if (EndContentPattern.matches(element.text)
        || EndContentPattern.matches(element.className)
        || EndContentPattern.matches(element.id)) {
        element.isEnd = true
        element.isPotential = false
      }
    })
  }
}
