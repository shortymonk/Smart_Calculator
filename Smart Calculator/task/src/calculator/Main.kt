package calculator

fun main() {
    val calculator = Calculator()
    var input: String

    while (true) {
        input = readLine()!!.replace("[\\s]+".toRegex(), " ")
        if (commandIncorrect(input)) continue

        when (input) {
            "/help" -> {
                println(
                    """
                The program calculates the sum and subtraction of numbers.
                Just type operands and operates separated by space.
                And so on.""".trimIndent()
                )
            }
            "" -> continue
            "/exit" -> {
                println("Bye!")
                break
            }
            else -> {
                var list = calculator.postfix.convertToList(input)
                list = calculator.Memory().findVariables(list)
                list = calculator.postfix.convertToPostfix(list)
                if (input.contains('=')) {
                    calculator.Memory().store(list)
                    continue
                }
                list = calculator.Memory().findVariables(list)
                if (!calculator.allVarsIsFound(list)) continue
                println(calculator.postfix.calcPostfix(list))
            }
        }
    }

}

fun commandIncorrect(input: String): Boolean {
    return if (input.isNotEmpty() && input.first() == '/') {
        if (input != "/exit") {
            if (input != "/help") {
                println("Unknown command")
                true
            } else false
        } else false
    } else false
}

fun checkExpression (input: String): Boolean {
    var pass = true
    //check numbers
    if (input.contains("\\s".toRegex())) {
        val incorrectNum = "\\d+[*/+-]+".toRegex()
        if (input.contains(incorrectNum)) pass = false
    }
    //check variables
    val incorrectVariable = "[a-zA-Z]+\\d+|\\d+[a-zA-Z]+".toRegex()
    if (input.contains(incorrectVariable)) pass = false
    //check multiple and division
    val invalidMulDiv = "[*/][*/]+".toRegex()
    if (input.contains(invalidMulDiv)) pass = false
    //check parenthesis with digits and operators
    val invalidBrackets = "[)]\\d|[(][*/+-]".toRegex()
    if (input.contains(invalidBrackets)) pass = false
    //check correct brackets
    var tmp = input.filter { it == '(' || it == ')' }
    while (tmp.contains("()")) {
        tmp = tmp.replace("()", "")
    }
    if (tmp.isNotEmpty()) pass = false

    if (!pass) println("Invalid expression")

    return pass
}

fun prepareString(input: String, /*memory: Calculator.Memory*/): String {
    var output = input
    var list = mutableListOf<String>()
    //remove redundant spaces
    while (output.contains("\\s+".toRegex())) {
        output = output.replace("\\s+".toRegex(), "")
    }
/*    //restore variables
    memory.findVariables(output)
    //convert subtract operations*/
    while (output.contains("---")) {
        output = input.replace("---".toRegex(), "-")
    }
    while (output.contains("--")) {
        output = input.replace("--", "+")
    }
    //remove redundant pluses
    while (output.contains("[+][+]+".toRegex())) {
        output = input.replace("[+]+".toRegex(), "+")
    }
    return output
}
