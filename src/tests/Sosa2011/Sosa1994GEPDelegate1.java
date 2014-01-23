package tests.Sosa2011;

import geneticAlgorithm.evaluation.EvaluationInterface;
import geneticProgramming.GPDelegate;

public class Sosa1994GEPDelegate1 extends
		GPDelegate {

	@Override
	public int getNumberOfGenerations() {
		return 30;
	}
	
	@Override
	public EvaluationInterface getEvaluationInterface() {
		return new Evaluator();
	}
	
}
