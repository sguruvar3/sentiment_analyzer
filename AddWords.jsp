<html>
<head>
<script src="jsGG.js" type="text/javascript"></script>
<script src="gg.js" type="text/javascript"></script>
<style type="text/css">
.btn{
text-decoration:none;
display: block;
padding:5px 21px;
background:#5F3222;
color:#eee;
float:left;
text-align:center;
border-top:2px solid #815444;
border-right:2px solid #3d1000;
border-bottom:2px solid #3d1000;
border-left:2px solid #815444;
}
.btn:hover {
background:#a37666;
color:#000;
border-top:2px solid #815444;
border-right:2px solid #c59888;
border-bottom:2px solid #c59888;
border-left:2px solid #815444;
}
.btn1{
text-decoration:none;
display: block;
padding:5px 21px;
background:#330066;
color:#eee;
float:left;
text-align:center;
border-top:2px solid #815444;
border-right:2px solid #3d1000;
border-bottom:2px solid #3d1000;
border-left:2px solid #815444;
}
.btn1:hover {
background:#FF99CC;
color:#000;
border-top:2px solid #815444;
border-right:2px solid #c59888;
border-bottom:2px solid #c59888;
border-left:2px solid #815444;
}
</style>
<script language="javascript">
   function selectiveDisable()
   {
     if ('<%= request.getAttribute("sentence")%>' == 'COMPLETEDALL')
       {
         document.getElementById('gw').disabled=true;
         document.getElementById('nw').disabled=true;
         document.getElementById('prod').disabled=true;
         document.getElementById('text1').disabled=true;
         document.getElementById('button1').disabled=true;
         document.getElementById('button1').disabled=true;
         document.getElementById('finalStatus').style.display='block';
      }
}
   function callAjax(type) {
    var url="";
    if(type == 'prod')
     {
       url = "/SA/AjaxAddProducts?word="+document.getElementById('text1').value;
     }
    if(type == 'gw')
     {
       url = "/SA/AjaxAddPositiveWords?word="+document.getElementById('text1').value;
     }
    if(type == 'nw')
     {
       url = "/SA/AjaxAddNegativeWords?word="+document.getElementById('text1').value;
     }
     request.open("GET", url, true);
     request.onreadystatechange = updatePage1
     request.send(null);
   }


   function updatePage1() {
     if (request.readyState == 4) {
       var result = request.responseText;
       document.getElementById('result').innerHTML=result;
    }
}

</script>
</head>
<body   onload="javascript:selectiveDisable();">
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<div id="fdinalStatus" align="center"><font size="6" color="blue"><i>Add words in Real Time</i></font></div>

<div id="finalStatus" style="display:none"><font size="12"><i>All files have been processed</i></font></div>
<form name="form1" id="form1" action="/SA/AddWords" method="POST">
<table width="100%" border=1 title+"Just hover the word and Add">
 <tr>
   <td width="25%" align="right"><font size="3pt"><b>Current Sentence:</b></font></td>
   <td width="75%"><font size="4pt"><b><%= request.getAttribute("sentence_deco") %></b></font></td>
</tr>
<tr></tr>
<tr></tr>
<tr></tr>
<tr></tr>
<tr></tr>
<tr></tr>
<tr></tr>
<tr></tr>
<tr></tr>
<tr></tr>
<tr></tr>
<tr></tr>
<tr></tr>
<tr></tr>
<tr></tr>
<tr></tr>
       <tr><td width="25%" align="right"><font size="3pt"><b> Selected Word: </b></font></td><td width="75%"><input type="text" id="text1"/></td></tr>
<tr></tr>
<tr></tr>
<tr></tr>
<tr></tr>
<tr></tr>
<tr></tr>
<tr></tr>
<tr><td><width="25%"><font size="3pt"><b>Please classify this as one of below</b></font></td><td width="75%"></td></tr>
  <tr><td width="25%"></td>
     <td><table> <tr>
          <td width="25%"><input class="btn" type="button" id="gw" value="+ve word" onclick="javascript:callAjax('gw');"></td>
          <td width="25%"><input class="btn" type="button" id="nw" value="-ve word" onclick="javascript:callAjax('nw');"></td>
          <td width="25%"><input class="btn" type="button" id="prod" value="Product" onclick="javascript:callAjax('prod');"></td>
        </tr>
     </table></td></tr>
      <tr><td width="25%" align="right"><font size="3pt"><b>Processing File:</b></font></td><td width="75%"><font size="3pt"><b><%= request.getAttribute("filename") %></b></font></td></tr>
      <tr><td width="25%"></td><td width="75%" align="right"><input type="submit" class="btn1" ame="button1" id="button1" value="Next Sentence>>" /></td></tr>
    </table>
  <input type="hidden" name="filename" value='<%= request.getAttribute("filename") %>'/>
  <input type="hidden" name="sentence" value='<%= request.getAttribute("sentence") %>'/>
   <div id="result"/></div>
<br>
<br>
<br>
<br>

<fieldset style="width:350" align="center">
   <legend> Legend</legend>
    <font size="4pt"><font color="RED"> - </font>words already in <font color="RED">Negative Words</font> List</font> <br>
    <font size="4pt"><font color="BLUE"> - </font>words already in <font color="BLUE">Product/Feature Words</font> List</font><br/>
    <font size="4pt"><font color="GREEN"> - </font>words already in <font color="GREEN">Positive Words</font> List</font><br>
</fieldset>
         
</body>
</html>
