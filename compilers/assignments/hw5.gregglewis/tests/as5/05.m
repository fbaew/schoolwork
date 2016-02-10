%a spot of practical M:
%Lists all the factors of the number given by the user.
%notably, tests that function arguments are getting loaded on
%the stack in the right order (reverse order)

var number:int;
var candidate:int;
var useless:bool;
fun check(num:int,candidate:int):bool {
	var i:int;
	var outcome:bool;
	outcome := false;

	i := 1;
	while (i =< num) && not(outcome)  do {
		if i*candidate = num then outcome:=true else outcome := false;
		i := i + 1;			
	};

	return outcome;
};
candidate:=0;
read number;
while candidate =< number do {
	if check(number,candidate) then print candidate else useless := true;
	candidate := candidate + 1;
};