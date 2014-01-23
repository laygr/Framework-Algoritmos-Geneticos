package geneticProgramming;

import geneticAlgorithm.grammar.Function;
import geneticAlgorithm.grammar.Grammar;
import geneticAlgorithm.grammar.Input;
import geneticAlgorithm.grammar.adf.ADF;
import geneticAlgorithm.grammar.adf.ADFDelegate.ADFType;

import java.util.ArrayList;

public class GPADFGene extends GPGene{

	protected GPADFGene(){
		super();
	}

	protected GPADFGene(GPGenome genome, GPGene upperGene, ArrayList<Function> functions, Grammar grammar, int level){
		super(genome, upperGene, functions, grammar, level);
	}

	protected GPADFGene(GPGenome genome, GPGene upperGene, Function function, Grammar grammar, int level, int maxDepth){
		super(genome, upperGene, function, grammar, level, maxDepth);
	}

	public ADF getADF() {
		return (ADF)this.function;
	}

	public Object evaluate(Input input){
		Object superResult = super.evaluate(input);
		if(superResult != null) return superResult;
		lastInput = input;
		Object[] parameters = new Object[function.getParameterTypes().length + 2];
		ADF adfFunction = (ADF) this.function;
		ArrayList<ADF> genomesADFs = this.genome.getADFs();
		for(int i = 0; i < genomesADFs.size(); i++){
			ADF aADF = genomesADFs.get(i);
			if(aADF == adfFunction){
				if(aADF.getADFType() != ADFType.COEVOLUTIVE){
					parameters[0] = this.genome.getIndexInPopulation();
				}else{
					parameters[0] = this.genome.getGenomeIndexForADFs()[i];
				}
			}
		}
		parameters[1] = !this.genome.isFromNewGeneration();
		int i = 2;
		int childsIterator = 0;
		for(Class<?> parameter : function.getParameterTypes()){
			if(Input.class.isAssignableFrom(parameter)){
				parameters[i] = input;
			}else{
				parameters[i] = childs.get(childsIterator).evaluate(input);
				childsIterator ++;
			}
			i++;
		}
		this.result = function.evaluate(parameters);

		return this.result;
	}

	public void lookForADFs(ArrayList<ADF> adfsAcum){
		super.lookForADFs(adfsAcum);
		if(!adfsAcum.contains(this.getADF())){
			adfsAcum.add(this.getADF());
		}
	}
}
