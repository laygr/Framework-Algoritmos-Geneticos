package tests.functionApproximation;

import geneticAlgorithm.grammar.adf.ADFDelegate;

public class ADF1DelegateSemicoevolutive extends ADFDelegate {

	@Override
	public ADFType getADFType() {
		return ADFType.SEMICOEVOLUTIVE;
	}

}
