package tests.koza1994;

import geneticAlgorithm.test.GroupOfTests;
import tests.koza1994.*;


public class GroupOfTestsKoza1994 {
	public static void main(String[] args){
		Class<?>[] testClasses = {Koza1994_1.class};
		GroupOfTests groupOfTests = new GroupOfTests("Koza 1994", testClasses, 10);
		groupOfTests.graphAverageAverage();
		groupOfTests.graphAverageBest();
		for(int i = 0; i < testClasses.length ; i++){
			System.out.printf("Aptitude of geneticAlgorithm.test %d: %f\n", i, groupOfTests.getTestResultAtIndex(i).getBestGenomeOfAllRuns().getAptitude());
		}
	}
}