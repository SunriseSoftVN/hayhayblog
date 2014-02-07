package controllers

import play.api.mvc._
import jp.t2v.lab.play2.auth.OptionalAuthElement
import auth.AuthConfigImpl
import dao.{BlogDao, ArticleDao, CategoryDao}
import dto.NewsBoxDto

object HomeCtr extends Controller with OptionalAuthElement with AuthConfigImpl with MainTemplate {

  def index = StackAction(implicit request => {
    val newsBoxs = CategoryDao.all.map(cat => {
      val (articles, totalPage) = ArticleDao.findByCatId(cat._id, page = 1, itemDisplay = 5)
      val blogs = BlogDao.findByCatId(cat._id)
      NewsBoxDto(cat, blogs, articles)
    })

    val blogs = BlogDao.all

    renderOk(views.html.index(newsBoxs, ArticleDao.mostRead(6), ArticleDao.latest(), blogs))
  })

}