package com.github.dwickern.macros

import scala.annotation.tailrec
import scala.quoted.*

object NameOfImpl {
  def nameOf(expr: Expr[Any])(using Quotes): Expr[String] = {
    import quotes.reflect.*
    @tailrec def extract(tree: Tree): String = tree match {
      case Ident(name) => name
      case Select(_, name) => name
      case DefDef("$anonfun", _, _, Some(term)) => extract(term)
      case Block(List(stmt), _) => extract(stmt)
      case Block(_, term) => extract(term)
      case Apply(term, _) if term.symbol.fullName != "<special-ops>.throw" => extract(term)
      case TypeApply(term, _) => extract(term)
      case Inlined(_, _, term) => extract(term)
      case Typed(term, _) => extract(term)
      case _ => throw new MatchError(s"Unsupported expression: ${expr.show}")
    }
    val name = extract(expr.asTerm)
    Expr(name)
  }

  def qualifiedNameOf(expr: Expr[Any])(using Quotes): Expr[String] = {
    import quotes.reflect.*
    def extract(tree: Tree): List[String] = tree match {
      case Ident(name) => List(name)
      case Select(tree, name) => extract(tree) :+ name
      case DefDef("$anonfun", _, _, Some(term)) => extract(term)
      case Block(List(stmt), _) => extract(stmt)
      case Block(_, term) => extract(term)
      case Apply(term, _) if term.symbol.fullName != "<special-ops>.throw" => extract(term)
      case TypeApply(term, _) => extract(term)
      case Inlined(_, _, term) => extract(term)
      case Typed(term, _) => extract(term)
      case _ => throw new MatchError(s"Unsupported expression: ${expr.show}")
    }
    val name = extract(expr.asTerm).drop(1).mkString(".")
    Expr(name)
  }

  def nameOfType[T](using Quotes, Type[T]): Expr[String] = {
    import quotes.reflect.*
    val name = TypeTree.of[T].tpe.dealias match {
      case t @ (AndType(_, _) | OrType(_, _)) => throw new MatchError(s"Unsupported type: ${t.show}")
      case t => t.typeSymbol.name
    }
    val clean = name.stripSuffix("$")
    Expr(clean)
  }

  def qualifiedNameOfType[T](using Quotes, Type[T]): Expr[String] = {
    import quotes.reflect.*
    val fullName = TypeTree.of[T].tpe.dealias match {
      case t @ (AndType(_, _) | OrType(_, _)) => throw new MatchError(s"Unsupported type: ${t.show}")
      case t => t.typeSymbol.fullName
    }
    val clean = fullName
      .split('.')
      .map(_.stripPrefix("_$").stripSuffix("$"))
      .mkString(".")
    Expr(clean)
  }
}
