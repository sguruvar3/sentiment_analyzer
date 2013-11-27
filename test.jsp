
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Iterator"%>

<%@page import="java.util.Map"%><html>
<body>
<%
HashMap map=(HashMap)session.getAttribute("tagCloud");
Iterator keyValuePairs = map.entrySet().iterator();
while(keyValuePairs.hasNext()){
	Map.Entry entry= (Map.Entry) keyValuePairs.next();
	out.println(entry.getKey().toString());
	out.println(Integer.getInteger(entry.getValue().toString()));
}
out.print(map.size());
%>
</body>
</html>
