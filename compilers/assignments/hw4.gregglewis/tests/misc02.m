/*  Fibonacci ...  */

var n:int;
fun fib(n:int):int
   {var z:int;
    begin 
      if n =< 1 then z:= 1
      else z:= fib(n-1) + fib(n-2);
      return z;
    end};
begin
  read n;
  print fib(n);
end