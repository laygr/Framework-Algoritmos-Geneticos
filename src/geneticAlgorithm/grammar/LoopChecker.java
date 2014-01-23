package geneticAlgorithm.grammar;

import java.util.ArrayList;

@SuppressWarnings("rawtypes")
public class LoopChecker {
	private ArrayList<Type> noLoopedTypes = new ArrayList<Type>();
	ArrayList<Type> usedTypes = new ArrayList<Type>();
	private Grammar grammar;
	
	public LoopChecker(Grammar grammar) {
		this.grammar = grammar;
	}

	public void checkForLoops() throws Exception{
		for(Type type : grammar.getTypes()){
			typeLoops(type);
		}
		
		String loopedTypesString = "";
		
		for(Type type : grammar.getTypes()){
			if(!noLoopedTypes.contains(type)){
				loopedTypesString += "\n" + type.typeClass.getName();				
			}
		}
		if(loopedTypesString != ""){
			Exception exception = new Exception("This type(s) would loop:" + loopedTypesString);
			throw exception;
		}
		noLoopedTypes.clear();
	}
	
	private boolean typeLoops(Type type){
		if(noLoopedTypes.contains(type)){
			return false;
		}
		
		usedTypes.add(type);
		
		boolean functionSucceded = false;
		
		for(int i = 0; i < type.getFunctions().size() && !functionSucceded; i++){
			Function function = type.getFunctions().get(i);
			
			boolean parametersSucceded = true;
			//Checar que ninguno de sus parametros ya esta intentandose probar
			for(int j = 0; j < function.getArity(); j++){
				Class parameterClass = function.getParameterTypes()[j];
				if(Input.class.isAssignableFrom(parameterClass)){
					continue;
				}
				Type parameterType = grammar.getTypeForClass(parameterClass);
				if(usedTypes.contains(parameterType)){
					parametersSucceded = false;
					break;
				}
			}
			if(!parametersSucceded){
				continue;
			}
			
			//Probar todos los tipos de sus parametros
			for(int j = 0; j < function.getParameterTypes().length; j++){
				Class parameterClass = function.getParameterTypes()[j];
				if(Input.class.isAssignableFrom(parameterClass)){
					continue;
				}
				Type parameterType = grammar.getTypeForClass(parameterClass);
				if(typeLoops(parameterType)){
					parametersSucceded = false;
					break;
				}
			}
			functionSucceded = parametersSucceded;
		}
		
		usedTypes.remove(type);
		
		if(functionSucceded){
			noLoopedTypes.add(type);
			return false;			
		}else{
			return true;
		}
	}
}
