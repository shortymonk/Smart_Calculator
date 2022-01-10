package calculator

class Calculator {
    val postfix = Postfix()
    lateinit var key: String
    lateinit var value: List<String>
    companion object {
        val vars = mutableMapOf<String, String>()
    }

    inner class Memory {

        override fun toString(): String  {
            return vars.toString()
        }

        fun store() {
            value = restoreVariables(value)
            value = postfix.convertToPostfix(value)
            if(allIsFound(value)) vars[key] = postfix.calculate(value)
        }

        fun restoreVariables(input: List<String>): List<String> {
            val output = input.toMutableList()
            for (i in output.indices) {
                val key = output[i]
                if (vars.containsKey(key)) {
                    output[i] = vars.getValue(output[i]).toString()
                }
            }
            return output
        }
    }

    fun allIsFound(input: List<String>): Boolean {
        input.forEach {
            if (it.matches("[a-zA-Z]+".toRegex()) && !vars.containsKey(it)) {
                println("Unknown variable")
                return false
            }
        }
        return true
    }

    fun getKeyValue(expression: String) {
        key = expression.substringBefore("=")
        value = postfix.convertToList(expression.substringAfter("="))
    }
}