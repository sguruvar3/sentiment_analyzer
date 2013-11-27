package com.SA.gate;

import gate.Document;
import gate.Factory;
import gate.Gate;
import gate.creole.SerialAnalyserController;
import gate.util.GateException;

import java.io.*;
import java.util.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class test extends HttpServlet {
	gate.Corpus corpus;
	HashMap positiveMap = new HashMap();
	HashMap negativeMap = new HashMap();
	HashMap popularityMap = new HashMap();
	HashMap goodWordsMap = new HashMap();
	HashMap badWordsMap = new HashMap();

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		HashMap<String, ArrayList<String>> xlMap = new HashMap<String, ArrayList<String>>();
		String dir = getServletContext().getRealPath("/");
		XlsFilter xlFilter = new XlsFilter("xls");
		dir += "XLDepo";
		File file = new File(dir);
		String fileList[] = file.list(xlFilter);
		String buf = null;

		try {
			System.setProperty("gate.home", getServletContext()
					.getRealPath("/")
					+ "/gate-5.0");
			Gate.init();
			Gate.getCreoleRegister().registerDirectories(
					new File(getServletContext().getRealPath("/")
							+ "/gate-5.0/plugins/popDet").toURL());
			Gate.getCreoleRegister().registerDirectories(
					new File(getServletContext().getRealPath("/")
							+ "/gate-5.0/plugins/ANNIE").toURL());
		} catch (Exception e) {
			e.printStackTrace();
		}

		// for (String s : fileList) {
		String category = request.getParameter("Category");
		String subcategory = request.getParameter("SubCat");
		String s = category + "_" + subcategory + ".xls";
		String strLine;
		System.out.println (s);
		
		BufferedReader catList = new BufferedReader(new FileReader(new File(dir + "/FileList.txt")));
		if ((buf = catList.readLine()) != null) {
			
		}
		
	    ArrayList<String> wordList = new ArrayList<String>();  

	    String[] a =null;
		try {
			FileInputStream fstream = new FileInputStream(dir + "/" + s);
		    DataInputStream in = new DataInputStream(fstream);
		    BufferedReader br = new BufferedReader(new InputStreamReader(in));    		        	    
		    while ((strLine = br.readLine()) != null) 
		      {
		       a = strLine.split("\\|");
		       wordList.add(removeSpecialCharacters(a[0]));
		      }    
		       	    	 
		    in.close();
		    /*InputStream input = new FileInputStream(dir + "/" + s);
			POIFSFileSystem fs = new POIFSFileSystem(input);
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(0);

			ArrayList<String> al = new ArrayList<String>();
			// Iterate over each row in the sheet
			Iterator<HSSFRow> rows = sheet.rowIterator();
			while (rows.hasNext()) {
				HSSFRow row = (HSSFRow) rows.next();
				// pw.print("Row #" + row.getRowNum());

				HSSFCell cell = (HSSFCell) row.getCell((short) 0);
				if (cell != null
						&& cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {

					al.add((String) cell.getStringCellValue());
				}*/

			
			// pw.println("LEN OF ARR " + al.size());
			String ss = s.substring(0);
			System.out.println(s.substring(0, s.indexOf(".")));
			xlMap.put(s.substring(0, s.indexOf(".")),wordList);      
		}catch (IOException ex) {
			ex.printStackTrace();
		}
		// }

		Collection<ArrayList<String>> c = xlMap.values();

		for (ArrayList<String> al : c) {
			/* START PROCESSING THRO GATE */
			try {

				createCorpus(al);

				String[] processingResources = {
						"gate.creole.tokeniser.DefaultTokeniser",
						"gate.creole.splitter.SentenceSplitter",
						"com.guru.creole.PopDet.PopularityDeterminer" };
				runProcessingResources(processingResources);
				displayDocumentFeatures();

				HttpSession session = request.getSession(true);
				HashMap<String, HashMap<String, Double>> stackedMap = getStructuredData(dir+ "/" + s);
				session.setAttribute("structuredData", stackedMap);
				session.setAttribute("positiveMap", positiveMap);
				session.setAttribute("negativeMap", negativeMap);
				session.setAttribute("popularityMap", popularityMap);
				session.setAttribute("positiveWordsMap", goodWordsMap);
				session.setAttribute("negativeWordsMap", badWordsMap);
				System.out.println("Positive Map" + positiveMap);
				System.out.println("Negative Map" + negativeMap);
				System.out.println("Popular Map" + popularityMap);
				System.out.println("Good Map" + goodWordsMap);
				System.out.println("Bad Map" + badWordsMap);
				

				// response.sendRedirect("TagCloud.jsp");
				session.setAttribute("category", request
						.getParameter("Category"));
				session.setAttribute("subcategory", request
						.getParameter("SubCat"));
				RequestDispatcher dispatcher = getServletContext()
						.getRequestDispatcher("/BarChart.jsp");
				dispatcher.forward(request, response);

				// response.sendRedirect("BarChart.jsp");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		/* END PROCESSING THRO GATE */
	}
	
	private static String removeSpecialCharacters(String str)
	{
	    return str.replaceAll("[^a-zA-Z0-9_ ]+", " ");
	}

	private void createCorpus(ArrayList<String> al) throws GateException {
		corpus = Factory.newCorpus("Transient Gate Corpus");

		for (int i = 0; i < al.size(); i++) {

			try {
				corpus.add(Factory.newDocument(al.get(i)));
			} catch (gate.creole.ResourceInstantiationException e) {
			} catch (Exception e) {

			}
		}
	}

	private void runProcessingResources(String[] processingResource)
			throws GateException {
		SerialAnalyserController pipeline = (SerialAnalyserController) Factory
				.createResource("gate.creole.SerialAnalyserController");

		for (int pr = 0; pr < processingResource.length; pr++) {
			pipeline.add((gate.LanguageAnalyser) Factory
					.createResource(processingResource[pr]));
		}

		pipeline.setCorpus(corpus);

		pipeline.execute();

	}

	private void displayDocumentFeatures() {
		positiveMap.clear();
		negativeMap.clear();
		popularityMap.clear();
		goodWordsMap.clear();
		badWordsMap.clear();

		Iterator documentIterator = corpus.iterator();
		HashMap tempMap = new HashMap();

		while (documentIterator.hasNext()) {
			Document currDoc = (Document) documentIterator.next();
			gate.FeatureMap documentFeatures = currDoc.getFeatures();

			Iterator featureIterator = documentFeatures.keySet().iterator();
			while (featureIterator.hasNext()) {
				String key = (String) featureIterator.next();
				if (key.equals("goodSecMap")) {
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
				} else if (key.equals("badSecsMap")) {
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

				} else if (key.equals("Map")) {
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

				} else if (key.equals("goodCntrMap")) {
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

				}

				else {
					// System.out.println("\t*) " + key + " --> "+
					// documentFeatures.get(key));
				}
			}
			// System.out.println();
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String dir = getServletContext().getRealPath("/");
		XlsFilter xlFilter = new XlsFilter("xls");
		dir += "XLDepo";
		File file = new File(dir);
		String fileList[] = file.list(xlFilter);
		HashMap<String, ArrayList<String>> xlMap = new HashMap<String, ArrayList<String>>();
		try {
			System.setProperty("gate.home", getServletContext()
					.getRealPath("/")
					+ "/gate-5.0");
			Gate.init();
			Gate.getCreoleRegister().registerDirectories(
					new File(getServletContext().getRealPath("/")
							+ "/gate-5.0/plugins/popDet").toURL());
			Gate.getCreoleRegister().registerDirectories(
					new File(getServletContext().getRealPath("/")
							+ "/gate-5.0/plugins/ANNIE").toURL());
		} catch (Exception e) {
			e.printStackTrace();
		}

		// for (String s : fileList) {
		String category = request.getParameter("Category").toLowerCase();
		String subcategory = request.getParameter("SubCat").toLowerCase();
		String s = category + "_" + subcategory + ".xls";
		try {
			InputStream input = new FileInputStream(dir + "/" + s);
			POIFSFileSystem fs = new POIFSFileSystem(input);
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(0);

			ArrayList<String> al = new ArrayList<String>();
			// Iterate over each row in the sheet
			Iterator<HSSFRow> rows = sheet.rowIterator();
			while (rows.hasNext()) {
				HSSFRow row = (HSSFRow) rows.next(); 	

				HSSFCell cell = (HSSFCell) row.getCell((short) 0);
				if (cell != null
						&& cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {

					
					al.add((String) cell.getStringCellValue());
				}

			}
			
			// using traditional for loop with iterator
			for(Iterator i = al.iterator();i.hasNext();)
			{
			System.out.println(i.next());
			}

			String ss = s.substring(0);
			xlMap.put(s.substring(0, s.indexOf(".")), al);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		// }

		Collection<ArrayList<String>> c = xlMap.values();

		for (ArrayList<String> al : c) {
			/* START PROCESSING THRO GATE */
			try {

				createCorpus(al);

				String[] processingResources = {
						"gate.creole.tokeniser.DefaultTokeniser",
						"gate.creole.splitter.SentenceSplitter",
						"com.guru.creole.PopDet.PopularityDeterminer" };
				runProcessingResources(processingResources);
				displayDocumentFeatures();
				HttpSession session = request.getSession(true);

				session.setAttribute("positiveMap", positiveMap);
				session.setAttribute("negativeMap", negativeMap);
				session.setAttribute("popularityMap", popularityMap);
				session.setAttribute("positiveWordsMap", goodWordsMap);
				session.setAttribute("negativeWordsMap", badWordsMap);
				session.setAttribute("category", request
						.getParameter("Category"));
				session.setAttribute("subcategory", request
						.getParameter("SubCat"));
				RequestDispatcher dispatcher = getServletContext()
						.getRequestDispatcher("/BarChart.jsp");
				dispatcher.forward(request, response);
				// response.sendRedirect("BarChart.jsp");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private static HashMap<String, HashMap<String, Double>> getStructuredData(
			String inputFile) {
		// for structured data read -start
		HashMap<String, HashMap<String, Double>> stackedMap = new HashMap();
		try {
			InputStream input = new FileInputStream(inputFile);
			POIFSFileSystem fs = new POIFSFileSystem(input);
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(0);

			HashMap<String, Double> star1Map = new HashMap();
			HashMap<String, Double> star2Map = new HashMap();
			HashMap<String, Double> star3Map = new HashMap();
			HashMap<String, Double> star4Map = new HashMap();
			HashMap<String, Double> star5Map = new HashMap();
			HashMap<Integer, String> namesMap = new HashMap();
			ArrayList<String> al = new ArrayList<String>();
			int index = 0;
			Iterator<HSSFRow> rows = sheet.rowIterator();
			while (rows.hasNext()) {
				HSSFRow row = (HSSFRow) rows.next();
				for (int i = 1; i < row.getPhysicalNumberOfCells(); i++) {
					if (index == 0) {
						HSSFCell cell = (HSSFCell) row.getCell((short) i);
						if (cell != null
								&& cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
							namesMap.put(new Integer(i), cell
									.getStringCellValue());
						}

					} else {
						HSSFCell cell = (HSSFCell) row.getCell((short) i);
						if (cell != null
								&& cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
							double value = cell.getNumericCellValue();
							String name = namesMap.get(new Integer(i));

							if (value == 1) {
								boolean isPresent = star1Map.containsKey(name);
								if (isPresent) {
									double preVal = star1Map.get(name);
									star1Map.put(name, preVal + value);
								} else {
									star1Map.put(name, value);
								}

							} else if (value == 2) {
								boolean isPresent = star2Map.containsKey(name);
								if (isPresent) {
									double preVal = star2Map.get(name);
									star2Map.put(name, preVal + value);
								} else {
									star2Map.put(name, value);
								}
							} else if (value == 3) {
								boolean isPresent = star3Map.containsKey(name);
								if (isPresent) {
									double preVal = star3Map.get(name);
									star3Map.put(name, preVal + value);
								} else {
									star3Map.put(name, value);
								}
							} else if (value == 4) {
								boolean isPresent = star4Map.containsKey(name);
								if (isPresent) {
									double preVal = star4Map.get(name);
									star4Map.put(name, preVal + value);
								} else {
									star4Map.put(name, value);
								}
							} else if (value == 5) {
								boolean isPresent = star5Map.containsKey(name);
								if (isPresent) {
									double preVal = star5Map.get(name);
									star5Map.put(name, preVal + value);
								} else {
									star5Map.put(name, value);
								}
							}
						}
					}

				}// for
				index++;
			}// while
			stackedMap.put("1-star", star1Map);
			stackedMap.put("2-star", star2Map);
			stackedMap.put("3-star", star3Map);
			stackedMap.put("4-star", star4Map);
			stackedMap.put("5-star", star5Map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// for structured data read -end
		return stackedMap;
	}

}
