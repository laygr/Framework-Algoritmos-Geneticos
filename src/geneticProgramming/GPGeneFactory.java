package geneticProgramming;

import java.util.ArrayList;

import geneticAlgorithm.grammar.Function;
import geneticAlgorithm.grammar.Grammar;
import geneticAlgorithm.grammar.SimpleFunction;
import geneticAlgorithm.grammar.Type;

public class GPGeneFactory {
	
	public static GPGene createGPGene(GPGenome genome, GPGene upperGene, Grammar grammar, Type geneType, int level, int maxDepth){
		Function function = geneType.getFunctionWithMaxDepth(maxDepth);
		if(function.getClass() == SimpleFunction.class){
			return new GPSimpleGene(genome, upperGene, function, grammar, level, maxDepth);
		}else{
			return new GPADFGene(genome, upperGene, function, grammar, level, maxDepth);
		}
	}
	
	public static GPGene createGPGene(GPGenome genome, GPGene upperGene, String representation, Grammar grammar, int level){
		representation = representation.replace("(", " ");
		representation = representation.replace("), ", " ");
		representation = representation.replace(");", "");
		representation = representation.replace(")", " ");
		representation = representation.replace(", ", " ");
		
		String[] functionsString = representation.split(" ");
		ArrayList<Function> functions = new ArrayList<Function>(functionsString.length - 1);
		for(int i = 1; i < functionsString.length; i++){
			String functionName = functionsString[i];
			functions.add(grammar.getFunctionNamed(functionName));
		}
		return GPGeneFactory.createGPGene(genome, upperGene, functions, grammar, level);
	}
	
	public static GPGene createGPGene(GPGenome genome, GPGene upperGene, ArrayList<Function> functions, Grammar grammar, int level){
		if(functions.get(0).getClass() == SimpleFunction.class){
			return new GPSimpleGene(genome, upperGene, functions, grammar, level);
		}else{
			return new GPADFGene(genome, upperGene, functions, grammar, level);
		}
	}
}
