package com.SA.gate;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * Servlet implementation class OTAChart
 */
public class OTAChart extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public OTAChart() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String file = getServletContext().getRealPath("/")
				+ "overTimeAnalysis.txt";
		FileInputStream fstream = new FileInputStream(file);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		PrintWriter pw = response.getWriter();
		DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();
		String imgName = "";
		HashMap<String, ArrayList<Double>> featMap = new HashMap<String, ArrayList<Double>>();
		HashMap<String, ArrayList<Double>> posMap = new HashMap<String, ArrayList<Double>>();
		HashMap<String, ArrayList<Double>> negMap = new HashMap<String, ArrayList<Double>>();
		while ((strLine = br.readLine()) != null) {
			pw.println(strLine);
			String[] strAr = strLine.split("\\|");
			for (String s : strAr) {
				pw.println("----" + s);
			}
			pw.println(strAr[0]);
			String[] fnAr = strAr[0].split("_");
			pw.println(fnAr[0]);
			pw.println(fnAr.length);
			if (fnAr.length == 2) {
				pw.println("if "+fnAr);
				imgName = strAr[0].substring(0, strAr[0].indexOf("."));
				defaultcategorydataset.addValue(Double.parseDouble(strAr[1]),
						"Positive", "Initial");
				defaultcategorydataset.addValue(Double.parseDouble(strAr[2]),
						"Negative", "Initial");
			}
			if (fnAr.length > 2) {
				pw.println("else "+fnAr);
				defaultcategorydataset.addValue(Double.parseDouble(strAr[1]),
						"Positive", fnAr[2]);
				defaultcategorydataset.addValue(Double.parseDouble(strAr[2]),
						"Negative", fnAr[2]);
			}

			// popMap
			String popMap = strAr[3];
			popMap = popMap.replaceAll("\\{", "");
			popMap = popMap.replaceAll("\\}", "");
			pw.println("+++++++++++++++++++ " + popMap);
			String featureAr[] = popMap.split(",");

			for (String f : featureAr) {
				String[] featVal = f.split("=");
				featVal[0] = featVal[0].replaceAll("^\\s+", "");
				boolean isPresent = featMap.containsKey(featVal[0]);
				pw.println("processing " + featVal[0] + "  == " + isPresent);
				if (isPresent) {
					ArrayList<Double> valAl = featMap.get(featVal[0]);
					valAl.add(Double.parseDouble(featVal[1]));
					featMap.put(featVal[0], valAl);
				} else {
					ArrayList<Double> valAl = new ArrayList<Double>();
					valAl.add(Double.parseDouble(featVal[1]));
					featMap.put(featVal[0], valAl);
				}
			}
				}
		pw.println("ssssssssssssssssssss"+featMap);
		
		// Overall map
		/*final JFreeChart chartStruc = ChartFactory.createStackedBarChart3D(
				"Over Time Period +/- Influence Report", "Category",
				"Response", defaultcategorydataset, PlotOrientation.VERTICAL,
				true, true, false);
		CategoryPlot plot = chartStruc.getCategoryPlot();
		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setSeriesPaint(0, Color.GREEN);
		renderer.setSeriesPaint(1, Color.RED);
		try {
			final ChartRenderingInfo info = new ChartRenderingInfo(
					new StandardEntityCollection());
			final File file3 = new File(this.getServletContext().getRealPath(
					"/")
					+ "ImgRepo/OTP_OV_" + imgName + ".png");
			ChartUtilities.saveChartAsPNG(file3, chartStruc, 600, 400, info);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// individual features map
		Iterator keyValuePairs = featMap.entrySet().iterator();
		 
		while (keyValuePairs.hasNext()) {
			Map.Entry entry = (Map.Entry) keyValuePairs.next();
			String key = entry.getKey().toString();
			ArrayList<Double> al = (ArrayList) entry.getValue();
			DefaultCategoryDataset popDS = new DefaultCategoryDataset();
			DefaultCategoryDataset popDSMul = new DefaultCategoryDataset();
			final XYSeries series = new XYSeries("Random Data");
			Double tot = 0.0;
			int i=1;Double max=-90.00;
			for (Double d : al) {
				tot += d;
				if(d>max){max=d;}
				popDSMul.addValue(d,i+"" ,"" );
				series.add(i,d);i++;
			}

			popDS.addValue(tot, "", "");
			 
			final JFreeChart chartPop = ChartFactory.createBarChart("", "", "",
					popDS, PlotOrientation.HORIZONTAL, false, false, false);
			
			final JFreeChart chartPopMul = ChartFactory.createBarChart("", "", "",
					popDSMul, PlotOrientation.HORIZONTAL, false, false, false);
			chartPopMul.setBackgroundPaint(new Color(255, 255, 255));
			plot = chartPopMul.getCategoryPlot();
			NumberAxis topAxis = (NumberAxis) plot.getRangeAxis();
	        topAxis.setUpperBound(10);

			 final XYSeriesCollection data = new XYSeriesCollection(series);
		     final JFreeChart chartSeries = ChartFactory.createXYLineChart("","","",data,PlotOrientation.VERTICAL,false,false,false);
			chartPop.setBackgroundPaint(new Color(255, 255, 255));
			plot = chartPop.getCategoryPlot();
			topAxis = (NumberAxis) plot.getRangeAxis();
	        topAxis.setUpperBound(10);

			

			renderer = (BarRenderer) plot.getRenderer();
			renderer.setMaximumBarWidth(0.10);
			plot = chartPopMul.getCategoryPlot();
			renderer = (BarRenderer) plot.getRenderer();
			renderer.setMaximumBarWidth(0.10);
			i=0;
			for (Double d:al){
				renderer.setSeriesPaint(i, Color.GREEN);
				i++;
			}
			try {
				final ChartRenderingInfo info = new ChartRenderingInfo(
						new StandardEntityCollection());

				final File file1 = new File(this.getServletContext()
						.getRealPath("/")
						+ "ImgRepo/OTP_POPT_" + imgName + "_" + key + ".png");
				final File file2 = new File(this.getServletContext()
						.getRealPath("/")
						+ "ImgRepo/OTP_POPT_" + imgName + "_" + key + "_mul.png");
				final File file3 = new File(this.getServletContext()
						.getRealPath("/")
						+ "ImgRepo/OTP_POPT_" + imgName + "_" + key + "_xy.png");
				ChartUtilities.saveChartAsPNG(file1, chartPop, 250, 150, info);
				ChartUtilities.saveChartAsPNG(file2, chartPopMul, 250, 150, info);
				ChartUtilities.saveChartAsPNG(file3, chartSeries, 250, 150, info);
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}*/
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}