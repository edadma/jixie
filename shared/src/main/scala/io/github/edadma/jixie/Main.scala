package io.github.edadma.jixie

import pprint.pprintln

import io.github.edadma.json.DefaultJSONReader

@main def run(): Unit =
  val interp = new Interpreter
  val program =
    DefaultJSONReader.fromString(
      """
        |[
        |  ["define", "x", 3],
        |  ["display", ["+", "x", 4]],
        |  ["define", "y", ["quote", "x"]],
        |  ["display", "y"]
        |]
        |""".stripMargin,
    )
  val result = interp.run(
    program.asInstanceOf[Seq[Any]],
  )

//  pprintln(result)
