package com.github.dwickern.macros

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import shapeless3.test.illTyped

enum MyEnum {
  case EnumValue
}

class Scala3FeaturesTest extends AnyFunSuite with Matchers {
  import NameOf._

  test("enum") {
    nameOf(MyEnum) should equal ("MyEnum")
    nameOfType[MyEnum] should equal ("MyEnum")
    nameOfType[MyEnum.EnumValue.type] should equal ("MyEnum")
    qualifiedNameOfType[MyEnum] should equal ("com.github.dwickern.macros.MyEnum")
  }

  test("enum value") {
    nameOf(MyEnum.EnumValue) should equal ("EnumValue")
  }

  test("intersection type") {
    trait Foo
    trait Bar
    illTyped(""" nameOfType[Foo & Bar] """)
    illTyped(""" qualifiedNameOfType[Foo & Bar] """)
  }

  test("union type") {
    trait Foo
    trait Bar
    illTyped(""" nameOfType[Foo | Bar] """)
    illTyped(""" qualifiedNameOfType[Foo | Bar] """)
  }
}
