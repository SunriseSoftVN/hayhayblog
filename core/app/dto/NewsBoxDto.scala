package dto

import model.Category

/**
 * The Class NewsBoxDto.
 *
 * @author Nguyen Duc Dung
 * @since 2/3/14 1:44 AM
 *
 */
case class NewsBoxDto(
                       title: String,
                       shortName: String
                       )

object NewsBoxDto {

  def apply(cat: Category) = new NewsBoxDto(
    title = cat.name,
    shortName = cat.shortName
  )

}