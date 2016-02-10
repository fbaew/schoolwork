% Heres a comment
/* 
	This file should not parse correctly. We should have a report of a stray
	semicolon on the last line. (line 18). I've thrown every comment-related
	gotcha I can think of into this file, as well as that syntax error on the
	/* % last line. */*/*/
	*/
	/* nested comments
	%*/
	*/ */
/* */

var y:int;
a := b+c(1,2,3);
{
	var x:bool;
	read f;
};;