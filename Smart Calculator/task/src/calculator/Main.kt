package calculator

fun main() {
    val calculator = Calculator()
    var input: String

    while (true) {
        input = cutString(readLine()!!)
        if (commandIncorrect(input)) continue
        if (invalidExpression(input)) continue
        when (input) {
            "/memory" -> println(calculator.Memory())
            "/help" -> {
                println(
                    """
                The program calculates sum and subtraction, multiplication, division,
                power and unary minus for integer values.
                Also supported storing of variables.
                Just type operands and operates separated by space.
                And so on.
                Operations with floating point isn't supported.
                Restriction: variable's name cannot contains digits.
                Supported commands:
                    /help - show this manual
                    /exit - exit program
                    /memory - show stored variables
                """.trimIndent()
                )
            }
            "" -> continue
            "/exit" -> {
                println("Bye!")
                break
            }
            else -> {
                var expression = calculator.postfix.convertToList(input)
                if (expression.contains("=")) {
                    calculator.getKeyValue(input)
                    calculator.Memory().store()
                    continue
                }
                expression = calculator.Memory().restoreVariables(expression)
                expression = calculator.postfix.convertToPostfix(expression)
                if (!calculator.allIsFound(expression)) continue
                println(calculator.postfix.calculate(expression))
            }
        }
    }

}

fun commandIncorrect(input: String): Boolean {
    return if (input.isNotEmpty() && input.first() == '/') {
        val commands = listOf("/exit", "/help", "/memory")
        if (!commands.contains(input)) {
            println("Unknown command")
            true
        } else false
    } else false
}

fun invalidExpression (input: String): Boolean {
    var fall = false
    //check assignment
    if (input.contains("=")) {
        if (!input.substringBefore("=").matches("[a-zA-Z]+".toRegex())) {
            println("Invalid identifier")
            return true
        }
        if (input.filter { it == '=' }.length > 1) {
            println("Invalid assignment")
            return true
        }
        if (input.substringAfter("=").contains("[\\d][a-zA-Z]|[a-zA-Z][\\d]".toRegex())) {
            println("Invalid assignment")
            return true
        }
    }
    //check numbers
    if (input.contains("\\s".toRegex())) {
        val incorrectNum = "\\d+[*/+-]+".toRegex()
        if (input.contains(incorrectNum)) fall = true
    }
    //check variables
    val incorrectVariable = "[a-zA-Z]+\\d+|\\d+[a-zA-Z]+".toRegex()
    if (input.contains(incorrectVariable)) fall = true
    //check multiple and division
    val invalidMulDiv = "[*/][*/]+".toRegex()
    if (input.contains(invalidMulDiv)) fall = true
    //check parenthesis with digits and operators
    val invalidBrackets = "[)]\\d|[(][*/+]".toRegex()
    if (input.contains(invalidBrackets)) fall = true
    //check correct brackets
    var tmp = input.filter { it == '(' || it == ')' }
    while (tmp.contains("()")) {
        tmp = tmp.replace("()", "")
    }
    if (tmp.isNotEmpty()) fall = true

    if (fall) println("Invalid expression")

    return fall
}

fun cutString(input: String): String {
    var output = input.replace("---", "-").replace("--", "+")

    //remove redundant spaces
    output = output.replace("\\s".toRegex(), "")

    while (output.contains("---")) {
        output = output.replace("---".toRegex(), "-")
    }

    while (output.contains("--")) {
        output = output.replace("--", "+")
    }
    while (output.contains("[+][-]|[-][+]".toRegex())) {
        output = output.replace("[+][-]|[-][+]".toRegex(), "+")
    }
    //remove redundant pluses
    while (output.contains("[+][+]+".toRegex())) {
        output = output.replace("[+]+".toRegex(), "+")
    }
    return output.replace("-", "-1*")
}
