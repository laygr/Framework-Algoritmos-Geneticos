package geneticAlgorithm.chart;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYIntervalSeries;
import org.jfree.data.xy.XYIntervalSeriesCollection;

import org.jfree.ui.RefineryUtilities;
import org.jfree.chart.renderer.xy.DeviationRenderer;

public class ARunChart extends JFrame {

		private static final long serialVersionUID = 1L;

		public ARunChart(String applicationTittle, String chartTitle, double[] averages, double[] deviations, double[] bests, int evaluationsPerGeneration){
			super(applicationTittle);
			
			XYDataset dataset = createDataset(averages, deviations, bests);
			JFreeChart chart = createChart(dataset, averages.length, evaluationsPerGeneration);
			ChartPanel chartPanel = new ChartPanel(chart);
			setContentPane(chartPanel);
		}
		
		private JFreeChart createChart(XYDataset dataset, int generations, int evaluationsPerGeneration){
			
			JFreeChart chart = ChartFactory.createXYLineChart(
					"Report", "Evaluations", "Aptitude", dataset, PlotOrientation.VERTICAL, true, true, false);
			
			XYPlot plot = (XYPlot) chart.getPlot();
			NumberAxis nA = (NumberAxis) plot.getDomainAxis();
			nA.setNumberFormatOverride(new EvaluationsNumberFormatter(evaluationsPerGeneration));
			plot.getRangeAxis().setTickMarksVisible(false);
			DeviationRenderer renderer = new DeviationRenderer();
		
			renderer.setSeriesShapesVisible(0, false);
			renderer.setSeriesShapesVisible(1, false);
			
			
			plot.setRenderer(renderer);
			
			return chart;
		}

		public XYDataset createDataset(double[] averages, double[] deviation, double[] bests){
			XYIntervalSeries averageSerie = new XYIntervalSeries("Average");
			for(int i = 0; i < averages.length; i++){
				averageSerie.add(i, i, i,averages[i], averages[i] - deviation[i], averages[i] + deviation[i]);
			}
			XYIntervalSeries bestsSerie = new XYIntervalSeries("Best");
			for(int i = 0; i < bests.length; i++){
				bestsSerie.add(i, i, i, bests[i], bests[i], bests[i]);
			}
			
			XYIntervalSeriesCollection dataset = new XYIntervalSeriesCollection();
			
			dataset.addSeries(bestsSerie);
			dataset.addSeries(averageSerie);
			
			return dataset;
		}
		
		public static void graph(double[][] aptitudes, int evaluationsPerGeneration, boolean maximization){
			int generations = aptitudes.length;
			double[] averages = new double[generations];
			double[] deviation = new double[generations];

			double[] bests = new double[generations];

			for(int i = 0; i < generations; i++){
				averages[i] = 0;
				bests[i] = maximization? 0 : Double.MAX_VALUE;
				for(int j = 0; j < aptitudes[0].length; j++){
					averages[i] += aptitudes[i][j];
					if(bests[i] < aptitudes[i][j] && maximization ||
							bests[i] > aptitudes[i][j] && !maximization){
						bests[i] = aptitudes[i][j];
					}

				}
				
				averages[i] /= aptitudes[0].length;

				deviation[i] = 0;
				for(int j = 0; j < aptitudes[i].length; j++){
					deviation[i] += Math.abs(averages[i] - aptitudes[i][j]);
				}
				deviation[i] /= aptitudes[i].length;
				
			}
		ARunChart chart = new ARunChart("Genetic Programming", "Report", averages, deviation, bests, evaluationsPerGeneration);
		
		chart.pack();
		
		RefineryUtilities.centerFrameOnScreen(chart);
        chart.setVisible(true);
        
        
	}
}