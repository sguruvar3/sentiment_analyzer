package com.SA.gate;

import gate.Document;
import gate.Factory;
import gate.Gate;
import gate.creole.SerialAnalyserController;
import gate.util.GateException;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.regex.Pattern;

import javax.crypto.spec.PSource;
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

/**
 * Servlet implementation class stockFinderNCounter
 */
public class TC2Table extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TC2Table() {
		super();

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			String strLine = null;
			PrintWriter pw = response.getWriter();
			String word = request.getParameter("word");
			String filename = request.getParameter("filename");
			String dir = getServletContext().getRealPath("/");
			/*InputStream input = new FileInputStream(dir + "/XLDepo/" + filename
					+ ".xls");
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

			}*/
			 String[] a =null;
			 ArrayList<String> al = new ArrayList<String>();  
			try {
				FileInputStream fstream = new FileInputStream(dir + "/XLDepo/" + filename + ".xls");
			    DataInputStream in = new DataInputStream(fstream);
			    BufferedReader br = new BufferedReader(new InputStreamReader(in));    		        	    
			    while ((strLine = br.readLine()) != null) 
			      {
			       a = strLine.split("\\|");
			       al.add(a[0]);
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
			}catch (IOException ex) {
				ex.printStackTrace();
			}
			
			// pw.println("LEN OF ARR " + al.size());
			// String res= getMatchingIndexes(al, word);
			int i=1;
			String res = "<table id=\"rounded-corner\"> <thead><tr><th scope=\"col\" class=\"rounded-company\">#</th><th scope=\"col\" class=\"rounded-q1\">Comment</th></tr></thead><tfoot></tfoot><tbody>";
  
			if (word.equals("AALL")) {
				ListIterator<String> li = al.listIterator();
				while (li.hasNext()) {
					String next = li.next();
					//next = next.replaceAll(regex, replacement)
					res +="<tr><td>"+i+"</td><td>"+next + "</td></tr>";
					i++;
				}
			}

			else {
				ListIterator<String> li = al.listIterator();
				while (li.hasNext()) {
					String next = li.next();
					if (next.contains(word)) {
						next = next.replaceAll(word,"<b>"+word+"</b>");
						res +="<tr><td>"+i+"</td><td>"+next + "</td></tr>";
						i++;
					}
				}
			}
			res+="</tbody></table>";
			pw.println(res);

		} catch (IOException ex) {
			ex.printStackTrace();
		}
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
