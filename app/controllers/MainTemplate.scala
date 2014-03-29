package controllers

import play.api.templates.Html
import play.api.i18n.Messages
import play.api.mvc.SimpleResult
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
trait MainTemplate extends ControllerHelper {


  def navBar(implicit dto: TopMenuDto) = views.html.partials.navbar(dto)

  def sideBar(implicit dto: TopMenuDto) = views.html.partials.sidebar(dto)

  def footer = views.html.partials.footer()

  def renderOk(content: Html,
               title: String = Messages("application.title"),
               description: String = Messages("application.description"))
              (implicit user: Option[User],
               dto: TopMenuDto = TopMenuDto(CategoryDao.all, "home")) = Ok(
    views.html.tml.main(content, title, description, navBar, sideBar, footer)
  )

  def renderBadRequest(content: Html,
                       title: String = Messages("application.title"),
                       description: String = Messages("application.description"))
                      (implicit user: Option[User],
                       dto: TopMenuDto = TopMenuDto(CategoryDao.all, "home")) = BadRequest(
    views.html.tml.main(content, title, description, navBar, sideBar, footer)
  )
}
