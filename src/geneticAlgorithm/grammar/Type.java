package geneticAlgorithm.grammar;

import java.util.ArrayList;

@SuppressWarnings({"rawtypes", "unchecked"})
public class Type {
	public Class typeClass;
	private ArrayList<Function> functions;
	private ArrayList<Function> terminals;
	private int minCalls = Integer.MAX_VALUE - 1;

	public Type(Class type){
		this.typeClass = type;
		this.functions = new ArrayList<Function>();
		this.terminals = new ArrayList<Function>();
	}

	public ArrayList<Function> getFunctions(){
		return functions;
	}

	protected void addFunction(Function function){
		this.functions.add(function);
		if(function.isTerminal){
			this.terminals.add(function);
		}
	}

	//Sort functions from smallest to biggest minCalls
	protected void sortFunctions(){
		ArrayList<Function> sortedFunctions = new ArrayList<Function>();
		int first;
		int last;
		int middle = 0;
		Function leftFunction;
		Function rightFunction;
		Function functionToAdd;
		Function pivot;
		sortedFunctions.add(this.functions.get(0));
		for(int i = 1; i < this.functions.size(); i++){
			first = 0;
			last = sortedFunctions.size() - 1;
			functionToAdd = this.functions.get(i);
			while(true){
				middle = (last - first) / 2 + first;
				pivot = sortedFunctions.get(middle);
				leftFunction = ( middle - 1 ) < first ? null : sortedFunctions.get(middle - 1);
				rightFunction = (middle + 1) > sortedFunctions.size() - 1 ? null : sortedFunctions.get(middle + 1);
				if((leftFunction == null || leftFunction.getMinCalls() <= functionToAdd.getMinCalls()) && (rightFunction == null || rightFunction.getMinCalls() >= functionToAdd.getMinCalls())){
					middle = pivot.getMinCalls() > functionToAdd.getMinCalls() ? middle - 1 : middle;
					break;
				}else if(pivot.getMinCalls() > functionToAdd.getMinCalls()){
					last = middle - 1;
				}else {
					first = middle + 1;
				}
			}
			sortedFunctions.add(middle + 1, functionToAdd);
		}
		this.functions = sortedFunctions;
	}

	public boolean isCompatibleWithType(Type type){
		return this.typeClass.isAssignableFrom(type.typeClass); 
	}

	public boolean isCompatibleWithClass(Class aClass){
		return this.typeClass.isAssignableFrom(aClass); 
	}

	public boolean correspondondsToType(Type type){
		return this.typeClass == type.typeClass;
	}

	public boolean correspondondsToClass(Class aClass){
		return this.typeClass == aClass;
	}
	
	public Function getFunctionAtIndex(int index, boolean onlyTerminals){
		if(onlyTerminals){
			return this.terminals.get(index);
		}
		return this.functions.get(index);
	}
	
	public Function getFunctionAtIndexWithModulus(int index, boolean onlyTerminals){
		int modulus = index % (onlyTerminals? this.terminals.size() : this.functions.size());
		
		return getFunctionAtIndex(modulus, onlyTerminals);
	}
	
	public Function getFunctionWithMaxDepth(int maxDepth){
		int lastSelectableFunction = 0;
		
		for(Function function : functions){
			if(function.getMinCalls() <= maxDepth){
				lastSelectableFunction ++;
			}
		}
		
		int functionIndex = (int) (Math.random() * lastSelectableFunction);
		return this.functions.get(functionIndex);
	}
	
	public int getMinCalls(){
		return this.minCalls;
	}
	
	public void setMinCalls(int minCalls){
		this.minCalls = minCalls;
	}

	public String toString(){
		String string = "";
		string += this.typeClass.getSimpleName() + "\t    ->";
		for(Function function : functions){
			string += "\n\t\t" + function.getName();
			for(Class type : function.getParameterTypes()){
				string += " " + type.getSimpleName();
			}
		}
		return string;
	}

}
