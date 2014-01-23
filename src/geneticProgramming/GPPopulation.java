package geneticProgramming;

import java.util.ArrayList;

import geneticAlgorithm.Genome;
import geneticAlgorithm.Population;
import geneticAlgorithm.PopulationDelegate;

public class GPPopulation extends Population {
	protected int maxLevel;
	
	
	public GPPopulation(PopulationDelegate gpPopulationDelegate) {
		super(gpPopulationDelegate);
		GPPopulationDelegate castedGPPopulationDelegate = (GPPopulationDelegate) gpPopulationDelegate;
		this.maxLevel = castedGPPopulationDelegate.getMaxLevel();
	}

	@Override
	protected Genome generateAGenome(int indexInPopulation) {
		return new GPGenome(indexInPopulation, grammar, this.rootType, this.maxLevel, this.maximization);
	}

	public Genome[] replicateGenomes(Genome[] genomesToReplicate) {
		Genome[] copies = new Genome[genomesToReplicate.length];
		for(int i = 0; i < genomesToReplicate.length; i++){
			copies[i] = genomesToReplicate[i].clone();
		}
		return copies;
	}

	public void mutation(Genome[] genomesToMutate){
		GPGenome castedGenome;
		for(Genome genome : genomesToMutate){
			castedGenome = (GPGenome)genome;
			if(Math.random() > chanceOfMutation) continue;

			int geneToMutateIndex = (int) (castedGenome.numberOfGenes() * Math.random());
			GPGene geneToMutate = castedGenome.getGene(geneToMutateIndex);
			GPGene newGene = GPGeneFactory.createGPGene(castedGenome, geneToMutate.upperGene, grammar, geneToMutate.getType(), geneToMutate.getLevel(), geneToMutate.genome.getMaxLevel() - geneToMutate.getLevel());
			if(geneToMutate.upperGene != null){
				geneToMutate.upperGene.childs.add(geneToMutate.getIndexInParent(), newGene);
				geneToMutate.upperGene.childs.remove(geneToMutate);
			}else{
				castedGenome.rootGene = newGene;
			}
			newGene.setAndPropagateNullResult();
			newGene.propagateDeepestGene();
			newGene.propagateChangeInNumberOfSubgenes(newGene.numberOfSubgenes - geneToMutate.numberOfSubgenes);
			castedGenome.propagateNewGenome();
			geneToMutate = null;

		}
	}

	/**
	 * Crosses 2 given parents. Is algorithm dependant.
	 * @param copy1 
	 * @param copy2
	 * @return An array with the generated descendants.
	 */
	@Override
	public void recombinateGenomes(Genome copy1, Genome copy2) {
		 this.crossGenomes((GPGenome) copy1, (GPGenome) copy2);
	}
	public void crossGenomes(GPGenome descendant1, GPGenome descendant2){
		if(Math.random() > chanceOfCrossing) return;

		int geneToCrossIndex = (int) (Math.random() * descendant1.numberOfGenes());

		GPGene geneToCrossDescendant1 = descendant1.getGene(geneToCrossIndex);

		Class<?> typeClass = geneToCrossDescendant1.getTypeClass();
		ArrayList<GPGene> descendant2CandidateGenes = descendant2.getCandidateGenesForCrossing(typeClass, geneToCrossDescendant1.getLevel(), geneToCrossDescendant1.levelOfDeepestGene);

		if(descendant2CandidateGenes.size() == 0){
			return;
		}
		int geneToCrossIndexParent2 = (int) (Math.random() * descendant2CandidateGenes.size());
		GPGene geneToCrossDescendant2 = descendant2CandidateGenes.get(geneToCrossIndexParent2);

		//Change upperGene
		GPGene upperGeneAux = geneToCrossDescendant1.upperGene;

		int geneToCrossDescendant1Index = geneToCrossDescendant1.getIndexInParent();
		int geneToCrossDescendant2Index = geneToCrossDescendant2.getIndexInParent();

		if(geneToCrossDescendant1.upperGene != null){
			geneToCrossDescendant1.upperGene.childs.remove(geneToCrossDescendant1);
		}
		if(geneToCrossDescendant2.upperGene != null){
			geneToCrossDescendant2.upperGene.childs.remove(geneToCrossDescendant2);
		}

		geneToCrossDescendant1.upperGene = geneToCrossDescendant2.upperGene;
		geneToCrossDescendant2.upperGene = upperGeneAux;

		//Add crossed genes to their parent gene
		if(geneToCrossDescendant1.upperGene == null){
			descendant2.rootGene = geneToCrossDescendant1;
		}else{
			geneToCrossDescendant1.upperGene.childs.add(geneToCrossDescendant2Index, geneToCrossDescendant1);
			geneToCrossDescendant1.upperGene.setAndPropagateNullResult();
		}
		if(geneToCrossDescendant2.upperGene == null){
			descendant1.rootGene = geneToCrossDescendant2;
		}else{
			geneToCrossDescendant2.upperGene.childs.add(geneToCrossDescendant1Index, geneToCrossDescendant2);
			geneToCrossDescendant2.upperGene.setAndPropagateNullResult();
		}

		//Update new levels
		int levelAux = geneToCrossDescendant1.getLevel();
		geneToCrossDescendant1.setLevel(geneToCrossDescendant2.getLevel());
		geneToCrossDescendant2.setLevel(levelAux);

		//Propagate change in new number of subgenes
		geneToCrossDescendant1.propagateChangeInNumberOfSubgenes(geneToCrossDescendant1.numberOfSubgenes - geneToCrossDescendant2.numberOfSubgenes);
		geneToCrossDescendant2.propagateChangeInNumberOfSubgenes(geneToCrossDescendant2.numberOfSubgenes - geneToCrossDescendant1.numberOfSubgenes);

		geneToCrossDescendant1.propagateNewLevel();
		geneToCrossDescendant2.propagateNewLevel();

		geneToCrossDescendant1.propagateDeepestGene();
		geneToCrossDescendant2.propagateDeepestGene();
		
		descendant1.propagateNewGenome();
		descendant2.propagateNewGenome();
	}

}
