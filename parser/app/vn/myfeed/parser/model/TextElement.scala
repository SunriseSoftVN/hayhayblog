package vn.myfeed.parser.model

import breeze.text.tokenize.WhitespaceTokenizer
import org.jsoup.nodes.Element
import org.apache.commons.lang3.StringUtils
import vn.myfeed.parser.util.StopWordCounter
import vn.myfeed.parser.util.ArticleUtil._
import RichSoup._

/**
 * This class represent for a text block inside a html page.
 * It using for scoring to decide witch block is content block.
 *
 * @author Nguyen Duc Dung
 * @since 12/19/12, 6:11 PM
 */

class TextElement(_jsoupElement: Element)(implicit article: Article) extends ArticleElement(_jsoupElement) {

  private val _counter = new StopWordCounter(article.languageCode)
  private val _tokenizer = WhitespaceTokenizer
  private val _text = jsoupEl.text

  var stopWordCount = 0
  var wordCount = 0

  if (StringUtils.isNotBlank(_text)) {
    stopWordCount = _counter.count(_text)
    wordCount = _tokenizer(_text).size
  }

  def text = _text

  override def score = wordCount * (stopWordCount + 1)

  /**
   * Checks the density of links within this element
   */
  def isHighLinkDensity: Boolean = {
    val linksElement = jsoupEl.select("a")
    if (!linksElement.isEmpty) {
      val numberWordInLink: Double = _tokenizer(linksElement.text()).size
      val score = numberWordInLink / wordCount * 100
      //If word in link more than 40% then return true
      if (score > 90) return true
    }
    false
  }

  override def toString = text
}

object TextElementMatcher {
  def unapply(jsoupElement: Element) = if (isArticleContentTag(jsoupElement.tagName.toLowerCase) && jsoupElement.isTextBlock) {
    Some(jsoupElement)
  } else if(jsoupElement.tagName == "pre") {
    Some(jsoupElement)
  } else {
    None
  }
}