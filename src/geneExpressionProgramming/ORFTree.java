package geneExpressionProgramming;

import geneticAlgorithm.grammar.Grammar;
import geneticAlgorithm.grammar.Input;
import geneticAlgorithm.grammar.Type;
import geneticAlgorithm.grammar.adf.ADF;

import java.util.ArrayList;

public class ORFTree {
	Node rootNode;
	int tailLength;
	
	public ORFTree(GEPGenome genome, Grammar grammar, Type type, ArrayList<Integer>genes, int tailLength){
		boolean onlyTerminals = genes.size() <= this.tailLength;
		this.rootNode = new Node(type, genes, onlyTerminals, genome);
		ArrayList<Node> emptySockets = this.currentEmptySockets();
		this.tailLength = tailLength;
		
		while(emptySockets.size() != 0){
			while(emptySockets.size() != 0){
				Node emptySocket = emptySockets.remove(0);
				Type typeOfNewNode = grammar.getTypeForClass(emptySocket.getNextEmptySocketClass());
				onlyTerminals = genes.size() <= this.tailLength;
				emptySocket.addChild(new Node(typeOfNewNode, genes, onlyTerminals, genome));
			}
			emptySockets = this.currentEmptySockets();
		}	
	}
	
	public ArrayList<Node> currentEmptySockets(){
		ArrayList<Node> emptySockets = new ArrayList<Node>();
		rootNode.currentEmptySockets(emptySockets);
		return emptySockets;
	}

	public Object evaluate(Input input) {
		return rootNode.evaluate(input);
	}
	
	public void lookForADFs(ArrayList<ADF>adfsAcum){
		this.rootNode.lookForADFs(adfsAcum);
	}
	
}
