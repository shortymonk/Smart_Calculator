package calculator

class Calculator {
    val postfix = Postfix()

    companion object {
        val vars = mutableMapOf<String, String>()
    }

    inner class Memory {

        fun store(input: List<String>) {
            val key = input.last()
            val value = postfix.calcPostfix(input.subList(0, input.indexOf("="))).toString()
            if (!passCheck(key, value)) return

            if (value.matches("[\\d]+".toRegex())) {
                vars[key] = value
            } else if (vars.containsKey(value)) {
                vars[key] = vars.getValue(value)
            } else {
                println("Unknown variable in store")
            }
        }

        private fun passCheck(key: String, value: String): Boolean {
            if (
                key.contains("[\\d]".toRegex()) ||
                key.contains("[\\W]".toRegex())
            ) {
                println("Invalid identifier")
                return false
            } else if (
                value.contains("[a-zA-Z]+[\\d]+".toRegex()) ||
                value.contains("[\\d]+[a-zA-Z]+".toRegex()) ||
                value.contains("[\\W]".toRegex())
            ) {
                println("Invalid assignment")
                return false
            }
            return true
        }

        fun findVariables(input: List<String>): List<String> {
            val output = input.toMutableList()
            for (i in output.indices) {
                if (vars.containsKey(output[i])) {
                    output[i] = vars.getValue(output[i])
                }
            }
            return output
        }
    }

    fun allVarsIsFound(input: List<String>): Boolean {
        input.forEach {
            if (it.matches("[a-zA-Z]+".toRegex())) return false
        }
        return true
    }

}