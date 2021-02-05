"nameOf" macro for scala
========================

[![Build](https://github.com/dwickern/scala-nameof/workflows/build/badge.svg)](https://github.com/dwickern/scala-nameof/actions)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.dwickern/scala-nameof_2.13/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.dwickern/scala-nameof_2.13)

Get the name of an variable, function, class member, or type as a string--at compile-time!

Inspired by the [nameof](https://msdn.microsoft.com/en-us/library/dn986596.aspx) operator in C#

> Used to obtain the simple (unqualified) string name of a variable, type, or member. When reporting errors in code, hooking up model-view-controller (MVC) links, firing property changed events, etc., you often want to capture the string name of a method. Using nameof helps keep your code valid when renaming definitions. Before you had to use string literals to refer to definitions, which is brittle when renaming code elements because tools do not know to check these string literals.

Usage
=====

Add the library as "provided", because it's only needed during compilation and not at runtime:

    libraryDependencies += "com.github.dwickern" %% "scala-nameof" % "2.0.0" % "provided"


Now you can use `nameOf` to get the name of a variable or class member:
```scala
  import com.github.dwickern.macros.NameOf._

  case class Person(name: String, age: Int)

  def toMap(person: Person) = Map(
    nameOf(person.name) -> person.name,
    nameOf(person.age) -> person.age
  )

  // compiles to:

  def toMap(person: Person) = Map(
    "name" -> person.name,
    "age" -> person.age
  )
```

To get the name of a function:
```scala
  import com.github.dwickern.macros.NameOf._

  def startCalculation(value: Int): Unit = {
    println(s"Entered ${nameOf(startCalculation _)}")
  }

  // compiles to:

  def startCalculation(value: Int): Unit = {
    println(s"Entered startCalculation")
  }
```

Without having an instance of the type:
```scala
  import com.github.dwickern.macros.NameOf._

  case class Person(name: String, age: Int) {
    def sayHello(other: Person) = s"Hello ${other.name}!"
  }
  
  println(nameOf[Person](_.age))

  // compiles to:

  println("age")

  println(nameOf[Person](_.sayHello(???))
  
  //compiles to: 
  println("sayHello")
```

You can also use `nameOfType` to get the unqualified name of a type:
```scala
  import com.github.dwickern.macros.NameOf._

  println(nameOf[java.lang.String])

  // compiles to:

  println("String")
```

And `qualifiedNameOfType` to get the qualified name:
```scala
  import com.github.dwickern.macros.NameOf._

  println(qualifiedNameOfType[java.lang.String])

  // compiles to:

  println("java.lang.String")
```


Development
===========

To run tests for all compilation targets:

    sbt +test

To publish to your local ivy repository:

    sbt +publishLocal

To publish to maven central (requires authorization):

    sbt release


License
=======

See [LICENSE](LICENSE.md) (MIT).
