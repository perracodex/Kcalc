
class Calc {

    private fun manageNegative(exp: String, index: Int, operators: ArrayDeque<Char>, operands: ArrayDeque<Float>): Boolean {

        val isNegative = (index == 0)
                || (exp[index - 1] == '(')
                || (exp[index - 1] != ')' && !exp[index - 1].isDigit())

        if (isNegative) {
            operators.add('*')
            operands.add(-1.0f)
        }

        return isNegative
    }

    private fun compute(operators: ArrayDeque<Char>, operands: ArrayDeque<Float>) {
        val action = operators.removeFirst()
        val right = operands.removeFirst()
        val left = operands.removeFirst()

        when (action) {
            '-' -> operands.addFirst(left - right)
            '+' -> operands.addFirst(left + right)
            '*' -> operands.addFirst(left * right)
            '/' -> operands.addFirst(left / right)
        }
    }

    fun solve(source: String): Float {
        val exp = "(" + source.replace(" ", "") + ")"

        val precedenceMap: MutableMap<Char, Int> = HashMap()
        precedenceMap['-'] = 1
        precedenceMap['+'] = 1
        precedenceMap['/'] = 2
        precedenceMap['*'] = 2

        val operators = ArrayDeque<Char>()
        val operands = ArrayDeque<Float>()

        var index = 0

        while (index < exp.length) {
            val current = exp[index]

            when {
                current == '(' -> {
                    operators.addFirst(current)
                }
                current == ')' ->  {
                    while (!operators.isEmpty() && operators.first() != '(') 
                        this.compute(operators, operands)
                    
                    operators.removeFirst()
                }
                current == '-' && manageNegative(exp, index, operators, operands) -> {
                    index++
                    continue
                }
                precedenceMap.containsKey(current) -> {
                    val precedence = precedenceMap[current]!!

                    while (!operators.isEmpty() && precedenceMap.getOrDefault(operators.first(), 0) >= precedence)
                        this.compute(operators, operands)

                    operators.addFirst(current)
                }
                else -> {
                    var number = 0

                    while (exp[index].isDigit()) {
                        number = (number * 10) + (exp[index] - '0')
                        index++
                    }

                    operands.addFirst(number.toFloat())
                    index--
                }
            }

            index++
        }

        return operands.removeFirst()
    }
}

fun main(args: Array<String>) {
    println(Calc().solve("1+(-2*-7)*((-4/2)+-3)*12/-4"))
}
