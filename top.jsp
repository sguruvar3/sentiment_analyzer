<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page language="java" import="java.io.*"%>
<html>
<head>
<style type="text/css">
.img1{
  cursor:pointer;
}
</style>
<script language="javascript">
function redirectPage(){
   top.location.href="/SA/welcome.html";
}
</script>
</head>
<body background="images/top.png">
<table width="100%"   border=0 >
<tr><td  width="20%" align="center"><font color="white" size="6"><%
// Remember to declare the path correctly:
// Windows: path_to_file\\file.txt
// Unix: path_to_file/file.txt
// First: Read the file
FileReader data = new FileReader(config.getServletContext().getRealPath("/")+"file.txt");
BufferedReader file = new BufferedReader(data);
String s="";
while((s = file.readLine()) != null) {
out.print(s);
}
// Close and flush everything

file.close();
data.close();
%></font></td>


  </td><td width="60%" align="center" valign="bottom"> <img src="images/logo.png" /> </h1></td>
  <td width="15%" valign="bottom" align="right"> <img src="images/logout.png" class="img1" title="Logout" onclick="javascript:redirectPage();" /><td>
  <td valign="bottom" align="right"> <img src="images/h1.png" class="img1" title="Home" onclick="javascript:redirectPage(); " height=45px /><td>
</tr>

</table>
</body>
</html>

