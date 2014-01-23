package geneticAlgorithm;

import geneticAlgorithm.evaluation.EvaluationInterface;
import geneticAlgorithm.grammar.Input;
import geneticAlgorithm.grammar.adf.ADF;
import geneticAlgorithm.grammar.adf.ADFDelegate.ADFType;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public abstract class Genome implements Comparable <Genome>{
	protected Population population;
	protected int indexInPopulation;


	protected double aptitude;

	protected ArrayList<ADF> adfs;
	protected int[] genomeIndexForADFs;
	protected Genome[] bestGenomeForEachADF;
	protected int iterationsToDo;
	protected GeneticAlgorithm geneticAlgorithm;

	protected boolean isFromNewGeneration;

	protected boolean maximization;

	public Genome(){

	}

	public Genome(Population population, Integer indexInPopulation){
		this.population = population;
		this.indexInPopulation = indexInPopulation;
	}

	public Genome(Integer indexInPopulation, boolean maximization){
		this.maximization = maximization;
		this.indexInPopulation = indexInPopulation;
	}

	public void setAptitude(double aptitude){
		this.aptitude = aptitude;
	}

	public void sendAptitude(double aptitude){
		if(maximization && this.aptitude < aptitude ||
				!maximization && this.aptitude > aptitude){
			this.setAptitude(aptitude);
			for(int i = 0; i < this.adfs.size(); i++){
				this.bestGenomeForEachADF[i] = this.adfs.get(i).getPopulation().getGenomeAtIndex(this.genomeIndexForADFs[i], false);
			}
		}
	}

	public double getAptitude(){
		return this.aptitude;
	}

	public int compareTo(Genome otherGenome){
		if(this.aptitude == otherGenome.getAptitude()) return 0;
		if(this.maximization && this.aptitude > otherGenome.getAptitude() ||
				!this.maximization && this.aptitude < otherGenome.getAptitude()
		){
			return -1;
		}else{
			return 1;
		}
	}

	public abstract Object evaluate(Input input);
	
	public void evaluate(EvaluationInterface evaluator){
		for(int i = 0; i < iterationsToDo; i++){
			double evaluation = evaluator.evaluateGenome(this);
			this.sendAptitude(evaluation);
			for(int j = 0; j < adfs.size(); j++){
				ADF adf = this.adfs.get(j);
				adf.sendAptitudeToGenome(evaluation, this.genomeIndexForADFs[j]);
			}
			this.increaseIndexes(this.adfs.size() - 1);
		}
	}

	public void resetAptitude(){
		this.aptitude = this.maximization? Double.MIN_VALUE : Double.MAX_VALUE;
	}

	public Genome clone(){
		Genome clone = null;
		try {
			clone = this.getClass().getConstructor(Population.class, Integer.class).newInstance(this.population, this.indexInPopulation);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return clone;
	}

	public void increaseIndexes(int startingFrom){
		if(this.adfs.size() <= 0) return;
		this.genomeIndexForADFs[startingFrom] ++;
		if(this.adfs.get(startingFrom).getPopulationSize() == this.genomeIndexForADFs[startingFrom] ||
				this.adfs.get(startingFrom).getADFType() != ADFType.COEVOLUTIVE){
			this.genomeIndexForADFs[startingFrom] = 0;
			if(startingFrom == 0){
				return;
			}else{
				this.increaseIndexes(startingFrom - 1);
			}
		}
	}

	public void computeIterationsToDo(){
		this.iterationsToDo = 1;

		int i = 0;
		for(ADF adf : this.adfs){
			if(adf.getADFType() == ADFType.COEVOLUTIVE){
				if(isFromNewGeneration){
					this.iterationsToDo *= adf.getPopulationSize();
				}else{
					this.iterationsToDo *= adf.getNewGenerationSize();
				}
			}else{
				this.genomeIndexForADFs[i] = this.indexInPopulation;
			}
			i++;
		}
		this.genomeIndexForADFs  = new int[this.adfs.size()];
	}

	public int[] getGenomeIndexForADFs(){
		return this.genomeIndexForADFs;
	}
	public ArrayList<ADF> getADFs(){
		return this.adfs;
	}

	public int getIterationsToDo(){
		return this.iterationsToDo;
	}

	public boolean getMaximization(){
		return this.maximization;
	}

	public boolean isFromNewGeneration(){
		return this.isFromNewGeneration;
	}

	public void lookForADFs(){
		this.bestGenomeForEachADF = new Genome[this.adfs.size()];
	}
	
	public Genome[] getBestGenomeForBestADF(){
		return this.bestGenomeForEachADF;
	}
	
	public void setIsFromNewGeneration(boolean isFromNewGeneration){
		this.isFromNewGeneration = isFromNewGeneration;
	}
	
	public Object getIndexInPopulation() {
		return this.indexInPopulation;
	}

	public void prepareForNextGeneration(){
		this.isFromNewGeneration = true;
		this.aptitude = this.maximization? 0 : Double.MAX_VALUE;
		this.lookForADFs();
		this.computeIterationsToDo();
	}
	
}