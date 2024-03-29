package tests.functionApproximation;

import geneticAlgorithm.evaluation.EvaluationInterface;
import geneticProgramming.GPDelegate;

public class FunctionAproximationGPDelegate3 extends
		GPDelegate {

	@Override
	public int getNumberOfGenerations() {
		return 1000;
	}

	@Override
	public EvaluationInterface getEvaluationInterface() {
		return new Evaluator();
	}
	
}
