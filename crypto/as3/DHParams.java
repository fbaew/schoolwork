import java.security.*;
import javax.crypto.spec.*;

public class DHParams {

    SecureRandom random = null;
    AlgorithmParameterGenerator parameterMaker = null;
    DHParameterSpec result = null;    

    public DHParams() {

        try {
            random = SecureRandom.getInstance("SHA1PRNG");
            parameterMaker = AlgorithmParameterGenerator.getInstance("DH");
        }
        catch (Exception e) { System.out.println("badness!"); }

        parameterMaker.init(1024,random);

        AlgorithmParameters parameters = parameterMaker.generateParameters();
        try {
            result = (DHParameterSpec)parameters.getParameterSpec(DHParameterSpec.class);
        } catch (Exception e) { System.out.println("Something bad happened."); }
    }
    
    public DHParameterSpec specs() {
        return result;
    }
    
}