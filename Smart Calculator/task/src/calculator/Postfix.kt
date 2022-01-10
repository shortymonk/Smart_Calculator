package calculator

import java.lang.Exception
import java.math.BigDecimal

class Postfix {
    private fun add(x: BigDecimal, y: BigDecimal) = (x + y)
    private fun subtract(x: BigDecimal, y: BigDecimal) = (x - y)
    private fun multiply(x: BigDecimal, y: BigDecimal) = (x * y)
    private fun divide(x: BigDecimal, y: BigDecimal) = (x / y)
    private fun getPower(x: BigDecimal, y: BigDecimal) = x.pow(y.toInt())

    private fun calculate(list: List<BigDecimal>, operator: (x: BigDecimal, y: BigDecimal) -> BigDecimal): BigDecimal {
        return operator(list[list.lastIndex - 1], list.last())
    }

    fun calculate(postfix: List<String>): String {
        val result = mutableListOf<BigDecimal>()

        fun String.isNumber(): Boolean {
            return this.matches("[+-]?\\d+".toRegex())
        }

        for (element in postfix) {
            var tmp: BigDecimal
            try {
                tmp = when (element) {
                    "+" -> calculate(result, ::add)
                    "-" -> calculate(result, ::subtract)
                    "/" -> calculate(result, ::divide)
                    "*" -> calculate(result, ::multiply)
                    "^" -> calculate(result, ::getPower)
                    else -> element.toBigDecimal()
                }
                if (!element.isNumber()) repeat(2) { result.removeAt(result.lastIndex) }
                result.add(tmp)
            } catch (e: Exception) {
                return "Whoa! Something went wrong. Try again."
            }
        }
        return result.first().toString()
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