/*  semicolon missing do you detect it and report position successfully?  */
var x:int;
fun max(a:int,b:int):int
   { var z:int;
     begin 
       if a > b then z:=a else z:=b;
       return z;
     end
   }
var y:int;

begin
  read x; read y; print max(x,y); 
end