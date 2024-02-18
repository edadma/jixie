package io.github.edadma.jixie

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

  List(BuiltinFunction("+", args => args(0).asInstanceOf[Double] + args(1).asInstanceOf[Double])) foreach {
    case f @ BuiltinFunction(name, _) => functions(name) = f
  }

  def run(code: Seq[Any]): Any = eval(global, code)

  def form(expr: Any, args: Type*): Unit =
    forms += Form(expr, args.toIndexedSeq)
