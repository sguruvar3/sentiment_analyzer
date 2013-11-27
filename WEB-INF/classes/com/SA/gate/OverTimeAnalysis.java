package com.SA.gate;

import gate.Document;
import gate.Factory;
import gate.Gate;
import gate.creole.SerialAnalyserController;
import gate.util.GateException;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
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
 * Servlet implementation class OverTimeAnalysis
 */
public class OverTimeAnalysis extends HttpServlet {
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public OverTimeAnalysis() {
		super();
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
		HashMap<String, ArrayList<String>> xlMap = new HashMap<String, ArrayList<String>>();
		String dir = getServletContext().getRealPath("/");
		XlsFilter xlFilter = new XlsFilter("xls");
		dir += "XLDepo";
		File file = new File(dir);
		String fileList[] = file.list(xlFilter);
		
		try {
			System.setProperty("gate.home", getServletContext().getRealPath("/")+ "/gate-5.0");
			Gate.init();
			Gate.getCreoleRegister().registerDirectories(new File(getServletContext().getRealPath("/")+ "/gate-5.0/plugins/popDet").toURL());
			Gate.getCreoleRegister().registerDirectories(new File(getServletContext().getRealPath("/")+ "/gate-5.0/plugins/ANNIE").toURL());
		} catch (Exception e) {
			e.printStackTrace();
		}
         int i=0;
        
		 for (String s : fileList) {
			 FileWriter fstream = new FileWriter(getServletContext().getRealPath("/")+"overTimeAnalysis.txt",true);
				BufferedWriter fOut = new BufferedWriter(fstream);
				
		  String write2file=null;
		  pw.println("GONNA PROCESS "+dir+"/"+s+"---"+write2file);
		  write2file=s+"|";
		  System.out.println("1111111111 "+write2file);
		  ArrayList<String> al = new ArrayList<String>();
		 try {
			InputStream input = new FileInputStream(dir + "/" + s);
			POIFSFileSystem fs = new POIFSFileSystem(input);
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(0);

			
			Iterator<HSSFRow> rows = sheet.rowIterator();
			while (rows.hasNext()) {
				HSSFRow row = (HSSFRow) rows.next();
				HSSFCell cell = (HSSFCell) row.getCell((short) 0);
				if (cell != null && cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
					al.add((String) cell.getStringCellValue());
				}
			}
			String ss = s.substring(0);
			//xlMap.put(s.substring(0, s.indexOf(".")), al);
			pw.println(s.substring(0, s.indexOf("."))+al.size());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		//Collection<ArrayList<String>> c = xlMap.values();
		//for (ArrayList<String> al : c) {
			try {
				createCorpus(al,request,i);
				gate.Corpus corpus = (gate.Corpus )request.getAttribute("corpus"+i);
				pw.println("DDDDDDDDDD i "+corpus.size());
				String[] processingResources = {
						"gate.creole.tokeniser.DefaultTokeniser",
						"gate.creole.splitter.SentenceSplitter",
						"com.guru.creole.PopDet.PopularityDeterminer" };
				runProcessingResources(processingResources,request,i);
				corpus = (gate.Corpus )request.getAttribute("corpus"+i);
				pw.println("DDDDDDDDDD i "+corpus.size());
				displayDocumentFeatures(request,i);
				 HashMap map=(HashMap)request.getAttribute("positiveWordsMap"+i);
				 pw.println("dafdf" +i+" GWM "+map.size());
		        double goodWordsCnt =0;
			      Iterator  keyValuePairs = map.entrySet().iterator();
			        while(keyValuePairs.hasNext())
			        {
			                Map.Entry entry= (Map.Entry) keyValuePairs.next();
			                goodWordsCnt+=Double.parseDouble(entry.getValue().toString());
			        }
			        write2file+=goodWordsCnt+"|";
			        System.out.println("222222 "+write2file);
			        map.clear();
			        request.setAttribute("positiveWordsMap", map);
					 map=(HashMap)request.getAttribute("negativeWordsMap"+i);
					  double badWordsCnt =0;
					  keyValuePairs = map.entrySet().iterator();
					   while(keyValuePairs.hasNext())
					      {
					        Map.Entry entry= (Map.Entry) keyValuePairs.next();
					        badWordsCnt+=Double.parseDouble(entry.getValue().toString());
					      }
					   //map.clear();
					   //request.setAttribute("negativeWordsMap", map);
					   write2file+=badWordsCnt+"|";
					   System.out.println("3333 "+write2file);
                       write2file+=request.getAttribute("popularityMap"+i)+"|";
                       //map = (HashMap)request.getAttribute("popularityMap");
                       //map.clear();
                       //request.setAttribute("popularityMap", map);
                       write2file+=request.getAttribute("positiveMap"+i)+"|";
                       //map = (HashMap)request.getAttribute("positiveMap");
                       //map.clear();
                       //request.setAttribute("positiveMap", map);
                       write2file+=request.getAttribute("negativeMap"+i);
                       //map = (HashMap)request.getAttribute("negativeMap");
                       //map.clear();
                       //request.setAttribute("negativeMap", map);
                       System.out.println("44444 "+write2file);
			} catch (Exception e) {
				e.printStackTrace();
			}
		//}
		fOut.write(write2file+"\n");
		
		write2file=null;
		i++;
		fOut.close();
		 }
		
		 
		 /*********
		  * 
		  * 
		  * overTimeAnalysis.txt has been written/updated. 
		  * 
		  * Now plot charts based on the info */
		 

			String file1 = getServletContext().getRealPath("/")
					+ "overTimeAnalysis.txt";
			FileInputStream fstream = new FileInputStream(file1);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			 pw = response.getWriter();
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
					imgName = strAr[0].substring(0, strAr[0].indexOf("."));
					defaultcategorydataset.addValue(Double.parseDouble(strAr[1]),
							"Positive", "Initial");
					defaultcategorydataset.addValue(Double.parseDouble(strAr[2]),
							"Negative", "Initial");
				}
				if (fnAr.length > 2) {
					defaultcategorydataset.addValue(Double.parseDouble(strAr[2]),
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
			pw.println(featMap);
			
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
				i=1;Double max=-90.00;
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

					final File file11 = new File(this.getServletContext()
							.getRealPath("/")
							+ "ImgRepo/OTP_POPT_" + imgName + "_" + key + ".png");
					final File file2 = new File(this.getServletContext()
							.getRealPath("/")
							+ "ImgRepo/OTP_POPT_" + imgName + "_" + key + "_mul.png");
					final File file3 = new File(this.getServletContext()
							.getRealPath("/")
							+ "ImgRepo/OTP_POPT_" + imgName + "_" + key + "_xy.png");
					ChartUtilities.saveChartAsPNG(file11, chartPop, 250, 150, info);
					ChartUtilities.saveChartAsPNG(file2, chartPopMul, 250, 150, info);
					ChartUtilities.saveChartAsPNG(file3, chartSeries, 250, 150, info);
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}*/
			
			
		
	
	}

	private void createCorpus(ArrayList<String> al, HttpServletRequest request,int j)
			throws GateException {
		
		gate.Corpus corpus = null;
		corpus = Factory.newCorpus("Transient Gate Corpus"+j);

		for (int i = 0; i < al.size(); i++) {

			try {
				corpus.add(Factory.newDocument(al.get(i)));
			} catch (gate.creole.ResourceInstantiationException e) {
			} catch (Exception e) {

			}
		}
		
		request.setAttribute("corpus"+j, "");
		request.setAttribute("corpus"+j, corpus);
	}

	private void runProcessingResources(String[] processingResource,
			HttpServletRequest request,int j) throws GateException {
		SerialAnalyserController pipeline = (SerialAnalyserController) Factory
				.createResource("gate.creole.SerialAnalyserController");

		for (int pr = 0; pr < processingResource.length; pr++) {
			pipeline.add((gate.LanguageAnalyser) Factory
					.createResource(processingResource[pr]));
		}
		gate.Corpus corpus = (gate.Corpus) request.getAttribute("corpus"+j);
		pipeline.setCorpus(corpus);

		pipeline.execute();

	}

	private void displayDocumentFeatures(HttpServletRequest request,int i) {
		HashMap positiveMap = new HashMap();
		HashMap negativeMap = new HashMap();
		HashMap popularityMap = new HashMap();
		HashMap goodWordsMap = new HashMap();
		HashMap badWordsMap = new HashMap();
		
		 positiveMap.clear(); negativeMap.clear(); popularityMap.clear();
		 goodWordsMap.clear(); badWordsMap.clear();
		 
		gate.Corpus corpus = (gate.Corpus) request.getAttribute("corpus"+i);
		Iterator documentIterator = corpus.iterator();
		

		while (documentIterator.hasNext()) {
			Document currDoc = (Document) documentIterator.next();
			gate.FeatureMap documentFeatures = currDoc.getFeatures();

			Iterator featureIterator = documentFeatures.keySet().iterator();
			while (featureIterator.hasNext()) {
				String key = (String) featureIterator.next();
				if (key.equals("goodSecMap")) {
					HashMap tempMap = new HashMap();
					tempMap = (HashMap) documentFeatures.get(key);
					
					Object[] keys = tempMap.keySet().toArray();
					for (Object s : keys) {
						if (!s.toString().equalsIgnoreCase(".")
								&& s.toString().length() > 0) {
							Boolean isPresent = positiveMap.containsKey(s);
							if (isPresent) {
								int val = Integer.parseInt(positiveMap.get(s)
										.toString());
								positiveMap.put(s, val
										+ Integer.parseInt(tempMap.get(s)
												.toString()));
							} else {
								positiveMap.put(s, Integer.parseInt(tempMap
										.get(s).toString()));
							}
						}
					}
					tempMap.clear();
				} else if (key.equals("badSecsMap")) {
					HashMap tempMap = new HashMap();
					tempMap = (HashMap) documentFeatures.get(key);

					Object[] keys = tempMap.keySet().toArray();
					for (Object s : keys) {
						if (!s.toString().equalsIgnoreCase(".")
								&& s.toString().length() > 0) {
							Boolean isPresent = negativeMap.containsKey(s);
							if (isPresent) {
								int val = Integer.parseInt(negativeMap.get(s)
										.toString());
								negativeMap.put(s, val
										+ Integer.parseInt(tempMap.get(s)
												.toString()));
							}

							else {
								negativeMap.put(s, Integer.parseInt(tempMap
										.get(s).toString()));
							}
						}
					}
					tempMap.clear();
				} else if (key.equals("Map")) {
					HashMap tempMap = new HashMap();
					tempMap = (HashMap) documentFeatures.get(key);

					Object[] keys = tempMap.keySet().toArray();
					for (Object s : keys) {

						if (!s.toString().equalsIgnoreCase(".")
								&& s.toString().length() > 0) {
							Boolean isPresent = popularityMap.containsKey(s);
							if (isPresent) {
								int val = Integer.parseInt(popularityMap.get(s)
										.toString());
								popularityMap.put(s, val
										+ Integer.parseInt(tempMap.get(s)
												.toString()));

							} else {
								popularityMap.put(s, Integer.parseInt(tempMap
										.get(s).toString()));
							}
						}
					}
					tempMap.clear();
				} else if (key.equals("goodCntrMap")) {
					HashMap tempMap = new HashMap();
					tempMap = (HashMap) documentFeatures.get(key);

					Object[] keys = tempMap.keySet().toArray();
					for (Object s : keys) {
						if (!s.toString().equalsIgnoreCase(".")
								&& s.toString().length() > 0) {
							Boolean isPresent = goodWordsMap.containsKey(s);
							if (isPresent) {
								int val = Integer.parseInt(goodWordsMap.get(s)
										.toString());
								goodWordsMap.put(s, val
										+ Integer.parseInt(tempMap.get(s)
												.toString()));

							} else {
								goodWordsMap.put(s, Integer.parseInt(tempMap
										.get(s).toString()));
							}
						}
					}

				} else if (key.equals("badCntrMap")) {
					HashMap tempMap = new HashMap();
					tempMap = (HashMap) documentFeatures.get(key);
					
					Object[] keys = tempMap.keySet().toArray();
					for (Object s : keys) {
						if (!s.toString().equalsIgnoreCase(".")
								&& s.toString().length() > 0) {
							Boolean isPresent = badWordsMap.containsKey(s);
							if (isPresent) {
								int val = Integer.parseInt(badWordsMap.get(s)
										.toString());
								badWordsMap.put(s, val
										+ Integer.parseInt(tempMap.get(s)
												.toString()));

							} else {
								badWordsMap.put(s, Integer.parseInt(tempMap
										.get(s).toString()));
							}
						}
					}
					tempMap.clear();
				}

				else {

				}
			}
		}
		request.setAttribute("positiveMap"+i, positiveMap);
		request.setAttribute("negativeMap"+i, negativeMap);
		request.setAttribute("popularityMap"+i, popularityMap);
		request.setAttribute("positiveWordsMap"+i, goodWordsMap);
		request.setAttribute("negativeWordsMap"+i, badWordsMap);
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
