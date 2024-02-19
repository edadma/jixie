package io.github.edadma.jixie

import scala.annotation.tailrec
import scala.collection.immutable.Seq
import scala.collection.mutable
import scala.collection.mutable.ListBuffer

abstract class Type
case object STRING extends Type
case object NUMBER extends Type
case object ANY extends Type

case class Form(expr: Any, args: IndexedSeq[Type])

abstract class JixieFunction { val name: String }
case class BuiltinFunction(name: String, func: (Scope, IndexedSeq[Any]) => Any) extends JixieFunction

class Interpreter:
  val global: Scope = new Scope
  val forms = new ListBuffer[Form]
  val functions = new mutable.HashMap[String, JixieFunction]

  List(
    BuiltinFunction("+", (_, args) => args.asInstanceOf[Seq[Number]].map(_.doubleValue).sum),
    BuiltinFunction("begin", (sc, args) => evalExpressions(sc, args)),
  ) foreach { case f @ BuiltinFunction(name, _) =>
    functions(name) = f
  }

  def run(code: Seq[Any]): Any = evalExpressions(global, code)

  def form(expr: Any, args: Type*): Unit =
    forms += Form(expr, args.toIndexedSeq)

  def eval(sc: Scope, code: Any): Any =
    code match
      case Seq(name: String, args*) if functions contains name =>
        functions(name) match
          case BuiltinFunction(_, func) => func(sc, evalList(sc, args).toIndexedSeq)
          case _                        => ???
      case s: Seq[?] => evalExpressions(sc, s)
      case v         => v

  @tailrec
  final def evalExpressions(sc: Scope, code: Seq[Any], result: Any = ()): Any =
    code match
      case head :: tail => evalExpressions(sc, tail, eval(sc, head))
      case Nil          => result

  def evalList(sc: Scope, list: Seq[Any]): Seq[Any] = list map (a => eval(sc, a))
