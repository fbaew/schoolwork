%but what about mutual recursion!?
%this should print a descending list of numbers,
%starting from the one 1 below the user's input

fun f(a:int):int {
	var result:int;
	if a = 0 then result:=a else result:=g(a-1);
	return result;
};

fun g(a:int):int {
	print a;
	return f(a);
};

var value:int;
read value;
print f(value);