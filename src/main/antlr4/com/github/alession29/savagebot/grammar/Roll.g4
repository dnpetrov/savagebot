grammar Roll;

// To generate parser and parse tree classes, run 'mvn generate-sources'.

// TODO lexer modes

// TODO Russian mnemonics support
// Please use unicode character literals for them

stmt
    : e=expr ';'?
        # RollOnceStmt
    | t=term ('x' | 'X') e=expr ';'?
        # RollRepeatedStmt
    ;

variableName: DollarVariable | DollarCurlyVariable;

expr
    : br=basicRoll
        # BasicRollExpr
    | e0=expr op=('*' | '/' | '%') e1=expr
        # MulExpr
    | e0=expr op=('+' | '-') e1=expr
        # AddExpr
    | op=('+' | '-') e=expr
        # PrefixExpr
    | t=term
        # TermExpr
    ;

term
    : i=Integer
        # IntTerm
    | '(' e=expr ')'
        # ParenthesizedExprTerm
    ;

basicRoll: (t1=term)? op=basicRollOp t2=term basicRollSuffix*;

basicRollOp: 'd' | 'D';

basicRollSuffix
    : op=('!' | 'e' | 'E') (c=conditionSpecifier)?
        # OpenEndedRollSuffix
    | op=('r' | 'R') (c=conditionSpecifier)?
        # RerollSuffix
    | op=('k' | 'kl' | 'K' | 'KL') t=term
        # RollAndKeepSuffix
    | op=('a' | 'd' | 'A' | 'D') (t=term)?
        # AdvantageSuffix
    ;

conditionSpecifier
    : t=term
        # TermCondition
    | op=('>' | '<' | '>=' | '<=' | '=') t=term
        # OperatorCondition
    ;

repeatOp: 'x' | 'X';

Integer: [0-9]+;

DollarVariable: '$'[a-zA-Z0-9_]+;
DollarCurlyVariable: '${'[a-zA-Z0-9_]+'}';

WS: [ \t\r\n]+ -> skip;