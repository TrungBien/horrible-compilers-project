package backend

abstract class Data

class IntData(val value:Int) : Data()

class StringData(val value: String) : Data() {
    override fun toString(): String {
        return value
    }
}


class BooleanData(val v:Boolean): Data() {
    override fun toString() = "$v"
}

class FuncData(
    val name: String,
    val parameters: List<String>,
    val body: List<Expr>
)

class NullData : Data() {
    

    override fun toString(): String {
        return "null"
    }
}


