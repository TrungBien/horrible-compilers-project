package backend

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor

//currently does not work and is just basically an example

class SimpleTypeCheckVisitor : PLBaseVisitor<Unit>() {
    private val symbolTable = mutableMapOf<String, String>()

    override fun visitVarDeclaration(ctx: PLParser.VarDeclarationContext) {
        val varName = ctx.ID().text
        val typeName = ctx.type().text

        if (typeName in listOf("INT", "STRING", "BOOLEAN")) {
            symbolTable[varName] = typeName
        } else {
            throw RuntimeException("Unsupported type: $typeName")
        }
    }

    override fun visitAssignment(ctx: PLParser.AssignmentContext) {
        val varName = ctx.ID().text
        val varType = symbolTable[varName] ?: throw RuntimeException("Undeclared variable: $varName")
        
        val exprType = visit(ctx.expr())

        if (varType != exprType) {
            throw RuntimeException("Type mismatch: $varName is not a $varType")
        }
    }

    override fun visitIntExpr(ctx: PLParser.IntExprContext) = "INT"

    override fun visitStringExpr(ctx: PLParser.StringExprContext) = "STRING"

    override fun visitBoolExpr(ctx: PLParser.BoolExprContext) = "BOOLEAN"

    override fun visitAddExpr(ctx: PLParser.AddExprContext): String {
        val leftType = visit(ctx.expr(0))
        val rightType = visit(ctx.expr(1))

        if (leftType != "INT" || rightType != "INT") {
            throw RuntimeException("Addition operands must be integers.")
        }

        return "INT"
    }

    override fun visitErrorNode(node: org.antlr.v4.runtime.tree.ErrorNode?) {
        throw RuntimeException("Error parsing expression: ${node?.text}")
    }
}
