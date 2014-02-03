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
    val split = newsBoxs.length / 2
    val (col1, col2) = newsBoxs.splitAt(split)

    def renderBox(html: String, col2: Boolean = false) =
      div(
        if(col2) "col-md-6 col2".cls else "col-md-6".cls
      )(
        raw(html),
        div("homepageboxlast".cls)(raw("&nbsp;"))
      ).toString()

    renderBox(col1.map(box => views.html.partials.newsbox(box).toString()).mkString) +
      renderBox(col2.map(box => views.html.partials.newsbox(box).toString()).mkString, true)
  }

}
