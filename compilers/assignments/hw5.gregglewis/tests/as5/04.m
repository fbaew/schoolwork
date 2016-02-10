%Here we test accessing function parameters across different scopes
%should produce a list of the squares of numbers from the number specified
%down.

var a:int;
fun square(n:int):int {
	fun subsquare(m:int):int {
		return m*n;
	};
	return subsquare(n);
};
read a;
while a > 0 do {
	print square(a);
	a:=a-1;
};
