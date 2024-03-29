package geneExpressionProgramming;

import geneticAlgorithm.grammar.Function;
import geneticAlgorithm.grammar.Input;
import geneticAlgorithm.grammar.SimpleFunction;
import geneticAlgorithm.grammar.Type;
import geneticAlgorithm.grammar.adf.ADF;
import geneticAlgorithm.grammar.adf.ADFDelegate.ADFType;

import java.util.ArrayList;

public class Node {

	ArrayList<Node> children;
	Function function;
	int numberOfChilds;
	GEPGenome genome;

	public Node(Type type, ArrayList<Integer> genes, boolean isTerminal, GEPGenome genome){
		this.function = type.getFunctionAtIndexWithModulus(genes.remove(0), isTerminal);
		this.numberOfChilds = function.getArity() - function.getNumberOfParametersThatAreInputType();
		this.genome = genome;
		this.children = new ArrayList<Node>();
	}

	public void currentEmptySockets(ArrayList<Node> emptySockets) {
		for(int i = 0; i < this.numberOfChilds - children.size(); i++){
			emptySockets.add(this);
		}
		for(Node child : children){
			child.currentEmptySockets(emptySockets);
		}
	}

	public Class<?> getNextEmptySocketClass(){
		return function.getParameterTypes()[numberOfChilds - 1 - children.size()];
	}

	public void addChild(Node child){
		this.children.add(child);
	}

	public Object evaluateAsSimpleGene(Input input){
		Object[] parameters = new Object[function.getParameterTypes().length];
		int i = 0;
		int childsIterator = 0;
		for(Class<?> parameter : function.getParameterTypes()){
			if(Input.class.isAssignableFrom(parameter)){
				parameters[i] = input;
			}else{
				parameters[i] = children.get(childsIterator).evaluate(input);
				childsIterator ++;
			}
			i++;
		}

		return function.evaluate(parameters);
	}

	public Object evaluateAsADFGene(Input input){
		Object[] parameters = new Object[function.getParameterTypes().length + 2];
		ArrayList<ADF> genomesADFs = this.genome.getADFs();

		for(int i = 0; i < genomesADFs.size(); i++){
			ADF aADF = genomesADFs.get(i);
			if(aADF == this.function){
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
				parameters[i] = children.get(childsIterator).evaluate(input);
				childsIterator ++;
			}
			i++;
		}
		return function.evaluate(parameters);
	}

	public Object evaluate(Input input) {
		if(SimpleFunction.class.isAssignableFrom(this.function.getClass())){
			return this.evaluateAsSimpleGene(input);
		}else {
			return this.evaluateAsADFGene(input);
		}
	}

	public void lookForADFs(ArrayList<ADF> adfsAcum){
		if(ADF.class.isAssignableFrom(this.function.getClass())){
			if(!adfsAcum.contains(this.function)){
				adfsAcum.add((ADF) this.function);
			}
		}
		for(Node child : children){
			child.lookForADFs(adfsAcum);
		}
	}

}
