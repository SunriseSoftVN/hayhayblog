package admin.controllers

import play.api.mvc._
import play.api.i18n.Messages
import play.api.templates.Html

/**
 * The Class AdminTemplate.
 *
 * @author Nguyen Duc Dung
 * @since 1/7/14 1:06 PM
 *
 */
trait AdminTemplate extends Controller {

  def renderOk(content: Html,
               title: String = Messages("application.title"),
               description: String = Messages("application.description")) = Ok(
    admin.views.html.tml.main(content, title, description)
  )

  def renderBadRequest(content: Html,
               title: String = Messages("application.title"),
               description: String = Messages("application.description")) = BadRequest(
    admin.views.html.tml.main(content, title, description)
  )

}
