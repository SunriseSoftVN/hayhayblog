package controllers

import play.api.templates.Html
import play.api.i18n.Messages
import play.api.mvc.SimpleResult
import model.{Blog, User}
import dao.{BlogDao, CategoryDao}
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

  def leftSideBar(implicit dto: TopMenuDto) = views.html.partials.leftSidebar(dto)
  def rightSideBar(implicit dto: TopMenuDto) = views.html.partials.rightSideBar(dto, BlogDao.top)

  def footer = views.html.partials.footer()

  def renderOk(content: Html,
               title: String = Messages("application.title"),
               description: String = Messages("application.description"))
              (implicit user: Option[User],
               dto: TopMenuDto = TopMenuDto(CategoryDao.all, "home")) = Ok(
    views.html.tml.main(content, title, description, navBar, leftSideBar, rightSideBar, footer)
  )

  def renderBadRequest(content: Html,
                       title: String = Messages("application.title"),
                       description: String = Messages("application.description"))
                      (implicit user: Option[User],
                       dto: TopMenuDto = TopMenuDto(CategoryDao.all, "home")) = BadRequest(
    views.html.tml.main(content, title, description, navBar, leftSideBar, rightSideBar, footer)
  )
}
