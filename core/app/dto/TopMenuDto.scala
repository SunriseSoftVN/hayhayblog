package dto

import model.Category

/**
 * The Class TopMenuDto.
 *
 * @author Nguyen Duc Dung
 * @since 2/3/14 4:49 AM
 *
 */
case class TopMenuDto(
                       categories: List[Category],
                       currentCat: String
                       )
