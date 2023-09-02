/**
 * Evaluates arithmetic expressions.
 */
class KCalc {

    /**
     * Handles the negative numbers and unary minus.
     */
    private fun manageNegative(exp: String, index: Int, operators: ArrayDeque<Char>, operands: ArrayDeque<Float>): Boolean {
        // Check if '-' is a unary minus
        val isNegative = (index == 0) || (exp[index - 1] == '(') || (exp[index - 1] != ')' && !exp[index - 1].isDigit())

        // If it's unary minus, push -1 and * to implement it as multiplication
        if (isNegative) {
            operands.addFirst(-1.0f)
            operators.addFirst('*')
        }
        return isNegative
    }

    /**
     * Handles the open parenthesis character in the expression.
     */
    private fun handleOpenParenthesis(operators: ArrayDeque<Char>) {
        operators.addFirst('(')
    }

    /**
     * Handles the close parenthesis character in the expression.
     */
    private fun handleCloseParenthesis(operators: ArrayDeque<Char>, operands: ArrayDeque<Float>) {
        while (operators.first() != '(') {
            compute(operators, operands)
        }
        operators.removeFirst()
    }

    /**
     * Handles other operators in the expression.
     */
    private fun handleOperator(
        current: Char,
        operators: ArrayDeque<Char>,
        precedenceMap: MutableMap<Char, Int>,
        operands: ArrayDeque<Float>
    ) {
        val precedence = precedenceMap[current]!!
        while (operators.isNotEmpty() && precedenceMap.getOrDefault(operators.first(), 0) >= precedence) {
            compute(operators, operands)
        }
        operators.addFirst(current)
    }

    /**
     * Checks if the given character is a digit, a decimal point, or an exponent (E/e).
     */
    private fun isPartOfNumber(ch: Char): Boolean {
        return ch.isDigit() || ch == '.' || ch.uppercase() == "E"
    }

    /**
     * Parses the number from the expression starting from the current index.
     */
    private fun parseNumber(expression: String, index: Int, operands: ArrayDeque<Float>): Int {
        val number = StringBuilder()
        var newIndex = index
        while (newIndex < expression.length && isPartOfNumber(expression[newIndex])) {
            number.append(expression[newIndex])
            newIndex++
        }
        operands.addFirst(number.toString().toFloat())
        return newIndex - 1
    }

    /**
     * Computes the result of a sub-expression.
     */
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

    /**
     * Solves the arithmetic expression.
     */
    fun solve(source: String): Float {
        // Preprocess the expression string.
        val expression = "(" + source.replace(" ", "") + ")"

        // Define operator precedence.
        val precedenceMap: MutableMap<Char, Int> = mutableMapOf('-' to 1, '+' to 1, '/' to 2, '*' to 2)

        // Create stacks for operators and operands
        val operators = ArrayDeque<Char>()
        val operands = ArrayDeque<Float>()

        var index = 0

        // Parse and evaluate the expression
        while (index < expression.length) {
            val current = expression[index]
            when {
                // Case for open parenthesis character in the expression.
                (current == '(') ->
                    handleOpenParenthesis(operators)

                // Case for close parenthesis character in the expression.
                (current == ')') ->
                    handleCloseParenthesis(operators, operands)

                // Case for negative numbers and unary minus.
                ((current == '-') && manageNegative(expression, index, operators, operands)) -> {
                    index++
                    continue
                }

                // Case for other operators in the expression.
                precedenceMap.containsKey(current) ->
                    handleOperator(current, operators, precedenceMap, operands)

                // Case for parsing the number from the expression starting from the current index.
                else ->
                    index = parseNumber(expression, index, operands)  // Update index after parsing number
            }
            index++
        }

        // Return the final result
        return operands.removeFirst()
    }
}

fun main() {
    val result = KCalc().solve("1 + (-2 * -7) * ((-4 / 2) + -3) * 12 / -4 + 2.0E4 + 100.E2 / 7E2 * (-2.5)")
    println(result)  // Should output 20175.285
}
