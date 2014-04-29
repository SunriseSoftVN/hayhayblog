package vn.myfeed.parser.processor

import vn.myfeed.parser.model.Article
import org.apache.commons.lang3.StringUtils

/**
 * This class will base on the title to find potential element.
 *
 * @author Nguyen Duc Dung
 * @since 12/24/12 8:13 AM
 *
 */
class TitleBaseFilter extends Processor {
  def process(implicit article: Article) {
    if (StringUtils.isNotBlank(article.title)) {
      article.elements.foreach(element => if (element.text == article.title) {
        element.isTitle = true
      })
    }
  }
}
