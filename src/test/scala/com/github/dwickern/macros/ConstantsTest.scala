package com.github.dwickern.macros

import com.github.dwickern.macros.NameOf._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class ConstantsTest extends AnyFunSuite with Matchers {

  test("user-defined constants, qualified") {
    nameOf(JavaConstants.SOME_CONST) should equal ("SOME_CONST")
    nameOf(JavaConstants.`SOME_CONST`) should equal ("SOME_CONST")
    nameOf(JavaConstants.Inner.INNER_CONSTANT) should equal ("INNER_CONSTANT")
    nameOf(com.github.dwickern.macros.JavaConstants.Inner.INNER_CONSTANT) should equal ("INNER_CONSTANT")

    import com.github.dwickern.macros.{JavaConstants => Aliased}
    nameOf(Aliased.SOME_CONST) should equal ("SOME_CONST")

    import JavaConstants._
    nameOf(Inner.INNER_CONSTANT) should equal ("INNER_CONSTANT")
  }

  test("user-defined constants, unqualified") {
    import JavaConstants._
    nameOf(SOME_CONST) should equal ("SOME_CONST")

    import JavaConstants.Inner._
    nameOf(INNER_CONSTANT) should equal ("INNER_CONSTANT")
  }

  test("system constants, qualified") {
    nameOf(java.lang.Byte.MAX_VALUE) should equal ("MAX_VALUE")
    nameOf(Byte.MaxValue) should equal ("MaxValue")
  }

  test("system constants, unqualified") {
    import java.lang.Byte.MAX_VALUE
    nameOf(MAX_VALUE) should equal ("MAX_VALUE")
  }

  test("qualified invocations") {
    NameOf.nameOf(JavaConstants.SOME_CONST) should equal ("SOME_CONST")
    com.github.dwickern.macros.NameOf.nameOf(JavaConstants.SOME_CONST) should equal ("SOME_CONST")
  }
}
