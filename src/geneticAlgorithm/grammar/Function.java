package geneticAlgorithm.grammar;

public abstract class Function{
	protected Class<?> returnType;
	protected String name;
	protected Class<?>[] parameterTypes;
	protected int minCalls = Integer.MAX_VALUE - 1;
	protected boolean isTerminal;

	public abstract Object evaluate(Object... parameters);

	public Class<?> getReturnType(){
		return this.returnType;
	}

	public String getName(){
		return this.name;
	}

	public void checkIfIsTerminal(){
		this.isTerminal = true;
		for(int i = 0; i < this.parameterTypes.length; i++){
			if(Input.class.isAssignableFrom(this.parameterTypes[i])){
				continue;
			}
			this.isTerminal = false;
			break;
		}
	}

	public abstract Class<?>[] getParameterTypes();

	public int getArity(){
		return this.getParameterTypes().length;
	}

	public int getNumberOfParametersThatAreInputType(){
		int acum = 0;
		for(Class<?> aClass : this.getParameterTypes()){
			if(Input.class.isAssignableFrom(aClass)){
				acum ++;
			}
		}
		return acum;
	}

	public boolean isTerminal() {
		return this.isTerminal;
	}
	public int getMinCalls() {
		return this.minCalls;
	}

	public void setMinCalls(int minCalls) {
		this.minCalls = minCalls;
	}

	public String toString(){
		return this.getName() + " " + this.minCalls;
	}
}