package geneticProgramming;

import geneticAlgorithm.PopulationDelegate;

public abstract class GPPopulationDelegate extends PopulationDelegate {
	
	public Class<?> getPopulationType(){
		return GPPopulation.class;
	}

	public abstract int getMaxLevel();
}
