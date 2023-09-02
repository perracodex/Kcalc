# KCalc
Very simple math expression evaluator in Kotlin, allowing to compute experssions as next:

```
fun main() {
    val result = KCalc().solve("1 + ((-2) * -7) * ((-4 / 2) + -3) * 12 / -4 + 2.0E4 + 100.E2 / 7E2")
    println(result)  // Output 20225.285
}
```
