package vn.myfeed.parser.model

import org.jsoup.nodes.Element
import collection.JavaConversions._
import java.util.regex.Pattern
import org.apache.commons.lang3.StringUtils

/**
 * The Class to warp jsoup element to add more function to it.
 *
 * @author Nguyen Duc Dung
 * @since 12/23/12 11:28 PM
 *
 */
class RichSoup(element: Element) {

  val hiddenStyle = Pattern.compile(".*display: none.*|" +
    ".*visibility: hidden.*|.*bottom: -.*px.*|.*left: -.*px.*|.*top: -.*px.*|.*right: -.*px.*", Pattern.CASE_INSENSITIVE)

  val centerStyle = Pattern.compile(".*text-align:.*center.*", Pattern.CASE_INSENSITIVE)

  /**
   * Which an element has attribute skip-parser is true, was marked by processor,
   * will be skip when parsing.
   * @return
   */
  def isSkipParse = element.attr("skip-parse") == "true"

  def isSkipParse_=(skip: Boolean) {
    element.attr("skip-parse", skip.toString)
  }

  /**
   * This attribute will be mark by white list processor.
   * @return
   */
  def isWhiteList = element.attr("white-list") == "true"

  /**
   * Check this element whether should get it's text or it's own text.
   * @return
   */
  def isTextBlock: Boolean = {
    if (element.tagName.toLowerCase == "a") return false
    var allIsInLineElement = true
    element.getAllElements.foreach(child => if (child != element) {
      if (child.isBlock || child.tagName == "a" || child.tagName == "select" || child.tagName == "img") {
        allIsInLineElement = false
      }
    })
    StringUtils.isNotBlank(element.ownText) && StringUtils.trim(element.ownText()).length > 10 || allIsInLineElement && StringUtils.isNotBlank(element.text)
  }


  /**
   * Check whether the element is hidden or not.
   * @return
   */
  def isHidden = hiddenStyle.matcher(element.attr("style")).matches() || element.hasClass("hidden")

  /**
   * Same with method getAllElements but exclude it's self.
   * @return
   */
  def innerAllElements = element.getAllElements.filter(_ != element)

  def isHeading = {
    val className = element.className.toLowerCase
    val tagName = element.tagName
    (tagName.startsWith("h") || className.contains("title") || className.contains("head") || className.contains("lead") || className.contains("summary")) && StringUtils.isNotBlank(element.text())
  }

  def isAlignCenter = centerStyle.matcher(element.attr("style")).matches()
}

object RichSoup {

  implicit def warp(element: Element) = new RichSoup(element)

}