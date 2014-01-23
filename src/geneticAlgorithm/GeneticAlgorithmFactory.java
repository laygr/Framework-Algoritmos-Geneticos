package geneticAlgorithm;

import geneticProgramming.GPDelegate;
import geneticProgramming.GeneticProgramming;

public class GeneticAlgorithmFactory {
	
	public static GeneticAlgorithm createGeneticAlgorithm(GADelegate gaDelegate){
		if(GPDelegate.class.isAssignableFrom(gaDelegate.getClass())){
			GPDelegate gpDelegate = (GPDelegate) gaDelegate;
			return new GeneticProgramming(gpDelegate);
		}
		return null;
	}
}
