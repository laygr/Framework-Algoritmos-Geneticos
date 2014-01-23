package geneticProgramming;

import geneticAlgorithm.grammar.Function;
import geneticAlgorithm.grammar.Grammar;
import geneticAlgorithm.grammar.Input;

import java.util.ArrayList;

public class GPSimpleGene extends GPGene {
	
	protected GPSimpleGene(){
		super();
	}
	
	protected GPSimpleGene(GPGenome genome, GPGene upperGene, ArrayList<Function> functions, Grammar grammar, int level){
		super(genome, upperGene, functions, grammar, level);
	}

	protected GPSimpleGene(GPGenome genome, GPGene upperGene, Function function, Grammar grammar, int level, int maxDepth){
		super(genome, upperGene, function, grammar, level, maxDepth);
	}
	
	public Object evaluate(Input input){
		Object superResult = super.evaluate(input);
		if(superResult != null) return superResult;
		lastInput = input;
		Object[] parameters = new Object[function.getParameterTypes().length];
		int i = 0;
		int childsIterator = 0;
		for(Class<?> parameter : function.getParameterTypes()){
			if(Input.class.isAssignableFrom(parameter)){
				parameters[i] = input;
			}else{
				parameters[i] = childs.get(childsIterator).evaluate(input);
				childsIterator ++;
			}
			i++;
		}

		this.result = function.evaluate(parameters);

		/*
		System.out.print("Invoked: " + function.method.getName() + " with parameters: ");
		for(Object object : parameters){
			System.out.print(object.toString() + " ");
		}
		System.out.println("\n\tResult: " + result.toString());
		 */
		return this.result;
	}
}
