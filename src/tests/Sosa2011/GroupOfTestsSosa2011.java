package tests.Sosa2011;

import geneticAlgorithm.test.GroupOfTests;
import tests.Sosa2011.Sosa2011_1GEP;
import tests.Sosa2011.Sosa2011_4GEP;


public class GroupOfTestsSosa2011 {
	public static void main(String[] args){
		Class<?>[] testClasses = {Sosa2011_1GEP.class, Sosa2011_4GEP.class};
		GroupOfTests groupOfTests = new GroupOfTests("Sosa 2011", testClasses, 20);
                groupOfTests.graph();

		for(int i = 0; i < testClasses.length ; i++){
			System.out.printf("Aptitude of geneticAlgorithm.test %d: %f\n", i, groupOfTests.getTestResultAtIndex(i).getBestGenomeOfAllRuns().getAptitude());
		}
	}
}