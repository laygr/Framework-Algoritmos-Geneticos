package geneticAlgorithm.grammar.adf;

import geneticAlgorithm.Genome;
import geneticAlgorithm.Population;
import geneticAlgorithm.grammar.Function;
import geneticAlgorithm.grammar.adf.ADFDelegate.ADFType;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
public class ADF extends Function{
	protected Population population;
	protected ADFDelegate adfDelegate;
	protected int index;
	protected Class<?> adfInputClass;

	public ADF(int index, ADFDelegate adfDelegate) {
		this.index = index;
		this.adfDelegate = adfDelegate;
		this.parameterTypes = this.adfDelegate.getParameterTypes();
		this.population = adfDelegate.getPopulationDelegate().getPopultion();
		this.adfInputClass = adfDelegate.getADFInputClass();
		this.returnType = this.population.getReturnType();
		this.name = "ADF:" + index;
		checkIfIsTerminal();
	}

	//First paramater indicates index, second indicates if is from old(false) or new(true) generation.
	@Override
	public Object evaluate(Object... parameters) {
		int genomeIndex = (Integer) parameters[0];
		boolean onlyNewGeneration = (Boolean) parameters[1] && this.getADFType() == ADFType.COEVOLUTIVE;

		Object[] parametersForADF = new Object[this.getParameterTypes().length];
		for(int i = 2; i < parameters.length; i++){
			parametersForADF[i - 2] = parameters[i];
		}
		ADFInput adfInput = null;
		try {
			Constructor<?> constructor = adfInputClass.getConstructor(Integer.class);
			adfInput = (ADFInput) constructor.newInstance(parametersForADF.length);
			for(int i = 0; i < parametersForADF.length; i++){
				adfInput.addParameter(parametersForADF[i]);
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return population.getGenomeAtIndex(genomeIndex, onlyNewGeneration).evaluate(adfInput);
	}

	public void evolve(){
		if(this.getADFType() != ADFType.SIMPLE){
			Genome[] newGeneration = this.population.generateNextGeneration();
			this.population.replaceOldGenomes(newGeneration);
		}
		this.population.increaseGeneration();
	}

	public void evolveAPair(Genome adf1, Genome adf2){
		adf1.setIsFromNewGeneration(false);
		adf2.setIsFromNewGeneration(false);
		Genome[] parents = {adf1, adf2};

		Genome[] descendants = this.population.replicateGenomes(parents);
		this.population.recombinateGenomes(descendants);
		this.population.mutation(descendants);
		for(Genome genome : descendants){
			genome.setIsFromNewGeneration(true);
			genome.lookForADFs();
			genome.computeIterationsToDo();
		}
		this.population.replaceOldGenomes(descendants);
	}

	@Override
	public Class<?>[] getParameterTypes() {
		return this.adfDelegate.getParameterTypes();
	}

	public int getIndex(){
		return this.index;
	}

	public int getPopulationSize(){
		return this.population.getPopulationSize();
	}

	public int getNewGenerationSize(){
		return this.population.getReplacementSize();
	}

	public int getOldGenerationSize(){
		return this.population.getPopulationSize() - this.population.getReplacementSize();
	}

	public ADFType getADFType(){
		return this.adfDelegate.getADFType();
	}

	public void sendAptitudeToGenome(double aptitude, int genomeIndex){
		Genome genome = this.population.getCompletePopulation()[genomeIndex];
		genome.sendAptitude(aptitude);
	}

	public void resetAptitudesOfNewGeneration(){
		this.population.resetAptitudesOfNewGeneration();
	}

	public void prepareInfoAboutADFs(){
		this.population.prepareInfoAboutADFs();
	}

	public Population getPopulation(){
		return this.population;
	}
}