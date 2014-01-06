package model

/**
 * The Class Permission.
 *
 * @author Nguyen Duc Dung
 * @since 10/5/13 6:33 PM
 *
 */
case class Permission(value: String)

object Administrator extends Permission("Administrator")

object NormalUser extends Permission("NormalUser")

object Role {
  def asSelectValue() = Seq(
    NormalUser.value -> NormalUser.value,
    Administrator.value -> Administrator.value
  )
}