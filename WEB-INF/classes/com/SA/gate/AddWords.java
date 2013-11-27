package com.SA.gate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 * Servlet implementation class AddWords
 */
public class AddWords extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddWords() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
		String sentence = request.getParameter("sentence");
		String fn = request.getParameter("filename");
		String curr_sen = "";
		if (fn == null || fn.length() == 0 || sentence == null
				|| sentence.length() == 0) {
			String ret = readFilesNFindFilename(getServletContext()
					.getRealPath("/"));
			request.setAttribute("filename", ret);
			fn = ret;
			System.out.println("fn "+fn);
		}
		request.setAttribute("filename",fn);
		ArrayList<String> linesAL = readXLFileNImportAsArrayList(getServletContext()
				.getRealPath("/")
				+ "/XLDepo/" + fn);
		if (sentence == null || sentence.length() == 0) {
			curr_sen = linesAL.get(0);
		} else {
			if((linesAL.indexOf(sentence)+1)==linesAL.size())
			{
				try 
				{
					BufferedWriter out = new BufferedWriter(new FileWriter(getServletContext().getRealPath("/")+"/Rulebase/status.txt", true)); 
					out.write(request.getParameter("filename")); 
					out.write("\n");
					out.close(); 
				} 
				catch (IOException e) { }
				String ret = readFilesNFindFilename(getServletContext()
						.getRealPath("/"));
				request.setAttribute("filename", ret);
				fn = ret;
				request.setAttribute("filename",fn);
				if(ret.equalsIgnoreCase("COMPLETED"))
				{
					curr_sen="COMPLETEDALL";
				}
				else
				{
					linesAL = readXLFileNImportAsArrayList(getServletContext()
							.getRealPath("/")+ "/XLDepo/" + fn);
					curr_sen = linesAL.get(0);
				}
				
			}
			else
			{
			curr_sen = linesAL.get(linesAL.indexOf(sentence) + 1);
			}
		}
		request.setAttribute("sentence",curr_sen);
		ArrayList<String> goodWordsList = readAsArrayList(getServletContext().getRealPath("/")+ "/Rulebase/ruleset_good.txt");
		ArrayList<String> badWordsList = readAsArrayList(getServletContext().getRealPath("/")+ "/Rulebase/ruleset_bad.txt");
		ArrayList<String> productsList = readAsArrayList(getServletContext().getRealPath("/")+ "/Rulebase/ruleset_products.txt");
		String[] wordsAr =  curr_sen.split(" ");
		curr_sen = "";
		String word="";
		boolean flag = false;
		for(String w: wordsAr){
			word =w;
			if (goodWordsList.contains(w.toLowerCase()) || w.equalsIgnoreCase("completedall")){
				word="<b><font color=\"GREEN\">"+w+"</font></b>";
				flag = true;
			}
			if (badWordsList.contains(w.toLowerCase())){
				word= "<b><font color=\"RED\">"+w+"</font></b>";
				flag = true;
			}
			if (productsList.contains(w.toLowerCase())){
				word= "<b><font color=\"BLUE\">"+w+"</font></b>";
				flag = true;
			}
			word+=" ";
			if (flag)
			{
				curr_sen+="<span onmouseover=\"selectNode(this,'"+w+"','so');\" OnMouseOut=\"clearSelection(this);\">"+word+"</span>";	
			}
			else
			{
				curr_sen+="<span onmouseover=\"selectNode(this,'"+w+"','ns');\" OnMouseOut=\"clearSelection(this);\">"+word+"</span>";
			}
			flag=false;
		}
		request.setAttribute("sentence_deco",curr_sen);
		
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/AddWords.jsp");
		dispatcher.forward(request, response);

	}

	public static String readFilesNFindFilename(String realpath)
			throws IOException {
		ArrayList<String> filesProcessedList = readAsArrayList(realpath
				+ "/Rulebase/status.txt");
		// read the files from Rulebase dir
		
		String b[]=null;	
		ArrayList<String> filenam=new ArrayList<String>();
		String a[]=null;
		int index = 0;
		try
		{
			FileInputStream fstream = new FileInputStream(realpath + "FileList.txt");
		    BufferedReader br = new BufferedReader(new InputStreamReader(new DataInputStream(fstream)));
		    String strLine;
	    
		    while ((strLine = br.readLine()) != null)
		    {
		    	b=strLine.split("\\|");
		    	filenam.add(index,b[0]); 
		    	index++;
		    }
	    			    		    
	   }
		catch (IOException ex) {
			ex.printStackTrace();
		}
		
		String dir = realpath;
		XlsFilter xlFilter = new XlsFilter("xls");
		dir += "XLDepo";
		String ret = "COMPLETED";
		for (String fileName : filenam) {
			if (filesProcessedList.contains(fileName)) {
				System.out.println("inside");
				
			} else {
				ret = fileName;
				break;
			}
		}
		return ret;

	}

	public static ArrayList readAsArrayList(String s)
			throws java.io.FileNotFoundException, java.io.IOException {
		FileReader fr = new FileReader(s);
		BufferedReader br = new BufferedReader(fr);
		ArrayList aList = new ArrayList(1024);
		String line = null;
		while ((line = br.readLine()) != null) {
			aList.add(line);
		}
		br.close();
		return aList;
	}
	
	private static String removeSpecialCharacters(String str)
	{
	    return str.replaceAll("[^a-zA-Z0-9_ ]+", " ");
	}

	public static ArrayList<String> readXLFileNImportAsArrayList(String file)
			throws IOException {
		ArrayList<String> al = new ArrayList<String>();
		String strLine = null;
		try {
			FileInputStream fstream = new FileInputStream(file);
		    DataInputStream in = new DataInputStream(fstream);
		    BufferedReader br = new BufferedReader(new InputStreamReader(in));    		        	    
		    while ((strLine = br.readLine()) != null) 
		      {
		       String[] sentences = (strLine.split("\\|"))[0].split("\\.");
		       for (int k=0;k<sentences.length;k++)
		    	   al.add(removeSpecialCharacters(sentences[k]));	
		      }    
		       	    	 
		    in.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return al;
	}

	

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);	
	}

}