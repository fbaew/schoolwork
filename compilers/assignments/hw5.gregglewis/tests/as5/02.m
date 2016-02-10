%testing recursive function calls
%should display a descending list of numbers, starting with the
%one specified by the user. Prints "true" at the end.
fun recurse(a:int):bool {
	var outcome:bool;
	print a;
	if a = 0 then outcome := true else outcome := recurse(a-1);

	return outcome;
};
var value:int;
read value;
print recurse(value);