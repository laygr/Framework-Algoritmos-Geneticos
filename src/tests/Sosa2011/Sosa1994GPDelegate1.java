package tests.Sosa2011;

import geneticAlgorithm.evaluation.EvaluationInterface;
import geneticProgramming.GPDelegate;

public class Sosa1994GPDelegate1 extends
		GPDelegate {

	@Override
	public int getNumberOfGenerations() {
		return 50;
	}
	
	@Override
	public EvaluationInterface getEvaluationInterface() {
		return new Evaluator();
	}
	
}
