package views.partials

import scalatags.all._
import scalatags._
import dto.NewsBoxDto
import play.api.templates.Html

/**
 * The Class NewsBoxRender.
 *
 * @author Nguyen Duc Dung
 * @since 2/3/14 2:33 AM
 *
 */
object NewsBoxRender {

  def apply(newsBoxs: List[NewsBoxDto]) = Html(render(newsBoxs))

  def render(newsBoxs: List[NewsBoxDto]): String = {
    def renderBox(html: String) = div("left_box_2col".cls)(
      raw(html),
      div("clearfix".cls)
    ).toString()

    newsBoxs match {
      case Nil => ""
      case box :: Nil => renderBox(views.html.partials.newsbox(box).toString())
      case box1 :: box2 :: rest =>
        renderBox(
          views.html.partials.newsbox(box1).toString()
            + views.html.partials.newsbox(box2).toString()
        ) + render(rest)
    }
  }

}
