package geneticAlgorithm;

import geneticAlgorithm.evaluation.EvaluationInterface;
import geneticAlgorithm.grammar.adf.ADF;

public abstract class GADelegate {
	
	protected PopulationDelegate mainPopulationDelegate;
	protected ADF[] adfs;
	
	public GADelegate(){
		adfs = new ADF[0];
	}

	public abstract int getNumberOfGenerations();
	
	public void setMainPopulationDelegate(PopulationDelegate populationDelegate){
		this.mainPopulationDelegate = populationDelegate;
	}
	
	public PopulationDelegate getMainPopulationDelegate(){
		return this.mainPopulationDelegate;
	}
	
	public void setADFs(ADF[] adfs){
		this.adfs = adfs;
	}
	
	public ADF[] getADFs(){
		return this.adfs;
	}
	
	public abstract EvaluationInterface getEvaluationInterface();	
	
}
