package controllers

import play.api.templates.Html
import play.api.i18n.Messages
import play.api.mvc.Controller

/**
 * The Class MainTemplate.
 *
 * @author Nguyen Duc Dung
 * @since 1/31/14 5:18 PM
 *
 */
trait MainTemplate extends Controller {

  def renderOk(content: Html,
               title: String = Messages("application.title"),
               description: String = Messages("application.description")) = Ok(
    views.html.tml.main(content, title, description)
  )

  def renderBadRequest(content: Html,
                       title: String = Messages("application.title"),
                       description: String = Messages("application.description")) = BadRequest(
    views.html.tml.main(content, title, description)
  )

}
