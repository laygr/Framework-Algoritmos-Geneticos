package geneExpressionProgramming;

import geneticAlgorithm.PopulationDelegate;

public abstract class GEPPopulationDelegate extends PopulationDelegate {

	@Override
	public Class<?> getPopulationType() {
		return GEPPopulation.class;
	}
	
	public abstract int getNumberOfORFs();
	
	public abstract int getHeadLength();
	
	public abstract LinkerFunction getLinkerFunction();
	
}
