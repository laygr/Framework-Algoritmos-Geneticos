/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tests.koza1994;
import geneticAlgorithm.evaluation.EvaluationInterface;
import geneticProgramming.GPDelegate;

public class Koza1994GPDelegate_1 extends
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
