
/*
    m+ program to calculate the average and variance 
    (to nearest integer) of a set of positive numbers terminated by 
    0.
 */

var vr:int;
var sum:int;
var count:int;
var flag: bool;
fun variance(av:int,vr:int,cnt:int):int
    {return vr/cnt - sqr(average(av,cnt));};
fun average(av:int,cnt:int):int
    {return (av/cnt);};
fun sqr(x:int):int
    {return x*x;}; 

   count := 0; flag := true; sum := 0; vr := 0;
   while flag do 
{var num:int;
	var flag: int;
    read num;
    if not(num=0) then
       { count := count + 1;
         sum := sum + num;
         vr := vr + sqr(num); }
    else flag := false;
   };
   print average(sum,count);
   print variance(sum,vr,count);

      
     
