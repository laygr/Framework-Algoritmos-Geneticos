package geneticProgramming;

import java.util.ArrayList;

import geneticAlgorithm.grammar.*;
import geneticAlgorithm.grammar.adf.ADF;
import geneticAlgorithm.*;

@SuppressWarnings({"rawtypes"})
public class GPGenome extends Genome{
	protected int maxLevel;
	
	public GPGene rootGene;

	public GPGenome(Population population, Integer indexInPopulation){
		super(population, indexInPopulation);
	}

	public GPGenome(String representation, Grammar grammar, int maxLevel, boolean maximization){
		this.maxLevel = maxLevel;
		this.maximization = maximization;
		this.rootGene = GPGeneFactory.createGPGene(this, null, representation, grammar, 0);
	}

	public GPGenome(Integer indexInPopulation, Grammar grammar, Type root, int maxLevel, boolean maximization){
		super(indexInPopulation, maximization);
		this.maxLevel = maxLevel;
		this.rootGene = GPGeneFactory.createGPGene(this, null, grammar, root, 0, this.maxLevel);
	}

	public int numberOfGenes(){
		return 1 + rootGene.numberOfSubgenes;
	}

	public String toString(){
		return rootGene.toString();
	}

	public String toString(Grammar grammar){
		String result = rootGene.toString(grammar);
		return result;
	}

	public GPGene getGene(int numberOfGene){
		return rootGene.getNode(numberOfGene);
	}

	public ArrayList<GPGene> getCandidateGenesForCrossing(Class typeClass, int otherGeneLevel, int otherGeneLevelOfDeepestGene){
		ArrayList<GPGene> candidates = new ArrayList<GPGene>();
		rootGene.addCandidateGenesForCrossing(candidates, typeClass, otherGeneLevel, otherGeneLevelOfDeepestGene);
		return candidates;
	}

	public Genome clone(){
		GPGenome newGenome = (GPGenome) super.clone();
		newGenome.rootGene = this.rootGene.clone();
		newGenome.aptitude = this.aptitude;
		newGenome.propagateNewGenome();
		newGenome.maxLevel = this.maxLevel;
		return newGenome;
	}

	public void lookForADFs(){
		this.adfs = new ArrayList<ADF>();
		rootGene.lookForADFs(this.adfs);
		this.genomeIndexForADFs = new int[this.adfs.size()];
		super.lookForADFs();
	}

	public Object evaluate(Input input){
		try {
			Object result = rootGene.evaluate(input);
			return result;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return null;
	}

	public int getMaxLevel(){
		return this.maxLevel;
	}
	
	public void propagateNewGenome(){
		this.rootGene.propagateNewGenome(this);
	}

	@Override
	public void prepareForNextGeneration() {
		super.prepareForNextGeneration();
	}

}