package taller

import org.scalatest.funsuite.AnyFunSuite

class ConjuntosDifusosTest extends AnyFunSuite {

  import ConjuntosDifusos._

  val g1: ConjDifuso = grande(2, 3)
  val g2: ConjDifuso = grande(4, 2)

  test("pertenece devuelve el mismo valor que la función del conjunto") {
    for (n <- 0 to 10) {
      assert(math.abs(pertenece(n, g1) - g1(n)) < 1e-8)
    }
  }

  test("complemento de un conjunto difuso devuelve 1 - pertenencia") {
    for (n <- 0 to 10) {
      val valor = pertenece(n, g1)
      val comp = complemento(g1)
      assert(math.abs(comp(n) - (1.0 - valor)) < 1e-8)
    }
  }

  test("union devuelve el máximo de las pertenencias") {
    val unionG = union(g1, g2)
    for (n <- 0 to 10) {
      val esperado = math.max(g1(n), g2(n))
      assert(math.abs(unionG(n) - esperado) < 1e-8)
    }
  }

  test("interseccion devuelve el mínimo de las pertenencias") {
    val interG = interseccion(g1, g2)
    for (n <- 0 to 10) {
      val esperado = math.min(g1(n), g2(n))
      assert(math.abs(interG(n) - esperado) < 1e-8)
    }
  }

  test("inclusion detecta correctamente la inclusión de conjuntos difusos") {
    assert(inclusion(g1, g1, 10))
    val c1 = (x: Int) => 0.2
    val c2 = (x: Int) => 0.5
    assert(inclusion(c1, c2, 10))
    assert(!inclusion(c2, c1, 10))
  }

  test("igualdad detecta conjuntos difusos iguales dentro de la tolerancia") {
    val g3 = grande(2, 3)
    assert(igualdad(g1, g3))
    val g4 = grande(2, 4)
    assert(!igualdad(g1, g4))
  }
}
