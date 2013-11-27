<%@page import="java.io.*" %>
<%@page import="java.util.ArrayList"%>

<html>
<body background="images/backg_digi.gif">
<div style="Height:600px">
<h3> Available Products</h3>

<%
String b[]=null;	
				ArrayList filenam=new ArrayList();
				ArrayList cate=new ArrayList();
				ArrayList subcate=new ArrayList();
				String a[]=null;
				int index = 0;
				try
				{
				String dir = getServletContext().getRealPath("/");
				FileInputStream fstream = new FileInputStream(dir + "FileList.txt");
			    DataInputStream in = new DataInputStream(fstream);
			    BufferedReader br = new BufferedReader(new InputStreamReader(in));
			    String strLine;
			    
			    while ((strLine = br.readLine()) != null)
			    {
			    	b=strLine.split("\\|");
			    	filenam.add(index,b[0].replaceAll("\\.xls","")); 
			    	cate.add(index,b[1]);
			    	subcate.add(index,b[2]);
			    	index++;
			    }
			    %>
<%		    		    
			   for(int j=0;j<index;j++)
			   { 
				   a=((String)filenam.get(j)).split("_");
				   String linkURL = "/SA/test?Category=" + a[0] + "&SubCat=" + a[1];
				   %>
				   
				 <a  href=<%=linkURL%>><%= (String)cate.get(j) %>-<%= (String)subcate.get(j)%></a>
				   </br>
				   </br>
			   <%}
			   %>
			 <%
			}
			
				catch (IOException ex) {
					ex.printStackTrace();
				}
%>
</div>
</body>
</html>



