var x:int;
fun f(x:int,y:int):int
{fun g(v:int):int
   { var r:int;
     if v=0 then r:= 0
            else {fun g(v:int):int {return f(z,w);};
                  print v*g(v); 
                  r:= z+w;};
     return r; };
  
 print w+y; print w+x;
 return g(x*y);};
var w:int;
var z:int;
read x;
read z;
z := x + z;
w := x - z;
print f(x,z);