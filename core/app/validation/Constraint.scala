package validation

import play.api.data.validation.Constraints

/**
 * The Class Constraint.
 *
 * @author Nguyen Duc Dung
 * @since 12/6/12 3:11 PM
 *
 */
object Constraint {

  lazy val urlConstraint = Constraints.pattern(
    "^(http|https)\\://[a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{1,3}(:[a-zA-Z0-9]*)?/?([a-zA-Z0-9\\-\\._\\?\\,\\'/\\\\\\+&amp;%\\$#\\=~])*[^\\.\\,\\)\\(\\s]$".r,
    error = "error.url"
  )

}
