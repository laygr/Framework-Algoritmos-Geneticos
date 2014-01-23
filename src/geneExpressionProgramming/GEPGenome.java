package geneExpressionProgramming;

import java.util.ArrayList;

import geneticAlgorithm.*;
import geneticAlgorithm.grammar.*;
import geneticAlgorithm.grammar.adf.ADF;

public class GEPGenome extends Genome{

	protected int[] genes;
	protected ORFTree[] orfTrees;
	protected GEPPopulation population;
	
	public GEPGenome(Population population, Integer indexInPopulation){
		super(population, indexInPopulation);
	}

	public GEPGenome(Population population, int indexInPopulation, int numberOfORFs, int headLength, int tailLength, int largestTypeNumberOfFunctions){
		super(population, indexInPopulation);
		this.population = (GEPPopulation) population;
		this.genes = new int[numberOfORFs * (headLength + tailLength)];
		for(int i = 0; i < genes.length; i++){
			this.genes[i] = (int) (Math.random() * largestTypeNumberOfFunctions);
		}
	}

	@Override
	public Object evaluate(Input input) {
		Object[] orfResults = new Object[orfTrees.length];
		for(int i = 0; i < orfTrees.length; i++){
			orfResults[i] = this.orfTrees[i].evaluate(input);
		}
		return this.population.getLinkerFunction().evaluate(orfResults);
	}
	
	public void generateTrees(){
		int numberOfORFs = this.population.getNumberOfORFs();
		int orfLength = this.population.getORFLength();
		int tailLength = orfLength - this.population.headLength;
		Type orfReturnType = this.population.getORFReturnType();
		Grammar grammar = this.population.getGrammar();
		this.orfTrees = new ORFTree[numberOfORFs];
		
		for(int i = 0; i < numberOfORFs; i++){
			ArrayList<Integer> orfGenes = new ArrayList<Integer>(orfLength);
			for(int j = i * orfLength; j < i*orfLength + orfLength; j++){
				orfGenes.add(genes[j]);
			}
			this.orfTrees[i] = new ORFTree(this, grammar, orfReturnType, orfGenes, tailLength);
		}
	}

	public Genome clone(){
		GEPGenome newGenome = (GEPGenome) super.clone();
		newGenome.population = this.population;
		newGenome.aptitude = this.aptitude;
		newGenome.genes = new int[this.genes.length];
		for(int i = 0; i < genes.length; i++){
			newGenome.genes[i] = this.genes[i];
		}
		return newGenome;
	}
	
	public void lookForADFs(){
		this.adfs = new ArrayList<ADF>();
                if(this.orfTrees == null || this.orfTrees[0] == null){
                    this.generateTrees();
                }
		for(ORFTree orfTree : this.orfTrees){
			orfTree.lookForADFs(adfs);
		}
		this.genomeIndexForADFs = new int[this.adfs.size()];
		super.lookForADFs();
	}
	
	public void prepareForNextGeneration(){
		this.generateTrees();
		super.prepareForNextGeneration();
	}
}
