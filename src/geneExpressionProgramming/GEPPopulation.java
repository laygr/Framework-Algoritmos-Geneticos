package geneExpressionProgramming;

import geneExpressionProgramming.VirtualGene.CrossoverPointsDistribution;
import geneticAlgorithm.Genome;
import geneticAlgorithm.Population;
import geneticAlgorithm.PopulationDelegate;
import geneticAlgorithm.grammar.Type;

public class GEPPopulation extends Population {

	LinkerFunction linkerFunction;
	int headLength;
	int tailLength;
	int numberOfORFs;
	int largestTypeNumberOfFunctions;

	Type orfReturnType;
	int orfLength;

	VirtualGene vg;

	public GEPPopulation(PopulationDelegate populationDelegate) {
		super(populationDelegate);
		GEPPopulationDelegate castedPopulationDelegate = (GEPPopulationDelegate) populationDelegate;
		this.linkerFunction = castedPopulationDelegate.getLinkerFunction();
		this.headLength = castedPopulationDelegate.getHeadLength();
		this.tailLength = headLength * (grammar.mostNeededParametersInAFunction - 1) + 1;
		this.orfLength = headLength + tailLength;
		this.numberOfORFs = castedPopulationDelegate.getNumberOfORFs();
		this.largestTypeNumberOfFunctions = 0;
		for(Type type : grammar.getTypes()){
			if(type.getFunctions().size() > this.largestTypeNumberOfFunctions){
				this.largestTypeNumberOfFunctions = type.getFunctions().size();
			}
		}
		this.orfReturnType = this.grammar.getTypeForClass(this.linkerFunction.getORFReturnType());
		
		try {
			this.vg = new VirtualGene(0, this.largestTypeNumberOfFunctions, 10, this.largestTypeNumberOfFunctions/10, 1, CrossoverPointsDistribution.CONTINUOUS);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected Genome generateAGenome(int indexInPopulation) {
		return new GEPGenome(this, indexInPopulation, numberOfORFs, headLength, tailLength, largestTypeNumberOfFunctions);
	}

	@Override
	public Genome[] replicateGenomes(Genome[] genomesToReplicate) {
		Genome[] replicates = new Genome[genomesToReplicate.length];
		for(int i = 0; i < genomesToReplicate.length; i++){
			replicates[i] = genomesToReplicate[i].clone();
		}
		return replicates;
	}

	@Override
	public void mutation(Genome[] genomesToMutate) {
		for(Genome genome : genomesToMutate){
			GEPGenome castedGenome = (GEPGenome) genome;
			int geneToMutateIndex = (int) (castedGenome.genes.length * Math.random());
			castedGenome.genes[geneToMutateIndex] = (int) vg.mutation(castedGenome.genes[geneToMutateIndex]);
		}
	}

	@Override
	public void recombinateGenomes(Genome descendant1, Genome descendant2) {
		GEPGenome castedGenomeDescendant1 = (GEPGenome) descendant1;
		GEPGenome castedGenomeDescendant2 = (GEPGenome) descendant2;

		int geneToMutateIndex = (int) (castedGenomeDescendant1.genes.length * Math.random());
		double[] crossedGenes = vg.crossover(castedGenomeDescendant1.genes[geneToMutateIndex], castedGenomeDescendant2.genes[geneToMutateIndex]);
		castedGenomeDescendant1.genes[geneToMutateIndex] = (int)crossedGenes[1];
		castedGenomeDescendant2.genes[geneToMutateIndex] = (int)crossedGenes[0];
		for(int i = geneToMutateIndex + 1; i < castedGenomeDescendant1.genes.length; i++){
			int aux = castedGenomeDescendant1.genes[i];
			castedGenomeDescendant1.genes[i] = castedGenomeDescendant2.genes[i];
			castedGenomeDescendant2.genes[i] = aux;
		}

	}
	
	public LinkerFunction getLinkerFunction(){
		return this.linkerFunction;
	}
	
	public int getNumberOfORFs(){
		return this.numberOfORFs;
	}
	
	public int getORFLength(){
		return this.orfLength;
	}
	
	public Type getORFReturnType(){
		return this.orfReturnType;
	}

}
