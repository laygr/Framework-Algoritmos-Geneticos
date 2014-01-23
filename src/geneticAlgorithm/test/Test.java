package geneticAlgorithm.test;

import geneticAlgorithm.GADelegate;
import geneticAlgorithm.GeneticAlgorithm;
import geneticAlgorithm.GeneticAlgorithmFactory;
import geneticAlgorithm.Genome;
public abstract class Test{

	public GeneticAlgorithm geneticAlgorithm;

	public abstract GADelegate configure();

	protected Genome[] bestGenomes;
	
	public double[][][] runTest(int runs){
		double[][][] collectedData = new double[runs][][];
		bestGenomes = new Genome[runs];
		for(int i = 0; i < runs; i++){
			this.geneticAlgorithm = GeneticAlgorithmFactory.createGeneticAlgorithm(this.configure());
			geneticAlgorithm.run();
			collectedData[i] = geneticAlgorithm.getAptitudes();
			bestGenomes[i] = geneticAlgorithm.getBestGenomeEver();
		}
		return collectedData;
	}
	
	public Genome[] getBestGenomes(){
		return this.bestGenomes;
	}

	public int getEvaluationsPerGeneration(){
		return this.geneticAlgorithm.getEvaluationsPerGeneration();
	}
	
	public boolean getMaximization(){
		return this.geneticAlgorithm.getMainPopulation().getMaximization();
	}
	
	public int getGenerations(){
		return this.geneticAlgorithm.getGenerationsToDo();
	}
	
	public int getTotalEvaluations(){
		return this.getGenerations() * this.getEvaluationsPerGeneration();
	}
	
	public abstract String getName();
}
