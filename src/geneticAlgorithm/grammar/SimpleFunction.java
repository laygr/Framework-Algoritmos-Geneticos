package geneticAlgorithm.grammar;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SimpleFunction extends Function{
	public Method method;

	public SimpleFunction(Method method){
		this.method = method;
		this.returnType = method.getReturnType();
		this.parameterTypes = method.getParameterTypes();
		this.name = method.getName();
		checkIfIsTerminal();
	}

	public String getName(){
		return method.getName();
	}

	@Override
	public Object evaluate(Object... parameters) {
		try {
			return this.method.invoke(null, parameters);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Class<?>[] getParameterTypes() {
		return this.parameterTypes;
	}
}
