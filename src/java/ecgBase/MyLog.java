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

import com.xeiam.xchart.BitmapEncoder;
import com.xeiam.xchart.Chart;
import com.xeiam.xchart.QuickChart;
import com.xeiam.xchart.BitmapEncoder.BitmapFormat;
import com.xeiam.xchart.SwingWrapper;
import com.xeiam.xchart.VectorGraphicsEncoder;
import com.xeiam.xchart.VectorGraphicsEncoder.VectorGraphicsFormat;
import com.xeiam.xchart.XChartPanel;

public class MyLog {
	
	private static MyLog logServiceInstance = null;
	private String lineSep = System.lineSeparator();
	private File outFile = null;
	private FileWriter fileWriter = null;
	private String titleString = null;

	private MyLog() {
	}

	public static MyLog getLogService() {

		if ( logServiceInstance == null ) {
			logServiceInstance = new MyLog();
		}
		
		return logServiceInstance;
	}

	public void initialize() {

		String logFileDir = "./logs";
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

	public void log( String arrayName, Object[]   array ) {
		
		StringBuffer sout = new StringBuffer(""); 
		int aSize = array.length;
		
		for (int i=0; i<aSize; i++) {
		    sout.append(arrayName + "["+ i + "] = " + array[i] + lineSep);
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

		// Show it
		// JFrame frame = new SwingWrapper(chart).displayChart();
		
		final JFrame frame = new JFrame(inChartName);

	    // Schedule a job for the event-dispatching thread:
	    // creating and showing this application's GUI.
	    // javax.swing.SwingUtilities.invokeLater(new Runnable() {

	      // @Override
	      // public void run() {

	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        JPanel chartPanel = new XChartPanel(chart);
	        frame.add(chartPanel);

	        // Display the window.
	        frame.pack();
	        frame.setVisible(true);
	      // }
	    // });
		
		BufferedImage image = new BufferedImage(frame.getWidth(),
				frame.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = image.createGraphics();
		frame.paint(graphics2D);
		try {
			ImageIO.write(image, "png", new File(fileName + ".png"));
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		
		// or save it in high-res
		/* TODO cleanup whole function
		 * try {
		 * 
		 * // String fileName = "./logs/" + inChartName; //
		 * VectorGraphicsEncoder.saveVectorGraphic(chart, fileName,
		 * VectorGraphicsFormat.PDF); // BitmapEncoder.saveBitmap(chart,
		 * fileName, BitmapFormat.PNG); //
		 * BitmapEncoder.saveBitmapWithDPI(chart, fileName, BitmapFormat.PNG,
		 * 300); BitmapEncoder.saveJPGWithQuality(chart, fileName, 0.95f); }
		 * catch (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
	}

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
