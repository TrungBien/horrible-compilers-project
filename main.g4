grammar PL;

@header {
import backend.*;
}

@members {
backend.Runtime runtime = new backend.Runtime();
}



program returns [Expr expr]
:   (s=stat { $expr = $s.expr; $expr.eval(runtime); })+  // Removing expr.eval() makes values assigned disappear
;                                                        // which seems to be a scope issue, but I can't really test for that
                                                         // since running it won't print certain things for some reason
stat returns [Expr expr]
    :   e=expression               { $expr = new Output($e.expr, runtime); }
    |   ID '=' e=expression ';'    { $expr = new Assign($ID.text, $e.expr); }
    ;

expression returns [Expr expr]
    : 'print' '(' c=concat ')' ';' {  $expr = $concat.expr;}
    |'print' '(' t1=term ')' ';' {  $expr = $t1.expr; }
    | term { $expr = $term.expr; }
    ;
    

    
oldExpression returns [Expr expr]
    : 'print' '(' t1=term '++' t2=term '++' t3=term ')' ';' {  $expr = new Concat3($t1.expr, $t2.expr, $t3.expr);}
    | term { $expr = $term.expr; }
    ;
    
print returns [Expr expr]
    : 'print' '(' expression ')' ;
    
concat returns [Expr expr] // This is a horrible method, but I couldn't make this work with a loop
    : t1=term '++' t2=term { $expr = new Concat($t1.expr, $t2.expr); }
    | t1=term '++' t2=term '++' t3=term { $expr = new Concat(new Concat($t1.expr, $t2.expr), $t3.expr); }
    | t1=term '++' t2=term '++' t3=term '++' t4=term { $expr = new Concat(new Concat(new Concat($t1.expr, $t2.expr), $t3.expr), $t4.expr); }
    | t1=term '++' t2=term '++' t3=term '++' t4=term '++' t5=term { $expr = new Concat(new Concat(new Concat(new Concat($t1.expr, $t2.expr), $t3.expr), $t4.expr), $t5.expr); }
    ;



term returns [Expr expr]
    : id=ID {$expr = new Deref($id.text);} 
    | str=STRING {$expr = new StringLiteral($str.text);} 
    | str=STRING '++' '(' r=repeat ')' ';' {$expr = new Concat2(new StringLiteral($str.text), $r.expr);}
    | id=ID '*' INT { $expr = new Repeat(new Deref($id.text), Integer.parseInt($INT.text)); }
    | 
    ;
    
repeat returns [Expr expr]
    :  id=ID '*' INT { $expr = new Repeat(new Deref($id.text), Integer.parseInt($INT.text)); } 
    ;


   
//Some lexer rules
ID  :   ('a'..'z'|'A'..'Z')+ ;
STRING: '"' ( ~[ \t\r\n"] | ' ' )* '"' { setText(getText().substring(1, getText().length()-1)); };
INT :   '0'..'9'+ ;

BOOLEAN: 'true' | 'false';
WHITESPACE : [ \t\r\n] -> skip;


//Below is old and most likely non-functional grammar

/*
blockStatement returns [List<Expr> exprs]
    : '{' expressions=expressionList '}' { $exprs = $expressions.exprList; };

expressionList returns [List<Expr> exprList]
    : expr=expression { $exprList = new ArrayList<>(); $exprList.add($expr.expr); }
      ( ',' expr=expression { $exprList.add($expr.expr); } )*
    ;

statement returns [Expr expr]
    : a=assignment { $expr = $a.expr; }
    | p=printStatement { $expr = $p.expr; }
    | f=forLoop { $expr = $f.expr; }
    | i=ifStatement { $expr = $i.expr; }
    | func=function { $expr = $func.expr; }
    ;

assignment returns [Expr expr]
    : n=NAME '=' e=expression ';' { $expr = new Assign($n.text, $e.expr); };

printStatement returns [Expr expr]
    : 'print' '(' e=expression ')' ';' { $expr = new Print($e.expr); };

forLoop returns [Expr expr]
    : 'for' '(' n=NAME 'in' begin=NUMBER '..' end=NUMBER ')' '{' block=blockStatement '}'
      {
          $expr = new ForLoop($n.text, Integer.parseInt($begin.text), Integer.parseInt($end.text), $block.exprs);
      };


// Updated grammar rule for function parameters
function returns [Function expr]
    : 'function' functionName=NAME '(' parameters+=NAME (',' parameters+=NAME)* ')' '{' block=blockStatement '}'
      {
          List<String> parameterNames = new ArrayList<>();
          for (Token param : $parameters) {
              parameterNames.add(param.getText());
          }
          $expr = new Function(new FuncData($functionName.getText(), parameterNames, $block.exprs));
      };



functionCall returns [FunctionCall expr]
    : functionName=NAME '(' arguments=expressionList ')' ';'
      {
          $expr = new FunctionCall(new Function(new FuncData($functionName.text, null, null)), $arguments.exprList);
      };




ifStatement returns [Expr expr]
    : 'if' '(' condition=expression ')' '{' trueExpr=blockStatement '}' 'else' '{' falseExpr=blockStatement '}'
      {
          $expr = new IfElse($condition.expr, $trueExpr.exprs, $falseExpr.exprs);
      };


expression returns [Expr expr]
    : t=term { $expr = $t.expr; }
      ( ADD t=term { $expr = new Arithmetics(Operator.Add, $expr, $t.expr); }
      | SUB t=term { $expr = new Arithmetics(Operator.Sub, $expr, $t.expr); }
      | CONCAT t=term { $expr = new ConcatString(Operator.Concat, $expr, $t.expr); }
      )*
    ;

term returns [Expr expr]
    : f=factor { $expr = $f.expr; }
      ( MUL f=factor { $expr = new Arithmetics(Operator.Mul, $expr, $f.expr); }
      | DIV f=factor { $expr = new Arithmetics(Operator.Div, $expr, $f.expr); }
      )*
    ;

factor returns [Expr expr]
    : NUMBER { $expr = new IntLiteral($NUMBER.text); }
    | STRING { $expr = new StringLiteral($STRING.text); }
    | 'true' { $expr = new BooleanLiteral("true"); }
    | 'false' { $expr = new BooleanLiteral("false"); }
    | '(' e=expression ')' { $expr = $e.expr; }
    ;


// Lexer rules
NUMBER: [0-9]+ ;
STRING: '"' (~["] | '""')* '"' ;
ADD : '+';
CONCAT : '++';
SUB : '-';
MUL : '*';
DIV : '/';
NAME : [a-zA-Z_][a-zA-Z0-9_]*;
WHITESPACE : [ \t\r\n] -> skip;
*/