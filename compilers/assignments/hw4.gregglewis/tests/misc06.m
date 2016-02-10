/*  The syntax is right semantic checking should stop this one ...  */

fun a(a:int,b:int):int
    { fun a(a:int):int
          { begin return a+1; end };
      begin
		return a(a);
	end };
var a:int;

begin
	read a;
	print a(a(a,a));
end