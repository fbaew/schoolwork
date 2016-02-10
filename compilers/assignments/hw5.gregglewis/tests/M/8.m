var y:int;
var x:int;
var a:bool;
fun exp(b:int,c:bool):int {
	var z:int;
	
	if b>=0
		then z:= 1
	else z:= x * exp(b-1,true);
		return z;
};

read x;
x := 3 + exp(y);

/*
	This M+ program makes a function call with the wrong number of arguments!
*/
