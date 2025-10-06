# 🧮 Informe Taller 2 – Conjuntos Difusos

**Universidad del Valle**  
**Programa:** Tecnología en Desarrollo de Software  
**Asignatura:** Programación Funcional y Concurrente  
**Estudiante:** Christian David Trujillo 2569134
                David Naranjo 2569497
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



## 3. Pila de llamadas (Call Stack) en funciones recursivas

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

## 4. Pruebas y corrección empírica

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


## 6. Conclusiones

- El programa cumple los principios de **programación funcional pura**.  
- Todas las operaciones están **matemáticamente justificadas** y correctamente implementadas.  
- La función `inclusion` demuestra una recursión **optimizable por cola**, eficiente en memoria.  
- Los diagramas Mermaid muestran la **dinámica interna de ejecución**, sin necesidad de imágenes externas.  
- Todas las pruebas unitarias validan la **corrección lógica y matemática** del módulo.

---
