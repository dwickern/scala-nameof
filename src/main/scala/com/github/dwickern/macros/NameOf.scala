package com.github.dwickern.macros

trait NameOf {
  import scala.language.experimental.macros

  /**
    * Obtain an identifier name as a constant string.
    *
    * Example usage:
    * {{{
    *   val amount = 5
    *   nameOf(amount) => "amount"
    * }}}
    */
  def nameOf(expr: Any): String = macro NameOfImpl.nameOf

  /**
    * Obtain an identifier name as a constant string.
    *
    * This overload can be used to access an instance method without having an instance of the type.
    *
    * Example usage:
    * {{{
    *   class Person(val name: String)
    *   nameOf[Person](_.name) => "name"
    * }}}
    */
  def nameOf[T](expr: T => Any): String = macro NameOfImpl.nameOf

  /**
    * Obtain a type's name as a constant string.
    *
    * Example usage:
    * {{{
    *   nameOfType[String] => "String"
    *   nameOfType[fully.qualified.ClassName] => "ClassName"
    * }}}
    */
  def nameOfType[T]: String = macro NameOfImpl.nameOfType[T]
}
object NameOf extends NameOf
