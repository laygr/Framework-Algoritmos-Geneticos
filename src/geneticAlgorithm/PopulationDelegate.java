package geneticAlgorithm;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import geneticAlgorithm.selection.Selection;
import geneticAlgorithm.grammar.adf.ADF;

public abstract class PopulationDelegate {
	ArrayList<ADF> adfs;
	Population population;
	
	public PopulationDelegate(){
		this.adfs = new ArrayList<ADF>();
	}
	
	public void initiate(){
		try {
			this.population = (Population) this.getPopulationType().getConstructor(PopulationDelegate.class).newInstance(this);
		
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
		this.population.generateInitialGeneration();
	}
	
	public abstract int getPopulationSize();
	public abstract boolean getMaximization();
	public abstract Selection getSelection();
	public abstract double getChanceOfMutation();
	public abstract double getChanceOfCrossing();
	
	public abstract Class<?> getPopulationType();
	
	public void addADF(ADF adf){
		this.adfs.add(adf);
	}
	
	public ADF[] getADFs(){
		return this.adfs.toArray(new ADF[0]);
	}
	
	public Population getPopultion(){
		return this.population;
	}
	
	public abstract Class<?>[] getClassesForComponents();
	public abstract Class<?> getReturnClassType();
	public abstract Class<?> getInputClass();

}