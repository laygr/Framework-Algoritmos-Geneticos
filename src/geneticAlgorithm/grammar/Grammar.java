package geneticAlgorithm.grammar;

import geneticAlgorithm.grammar.adf.ADF;

import java.util.*;
import java.lang.reflect.Method;

@SuppressWarnings({"rawtypes"})
public class Grammar {
	protected ArrayList<Type> types;
	protected ArrayList<SimpleFunction> simpleFunctions;
	protected ArrayList<Function> allFunctions;
	
	protected ADF[] adfs;
	
	protected ArrayList<Function> nonTerminalFunctions;
	protected ArrayList<Function> terminalFunctions;
	
	protected Class<?> inputClass;

	public int mostNeededParametersInAFunction;

	public Grammar(ADF[] adfs, Class<?> inputClass, Class... classes) throws Exception{
		this.types = new ArrayList<Type>();
		this.simpleFunctions = new ArrayList<SimpleFunction>();
		this.nonTerminalFunctions = new ArrayList<Function>();
		this.terminalFunctions = new ArrayList<Function>();
		this.allFunctions = new ArrayList<Function>();
		this.inputClass = inputClass;
		
		this.addClasses(classes);
		
		this.adfs = adfs;
		for(int i = 0; i < adfs.length; i++){
			this.addADF(adfs[i]);
		}

		checkForEmptyTypes();
		LoopChecker loopChecker = new LoopChecker(this);
		loopChecker.checkForLoops();
		this.findFunctionsMinSubcalls();
		this.sortFunctions();
	}
	
	private void checkForEmptyTypes() throws Exception{
		String emptyTypes = "";
		for(Type type : types){
			if(type.getFunctions().size() < 1){
				emptyTypes += type.typeClass.getName() + "\n";
			}
		}

		if(emptyTypes != ""){
			Exception exception = new Exception("This type(s) are empty:\n" + emptyTypes);
			throw exception;
		}
	}

	private void addClasses(Class<?>... classes){
		for(Class aClass: classes){
			this.addTypes(aClass);
		}
		this.mostNeededParametersInAFunction = 0;
		for(Class aClass: classes){
			this.addFunctionsFromClass(aClass);
		}
	}

	private void findFunctionsMinSubcalls(){
		boolean changeOcurred = false;
		for(int i = 0; i < this.allFunctions.size(); i++){
			Function function = this.allFunctions.get(i);
			if(function.getMinCalls() == 1){
				continue;
			}
			int minSubcalls = 0;
			for(Class aClass : function.getParameterTypes()){
				if(Input.class.isAssignableFrom(aClass)){
					continue;
				}
				Type type = this.getTypeForClass(aClass);
				if(type.getMinCalls() > minSubcalls){
					minSubcalls = type.getMinCalls();
				}
			}
			int newMinCalls = minSubcalls + 1;

			if(newMinCalls < function.getMinCalls()){
				function.setMinCalls(newMinCalls);
				changeOcurred = true;
			}
		}
		if(changeOcurred){
			findTypesMinSubcalls();
		}
	}

	private void findTypesMinSubcalls(){
		boolean changeOcurred = false;
		for(int i = 0; i < types.size(); i++){
			Type type = types.get(i);
			for(Function function : type.getFunctions()){
				if(function.getMinCalls() < type.getMinCalls()){
					type.setMinCalls(function.getMinCalls());
					changeOcurred = true;
				}
			}
		}
		if(changeOcurred){
			findFunctionsMinSubcalls();
		}
	}

	private void sortFunctions(){
		for(Type type : this.types){
			type.sortFunctions();
		}
	}

	public ArrayList<Type> getTypes(){
		return types;
	}

	public ArrayList<Function> getTerminalFunctions(){
		return this.terminalFunctions;
	}

	public Function getFunctionNamed(String name){
		for(Function function : simpleFunctions){
			if(function.getName().equals(name)){
				return function;
			}
		}

		return null;
	}

	public Type getTypeForClass(Class aClass){
		for(Type type : types){
			if(type.correspondondsToClass(aClass)){
				return type;
			}
		}
		return null;
	}



	private void addTypes(Class aClass){
		for(Method method : aClass.getDeclaredMethods()){
			this.addClass(method.getReturnType());
			for(Class parameter : method.getParameterTypes()){
				if(!Input.class.isAssignableFrom(parameter)){
					addClass(parameter);
				}
			}
		}
	}

	private void addClass(Class aClass){
		boolean alreadyAdded = false;
		for(Type type : types){
			if(type.correspondondsToClass(aClass)){
				alreadyAdded = true;
				break;
			}
		}
		if(!alreadyAdded){
			Type newType = new Type(aClass);
			this.types.add(newType);
		}
	}

	private void addFunctionsFromClass(Class aClass){
		for(Method method : aClass.getDeclaredMethods()){
			//should skip method because it uses other's input type?
			boolean shouldSkip = false;
			for(Class<?> parameterClass : method.getParameterTypes()){
				if(Input.class.isAssignableFrom(parameterClass) && parameterClass != this.inputClass){
					shouldSkip = true;
					break;
				}
			}
			if(shouldSkip) continue;
			SimpleFunction newFunction = new SimpleFunction(method);
			this.addFunction(newFunction);
			this.allFunctions.add(newFunction);
			this.simpleFunctions.add(newFunction);
		}
	}

	private void addFunction(Function function){
		for(Type type : types){
			if(type.isCompatibleWithClass(function.getReturnType())){
				type.addFunction(function);
			}
		}
		if(function.getArity() > this.mostNeededParametersInAFunction){
			this.mostNeededParametersInAFunction = function.getArity();
		}
		if(function.isTerminal()){
			this.terminalFunctions.add(function);
		}else{
			this.nonTerminalFunctions.add(function);
		}
	}
	
	private void addADF(ADF adf){
		this.allFunctions.add(adf);
		for(Type type : types){
			if(type.isCompatibleWithClass(adf.getReturnType())){
				type.addFunction(adf);
			}
		}
	}

	public String toString(){
		String string = "";
		for(Type type : types){
			string += type.toString();
			string += "\n-------\n";
		}

		return string;
	}
	
	public ADF[] getADFs(){
		return this.adfs;
	}
	
//	public Function getFunctionForIndexAndType(){
//		
//	}
}
