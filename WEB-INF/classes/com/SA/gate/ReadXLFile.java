package com.SA.gate;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
 * Servlet implementation class ReadXLFile
 */
public class ReadXLFile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReadXLFile() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		InputStream input = new FileInputStream(getServletContext().getRealPath("/") + "/XLDepo/webcams_c600.xls");
		//for structured data read -start
		POIFSFileSystem fs = new POIFSFileSystem(input);
		HSSFWorkbook wb = new HSSFWorkbook(fs);
		HSSFSheet sheet = wb.getSheetAt(0);
		HashMap<String,HashMap<String,Double>> stackedMap = new HashMap();
		HashMap<String,Double> star1Map = new HashMap();
		HashMap<String,Double> star2Map = new HashMap();
		HashMap<String,Double> star3Map = new HashMap();
		HashMap<String,Double> star4Map = new HashMap();
		HashMap<String,Double> star5Map = new HashMap();
		HashMap<Integer,String> namesMap = new HashMap();
     	ArrayList<String> al = new ArrayList<String>();
		int index=0;
		Iterator<HSSFRow> rows = sheet.rowIterator();
		while (rows.hasNext()) {
			HSSFRow row = (HSSFRow) rows.next();
            for(int i=1;i< row.getPhysicalNumberOfCells();i++){
            	 if(index==0)
            	 {
            		HSSFCell cell = (HSSFCell) row.getCell((short) i);
         			if (cell != null && cell.getCellType() == HSSFCell.CELL_TYPE_STRING) 
         			{
        				namesMap.put(new Integer(i),cell.getStringCellValue());
        			}
         			 
            	 }
            	 else
            	 {
            			HSSFCell cell = (HSSFCell) row.getCell((short) i);
             			if (cell != null && cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) 
             			{
             			 double value = cell.getNumericCellValue();
             			 String name = namesMap.get(new Integer(i));
             		
             			 if (value==1)
             			 {
             				boolean isPresent = star1Map.containsKey(name);
             				if (isPresent){
             					double preVal = star1Map.get(name);
                				 star1Map.put(name, preVal+value);	
             				}
             				else
             				{
             					 star1Map.put(name, value);
             				}
             				 
             			 }
             			 else if (value==2)
            			 {
             				boolean isPresent = star2Map.containsKey(name);
             				if (isPresent){
             					double preVal = star2Map.get(name);
                				 star2Map.put(name, preVal+value);	
             				}
             				else
             				{
             					 star2Map.put(name, value);
             				}
            			 }
             			 else if (value==3)
            			 {
             				boolean isPresent = star3Map.containsKey(name);
             				if (isPresent){
             					double preVal = star3Map.get(name);
                				 star3Map.put(name, preVal+value);	
             				}
             				else
             				{
             					 star3Map.put(name, value);
             				}
            			 }
             			 else if (value==4)
            			 {
             				boolean isPresent = star4Map.containsKey(name);
             				if (isPresent){
             					double preVal = star4Map.get(name);
                				 star4Map.put(name, preVal+value);	
             				}
             				else
             				{
             					 star4Map.put(name, value);
             				}
            			 }
             			 else if (value==5)
            			 {
             				boolean isPresent = star5Map.containsKey(name);
             				if (isPresent){
             					double preVal = star5Map.get(name);
                				 star5Map.put(name, preVal+value);	
             				}
             				else
             				{
             					 star5Map.put(name, value);
             				}
            			 }
             			}
            	 }
            	 
             }//for
            index++;
       		}//while
		stackedMap.put("1-star",star1Map);
		stackedMap.put("2-star",star2Map);
		stackedMap.put("3-star",star3Map);
		stackedMap.put("4-star",star4Map);
		stackedMap.put("5-star",star5Map);
		//for structured data read -
	    Set<String> set = stackedMap.keySet();
		 Iterator<String> iter = set.iterator();
		 while(iter.hasNext())
	        {
			String sss=iter.next();
	        
			   HashMap<String,Double> starMap= stackedMap.get(sss);
			   Iterator keyValuePairs = starMap.entrySet().iterator();
		       while(keyValuePairs.hasNext())
		        {
	                Map.Entry entry= (Map.Entry) keyValuePairs.next();
	                String key=entry.getKey().toString();
	                Double value = Double.parseDouble(entry.getValue().toString());
	               // pw.println("defaultcategorydataset.addValue("+value+ ",\""+sss+"\",\""+key+"\");");
	             }
	        }
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
