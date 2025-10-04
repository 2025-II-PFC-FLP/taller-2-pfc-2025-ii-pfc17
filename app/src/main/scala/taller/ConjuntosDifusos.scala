package taller

  object ConjuntosDifusos {

    type ConjDifuso = Int => Double

    def pertenece(elem: Int, s: ConjDifuso): Double = s(elem)

    def grande(d: Int, e: Int): ConjDifuso = {
      require(d >= 1, "d debe ser mayor o igual a 1")
      require(e > 1, "e debe ser mayor que 1")
      (n: Int) => {
        if (n <= 0) 0.0
        else {
          val base = n.toDouble / (n + d)
          math.pow(base, e)
        }
      }
    }

    def complemento(c: ConjDifuso): ConjDifuso = {
      (n: Int) => 1.0 - c(n)  //Entre mas pertenece al conjunto c, menos pertenece a su complemento
    }

    def union(cd1: ConjDifuso, cd2: ConjDifuso): ConjDifuso = {
      (n: Int) => math.max(cd1(n), cd2(n))
    } 

    def interseccion(cd1: ConjDifuso, cd2: ConjDifuso): ConjDifuso = {
      (n: Int) => math.min(cd1(n), cd2(n))
    }
    def inclusion(cd1: ConjDifuso, cd2: ConjDifuso, limite: Int): Boolean = {
      @annotation.tailrec
      def loop(n: Int): Boolean = {
        if (n > limite) true
        else if (cd1(n) > cd2(n)) false
        else loop(n + 1)
      }
      loop(0)
    }
    def igualdad(cd1: ConjDifuso, cd2: ConjDifuso): Boolean = {
      val limite = 1000          // Dominio finito evaluado (de 0 a 1'00)
      val tolerancia = 1e-6 // Tolerancia para comparar números en punto flotante

      @annotation.tailrec
      def loop(n: Int): Boolean = {
        if (n > limite) true
        else {
          val diferencia = math.abs(cd1(n) - cd2(n))
          if (diferencia > tolerancia) false
          else loop(n + 1)
        }
      }
      loop(0)
    }

}

  }
