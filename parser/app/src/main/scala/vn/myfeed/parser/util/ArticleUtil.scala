package vn.myfeed.parser.util

import org.apache.commons.lang3.StringUtils

/**
 * The Class ArticleUtil.
 *
 * @author Nguyen Duc Dung
 * @since 12/23/12 4:57 AM
 *
 */
object ArticleUtil {

  def isArticleContentTag(tagName: String) = StringUtils.isNotBlank(tagName) &&
    tagName != "title" &&
    tagName != "head" &&
    tagName != "meta" &&
    tagName != "script" &&
    tagName != "style" &&
    tagName != "link" &&
    tagName != "iframe" &&
    tagName != "select" &&
    tagName != "option" &&
    tagName != "noscript"

  def removeNonAlphabetsCharacter(st: String) = st.replaceAll("[^a-zA-Z0-9]", "")

  /**
   * Checking whether the title is contain the text or not, by skip non alphabet character.
   * @param title
   * @param text
   * @return
   */
  def titleContain(title: String, text: String) = {
    val cleanTitle = removeNonAlphabetsCharacter(title.toLowerCase)
    val cleanText = removeNonAlphabetsCharacter(text.toLowerCase)
    StringUtils.isNotBlank(cleanText) && cleanTitle.contains(cleanText)
  }

}
