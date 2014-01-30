package dto

import play.api.mvc._
import utils.{Options, String2Int}


/**
 * The Class PageDto.
 *
 * @author Nguyen Duc Dung
 * @since 1/6/14 3:10 AM
 *
 */
case class PageDto[E](
                       items: Seq[E] = Nil,
                       fieldName: Option[String],
                       filter: Option[String] = None,
                       currentPage: Int = 0,
                       totalPage: Int = 0,
                       orderedField: Option[String],
                       sortAsc: Int = 1,
                       itemDisplay: Int = 50,
                       colHeader: List[(String, Int)] = Nil
                       ) {

  override def toString = s"?filter=${filter.getOrElse("")}&page=$currentPage&orderedField=$orderedField"

  def previousPage = s"?filter=${filter.getOrElse("")}&page=${currentPage - 1}&orderedField=$orderedField"

  def nextPage = s"?filter=${filter.getOrElse("")}&page=${currentPage + 1}&orderedField=$orderedField"
}

object PageDto {

  def apply[E](request: Request[AnyContent]) = {
    val filter = request.getQueryString("filter")
    val currentPage = request.getQueryString("page").collect {
      case String2Int(_page) => _page
    }.getOrElse(1)

    val sortAsc = request.getQueryString("sortAsc").collect {
      case String2Int(_page) => _page
    }.getOrElse(1)

    val orderedField = request.getQueryString("orderedField")

    val fieldName = request.getQueryString("fieldName")

    new PageDto[E](
      filter = Options.trim(filter),
      currentPage = currentPage,
      orderedField = orderedField,
      fieldName = fieldName,
      sortAsc = sortAsc
    )
  }

}