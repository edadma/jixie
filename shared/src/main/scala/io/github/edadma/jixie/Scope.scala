package io.github.edadma.jixie

import scala.collection.mutable

class Scope:
  val defines = new mutable.HashMap[String, Define]

  def get(name: String): Option[Any] = defines get name

  def set(name: String, value: Any): Unit =
    get(name) match
      case None     => problem(s"'$name' not found")
      case Some(df) => df.asInstanceOf[Variable].value = value

  def contains(name: String): Boolean = defines contains name

  def define(df: Define): Unit =
    if defines contains df.name then problem(s"'${df.name}' is already defined")
    else defines(df.name) = df

  def apply(name: String): Define = defines(name)
