<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">


<%@ page buffer="256kb" %>
<%@ page import="it.exprivia.cnos.opencloud.*" %>
<%@ page import="it.exprivia.cnos.opencloud.formatters.*" %>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Set"%>
<%@ page import="com.SA.gate.GenerateChart"%>
<%@ page import="com.SA.gate.ChartDataSet"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.awt.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.awt.Color" %>
<%@ page import="org.jfree.chart.*" %>
<%@ page import="org.jfree.chart.axis.*" %>
<%@ page import="org.jfree.chart.entity.*" %>
<%@ page import="org.jfree.chart.labels.*" %>
<%@ page import="org.jfree.chart.plot.*" %>
<%@ page import="org.jfree.chart.renderer.category.*" %>
<%@ page import="org.jfree.chart.urls.*" %>
<%@ page import="org.jfree.data.category.*" %>
<%@ page import="org.jfree.data.general.*" %>

<%
	Iterator keyValuePairs = null;
    final DefaultPieDataset polarityData = new DefaultPieDataset();
	HashMap polarData = new HashMap();
	HashMap map=(HashMap)session.getAttribute("positiveWordsMap");
	polarData.put("Positive",map);
        	
    map=(HashMap)session.getAttribute("negativeWordsMap");
	polarData.put("Negative",map);
 	
	String filename = GenerateChart.generatePieChart(polarData, session, new PrintWriter(out));
	System.out.println("filename =" + filename); 
    String graphURL = request.getContextPath() + "/servlet/DisplayChart?filename=" + filename;
	String section = request.getParameter("section");
	session.setAttribute("section",section);
    String category = (String)session.getAttribute("Category");
    String subcategory = (String)session.getAttribute("SubCat");
%>




<%
HTMLFormatter formatter = new HTMLFormatter();

// Sets HTML fragments
formatter.setHtmlTemplateTop("<div class=\"tagcloud\" style=\"margin: auto; width: 80%;\">\n");
formatter.setHtmlTemplateTag("<a href=\"%tag-link%\" style=\"font-size: %tag-weight%px\">%tag-name%</a>\n");
formatter.setHtmlTemplateBottom("</div>\n");

// Sets CSS fragments
formatter.addCss("body { font-family: Arial, Helvetica, Sans-serif; }");
formatter.addCss("a { text-decoration: none; }");
formatter.addCss("a:link { color: #0063DC; }");
formatter.addCss("a:visited { color: #1057ae; }");
formatter.addCss("a:hover { color: #FFFFFF; background: #0063DC; }");
formatter.addCss("a:active { color: #FFFFFF; background: #0259C4; }");
formatter.addCss(".tagcloud { text-align: justify; padding: 15px; border: 1px solid #eeeeee; background-color: #f5f5f5; }");

Cloud cloud = new Cloud();
cloud.setMaxTagsToDisplay(100);
cloud.setMaxWeight(38.0);
cloud.setDefaultLink("javascript:getthat('%s');");

map=(HashMap)session.getAttribute("positiveWordsMap");
keyValuePairs = map.entrySet().iterator();
while(keyValuePairs.hasNext()){
	Map.Entry entry= (Map.Entry) keyValuePairs.next();
	
	cloud.addTag(new Tag(entry.getKey().toString(),Integer.parseInt(entry.getValue().toString())));
}
 map=(HashMap)session.getAttribute("negativeWordsMap");
 keyValuePairs = map.entrySet().iterator();
while(keyValuePairs.hasNext()){
	Map.Entry entry= (Map.Entry) keyValuePairs.next();
	
	cloud.addTag(new Tag(entry.getKey().toString(),Integer.parseInt(entry.getValue().toString())));
}
map=(HashMap)session.getAttribute("negativeMap");
keyValuePairs = map.entrySet().iterator();
while(keyValuePairs.hasNext())
{
	Map.Entry entry= (Map.Entry) keyValuePairs.next();
      cloud.addTag(new Tag(entry.getKey().toString(),Integer.parseInt(entry.getValue().toString())));
}
map=(HashMap)session.getAttribute("positiveMap");
keyValuePairs = map.entrySet().iterator();
while(keyValuePairs.hasNext())
{
	Map.Entry entry= (Map.Entry) keyValuePairs.next();
      cloud.addTag(new Tag(entry.getKey().toString(),Integer.parseInt(entry.getValue().toString())));
}
%>


