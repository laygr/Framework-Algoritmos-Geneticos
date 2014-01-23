package geneticAlgorithm.test;

import geneticAlgorithm.GeneticAlgorithm;
import geneticAlgorithm.Genome;
import geneticAlgorithm.Population;

public class TestResult {
	public int evaluationsPerGeneration;
	public int generations;
	public int totalEvaluations;

	public String testName;

	public double[] averageBest;
	public double[] averageAptitude;
	public Genome[] bestGenome;

	public double[][][] collectedData;
	public double[][] averageAptitudes;
	public double[][] bestAptitudes;

	public Test test;

	public int getGenerations(){
		return generations;
	}

	public TestResult(Test test, int runs){
		this.test = test;
		this.collectedData = test.runTest(runs);
		bestGenome = test.getBestGenomes();
		this.evaluationsPerGeneration = test.getEvaluationsPerGeneration();
		this.generations = test.getGenerations();
		this.totalEvaluations = test.getTotalEvaluations();
		this.testName = test.getName();

		int generations = test.getGenerations();
		averageAptitude = new double[generations];
		averageBest = new double[generations];

		this.calculateAverageAptitudesAndBestAptitudes();
		this.calculateAverageAptitude();
		this.calculateAverageBest();
	}


	public void calculateAverageAptitudesAndBestAptitudes(){
		GeneticAlgorithm geneticAlgorithm = this.test.geneticAlgorithm;
		int generationsToDo = geneticAlgorithm.getGenerationsToDo();
		Population mainPopulation = geneticAlgorithm.getMainPopulation();
		int populationSize = mainPopulation.getPopulationSize();

		this.averageAptitudes = new double[generationsToDo][populationSize];

		boolean maximization = geneticAlgorithm.getMainPopulation().getMaximization();
		this.bestAptitudes = new double[collectedData.length][generationsToDo];
		for(int i = 0; i < this.bestAptitudes.length; i++){
			for(int j = 0; j < this.bestAptitudes[i].length; j++){
				this.bestAptitudes[i][j] = maximization? Double.MIN_VALUE : Double.MAX_VALUE;
			}
		}

		for(int i = 0; i < generationsToDo; i++){
			for(int j = 0; j < collectedData.length; j++){
				for(int k = 0; k < populationSize; k++){
					averageAptitudes[i][k] += collectedData[j][i][k];
					if(maximization && collectedData[j][i][k] > this.bestAptitudes[j][i] ||
							!maximization && collectedData[j][i][k] < this.bestAptitudes[j][i]){
						this.bestAptitudes[j][i] = collectedData[j][i][k];
					}
				}
			}
			for(int k = 0; k < populationSize; k++){
				averageAptitudes[i][k] /= collectedData.length;
			}
		}
	}

	public void calculateAverageAptitude(){
		for(int i = 0; i < generations; i++){
			averageAptitude[i] = 0;
			for(int j = 0; j < this.averageAptitudes[i].length; j++){
				averageAptitude[i] += this.averageAptitudes[i][j];
			}
			averageAptitude[i] /= averageAptitudes[i].length;

		}
	}
	
	public void calculateAverageBest(){
		for(int i = 0; i < generations; i++){
			averageBest[i] = 0;
			for(int j = 0; j < this.bestAptitudes.length; j++){
				averageBest[i] += this.bestAptitudes[j][i];				
			}
			averageBest[i] /= this.bestAptitudes.length;
		}
	}
	
	public Genome getBestGenomeOfAllRuns(){
		boolean maximization = this.test.getMaximization();
		Genome bestGenomeOfAllRuns = this.bestGenome[0];
		for(int i = 0; i < this.bestGenome.length; i++){
			if(maximization && this.bestGenome[i].getAptitude() > bestGenomeOfAllRuns.getAptitude() ||
					!maximization && this.bestGenome[i].getAptitude() < bestGenomeOfAllRuns.getAptitude()){
				bestGenomeOfAllRuns = this.bestGenome[i];
			}
		}
		return bestGenomeOfAllRuns;
	}

}
