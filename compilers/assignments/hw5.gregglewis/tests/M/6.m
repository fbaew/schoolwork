var x:int;
var y:bool;
var z:int;
fun exp(b:int):int {
	var z:int;
	if b=3
		then z:= 1
		else z:= x * exp(b-1);
	return z;
};
if b=4 then {
	while c<10 do {
		read x;
		c := c+1;
		print exp(c);
	};
} else {
	print z;
};

/*
	This M+ program refers to a variable (c) which is not declared.
*/