<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Cognizant welcomes Logitech...</title>
  <link rel="stylesheet" type="text/css" href="style.css" />
<script src="gg.js" type="text/javascript"></script>
<script type="text/javascript" src="list.js"></script>
<script type="text/javascript" src="jquery.js"></script>
<script type="text/javascript" src="jquery-ui.js"></script>
<script language="javascript">
$(function() {
                $("#dshow").accordion({
                        header: "h3"
                });
        });
   function getthat(val) {
     var filename='<%= session.getAttribute("Category")%>'+"_"+'<%= session.getAttribute("SubCat")%>';
     var url = "http://localhost:8080/LBTRReport/tc2tbl?word="+val+"&filename="+filename;
     request.open("GET", url, true);
     request.onreadystatechange = updatePage
     request.send(null);
   }


   function updatePage() {
     if (request.readyState == 4) {
       var result = request.responseText;
       document.getElementById('result').innerHTML=result;
    }
}

  function loadDropDownBoxes()
   {
        s=document.getElementById('Category');
       for ( var i = 0; i < s.options.length; i++ ) {
            if ( s.options[i].value == '<%=category %>') {
               s.options[i].selected = true;
               i=s.options.length;
            }
         }
        SelectSubCat(); 
        s=document.getElementById('SubCat');
       for ( i = 0; i < s.options.length; i++ ) {
            if ( s.options[i].value == '<%=subcategory %>') {
               s.options[i].selected = true;
               return;
            }
         }
   }

</script>
    </head>
    <body onload="javascript:loadDropDownBoxes();">
   <form action="http://localhost:8080/LBTRReport/test" method="GET">
 
<div id="dshow" class="dshow">

   <h3><a href="#">.:: Unstructured Opinion (Tester's Comments) ::.</a></h3>
  <div>
     <table width="100%" border=1>
       <tr>
         <td width="33%" align="left">        <img src="<%= graphURL %>" width=600 height=400 border=0 usemap="#<%= filename %>"></td>
	<td width="67%" align="left">
	<table>
	<tr><th><font face="Monotype Corsiva, Verdana" size="4" color="#CC0000">Keyword</th><th><font face="Monotype Corsiva, Verdana" size="4" color="#CC0000">Comments</th></tr>
	<%
	String dir = getServletContext().getRealPath("/");
	String commentsFile = dir + "XLDepo/" + category+"_"+subcategory+".xls";
	System.out.println (commentsFile);

	HashMap comments = null;
	if (section.equals("Positive"))
		comments = GenerateChart.generateCommentsList(commentsFile, (HashMap)session.getAttribute("positiveWordsMap"));
	else if (section.equals("Negative"))
		comments = GenerateChart.generateCommentsList(commentsFile, (HashMap)session.getAttribute("negativeWordsMap"));
	if (comments != null) {
	Iterator iter = comments.entrySet().iterator();
		while(iter.hasNext()){
	Map.Entry entry= (Map.Entry) iter.next();
	String printKey = (String)entry.getKey();
	String printValue = (String)entry.getValue();
	
%>       
<tr><td><font face="Monotype Corsiva, Verdana" size="4" color="#000099"><%= printKey %></td><td><font face="Monotype Corsiva, Verdana" size="4" color="#006600"><%= printValue %></td></tr>
<%
}
}
%>
</table>
	</td>
       </tr>
     </table>
  </div>
  <h3><a href="#">.:: Unstructured Opinion - Tag Cloud ::.</a></h3>
  <div>
     <table width="100%">
     <tr>
        <td  align="center">
          <% out.println(formatter.html(cloud)); %>
        </td>
     </tr>
     <tr>
        <td align="center">
           <div id='result' align="center"></div>
        </td>
     </tr>
     </table>
  </div>
</div>
<a href="Links.jsp">Home</a>
</body>
</html>
