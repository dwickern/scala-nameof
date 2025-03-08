package com.github.dwickern.macros

import scala.quoted.*

trait NameOf {
  /**
    * Obtain an identifier name as a constant string.
    *
    * Example usage:
    * {{{
    *   val amount = 5
    *   nameOf(amount) => "amount"
    * }}}
    */
  transparent inline def nameOf(inline expr: Any): String = ${NameOfImpl.nameOf('expr)}

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
  transparent inline def nameOf[T](inline expr: T => Any): String = ${NameOfImpl.nameOf('expr)}

  /**
   * Obtain a fully qualified identifier name as a constant string.
   *
   * This overload can be used to access an instance method without having an instance of the type.
   *
   * Example usage:
   * {{{
   *   class Pet(val age: Int)
   *   class Person(val name: String, val pet: Pet)
   *   nameOf[Person](_.pet.age) => "pet.age"
   * }}}
   */
  transparent inline def qualifiedNameOf[T](inline expr: T => Any): String = ${NameOfImpl.qualifiedNameOf('expr)}

  /**
    * Obtain a type's unqualified name as a constant string.
    *
    * Example usage:
    * {{{
    *   nameOfType[String] => "String"
    *   nameOfType[fully.qualified.ClassName] => "ClassName"
    * }}}
    */
  transparent inline def nameOfType[T]: String = ${NameOfImpl.nameOfType[T]}

  /**
    * Obtain a type's qualified name as a constant string.
    *
    * Example usage:
    * {{{
    *   nameOfType[String] => "java.lang.String"
    *   nameOfType[fully.qualified.ClassName] => "fully.qualified.ClassName"
    * }}}
    */
  transparent inline def qualifiedNameOfType[T]: String = ${NameOfImpl.qualifiedNameOfType[T]}
}
object NameOf extends NameOf
