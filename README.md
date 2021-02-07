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
```sbt
libraryDependencies += "com.github.dwickern" %% "scala-nameof" % "3.0.0" % "provided"
```

And import the package:
```scala mdoc
import com.github.dwickern.macros.NameOf._
```

Now you can use `nameOf` to get the name of a variable or class member:
```scala mdoc:nest
case class Person(name: String, age: Int)

def toMap(person: Person) = Map(
  nameOf(person.name) -> person.name,
  nameOf(person.age) -> person.age
)
```
``` mdoc:nest
// compiles to:

def toMap(person: Person) = Map(
  "name" -> person.name,
  "age" -> person.age
)
```

To get the name of a function:
```scala mdoc:nest
def startCalculation(value: Int): Unit = {
  println("Entered " + nameOf(startCalculation _))
}
```
``` mdoc:nest
// compiles to:

def startCalculation(value: Int): Unit = {
  println("Entered startCalculation")
}
```

Without having an instance of the type:
```scala mdoc:nest
case class Person(name: String, age: Int) {
  def sayHello(other: Person) = s"Hello ${other.name}!"
}

println(nameOf[Person](_.age))
println(nameOf[Person](_.sayHello(???)))
```
``` mdoc:nest
// compiles to:

println("age")
println("sayHello")
```

You can also use `nameOfType` to get the unqualified name of a type:
```scala mdoc:nest
println(nameOfType[java.lang.String])
```
``` mdoc:nest
// compiles to:

println("String")
```

And `qualifiedNameOfType` to get the qualified name:
```scala mdoc:nest
println(qualifiedNameOfType[java.lang.String])
```
``` mdoc:nest
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
