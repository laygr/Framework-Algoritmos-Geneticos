package geneticAlgorithm;

import java.util.ArrayList;

public class Utility {
	
	/**
	 * Sorts the genomes in descendant order with boolean search.
	 */
	public static void sortGenomesDescendantOrder(Genome[] population){
		ArrayList<Genome> sortedGenomes = new ArrayList<Genome>(population.length);

		int first;
		int last;
		int middle = 0;
		Genome leftGenome;
		Genome rightGenome;
		Genome genomeToAdd;
		Genome pivot;
		sortedGenomes.add(population[0]);
		for(int i = 1; i < population.length; i++){
			first = 0;
			last = sortedGenomes.size() - 1;
			genomeToAdd = population[i];
			while(true){
				middle = (last - first) / 2 + first;
				pivot = sortedGenomes.get(middle);
				leftGenome = ( middle - 1 ) < first ? null : sortedGenomes.get(middle - 1);
				rightGenome = (middle + 1) > sortedGenomes.size() - 1 ? null : sortedGenomes.get(middle + 1);
				if((leftGenome == null || leftGenome.aptitude >= genomeToAdd.aptitude) && (rightGenome == null || rightGenome.aptitude <= genomeToAdd.aptitude)){
					middle = pivot.aptitude < genomeToAdd.aptitude ? middle - 1 : middle;
					break;
				}else if(pivot.aptitude < genomeToAdd.aptitude){
					last = middle - 1;
				}else {
					first = middle + 1;
				}
			}
			sortedGenomes.add(middle + 1, genomeToAdd);
		}
		population = sortedGenomes.toArray(population);
	}
	
}
