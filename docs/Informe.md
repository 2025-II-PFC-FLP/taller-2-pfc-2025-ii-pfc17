# 🧮 Informe Taller 2 – Conjuntos Difusos

**Universidad del Valle**  
**Programa:** Tecnología en Desarrollo de Software  
**Asignatura:** Programación Funcional y Concurrente  
**Estudiante:** Christian David Trujillo 2569134
                David Naranjo *codigo jajaja*
**Fecha:** 05/10/2025  

---

## 1. Introducción

Este taller tiene como objetivo implementar y analizar el comportamiento de **conjuntos difusos** en el lenguaje **Scala**, aplicando principios de **programación funcional** y **razonamiento matemático formal**.

Un conjunto difuso \( A \) está definido por una función de pertenencia:  

\[
\mu_A : X \rightarrow [0, 1]
\]

donde a cada elemento \( x \in X \) se le asigna un grado de pertenencia \( \mu_A(x) \).

---

## 2. Representación funcional

En Scala, un conjunto difuso se representa como una **función pura**:

```scala
type ConjDifuso = Int => Double
```

Esto significa que cada conjunto difuso es una **aplicación matemática directa**, sin efectos secundarios, y cumple con los principios de **inmutabilidad** y **referential transparency**.

---

## 3. Operaciones matemáticas y su corrección

**Pertenencia:**
Matemáticamente se define como \( \mu_A(x) = A(x) \).
En la implementación funcional, se expresa como def pertenece(elem, s) = s(elem).
Esta función simplemente devuelve el valor de pertenencia del elemento en el conjunto, por lo que actúa como una identidad funcional, garantizando corrección directa.

**Complemento:**
La definición es \( \mu_{A^c}(x) = 1 - \mu_A(x) \).
En el código, se implementa como (n: Int) => 1.0 - c(n).
Dado que los valores de pertenencia están en el rango \([0, 1]\), al restarlos de 1 se preserva ese mismo rango, asegurando que el resultado siga siendo un conjunto difuso válido.

**Unión:**
La definición matemática es \( \mu_{A \cup B}(x) = \max(\mu_A(x), \mu_B(x)) \).
Su versión funcional es (n: Int) => math.max(cd1(n), cd2(n)).
El uso de max mantiene los valores dentro del intervalo \([0, 1]\) y garantiza las propiedades conmutativa y asociativa de la unión difusa.

**Intersección:**
Definida como \( \mu_{A \cap B}(x) = \min(\mu_A(x), \mu_B(x)) \).
En el código: (n: Int) => math.min(cd1(n), cd2(n)).
La función min también preserva las cotas del dominio y cumple las propiedades conmutativa y asociativa, por lo que su implementación es correcta.

**Inclusión:**
La relación \( A \subseteq B \iff \forall x,\ \mu_A(x) \le \mu_B(x) \) se implementa con una función recursiva que compara cada punto hasta un límite finito.
Si en todo el dominio se cumple \( \mu_A(x) \le \mu_B(x) \), la función retorna true.
Su corrección se justifica por inducción sobre el dominio, ya que la función revisa cada caso sucesivamente.

**Igualdad:**
Dos conjuntos son iguales si \( |\mu_A(x) - \mu_B(x)| < \varepsilon \), con una tolerancia \( \varepsilon = 10^{-5} \).
La función implementada compara los valores de pertenencia con esa tolerancia, asegurando una igualdad aproximada entre funciones continuas.

---

## 4. Pila de llamadas (Call Stack) en funciones recursivas

La única función recursiva es `inclusion`.  
Se usa **recursión de cola (tail recursion)**, lo cual significa que el compilador puede optimizar la recursión para no consumir pila adicional.

### Código:

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

### Estado de la pila (ejemplo con `limite = 3`):

```mermaid
sequenceDiagram
    participant Main
    participant inclusion
    participant loop

    Main->>inclusion: inclusion(g1, g2, 3)
    inclusion->>loop: n = 0
    loop-->>loop: n = 1
    loop-->>loop: n = 2
    loop-->>loop: n = 3
    loop-->>inclusion: retorna true
    inclusion-->>Main: true
```

### Pila expandida paso a paso

```mermaid
graph TD
    A0[Inicio: n=0] --> A1[Evalúa cd1(0) <= cd2(0)]
    A1 --> A2[llamada recursiva loop(1)]
    A2 --> A3[Evalúa cd1(1) <= cd2(1)]
    A3 --> A4[llamada recursiva loop(2)]
    A4 --> A5[Evalúa cd1(2) <= cd2(2)]
    A5 --> A6[llamada recursiva loop(3)]
    A6 --> A7[n > limite → return true]
```

### Explicación:
- Cada llamada **reutiliza el mismo frame de pila** gracias a `@tailrec`.  
- No hay acumulación de frames → complejidad **O(1)** en espacio.  
- Matemáticamente, se prueba correcta por **inducción sobre n**.

---

## 5. Pruebas y corrección empírica

Los **6 tests automáticos** de ScalaTest confirman la validez funcional y matemática de las operaciones:

|     Test      |                Propósito               | Resultado |
|---------------|----------------------------------------|-----------|
| `pertenece`   | Verifica equivalencia entre `pertenece`| Correcto  |
|               | y la función del conjunto              |           |
| `complemento` | Verifica que \(1 - A(x)\) sea correcto | Correcto  |
| `union`       | Verifica el uso de `max`               | Correcto  |
| `interseccion`| Verifica el uso de `min`               | Correcto  |
| `inclusion`   | Valida la recursión de cola y          |           |
|               | comparación total                      | Correcto  |
| `igualdad`    | Valida comparación                     | Correcto  |
|               | dentro de tolerancia                   |           |

---

## 6. Argumento formal de corrección

La corrección del código puede demostrarse mediante **inducción estructural**:

1. **Caso base:**  
   Para `n = 0`, `loop(0)` compara \( cd1(0) \le cd2(0) \).  
   Si se cumple, continúa; de lo contrario, retorna `false`.  

2. **Paso inductivo:**  
   Supongamos que para un `n = k` la función es correcta.  
   Entonces, para `n = k + 1`, se evalúa nuevamente y se cumple la misma condición recursiva.  
   Por lo tanto, la corrección se mantiene para todo `n ≤ limite`.

\[
\forall n \in [0, limite], cd1(n) \le cd2(n) \implies inclusion(cd1, cd2, limite) = \text{true}
\]

---

## 7. Conclusiones

- El programa cumple los principios de **programación funcional pura**.  
- Todas las operaciones están **matemáticamente justificadas** y correctamente implementadas.  
- La función `inclusion` demuestra una recursión **optimizable por cola**, eficiente en memoria.  
- Los diagramas Mermaid muestran la **dinámica interna de ejecución**, sin necesidad de imágenes externas.  
- Todas las pruebas unitarias validan la **corrección lógica y matemática** del módulo.

---
