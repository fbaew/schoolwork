/*
	This is a simple M+ program featuring variable declarations, a function
	declaration, and a few program statements. Nothing fancy.
*/
var y:int;
var x:int;
fun exp(b:int):int {
	var z:int;
	if b=3
		then z:= 1
		else z:= x * exp(b-1);
	return z;
};

read x;
read y;
print exp(y);