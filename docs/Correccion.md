# Informe de corrección Taller 2

**Fundamentos de Programación Funcional y Concurrente**  
  
---

## Modelado formal

Sea un conjunto difuso ( \mu: \mathbb{Z} \to [0,1] ), es decir, una función que asigna a cada entero un valor en el intervalo ([0,1]) representando el grado de pertenencia.

Las funciones implementadas operan sobre conjuntos difusos y devuelven nuevos conjuntos difusos o valores booleanos según sus definiciones matemáticas.

---

## Funciones y corrección

### Función: `pertenece`

* **Definición matemática:**
  [
  \mu_A(x) = A(x)
  ]

* **Implementación en Scala:**

  ```scala
  def pertenece(elem: Int, s: ConjDifuso): Double = s(elem)
  ```

* **Corrección:**
  Esta función es una identidad funcional que devuelve el grado de pertenencia directamente. No tiene recursividad ni complejidad adicional, por lo que se considera correcta por definición.

---

### Función: `complemento`

* **Definición matemática:**
  [
  \mu_{A^c}(x) = 1 - \mu_A(x)
  ]

* **Implementación:**

  ```scala
  def complemento(c: ConjDifuso): ConjDifuso = (n: Int) => 1.0 - c(n)
  ```

* **Corrección:**
  Para todo ( x \in \mathbb{Z} ), dado que ( 0 \leq \mu_A(x) \leq 1 ), se garantiza que:
  [
  0 \leq 1 - \mu_A(x) \leq 1
  ]
  Por lo tanto, el complemento definido es también un conjunto difuso válido. La función es total y no recursiva, por lo que es correcta conforme a su especificación.

---

### Función: `union`

* **Definición matemática:**
  [
  \mu_{A \cup B}(x) = \max(\mu_A(x), \mu_B(x))
  ]

* **Implementación:**

  ```scala
  def union(cd1: ConjDifuso, cd2: ConjDifuso): ConjDifuso = (n: Int) => math.max(cd1(n), cd2(n))
  ```

* **Corrección:**
  Para cada ( x ), como ( \mu_A(x), \mu_B(x) \in [0,1] ),
  [
  \max(\mu_A(x), \mu_B(x)) \in [0,1]
  ]
  Además, la operación `max` es conmutativa y asociativa, propiedades deseables en la unión difusa. La función no es recursiva y produce resultados válidos, por lo que es correcta.

---

### Función: `interseccion`

* **Definición matemática:**
  [
  \mu_{A \cap B}(x) = \min(\mu_A(x), \mu_B(x))
  ]

* **Implementación:**

  ```scala
  def interseccion(cd1: ConjDifuso, cd2: ConjDifuso): ConjDifuso = (n: Int) => math.min(cd1(n), cd2(n))
  ```

* **Corrección:**
  Similar a la unión, el valor mínimo de dos números en ([0,1]) permanece en el intervalo. `min` también es conmutativa y asociativa. La función es correcta.

---

### Función: `inclusion`

* **Definición matemática:**
  [
  A \subseteq B \iff \forall x \in [0, limite],\ \mu_A(x) \leq \mu_B(x)
  ]

* **Implementación (recursiva):**

  ```scala
  def inclusion(cd1: ConjDifuso, cd2: ConjDifuso, limite: Int): Boolean = {
    @annotation.tailrec
    def loop(n: Int): Boolean = {
      if (n > limite) true
      else if (cd1(n) > cd2(n)) false
      else loop(n + 1)
    }
    loop(0)
  }
  ```

* **Corrección (por inducción estructural):**

  * **Caso base:**
    Para ( n = 0 ), el programa verifica que
    [
    cd1(0) \leq cd2(0)
    ]
    Si esto es cierto, continúa; si no, retorna `false`. Esto corresponde exactamente a la definición matemática.

  * **Paso inductivo:**
    Suponga que para un ( k \geq 0 ), se cumple que la función verifica la inclusión para todos ( n \leq k ). Entonces, para ( n = k+1 ), la función evalúa la condición y, si se cumple, continúa verificando para ( n = k+2 ), y así sucesivamente hasta ( limite ).

  * **Conclusión:**
    Por inducción estructural, la función retorna `true` si y solo si la inclusión se cumple en todos los puntos del dominio finito. La implementación es correcta.

---

### Función: `igualdad`

* **Definición matemática:**
  Dos conjuntos difusos son iguales si para todo ( x \in [0, limite] ),
  [
  |\mu_A(x) - \mu_B(x)| < \varepsilon
  ]
  para una tolerancia pequeña ( \varepsilon ).

* **Implementación:**

  ```scala
  def igualdad(cd1: ConjDifuso, cd2: ConjDifuso): Boolean = {
    val limite = 1000
    val tolerancia = 1e-6

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
  ```

* **Corrección:**

  * **Caso base:**
    Para ( n = 0 ), la función verifica si la diferencia está dentro de la tolerancia; de no ser así, retorna `false`.

  * **Paso inductivo:**
    Suponga que para ( n = k ) la condición se cumple, entonces la función verifica para ( n = k+1 ) y continúa hasta el límite.

  * **Conclusión:**
    La función verifica punto por punto que las diferencias sean menores que la tolerancia y por inducción estructural confirma que la igualdad difusa aproximada es correcta.

---

## Conclusiones generales

* Todas las funciones definidas para operaciones sobre conjuntos difusos cumplen con sus especificaciones matemáticas.
* Las funciones recursivas (`inclusion`, `igualdad`) fueron formalmente justificadas mediante inducción estructural, asegurando que siempre terminan y devuelven resultados correctos.
* Las funciones no recursivas (`pertenece`, `complemento`, `union`, `interseccion`) son correctas directamente por su correspondencia exacta con las definiciones matemáticas y por operar sobre valores dentro del rango esperado.
* Se confirma que la implementación es confiable para trabajar con conjuntos difusos discretos definidos en enteros y que respeta las propiedades fundamentales de la lógica difusa.

