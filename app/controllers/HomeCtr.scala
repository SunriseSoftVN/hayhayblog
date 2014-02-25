package controllers

import play.api.mvc._
import jp.t2v.lab.play2.auth.OptionalAuthElement
import auth.AuthConfigImpl
import dao.ArticleDao

object HomeCtr extends Controller with OptionalAuthElement with AuthConfigImpl with MainTemplate {

  def index = StackAction(implicit request => {
    renderOk(views.html.index(ArticleDao.latest(20)))
  })

}