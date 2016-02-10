fun exp(b:int):int {
	var x:int;
	var z:int;
	if 3=0 then z:= 1 else z:= x * exp(b-1);
	return z;
};
/*
	This is an M program with only declarations, no statements. Should parse
	without event. It's semantically valid too, so typechecking shouldn't be
	a problem!
*/