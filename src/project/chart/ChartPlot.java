package project.chart;

import java.util.Arrays;

import android.graphics.Color;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;


public class ChartPlot {

	private XYPlot mySimpleXYPlot;
	private Number[] serie;
	private XYSeries series;
	private LineAndPointFormatter seriesFormat;
	public Number[] getSerie(){
		return this.serie;
	}
	
	public void setSerie1(Number[]serie){
		this.serie=serie;
	}
	
	
	// Añadimos Línea Número UNO:
    // Exiten dos formatos del array
    // 1.XY_VALS_INTERLEAVED => obtiene los valoes en pares es decir (1,8), (5,2) ,(7,4)
    // 2.Y_VALS_ONLY => obtiene todo el array pero para valores en la ordenadas
	public XYSeries getSeries(){
		return this.series;
	}
	
	public void setXYSeries(Number[] serie,String name_serie){
		this.series=new SimpleXYSeries(Arrays.asList(serie),  SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,name_serie);
		
	}
	
	public LineAndPointFormatter getSeriesFormat(){
		return seriesFormat;
	}
	
	public void setSeriesFormat(int linea,int punto,int relleno){
			seriesFormat= new LineAndPointFormatter(linea,punto,relleno, null);
	}
	
	public void chart(){

 
        // Creamos dos arrays de prueba. En el caso real debemos reemplazar
        // estos datos por los que realmente queremos mostrar
        //Number[] series1Numbers = {1, 8, 5, 2, 7, 4};
        //Number[] series2Numbers = {4, 6, 3, 8, 2, 10};
 
        
        //XYSeries series1 = 
        
        		// Array de datos
                 // Sólo valores verticales
                 // Nombre de la primera serie
 
        // Repetimos para la segunda serie
        //XYSeries series2 = new SimpleXYSeries(Arrays.asList(series2Numbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series2");
 
        // Modificamos los colores de la primera serie
           
        // Color de la línea                   
        // Color del punto
         // Relleno
        
        
        
       
        
 
        // Repetimos para la segunda serie
      //  mySimpleXYPlot.addSeries(series2, new LineAndPointFormatter(Color.rgb(0, 0, 200), Color.rgb(0, 0, 100), Color.rgb(150, 150, 190), null));
 

	
	}
	
}
