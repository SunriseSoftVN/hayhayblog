package controllers

import play.api.templates.Html
import play.api.i18n.Messages
import play.api.mvc.Controller
import model.Article
import dto.TopMenuDto
import dao.CategoryDao

/**
 * The Class IFrameTemplate.
 *
 * @author Nguyen Duc Dung
 * @since 1/31/14 5:18 PM
 *
 */
trait IFrameTemplate extends Controller {

  def navBar(article: Option[Article])(implicit dto: TopMenuDto) = views.html.partials.navbar(dto, article)


  def renderOk(content: Html,
               article: Option[Article] = None,
               title: String = Messages("application.title"),
               description: String = Messages("application.description"))
              (implicit dto: TopMenuDto = TopMenuDto(CategoryDao.all, "home")) = Ok(
    views.html.tml.iframe(content, title, description, navBar(article), article)
  )


}
