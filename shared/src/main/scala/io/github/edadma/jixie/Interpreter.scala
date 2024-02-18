package io.github.edadma.jixie

import scala.annotation.tailrec
import scala.collection.mutable
import scala.collection.mutable.ListBuffer

abstract class Type
case object STRING extends Type
case object NUMBER extends Type
case object ANY extends Type

case class Form(expr: Any, args: IndexedSeq[Type])

abstract class JixieFunction { val name: String }
case class BuiltinFunction(name: String, func: IndexedSeq[Any] => Any) extends JixieFunction

class Interpreter:
  val global: Scope = new Scope
  val forms = new ListBuffer[Form]
  val functions = new mutable.HashMap[String, JixieFunction]

  List(
    BuiltinFunction("+", args => args(0).asInstanceOf[Number].doubleValue + args(1).asInstanceOf[Number].doubleValue),
  ) foreach { case f @ BuiltinFunction(name, _) =>
    functions(name) = f
  }

  def run(code: Seq[Any]): Any = eval(global, code)

  def form(expr: Any, args: Type*): Unit =
    forms += Form(expr, args.toIndexedSeq)

  def eval(sc: Scope, code: Any): Any =
    def eval(code: Any): Any =
      code match
        case Seq(name: String, args*) if functions contains name =>
          functions(name) match
            case BuiltinFunction(_, func) => func(args.toIndexedSeq)
            case _                        => ???
        case s: Seq[?] => evalSequence(s)
        case v         => v

    @tailrec
    def evalSequence(code: Seq[Any], result: Any = ()): Any =
      code match
        case head :: tail => evalSequence(tail, eval(head))
        case Nil          => result

    eval(code)
