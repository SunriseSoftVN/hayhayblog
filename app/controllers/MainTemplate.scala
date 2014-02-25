package controllers

import play.api.templates.Html
import play.api.i18n.Messages
import play.api.mvc.{SimpleResult, Controller}
import model.User
import dao.CategoryDao
import dto.TopMenuDto

/**
 * The Class MainTemplate.
 *
 * @author Nguyen Duc Dung
 * @since 1/31/14 5:18 PM
 *
 */
trait MainTemplate extends Controller {


  def navBar(implicit dto: TopMenuDto) = views.html.partials.navbar(dto)

  def footer = views.html.partials.footer(CategoryDao.all)

  def renderOk(content: Html,
               title: String = Messages("application.title"),
               description: String = Messages("application.description"))
              (implicit user: Option[User],
               dto: TopMenuDto = TopMenuDto(CategoryDao.all, "home")) = Ok(
    views.html.tml.main(content, title, description, navBar, footer)
  )

  def renderBadRequest(content: Html,
                       title: String = Messages("application.title"),
                       description: String = Messages("application.description"))
                      (implicit user: Option[User],
                       dto: TopMenuDto = TopMenuDto(CategoryDao.all, "home")) = BadRequest(
    views.html.tml.main(content, title, description, navBar, footer)
  )

  implicit class RichOption[E](op: Option[E]) {
    def mapRender(f: E => SimpleResult) = op.map(f).getOrElse(NotFound)
  }

}
