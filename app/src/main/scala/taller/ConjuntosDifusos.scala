package taller

type ConjDifuso = Int => Double

class ConjuntosDifusos {
  def grande(d: Int, e: Int): ConjDifuso = {
    require(d >= 1, "d debe ser mayor o igual a 1")
    require(e > 1, "e debe ser mayor que 1")
    (n: Int) => {
      if (n <= 0) 0.0 //0 o numeros negativos no se evaluan
      else {
        val base = n.toDouble / (n + d)
        math.pow(base, e)  //uso directo formula
      }
    }
  }

  def pertenece(elem: Int, s: ConjDifuso): Double = s(elem)
}

