package controllers

import play.api.templates.Html
import play.api.i18n.Messages
import play.api.mvc.{SimpleResult, Controller}
import model.User
import dao.{ArticleDao, CategoryDao}
import dto.TopMenuDto

/**
 * The Class MainTemplate.
 *
 * @author Nguyen Duc Dung
 * @since 1/31/14 5:18 PM
 *
 */
trait MainTemplate extends Controller {

  def rightSideBar = views.html.partials.rightsidebar(ArticleDao.topTen)

  def topMenu(implicit dto: TopMenuDto) = views.html.partials.topmenu(dto)

  def footer = views.html.partials.footer(CategoryDao.all)

  def renderOk(content: Html,
               title: String = Messages("application.title"),
               description: String = Messages("application.description"))
              (implicit user: Option[User],
               dto: TopMenuDto = TopMenuDto(CategoryDao.all, "home")) = Ok(
    views.html.tml.main(content, title, description, topMenu, footer, rightSideBar)
  )

  def renderBadRequest(content: Html,
                       title: String = Messages("application.title"),
                       description: String = Messages("application.description"))
                      (implicit user: Option[User],
                       dto: TopMenuDto = TopMenuDto(CategoryDao.all, "home")) = BadRequest(
    views.html.tml.main(content, title, description, topMenu, footer, rightSideBar)
  )

  implicit class RichOption[E](op: Option[E]) {
    def mapRender(f: E => SimpleResult) = op.map(f).getOrElse(NotFound)
  }

}
