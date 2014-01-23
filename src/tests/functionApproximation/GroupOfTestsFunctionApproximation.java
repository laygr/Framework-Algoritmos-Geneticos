package tests.functionApproximation;

import geneticAlgorithm.test.GroupOfTests;
import tests.functionApproximation.*;

public class GroupOfTestsFunctionApproximation {
	public static void main(String[] args){
		Class<?>[] testClasses =
                {
                    FunctionAproximation1.class, FunctionAproximation2.class,
                    FunctionAproximation3.class, FunctionAproximation4.class
                };
		GroupOfTests groupOfTests = new GroupOfTests("Function Aproximation", testClasses, 30);
		groupOfTests.graphAverageAverage();
		groupOfTests.graphAverageBest();
	}
}