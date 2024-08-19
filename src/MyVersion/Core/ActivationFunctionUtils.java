package MyVersion.Core;

public class ActivationFunctionUtils {
    static double sigmoidActivaionFunction(double x){
        return (1/(1+ Math.pow(Math.E,-x)));
    }
    
    static double tanActivationFunction(double x) {
    	return Math.tanh(x);
    }
    
    static double leackyReluActivaionFunction(double x){
        return Math.max(0.1*x, x);
    }
    static double tanActivationFunctionDX(double x) {
    	return 1/(Math.pow(Math.cosh(x),2));
    }
    
    static double sigmoidActivationFunctionDX(double x){
        return sigmoidActivaionFunction(x)*(1-sigmoidActivaionFunction(x));
    }
    static double activationFunctionDX(double x,ActivationFunctions myFunc){
    	if(myFunc==ActivationFunctions.Tan) {
    		return tanActivationFunctionDX(x);
    	}else if(myFunc==ActivationFunctions.Sigmoid) {
    		return sigmoidActivationFunctionDX(x);
    	}else {
    		return (Double) null;
    	}
    }
    public static FunctionChooseInterface chooseFunctionStatic(ActivationFunctions myFunc) {
   	if(myFunc==ActivationFunctions.Tan){
   		return (double x)->{
   			return ActivationFunctionUtils.tanActivationFunction(x);
   		};
   	}else if(myFunc==ActivationFunctions.Sigmoid) {
   		return (double x)->{
   			return ActivationFunctionUtils.sigmoidActivaionFunction(x);
   		};
   	}else if(myFunc==ActivationFunctions.LeackyRelu) {
   		return (double x)->{
   			return ActivationFunctionUtils.leackyReluActivaionFunction(x);
   		};
   	}
   	return null;
   }
}
