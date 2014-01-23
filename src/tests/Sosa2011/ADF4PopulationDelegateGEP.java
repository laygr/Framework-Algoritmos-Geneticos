package tests.Sosa2011;

import geneExpressionProgramming.GEPPopulationDelegate;
import geneExpressionProgramming.LinkerFunction;
import geneticAlgorithm.selection.Selection;
import geneticAlgorithm.selection.TournamentSelection;

public class ADF4PopulationDelegateGEP extends
		GEPPopulationDelegate {

	
	@Override
	public int getPopulationSize() {
		return 500;
	}

	@Override
	public boolean getMaximization() {
		return false;
	}

	@Override
	public Selection getSelection() {
		return new TournamentSelection(499, 2);
	}

	@Override
	public double getChanceOfMutation() {
		return .05;
	}

	@Override
	public double getChanceOfCrossing() {
		return .95;
	}

	@Override
	public Class<?>[] getClassesForComponents() {
		Class<?>[] components = {Components.class};
		return components;
	}

	@Override
	public Class<?> getReturnClassType() {
		return Number.class;
	}

	@Override
	public Class<?> getInputClass() {
		return ADF1Input.class;
	}

    @Override
    public int getNumberOfORFs() {
        return 1;
    }

    @Override
    public int getHeadLength() {
        return 10;
    }

    @Override
    public LinkerFunction getLinkerFunction() {
        return new Sosa2011LinkerFunction();
    }

}
