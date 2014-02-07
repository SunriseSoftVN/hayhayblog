package controllers

import play.api.mvc.{Action, Controller}
import dao.{TagDao, BlogDao, ArticleDao}
import org.apache.commons.lang3.StringUtils

/**
 * The Class CounterCtr.
 *
 * @author Nguyen Duc Dung
 * @since 2/3/14 2:48 PM
 *
 */
object CounterCtr extends Controller {

  def count(blogName: String, uniqueTitle: String) = Action {
    ArticleDao.findByUniqueTitleAndBlogName(blogName, uniqueTitle).map(article => {
      article.tagList.foreach(tagName => {
        if(StringUtils.isNotBlank(tagName)) {
          val tag = TagDao.findOrCreate(tagName)
          TagDao.save(tag.copy(read = tag.read + 1))
        }
      })
      ArticleDao.save(article.copy(clicked = article.clicked + 1))
      BlogDao.increaseRead(article.blogId)
      Redirect(article.url)
    }).getOrElse(NotFound)
  }

}
