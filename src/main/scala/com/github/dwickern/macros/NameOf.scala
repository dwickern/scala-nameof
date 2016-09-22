package com.github.dwickern.macros

object NameOf {
  import scala.language.experimental.macros

  def nameOf(x: Any): String = macro NameOfImpl.nameOfImpl
  def nameOf[T](f: T => Any): String = macro NameOfImpl.nameOfMemberImpl
  def nameOfType[T]: String = macro NameOfImpl.nameOfTypeImpl[T]
}
