package controllers

import play.api.mvc.{Action, Controller}
import dao.{TagDao, BlogDao, ArticleDao}
import org.apache.commons.lang3.StringUtils

/**
 * The Class DetailCtr.
 *
 * @author Nguyen Duc Dung
 * @since 2/3/14 2:48 PM
 *
 */
object DetailCtr extends Controller with IFrameTemplate {

  def view(blogName: String, uniqueTitle: String) = Action {
    ArticleDao.findByUniqueTitleAndBlogName(blogName, uniqueTitle).map(article => {
      article.tagList.foreach(tagName => {
        if (StringUtils.isNotBlank(tagName)) {
          val tag = TagDao.findOrCreate(tagName)
          TagDao.save(tag.copy(read = tag.read + 1))
        }
      })
      ArticleDao.save(article.copy(clicked = article.clicked + 1))
      BlogDao.increaseRead(article.blogId)

      if (article.url.contains("youtube.com")) {
        Redirect(article.url)
      } else {
        renderOk(
          views.html.article.embed(article),
          description = article.shortDescription(200),
          title = article.title,
          article = Some(article)
        )
      }

    }).getOrElse(NotFound)
  }

}
