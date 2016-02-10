var y:int;
var x:int;
var a:bool;
fun exp(b:int):int {
	var z:int;
	
	if b>=0
		then z:= 1
	else z:= x * exp(b-1);
		return z;
};

read x;
x := exp(y);
read y;
{
	var g:int;
	var h:bool;
	var i:bool;
	fun exp(g:int):int {
		read x;
		return g;
	};
	read h;
	print 3;
	if (x=3) then {} else {};
	while x=3 do {};
	g:=3;
	h:=true;
	{};
};
/*
	This M+ program has a little bit of everything, to try and trip up our
	IR interpretation, but is semantically valid.
*/