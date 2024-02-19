package io.github.edadma.jixie

import pprint.pprintln

import io.github.edadma.json.DefaultJSONReader

@main def run(): Unit =
  val interp = new Interpreter
  val program =
    DefaultJSONReader.fromString(
      """
        |[
        |  ["display", 123]
        |]
        |""".stripMargin,
    )
  val result = interp.run(
    program.asInstanceOf[Seq[Any]],
  ) // Seq(Seq("define", "x", 3), Seq("display", "answer", Seq("+", "x", 4))))

//  pprintln(result)

//  Seq(1, 2, 3) match
//    case Seq(a, b*) => pprintln((a, b))
//    case _          => println("no match")
