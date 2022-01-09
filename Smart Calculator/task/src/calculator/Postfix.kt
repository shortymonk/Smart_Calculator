package calculator

class Postfix {
    private fun add(x: Int, y: Int) = x + y
    private fun subtract(x: Int, y: Int) = x - y
    private fun multiply(x: Int, y: Int) = x * y
    private fun divide(x: Int, y: Int) = x / y
    private fun getPower(x: Int, y: Int): Int {
        var result = x
        repeat(y) { result *= x }
        return result
    }

    private fun calculate(list: List<Int>, operator: (x: Int, y: Int) -> Int): Int {
        return operator(list[list.lastIndex - 1], list.last())
    }

    fun calculate(postfix: List<String>): Int {
        val result = mutableListOf<Int>()

        fun String.isNumber(): Boolean {
            return this.matches("[+-]?\\d+".toRegex())
        }

        for (element in postfix) {
            val tmp = when (element) {
                "+" -> calculate(result, ::add)
                "-" -> calculate(result, ::subtract)
                "/" -> calculate(result, ::divide)
                "*" -> calculate(result, ::multiply)
                "^" -> calculate(result, ::getPower)
                else -> element.toInt()
            }
            if (!element.isNumber()) repeat(2) { result.removeAt(result.lastIndex) }
            result.add(tmp)
        }
        return result.first()
    }

    fun convertToList(input: String): List<String> {
        val list = mutableListOf<String>()
        var num=""
        for (element in input) {
            if (element.isDigit() || element.isLetter()) {
                num += element
            } else {
                if (num.isNotEmpty()) {
                    list.add(num)
                    num = ""
                }
                if ((list.isEmpty() || list.last() == "(") &&
                    (element.toString().matches("[+-]".toRegex()))) {
                    num += element
                } else {
                    list.add(element.toString())
                }
            }
        }

        if (num.isNotEmpty()) {
            list.add(num)
        }
        return list
    }

    fun convertToPostfix(input: List<String>): List<String> {
        val postfix = mutableListOf<String>()
        val stack = mutableListOf<String>()
        for (element in input) {
            if (element.matches("[+-]?[\\d]+".toRegex())) {
                postfix.add(element)
            } else {
                if (stack.isEmpty() || element == "(") {
                    stack.add(element)
                } else {
                    if (element == ")") {
                        while (stack.last() != "(") {
                            postfix.add(stack.last())
                            stack.removeAt(stack.lastIndex)
                        }
                        stack.removeAt(stack.lastIndex)
                        continue
                    }
                    while (precedence(element) <= precedence(stack.last())) {
                        if (stack.last() == "(") break
                        postfix.add(stack.last())
                        stack.removeAt(stack.lastIndex)
                        if (stack.isEmpty()) break
                    }
                    stack.add(element)
                }
            }
        }
        postfix.addAll(stack.reversed())
        return postfix
    }

    private fun precedence(ch: String): Int {
        val precedenceList = listOf("=".toRegex(), "[+-]".toRegex(), "[/*]".toRegex(), "\\^".toRegex())
        for (i in precedenceList.indices) {
            if (ch.matches(precedenceList[i])) return i
        }
        return -1
    }
    
}