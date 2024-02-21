package io.github.edadma.jixie

import pprint.pprintln

import io.github.edadma.json.DefaultJSONReader

import math.Ordered.orderingToOrdered

@main def run(): Unit =
//  val a: Comparable[Any] = "3".asInstanceOf[Comparable[Any]]
//  val b: Comparable[Any] = "4".asInstanceOf[Comparable[Any]]
//
//  println(a.compare(b))

  val interp = new Interpreter
  val program =
    DefaultJSONReader
      .fromString(
        """
        |[
        |  ["define", "x", 3],
        |  ["display", ["+", "x", 4]],
        |  ["display", ["sqrt", 2]],
        |  ["display", ["<", "x", 4, 5]],
        |  ["define", "y", ["quote", "x"]],
        |  ["display", "y"]
        |]
        |""".stripMargin,
      )
      .asInstanceOf[Seq[Any]]
  val result = interp.run(program)

//  pprintln(result)
