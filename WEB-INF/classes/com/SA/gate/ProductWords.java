package com.SA.gate;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ReadFiles
 */
public class ProductWords extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProductWords() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		//PrintWriter pw = response.getWriter();
		String dir = getServletContext().getRealPath("/");
		XlsFilter xlFilter = new XlsFilter("xls");
		dir += "Rulebase";
		ArrayList<String> goodWords = readAsArrayList(dir+"/ruleset_products.txt");
		Collections.sort(goodWords);
		String alph = Character.toString(goodWords.get(0).charAt(0));
		String w = goodWords.get(0);
		boolean firstTime=true;
		String glosContainer = "<div class=\"glossary-container\"><br><ul class=\"firstUL\">";
		String contentContainer = "<div class=\"content-container\">";
		boolean newAlph=false;
		int i=1;
		
		while(w.startsWith(alph) &&i<goodWords.size()){
			if(firstTime){
				glosContainer+="<li id=\""+alph.toLowerCase()+"\" class=\"selected\">"+alph.toUpperCase()+"</li>\n";
				contentContainer+="<div id=\"content-for-"+alph.toLowerCase()+"\" style=\"background-color:#d2e2fc\"><h2>"+alph.toUpperCase()+"</h2>\n";
				firstTime=false;
			}
			if (newAlph)
			{
				glosContainer+="<li id=\""+alph.toLowerCase()+"\">"+alph.toUpperCase()+"</li>\n";
				contentContainer+="<div id=\"content-for-"+alph.toLowerCase()+"\"><h2>"+alph.toUpperCase()+"</h2>\n";
				newAlph=false;
			}
			
			contentContainer+=w+"<br/>";
			
			w=goodWords.get(i);
			if (!w.startsWith(alph))
			{
			contentContainer+="<p class=\"return-to-top\">Return to Top</p></div><br/>";
			newAlph=true;
			alph = Character.toString(goodWords.get(i).charAt(0));
			}
            i++;
		}
		if(newAlph){
			glosContainer+="<li id=\""+alph.toLowerCase()+"\">"+alph.toUpperCase()+"</li>\n";
			contentContainer+="<div id=\"content-for-"+alph.toLowerCase()+"\"><h2>"+alph.toUpperCase()+"</h2>\n";
		}
		contentContainer+=w+"<br/>";
		contentContainer+="<p class=\"return-to-top\">Return to Top</p></div><br/>";
		glosContainer+="</ul> </div><br/>";
		String finalString = glosContainer+contentContainer;
		request.setAttribute("finalString", finalString);
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/ProductWords.jsp");
	    dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
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
}
