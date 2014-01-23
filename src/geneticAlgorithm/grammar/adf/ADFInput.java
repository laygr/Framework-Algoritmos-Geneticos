package geneticAlgorithm.grammar.adf;

import geneticAlgorithm.grammar.Input;

public abstract class ADFInput extends Input {

	protected Object[]parameters;
	protected int iterator = 0;
	
	public ADFInput(Integer numberOfParameters){
		this.parameters = new Object[numberOfParameters];
	}
	
	public void addParameter(Object parameter){
		this.parameters[iterator] = parameter;
		iterator++;
	}
	
	public Object getObjectAtIndex(int index){
		return this.parameters[index];
	}
	
	public Object[] getParameters(){
		return this.parameters;
	}
	
	@Override
	public boolean equals(Input otherInput) {
		ADFInput otherADFInput = (ADFInput) otherInput;
		if(this.parameters.length != otherADFInput.parameters.length) return false;
		for(int i = 0; i < this.parameters.length; i++){
			if(!this.parameters[i].equals(otherADFInput.parameters[i])) return false;
		}
		return false;
	}
	
	public abstract Class<?>[] getParameterTypes();

}
