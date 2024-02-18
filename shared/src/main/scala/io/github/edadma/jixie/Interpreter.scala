package io.github.edadma.jixie

import scala.collection.mutable.ListBuffer

abstract class Type
case object STRING extends Type
case object NUMBER extends Type
case object ANY extends Type

case class Form(expr: Any, args: IndexedSeq[Type])

class Interpreter:
  val global: Scope = new Scope
  val forms = new ListBuffer[Form]

  def run(code: Seq[Any]): Any = eval(global, code)

  def form(expr: Any, args: Type*): Unit =
    forms += Form(expr, args.toIndexedSeq)
