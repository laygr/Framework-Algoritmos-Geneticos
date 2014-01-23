package geneticAlgorithm.chart;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;

public class EvaluationsNumberFormatter extends NumberFormat {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public double evaluationsPerGeneration;
	
	public EvaluationsNumberFormatter(double evaluationsPerGeneration){
		this.evaluationsPerGeneration = evaluationsPerGeneration;
	}

	@Override
	public StringBuffer format(double arg0, StringBuffer arg1,
			FieldPosition arg2) {
		StringBuffer sb = new StringBuffer();
		String result = (int)(arg0 * this.evaluationsPerGeneration )+ "";
		sb.append(result);
		
		return sb;
	}

	@Override
	public StringBuffer format(long number, StringBuffer toAppendTo,
			FieldPosition pos) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Number parse(String source, ParsePosition parsePosition) {
		// TODO Auto-generated method stub
		return null;
	}

}
