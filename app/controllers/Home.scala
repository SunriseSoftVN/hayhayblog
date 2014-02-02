package controllers

import play.api.mvc._
import jp.t2v.lab.play2.auth.OptionalAuthElement
import auth.AuthConfigImpl
import dao.CategoryDao
import dto.NewsBoxDto

object Home extends Controller with OptionalAuthElement with AuthConfigImpl with MainTemplate {

  def index = StackAction {
    implicit request =>
      val newBosx = CategoryDao.all.map(cat => NewsBoxDto(cat))
      renderOk(views.html.index(newBosx))
  }

}