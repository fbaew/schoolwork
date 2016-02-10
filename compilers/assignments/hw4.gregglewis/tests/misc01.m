/*   
      m+ program to  calculate x raised to the power y

      The program also illustrates a recursive subroutine and 
      the use of non-local references.

 */
          var x:int;
          var y:int;
          fun exp(b:int):int
%   The exponential function takes as argument the exponent
%   accessing the base through a non-local reference.
          { var z:int;
            begin if b=0 then z:= 1
                  else z:= x * exp(b-1);
            return z;
            end };
          begin
            read x; 
            read y;
            print exp(y);
          end
