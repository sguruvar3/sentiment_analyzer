package com.SA.gate;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.*;
import java.util.Iterator;
import java.util.Locale;
import java.text.NumberFormat;
import javax.servlet.http.HttpSession;
import java.io.*;
import org.jfree.data.*;
import org.jfree.chart.*;
import org.jfree.chart.axis.*;
import org.jfree.chart.renderer.BarRenderer;
import org.jfree.chart.renderer.StandardXYItemRenderer;
import org.jfree.chart.renderer.StackedXYAreaRenderer;
import org.jfree.chart.renderer.XYAreaRenderer;
import org.jfree.chart.plot.*;
import org.jfree.chart.entity.*;
import org.jfree.chart.labels.*;
import org.jfree.chart.urls.*;
import org.jfree.chart.servlet.*;

public class GenerateChart {
	public static String generatePieChart(HashMap sectionMap, HttpSession session, PrintWriter pw) {
		String filename = null;
		try {
			//  Retrieve list of WebHits
			ChartDataSet chartDataSet = new ChartDataSet(sectionMap);
			ArrayList list = chartDataSet.getDataBySection();

			if (list.size() == 0) {
				System.out.println("No data has been found");
				throw new NoDataException();
			}

			//  Create and populate a PieDataSet
			DefaultPieDataset data = new DefaultPieDataset();
			Iterator iter = list.listIterator();
			while (iter.hasNext()) {
				ChartSection wh = (ChartSection)iter.next();
				data.setValue(wh.getSection(), new Long(wh.getCount()));
			}

			//  Create the chart object
			PiePlot plot = new PiePlot(data);
			plot.setInsets(new Insets(0, 5, 5, 5));
			StandardPieItemLabelGenerator spilg = new StandardPieItemLabelGenerator("{0}" );
			plot.setLabelGenerator(spilg); 
			plot.setURLGenerator(new StandardPieURLGenerator("xy_chart.jsp","section"));
            //plot.setToolTipGenerator(new StandardPieItemLabelGenerator());
			JFreeChart chart = new JFreeChart("Polarity Map", JFreeChart.DEFAULT_TITLE_FONT, plot, true);
			chart.setBackgroundPaint(java.awt.Color.white);

			//  Write the chart image to the temporary directory
			ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
			filename = ServletUtilities.saveChartAsPNG(chart, 600, 400, info, session);

			//  Write the image map to the PrintWriter
			ChartUtilities.writeImageMap(pw, filename, info);
			pw.flush();

		} catch (NoDataException e) {
			System.out.println(e.toString());
			filename = "public_nodata_500x300.png";
			
		} catch (Exception e) {
			System.out.println("Exception - " + e.toString());
			e.printStackTrace(System.out);
			filename = "public_error_500x300.png";
		}
		return filename;
	}
	
	public static HashMap generateCommentsList(String fileName, HashMap keyWords){
		BufferedReader buf = null;
		String line = null;
		String printLine = null;
		HashMap toPrint = null;
		try {
			Set entries = keyWords.entrySet();
			Iterator it = entries.iterator();
			while(it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
			    String key = (String)entry.getKey();
			
			    Integer value = (Integer)entry.getValue();
			    buf = new BufferedReader(new FileReader(fileName));
				while ((line = buf.readLine()) != null ) {
					String comment = line.substring(0,line.indexOf('|'));
					String[] sentences = comment.split("\\.");
					boolean matchFound = false;
				    for(String sentence : sentences) {
				    	if (sentence.toUpperCase().contains(key)) {
				    		matchFound = true;
				    		if (toPrint == null) {
				    			toPrint = new HashMap();
				    		}
				    		if (toPrint.containsKey(key)) {
				    			printLine = (String)toPrint.get(key);
				    			printLine = printLine + sentence + ".";
				    			toPrint.remove(key);
				    		}
				    		else
				    			printLine = sentence + ".";
				    		toPrint.put(key,printLine);		    		
				    	}
				    }
				    if (matchFound==true && toPrint.containsKey(key)) {
					    printLine = (String)toPrint.get(key);
			    		printLine = printLine + "\n";
		    			toPrint.remove(key);
			    		toPrint.put(key,printLine);	
				    }
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return toPrint;
		
	}

}
