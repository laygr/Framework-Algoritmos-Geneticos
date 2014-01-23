package geneticAlgorithm.grammar.adf;

import geneticAlgorithm.PopulationDelegate;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class ADFDelegate
{
	public PopulationDelegate populationDelegate;
	public Class<?>[] parameterTypes = null;
	
	public enum ADFType{
		SIMPLE, SEMICOEVOLUTIVE, COEVOLUTIVE
	}
	
	public Class<?> getADFInputClass(){
		return this.getPopulationDelegate().getInputClass();
	}
	
	public Class<?>[] getParameterTypes(){
		if(parameterTypes != null) return parameterTypes;
		ADFInput adfInputMock = null;
		try {
			Constructor<?> constructor = this.getADFInputClass().getConstructor(Integer.class);
			adfInputMock = (ADFInput)constructor.newInstance(0);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		this.parameterTypes = adfInputMock.getParameterTypes();
		return this.parameterTypes;
	}
	
	public abstract ADFType getADFType();

	public void setPopulationDelegate(PopulationDelegate populationDelegate){
		this.populationDelegate = populationDelegate;
	}
	
	public PopulationDelegate getPopulationDelegate(){
		return this.populationDelegate;
	}
	
}