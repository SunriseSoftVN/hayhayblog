package controllers

import play.api.templates.Html
import play.api.i18n.Messages
import play.api.mvc.{SimpleResult, Controller}
import model.User
import dao.CategoryDao

/**
 * The Class MainTemplate.
 *
 * @author Nguyen Duc Dung
 * @since 1/31/14 5:18 PM
 *
 */
trait MainTemplate extends Controller {


  def navbar(user: Option[User]) = views.html.partials.navbar(user, CategoryDao.all)

  def renderOk(content: Html,
               title: String = Messages("application.title"),
               description: String = Messages("application.description"))(implicit user: Option[User]) = Ok(
    views.html.tml.main(content, title, description, navbar(user))
  )

  def renderBadRequest(content: Html,
                       title: String = Messages("application.title"),
                       description: String = Messages("application.description"))(implicit user: Option[User]) = BadRequest(
    views.html.tml.main(content, title, description, navbar(user))
  )

  implicit class RichOption[E](op: Option[E]) {
    def mapRender(f: E => SimpleResult) = op.map(f).getOrElse(NotFound)
  }

}
