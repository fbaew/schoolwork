type Prog = [Stmt]

data Stat
    = If Exp Stmt Stmt
    | While Exp Stmt
    | Input Id
    | Assign Id Exp
    | Write Exp
    | Block [Stmt]
data Exp
    = Add Exp Exp
		| Mul Exp Exp
		| Div Exp Exp
		| Sub Exp Exp
    | ID String
		| Num Int