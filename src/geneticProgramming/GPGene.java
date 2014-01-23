package geneticProgramming;

import geneticAlgorithm.grammar.*;
import geneticAlgorithm.grammar.adf.ADF;

import java.util.ArrayList;

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class GPGene {
	protected Type type;
	protected ArrayList<GPGene> childs = new ArrayList<GPGene>();
	protected GPGene upperGene;
	protected Function function;
	protected Object result = null;
	protected Input lastInput = null;

	protected int levelOfDeepestGene;
	protected int numberOfSubgenes;
	protected int level;

	protected GPGenome genome;

	protected GPGene(){
		this.upperGene = null;
	}

	protected GPGene(GPGenome genome, GPGene upperGene, ArrayList<Function> functions, Grammar grammar, int level){
		this.upperGene = upperGene;
		this.level = level;
		this.function = functions.remove(0);
		this.type = grammar.getTypeForClass(function.getReturnType());
		this.genome = genome;
		boolean existAParameterOtherThanInput = false;
		for(Class aClass : this.function.getParameterTypes()){
			if(Input.class.isAssignableFrom(aClass)){
				continue;
			}
			existAParameterOtherThanInput = true;
			childs.add(GPGeneFactory.createGPGene(this.genome, this, functions, grammar, level + 1));
		}

		concludeInitialization(existAParameterOtherThanInput);
	}



	protected GPGene(GPGenome genome, GPGene upperGene, Function function, Grammar grammar, int level, int maxDepth){
		this.upperGene = upperGene;
		this.level = level;
		this.type = grammar.getTypeForClass(function.getReturnType());
		this.genome = genome;

		this.function = function;

		Class[] parameters = function.getParameterTypes();
		boolean existAParameterOtherThanInput = false;
		for(Class aClass : parameters){
			if(Input.class.isAssignableFrom(aClass)){
				continue;
			}
			existAParameterOtherThanInput = true;
			Type childType = grammar.getTypeForClass(aClass);
			childs.add(GPGeneFactory.createGPGene(this.genome, this, grammar, childType, level + 1, maxDepth - 1));
		}

		concludeInitialization(existAParameterOtherThanInput);
	}

	protected void concludeInitialization(boolean existAParameterOtherThanInput){
		if(!existAParameterOtherThanInput){
			levelOfDeepestGene = 0;
			numberOfSubgenes = 0;
		}else{
			this.calculateLevelOfDeepestGene();
			this.numberOfSubgenes = 0;
			for(GPGene child : childs){
				numberOfSubgenes += child.numberOfSubgenes + 1;				
			}
		}
	}

	public Object evaluate(Input input) {
		if(this.result != null && lastInput != null && input.equals(lastInput)){
			return this.result;
		}else{
			return null;
		}
	}

	public String toString(){
		return toString(null);
	}

	public String toString(Grammar grammar){
		String description = "";
		description += function.getName();
		if(grammar != null) description += " = " + result;
		description += "\n";

		String parameterString;
		int childIterator = 0;
		int parameterIterator = 0;
		for(Class parameter : function.getParameterTypes()){
			if(Input.class.isAssignableFrom(parameter)){
				parameterString = parameter.getSimpleName();
			}else{
				parameterString = childs.get(childIterator).toString(grammar);
				childIterator ++;
			}
			parameterIterator ++;

			String[] lines = parameterString.split("\n");
			for(int i = 0; i < lines.length; i++){
				lines[i] = "\t" + lines[i];
			}
			if(parameterIterator != function.getParameterTypes().length){
				parameterString = "";
				for(int i = 0; i < lines.length; i++){
					String head = lines[i].substring(0, 2);
					if(head.compareTo("\t\t") == 0){
						lines[i] = "\t|\t" + lines[i].substring(2);
					}
					parameterString += lines[i] + "\n";
				}
			}else{
				parameterString = "";
				for(int i = 0; i < lines.length; i++){
					parameterString += lines[i] + "\n";
				}
			}
			description += parameterString;
		}
		return description;
	}

	public GPGene getNode(int nodeIndex){
		if(nodeIndex == 0 ) return this;
		int acum = 1;
		for(GPGene gene : childs){
			if(acum + gene.numberOfSubgenes >= nodeIndex){
				return gene.getNode(nodeIndex - acum);
			}
			acum += gene.numberOfSubgenes + 1;
		}
		return null;
	}

	public ArrayList<GPGene> getGenesOfTypeClass(Class typeClass){
		ArrayList<GPGene> genes = new ArrayList<GPGene>();
		if(typeClass.isAssignableFrom(getTypeClass())) genes.add(this);
		for(GPGene child : childs){
			genes.addAll(child.getGenesOfTypeClass(typeClass));
		}
		return childs;
	}

	public Class getTypeClass(){
		return this.type.typeClass;
	}

	public int getMargin(){
		return this.genome.getMaxLevel() - (this.levelOfDeepestGene + this.level);
	}

	public void addCandidateGenesForCrossing(ArrayList<GPGene> candidates, Class typeClass, int otherGeneLevel, int otherGeneLevelOfDeepestGene){
		if(		typeClass.isAssignableFrom(getTypeClass()) && 
				otherGeneLevel + this.levelOfDeepestGene <= this.genome.getMaxLevel() && //Asegurarse que este gene cabe en el otro genoma
				otherGeneLevelOfDeepestGene + this.level <= this.genome.getMaxLevel() //Asegurarse que el otro gene cabria en este genoma
		){
			candidates.add(this);
		}
		for(GPGene child : childs){
			child.addCandidateGenesForCrossing(candidates, typeClass, otherGeneLevel, otherGeneLevelOfDeepestGene);
		}
	}

	public GPGene clone(){
		GPGene newGene;
		try {
			newGene = this.getClass().newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}

		newGene.type = this.type;
		newGene.function = this.function;
		newGene.result = this.result;
		newGene.levelOfDeepestGene = this.levelOfDeepestGene;
		newGene.numberOfSubgenes = this.numberOfSubgenes;
		newGene.level = this.level;
		newGene.genome = this.genome;

		for(GPGene gene : childs){
			GPGene clonedSubgene = gene.clone();
			clonedSubgene.upperGene = newGene;
			newGene.childs.add(clonedSubgene);
		}
		return newGene;
	}

	public int getLevel(){
		return this.level;
	}

	public void setLevel(int newLevel){
		this.level = newLevel;
	}

	public void propagateNewLevel(){
		for(GPGene gene : childs){
			gene.level = this.level + 1;
			gene.propagateNewLevel();
		}
	}
	public void propagateDeepestGene(){
		if(this.upperGene == null) return;

		if(this.levelOfDeepestGene + 1 > this.upperGene.levelOfDeepestGene){
			this.upperGene.levelOfDeepestGene = this.levelOfDeepestGene +1;
		}else{
			this.upperGene.calculateLevelOfDeepestGene();
		}
		this.upperGene.propagateDeepestGene();
	}

	private void calculateLevelOfDeepestGene(){
		this.levelOfDeepestGene = 0;
		for(GPGene child : childs){
			if(child.levelOfDeepestGene > this.levelOfDeepestGene){
				this.levelOfDeepestGene = child.levelOfDeepestGene;
			}
		}
		this.levelOfDeepestGene += 1;
	}

	public void setAndPropagateNullResult(){
		if(this.upperGene == null) return;
		this.upperGene.result = null;
		this.upperGene.setAndPropagateNullResult();
	}

	/**
	 * Changes the number of subgenes in superior genes
	 * @param change The change that upperGene will suffer. Calculated as newGene.numberOfSubgenes - oldGene.numberOfSubgenes
	 */
	public void propagateChangeInNumberOfSubgenes(int change){
		if(this.upperGene == null) return;
		this.upperGene.numberOfSubgenes += change;
		this.upperGene.propagateChangeInNumberOfSubgenes(change);
	}

	public int getIndexInParent(){
		if(this.upperGene == null) return -1;
		return this.upperGene.childs.indexOf(this);
	}

	public Type getType(){
		return this.type;
	}

	public boolean functionIsADF(){
		return this.function.getClass() == ADF.class;
	}

	public void lookForADFs(ArrayList<ADF> adfsAcum){

		for(GPGene gene : childs){
			gene.lookForADFs(adfsAcum);
		}

	}

	public void propagateNewGenome(GPGenome descendant) {
		this.genome = descendant;
		for(GPGene child : this.childs){
			child.propagateNewGenome(descendant);
		}
		
	}

}
