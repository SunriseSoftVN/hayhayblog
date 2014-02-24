package actor

import akka.actor.Actor
import model.Article
import dao.ArticleDao
import com.mongodb.casbah.commons.MongoDBObject

/**
 * The Class Persistent.
 *
 * @author Nguyen Duc Dung
 * @since 2/24/14 12:41 PM
 *
 */
class Persistent extends Actor {
  override def receive = {
    case article: Article =>
      val exist = ArticleDao.findOne(MongoDBObject("description" -> article.description))
      if (exist.isEmpty) {
        ArticleDao.save(article)
      }
  }
}
