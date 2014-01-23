package geneticAlgorithm;


import geneticAlgorithm.chart.ARunChart;
import geneticAlgorithm.evaluation.EvaluationInterface;
import geneticAlgorithm.grammar.adf.ADF;
import geneticAlgorithm.grammar.adf.ADFDelegate.ADFType;

public abstract class GeneticAlgorithm {

	protected int currentGeneration = 0;
	protected int generationsToDo;
	protected double[][] aptitudes;

	protected Population mainPopulation;	
	protected ADF[] adfs;

	protected Genome bestGenomeEver;

	protected EvaluationInterface evaluator;

	public GeneticAlgorithm(
			GADelegate gaDelegate){

		this.generationsToDo = gaDelegate.getNumberOfGenerations();
		this.mainPopulation = gaDelegate.getMainPopulationDelegate().population;
		this.evaluator = gaDelegate.getEvaluationInterface();
		this.adfs = this.mainPopulation.grammar.getADFs();
		this.evaluateInitialPopulation(gaDelegate.getEvaluationInterface());
		int populationSize = this.mainPopulation.populationSize;

		aptitudes = new double[this.generationsToDo][populationSize];

		//Creates and starts the threads that will be used.

	}

	public void evaluateInitialPopulation(EvaluationInterface evaluator){
		Genome[] initialPopulation = this.mainPopulation.completePopulation;
		for(int i = 0; i < initialPopulation.length; i++){
			initialPopulation[i].evaluate(evaluator);
		}
		bestGenomeEver = this.mainPopulation.getBestGenome();
	}

	public int getCurrentGeneration(){
		return this.currentGeneration;
	}

	/**
	 * Main loop of the algorithm.
	 * @return The population generated in the last iteration.
	 */
	public Genome[] run(){
		while(currentGeneration < generationsToDo){
			Genome[] newGeneration = this.mainPopulation.generateNextGeneration();
			this.mainPopulation.replaceOldGenomes(newGeneration);
			this.evaluateNextGeneration(this.mainPopulation.completePopulation);
			this.mainPopulation.increaseGeneration();

			for(ADF adf : this.adfs){
				adf.evolve();
			}
			this.collectData();
			System.out.println("Current generation: " + currentGeneration);

			Genome bestGenomeInGeneration = this.mainPopulation.getBestGenome();
			if(bestGenomeInGeneration.compareTo(this.bestGenomeEver) == -1){
				this.bestGenomeEver = bestGenomeInGeneration;
			}

			currentGeneration ++;
		}
		return this.mainPopulation.getSortedFromBestToWorst();
	}

	/**
	 * Wakes up and waits for the evaluation threads to evaluate the current population.
	 */
	public void evaluateNextGeneration(Genome[] genomesToEvaluate){
		this.mainPopulation.resetAptitudesOfNewGeneration();
		this.mainPopulation.prepareInfoAboutADFs();
		for(ADF adf : this.adfs){
			if(adf.getADFType() == ADFType.SIMPLE) continue;
			adf.resetAptitudesOfNewGeneration();
			adf.prepareInfoAboutADFs();
		}

		for(Genome genome : genomesToEvaluate){
			genome.evaluate(this.evaluator);
		}

	}

	public Population getPopulation(){
		return this.mainPopulation;
	}

	public void printCurrentAptitudes(){
		for(Genome genome : this.mainPopulation.completePopulation){
			System.out.println(genome.getAptitude());
		}
	}

	/**
	 * Collects data about the aptitude of the current population.
	 */
	protected void collectData(){
		for(int i = 0; i < this.mainPopulation.getPopulationSize(); i++){
			this.aptitudes[this.currentGeneration][i] = this.mainPopulation.completePopulation[i].getAptitude();
		} 
	}

	/**
	 * Creates a graph with the data collected through the generations.
	 */
	public void graph(){
		boolean maximization = this.mainPopulation.getMaximization();
		ARunChart.graph(this.aptitudes, this.getEvaluationsPerGeneration(), maximization);
	}

	public Genome getBestGenomeEver(){
		return this.bestGenomeEver;
	}

	public double[][] getAptitudes(){
		return this.aptitudes;
	}

	public int getEvaluationsPerGeneration(){
		if(this.adfs.length == 0){
			return this.getPopulation().getPopulationSize();
		}

		int result = 0;

		double evaluationsForOldGenomes = 1;
		int oldPopulationSize = this.getPopulation().getPopulationSize() - this.getPopulation().getReplacementSize();
		for(ADF adf : this.adfs){
			if(adf.getADFType() != ADFType.COEVOLUTIVE) continue;
			evaluationsForOldGenomes *= adf.getPopulation().getReplacementSize();
		}
		result += evaluationsForOldGenomes * oldPopulationSize;

		double evaluationsForNewGenomes = 1;
		for(ADF adf : this.adfs){
			if(adf.getADFType() != ADFType.COEVOLUTIVE) continue;
			evaluationsForNewGenomes *= adf.getPopulationSize();
		}
		result += evaluationsForNewGenomes * this.getPopulation().getReplacementSize();

		return result;
	}

	public Population getMainPopulation(){
		return this.mainPopulation;
	}

	public int getGenerationsToDo(){
		return this.generationsToDo;
	}

}
