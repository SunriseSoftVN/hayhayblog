package vn.myfeed.parser.ui.controllers

import play.api.mvc.{Action, Controller}
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import vn.myfeed.parser.HtmlExtractor
import org.jsoup.Jsoup

/**
 * The Class Extractor.
 *
 * @author Nguyen Duc Dung
 * @since 6/16/13 12:26 PM
 *
 */
object Extractor extends Controller {
  def index = Action {
    Ok(vn.myfeed.parser.ui.views.html.extractor())
  }

  def parseHtml = Action {
    implicit request =>
      Form("html" -> nonEmptyText).bindFromRequest.fold(
        error => BadRequest,
        html => {
          val doc = Jsoup.parse(html)
          val extractor = new HtmlExtractor
          extractor.extract(doc)
          Ok(Json.obj(
            "text" -> extractor.text,
            "images" -> extractor.images
          ))
        }
      )
  }

}
