package ecgBase;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import jline.internal.Log;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;

import com.opensymphony.module.sitemesh.util.Container;
import com.xeiam.xchart.BitmapEncoder;
import com.xeiam.xchart.Chart;
import com.xeiam.xchart.QuickChart;
import com.xeiam.xchart.BitmapEncoder.BitmapFormat;
import com.xeiam.xchart.Series;
import com.xeiam.xchart.SwingWrapper;
import com.xeiam.xchart.VectorGraphicsEncoder;
import com.xeiam.xchart.VectorGraphicsEncoder.VectorGraphicsFormat;
import com.xeiam.xchart.XChartPanel;

public class AppLog {
	
	private static AppLog logServiceInstance = null;
	private String lineSep = System.lineSeparator();
	private File outFile = null;
	private FileWriter fileWriter = null;
	private String titleString = null;

	private AppLog() {
	}

	public static AppLog getLogService() {

		if ( logServiceInstance == null ) {
			logServiceInstance = new AppLog();
		}
		
		return logServiceInstance;
	}

	public void initialize() {

		String logFileDir = "./logs";
		File dir = new File(logFileDir);
		if ( !dir.exists() ) {
			dir.mkdir();
		}
		String logFileName = "applog.log";
		outFile = new File(logFileDir,logFileName);
		
		try {
			fileWriter = new FileWriter(outFile);
		} catch (IOException e) {
			Log.error("Cannot create FileWriter for logFileDir=" + logFileDir + " logFileName=" + logFileName);
		}

		String appName = "unknown";
		titleString = "Application Log File for " + appName + " Created on " + new Date() + lineSep;
		
		log(titleString);
	
	}	

	public void log( String arrayName, Object[] array ) {
		
		StringBuffer sout = new StringBuffer(""); 
		int aSize = array.length;
		
		for (int i=0; i<aSize; i++) {
		    sout.append(arrayName + "["+ i + "] = " + array[i] + lineSep);
		}
		log(sout);
		
	}
	
	public void log( String inArrayName1, Object[] inArray1, String inArrayName2, Object[] inArray2 ) {
		
		StringBuffer sout = new StringBuffer(""); 
		int aSize = inArray1.length;
		
		for (int i=0; i<aSize; i++) {
		    sout.append(inArrayName1 + "["+ i + "] = " + inArray1[i]);
		    sout.append("  |  ");
		    sout.append(inArrayName2 + "["+ i + "] = " + inArray2[i]);
		    sout.append(lineSep);
		}
		log(sout);
		
	}
	
	public void log( String s ) {
		
		StringBuffer sout = null;
		if (s != null) {
			sout = new StringBuffer(s); 
		} else {
			sout = new StringBuffer("Warning Log Called with Null");
		} 
		log(sout);
	
	}
	
	public void log( StringBuffer s) {
		if (outFile == null) {
			initialize();
		}
		try {
			   fileWriter.append(s);
			   fileWriter.append(lineSep);
			   fileWriter.flush();
			} catch (IOException e) {
				Log.error("Cannot append to or flush FileWriter for logFileDir=" + outFile.getPath() + " logFileName=" + outFile.getName());
	    }
	}

	public static void logChart(String inChartName, String inXLabel,
			String inYLabel, String inLegend, Double[] inXData, Double[] inYData) {
		

		double[] xDat = ArrayUtils.toPrimitive(inXData);
		double[] yDat = ArrayUtils.toPrimitive(inYData);

		String fileName = "./logs/" + inChartName;

		// Create Chart
		Chart chart = QuickChart.getChart(inChartName, inXLabel, inYLabel,
				inLegend, xDat, yDat);
		
		try {
			BitmapEncoder.saveBitmapWithDPI(chart, fileName, BitmapFormat.JPG, 300);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static void logChart(String inChartName, String inXLabel,
			String inYLabel, String[] inLegend, Double[] inXData, Double[][] inYData) {
		

		double[] xDat = ArrayUtils.toPrimitive(inXData);
		double[][] yDat = ArrUtil.toPrimitive(inYData);

		String fileName = "./logs/" + inChartName;

		// Create Chart
		Chart chart = QuickChart.getChart(inChartName, inXLabel, inYLabel,
				inLegend, xDat, yDat);
		
		try {
			BitmapEncoder.saveBitmapWithDPI(chart, fileName, BitmapFormat.JPG, 300);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * Returns a list of lines written to the log file. Mostly used for testing and not for large log files.
	 */
	public String[] readLines() {
		
        List<String> lineList = null;
		try {
			lineList = FileUtils.readLines(outFile);
		} catch (IOException e) {
			Log.error( "Could not read file " + outFile.getPath() );
		}
        String [] lines = new String[lineList.size()];
        lineList.toArray(lines);
		return lines;
		
	}

}
