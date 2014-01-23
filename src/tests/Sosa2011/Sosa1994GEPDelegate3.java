package tests.Sosa2011;

import geneExpressionProgramming.GEPDelegate;
import geneticAlgorithm.evaluation.EvaluationInterface;

public class Sosa1994GEPDelegate3 extends
		GEPDelegate {

	@Override
	public int getNumberOfGenerations() {
		return 1000;
	}
	
	@Override
	public EvaluationInterface getEvaluationInterface() {
		return new Evaluator();
	}
	
}
