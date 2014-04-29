package vn.myfeed.parser.util

import org.jsoup.nodes.Element

/**
 * The Class DirtyElementPattern.
 *
 * @author Nguyen Duc Dung
 * @since 12/29/12 5:54 PM
 *
 */
object DirtyElementPattern extends FilePattern {

  val regexFile = "filter/remove.lst"
  override val start = ""
  override val end = ""
  override val next = " "

  def matchClass(el: Element) = regex.split(" ").exists(el.hasClass)

  def matchId(el: Element) = regex.split(" ").exists(_ == el.id)

}


