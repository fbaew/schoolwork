/*
	Here's an M program with only declarations, no statements. Should parse
	without event.
*/
fun exp(b:int):int {
	var z:int;
	if 3=0 then z:= 1 else z:= x * exp(b-1);
	return z; 
};