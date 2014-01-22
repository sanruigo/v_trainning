/**
 * This class provides information about plot or different charts
 *
 * @author Sandra Ruiz
 * @author 
 * @version 1.0, 1/12/2013
 * @since 1.4
 */


package project.chart;

import java.text.DecimalFormat;
import java.util.Arrays;


import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
//import com.androidplot.xy.XYStep;
import com.androidplot.xy.XYStepMode;


public class ChartPlot {

	protected XYPlot myXYPlot;
	/**
	 * Set of number for plot
	 * */
	private Number[] serie;
	private XYSeries series;
	private LineAndPointFormatter seriesFormat;
	//private XYStepMode mode;
	private int valorMode;
	
	/**
	 * Get all Numbers for Plot
	 * @return set Numbers for Plot
	 * */
	public Number[] getSerie(){
		return this.serie;
	}
	
	
	/**
	 * Store or Set all number for plot
	 * @param serie Set of number for the plot
	 * */
	public void setSerie(Number[]serie){
		this.serie=serie;
	}
	
	
	/**
	 * @return series XYSeries
	 * 
	 * */
	public XYSeries getSeries(){
		return this.series;
	}
	
	
	/**
	 * In this method exist two types of Array Format:
	 * 1.XY_VALS_INTERLEAVED => get values in pairs for abscissa and ordinate axes, (1,8), (5,2), (7,4)
	 * 2.Y_VALS_ONLY => get values only in ordinate axes
	 * @param serie Set of Number for Plot
	 * @param name_serie Name of set data 
	 * */
	public void setXYSeries(Number[] serieX, Number[] serieY, String name_serie){
		this.series=new SimpleXYSeries(Arrays.asList(serieX), Arrays.asList(serieY),name_serie);
		
	}

	/**
	 * Get the format of Series
	 *@return seriesFormat LineAndPointFormatter 
	 **/
	public LineAndPointFormatter getSeriesFormat(){
		return seriesFormat;
	}
	
	
	/**
	 * Set a Format, Color for line, point and fill
	 *@param linea color of line
	 *@param punto color of point
	 *@param relleno color of fill
	 **/
	public void setSeriesFormat(int linea,int punto,int relleno){
			seriesFormat= new LineAndPointFormatter(linea,punto,relleno, null);
	}
	/**
	 * Plot a XY graphic. 
	 * 
	 * @return true if it can plot
	 * false if not.
	 */
	public boolean Plot(String label, int valmode){
		this.valorMode=valmode;
		
		myXYPlot.setDomainStep(XYStepMode.SUBDIVIDE, valorMode);
		myXYPlot.setDomainLabel(label);
		myXYPlot.setDomainValueFormat(new DecimalFormat("##.##"));
		myXYPlot.setRangeLabel("km/h");
		
		if (this.myXYPlot!=null && this.series!=null && this.seriesFormat!=null){
			myXYPlot.addSeries(series, seriesFormat);
			return true;
		}else{
			return false;
		}
	}
	
	
	
}
