
typedef enum {Add,Sub,Mul,Div,Num} NodeKind;

typedef struct treeNode { 
  NodeKind nodekind;
  int val;
  int lineno;
  struct treeNode * child[3];
} treeNode;

