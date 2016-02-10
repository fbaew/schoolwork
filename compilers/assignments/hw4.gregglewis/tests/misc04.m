fun a(x:int):bool
   { fun a(x:bool):int
      { var z:int;
        begin 
          if x then z:= 1 else z:= -1;
          return z;
        end
      };
     begin 
       return a(x<1)=1;
     end
    };
fun b(x:int,y:int,z:int):int
   { var w:int;
     begin 
      if a(x) then w:=y else w:=z;
      return w;
     end
   };
   
begin 
  print b(2,13,7);
end