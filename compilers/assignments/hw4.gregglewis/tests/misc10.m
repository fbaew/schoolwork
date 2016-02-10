fun f(a:bool,b:int):bool
   { fun f(a:int):bool { return g(b,false); };
     return f(b); };
fun g(b:int,a:bool):bool
   { fun f(b:int,a:bool):bool { return g(a,b); };
     fun g(a:bool,b:int):bool { return f(b,a); };
     return x;};
	var x:bool;
	var y:bool;
print y;
x := f(true,3); y:= g(7,true);
