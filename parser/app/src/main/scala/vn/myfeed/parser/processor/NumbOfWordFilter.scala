package vn.myfeed.parser.processor

import vn.myfeed.parser.model.Article

/**
 * The Class NumbOfWordFilter.
 *
 * @author Nguyen Duc Dung
 * @since 12/23/12 5:14 AM
 *
 */
class NumbOfWordFilter(minNumbOfStopWord: Int = 2, minNumbOfWord: Int = 5) extends Processor {
  def process(implicit article: Article) {
    article.textElements.foreach(el => {
      if (el.stopWordCount >= minNumbOfStopWord && el.wordCount >= minNumbOfWord && !el.isHighLinkDensity) {
        el.isPotential = true
      }
    })
  }
}
