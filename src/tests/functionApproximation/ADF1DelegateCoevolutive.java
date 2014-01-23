package tests.functionApproximation;

import geneticAlgorithm.grammar.adf.ADFDelegate;

public class ADF1DelegateCoevolutive extends ADFDelegate {

	@Override
	public ADFType getADFType() {
		return ADFType.COEVOLUTIVE;
	}

}
