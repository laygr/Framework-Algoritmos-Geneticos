package geneExpressionProgramming;

public abstract class LinkerFunction {
	
	public abstract Class<?> getReturnType();
	
	public abstract Object evaluate(Object[] parameters);
	
	public abstract Class<?> getORFReturnType();
	
}
