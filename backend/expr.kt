package backend

abstract class Expr {
    abstract fun eval(runtime:Runtime):Data
}

enum class Operator {
    Add,
    Sub,
    Mul,
    Div
}


class IntLiteral(val lexeme:String): Expr() {
    override fun eval(runtime:Runtime): Data
    = IntData(Integer.parseInt(lexeme))
}

class StringLiteral(val lexeme:String): Expr() {
    override fun eval(runtime:Runtime): Data
    = StringData(lexeme)
}

class BooleanLiteral(val lexeme:String): Expr() {
    override fun eval(runtime:Runtime): Data
    = BooleanData(lexeme.equals("true"))
}


class Assign(val id: String, val expr: Expr) : Expr() {
    override fun eval(runtime: Runtime): Data {
        val value = expr.eval(runtime)
        runtime.symbolTable[id] = value
        return value
    }
}

class Deref(val id: String) : Expr() {
    override fun eval(runtime: Runtime): Data {
        return runtime.symbolTable[id] ?: NullData() 
    }
}



class Repeat(val expr: Expr, val times: Int) : Expr() {
    override fun eval(runtime: Runtime): Data {
        val value = expr.eval(runtime)
        println(value)
        return StringData(value.toString().repeat(times)) 
    }
}


class Concat(val expr1: Expr, val expr2: Expr): Expr() {
    override fun eval(runtime: Runtime): Data {
        val value1 = expr1.eval(runtime).toString()
        val value2 = expr2.eval(runtime).toString()

        return StringData(value1 + value2)
    }
}

class Concat3(val expr1: Expr, val expr2: Expr, val expr3: Expr): Expr() {
    override fun eval(runtime: Runtime): Data {
        val value1 = expr1.eval(runtime).toString()
        val value2 = expr2.eval(runtime).toString()
        val value3 = expr3.eval(runtime).toString()
        return StringData(value1 + value2 + value3)
    }
}


class Output(val expr: Expr, val runtime: Runtime) : Expr() {
    val data: Data = expr.eval(runtime)

    override fun eval(runtime: Runtime): Data {
        println(data)
        return data
    }
}

//old stuff that doesn't work

// enum class Operator {
//     Add,
//     Sub,
//     Mul,
//     Div,
//     Concat
// }

// class NoneExpr(): Expr() {
//     override fun eval(runtime:Runtime) = None
// }

// class IntLiteral(val lexeme:String): Expr() {
//     override fun eval(runtime:Runtime): Data
//     = IntData(Integer.parseInt(lexeme))
// }

// class StringLiteral(val lexeme:String): Expr() {
//     override fun eval(runtime:Runtime): Data
//     = StringData(lexeme)
// }

// class BooleanLiteral(val lexeme:String): Expr() {
//     override fun eval(runtime:Runtime): Data
//     = BooleanData(lexeme.equals("true"))
// }

// class Arithmetics(
//     val op:Operator,
//     val left:Expr,
//     val right:Expr
// ): Expr() {
//     override fun eval(runtime:Runtime): Data {
//         val x = left.eval(runtime)
//         val y = right.eval(runtime)
//         if(x !is IntData || y !is IntData) {
//             throw Exception("cannot handle non-integer")
//         }
//         return IntData(
//             when(op) {
//                 Operator.Add -> x.value + y.value
//                 Operator.Sub -> x.value - y.value
//                 Operator.Mul -> x.value * y.value
//                 Operator.Div -> {
//                     if(y.value != 0) {
//                         x.value / y.value
//                     } else {
//                         throw Exception("cannot divide by zero")
//                     }
//                 }
//                 else -> throw Exception("Unknown operator")
//             }
//         )
//     }
// }


// class ConcatString(
//     val op:Operator,
//     val left:Expr,
//     val right:Expr
// ): Expr() {
//     override fun eval(runtime:Runtime): Data {
//         val x = left.eval(runtime)
//         val y = right.eval(runtime)
//         return when(op) {
//                 Operator.Concat -> {
//                     if (y is StringData && x is StringData){
//                         StringData(x.value + y.value)
//                     } else {
//                         throw Exception("Can't concat with non-string values")
//                 }
//             } 
//                 else -> throw Exception("Invalid operator")
//         }
//     }
// }

// class Concat(
//     val op: Operator,
//     val left: String,
//     val right: String
// ): Expr() {
//     override fun eval(runtime: Runtime): Data {
//         return when (op) {
//             Operator.Concat -> {
//                 StringData(left + right)
//             }
//             else -> throw Exception("Invalid operator")
//         }
//     }
// }



// class Assign(
//     val name: String,
//     val expr: Expr
// ): Expr() {
//     override fun eval(runtime:Runtime):Data {
//         val v:Data = expr.eval(runtime)
//         runtime.symbolTable.put(name, v)
//         return None
//     }
// }

// class Deref(
//     val name: String
// ): Expr() {
//     override fun eval(runtime: Runtime): Data {
//         val v = runtime.symbolTable[name]
//         return v ?: None
//     }
// }

// class Print(
//     val expr: ArrayList<Expr>
// ): Expr() {
//     override fun eval(runtime: Runtime): Data {
//         val results = expr.map { it.eval(runtime) }
//         results.forEach { println(it) }
//         return None
//     }
// }



// class IfElse(
//     val cond: Expr,
//     val trueBlock: List<Expr>,
//     val falseBlock: List<Expr>
// ) : Expr() {
//     override fun eval(runtime: Runtime): Data {
//         val result = cond.eval(runtime) as BooleanData
//         return if (result.value) {
//             evalBlock(trueBlock, runtime)
//         } else {
//             evalBlock(falseBlock, runtime)
//         }
//     }

//     private fun evalBlock(block: List<Expr>, runtime: Runtime): Data {
//         var result: Data = None
//         for (expr in block) {
//             result = expr.eval(runtime)
//         }
//         return result
//     }
// }


// class ForLoop(
//     val variable: String,
//     val rangeStart: Int,
//     val rangeEnd: Int,
//     val body: List<Expr>,
// ): Expr() {
//     override fun eval(runtime: Runtime): Data {
//         runtime.symbolTable[variable] = IntData(rangeStart)

//         for (i in rangeStart..rangeEnd) {
//             for (expr in body) {
//                 expr.eval(runtime)
//             }
//             val currentValue = (runtime.symbolTable[variable] as IntData).value
//             runtime.symbolTable[variable] = IntData(currentValue + 1)
//         }

//         return None
//     }
// }




// class Block(
//     val exprs:List<Expr>
// ):Expr() {
//     override fun eval(runtime:Runtime):Data
//     = exprs.map { it.eval(runtime) }.last()
// }


// // Function class for storing function data
// class Function(
//     val funcData: FuncData
// ): Expr() {
//     override fun eval(runtime: Runtime): Data {
//         runtime.functions[funcData.name] = this
//         // Implement the logic for evaluating the function body here
//         // For example:
//         return funcData.body.last().eval(runtime)  // Assuming body is a list of Expr
//     }
// }

// // FunctionCall class for calling functions
// class FunctionCall(
//     val function: Function,
//     val arguments: List<Expr>
// ): Expr() {
//     override fun eval(runtime: Runtime): Data {
//         val newRuntime = Runtime(runtime)
//         for ((param, arg) in function.funcData.parameters.zip(arguments)) {
//             newRuntime.symbolTable[param] = arg.eval(runtime)
//         }
//         return function.funcData.body.last().eval(newRuntime)  // Assuming body is a list of Expr
//     }
// }
