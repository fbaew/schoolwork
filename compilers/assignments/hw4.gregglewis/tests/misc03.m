/*  Another more efficient Fibonacci function 
    Needs ameliorated syntax ..*/
var m:int;
var n:int;
fun fib(m:int):int 
 {var r:int;
  fun ffib(m:int):int
   {var r:int; 
    r:= fib(m-1);m:=n+r;n:=r;return m;
   };
  if m=< 2 then r:= 1 else r:= ffib(m);
  return r;
 };
n:=1;read m;print fib(m);