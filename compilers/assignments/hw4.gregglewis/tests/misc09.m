/*  Should generate syntax error at embedded return  */ 
fun a(b:int,c:int):int
 { begin 
    if c>b then return c-b
    else if c=b then return(c) else return(b-c); 
   end }
var x:int; var y:int;

begin 
  read x; read y; print a(x,y);
end