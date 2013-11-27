<html>
<head>
<script src="jquery.js" type="text/javascript"></script>
<script src="jsGG.js" type="text/javascript"></script>
<script type="text/javascript" charset="utf-8">
var headline_count;
var headline_interval;
var old_headline = 0;
var current_headline=0;

$(document).ready(function(){
  headline_count = $("div.headline").size();
  $("div.headline:eq("+current_headline+")").css('top','5px');

  headline_interval = setInterval(headline_rotate,5000); //time in milliseconds
  $('#scrollup').hover(function() {
    clearInterval(headline_interval);
  }, function() {
    headline_interval = setInterval(headline_rotate,5000); //time in milliseconds
    headline_rotate();
  });
});

function headline_rotate() {
  current_headline = (old_headline + 1) % headline_count; //remainder will always equal old_headline until it reaches headline_count - at which point it becomes zero. clock arithmetic
  $("div.headline:eq(" + old_headline + ")").animate({top: -205},"slow", function() {
    $(this).css('top','210px');
    });
  $("div.headline:eq(" + current_headline + ")").show().animate({top: 5},"slow");
  old_headline = current_headline;
}
function loadUser()
{
var user='<%= session.getAttribute("SessionUser")%>';
if (user=='Admin'){
 document.getElementById("dynaLoad").innerHTML='<img src="./images/admin1.png"/><font size="2pt" color="slateGray">Admin</font>';
 document.getElementById('addWordsAdmin').innerHTML='<div id=siva><input class="b1" type="button" name="b3" value="Add Words" onclick='+'"setServer('+"'/SA/AddWords'"+')"></div>';
}
if (user=='Guest'){
 document.getElementById('dynaLoad').innerHTML='<td><img src="images/guest.png"/> <font size="2pt" color="slateGray">Guest</font></td>';
}
}



</script>
	<style type="text/css" media="screen">
	 #scrollup {
	   position: relative;
	   overflow: hidden;
	   border: 0px solid #000;
	   height: 180px;
	   width: 100px
	 }
	 .headline {
	   position: absolute;
	   top: 210px;
     left: 5px;
	   height: 175px;
	   width:90px;
           color: #3300FF; 
	 }
	</style>


<style type="text/css">
#siva input.b1{
font-family: helvetica, verdana, arial;
font-size: 10pt;
color:#0000FF;
border:#fff 0px solid;
text-decoration: underline;
cursor: hand;
Background-Color:#FFFFFF;
}



</style>


</head>
<body   onload="javascript:loadUser();"><form name="gg"><table>
<tr><td><div id="dynaLoad"><div></td></tr>
<tr>
<td><font color="#336699"><b><font size="2pt">Reports</font></b></font></td></tr>
<tr><td><div id=siva><input class="b1" type="button" name="b1" value="Products" onclick="setServer('/SA/Links.jsp')"></div></td></tr>
<td><font color="#336699"><b><font size="2pt">Opinion Words</font></b></font></td></tr>
<tr><td><div id=siva><input class="b1" type="button" name="b2" value="--> +ve Words" onclick="setServer('/SA/PositiveWords')"></div></td></tr>
<tr><td><div id=siva><input class="b1" type="button" name="b3" value="--> -ve Words" onclick="setServer('/SA/NegativeWords')"></div></td></tr>
<tr><td><div id=siva><input class="b1" type="button" name="b3" value="--> Features" onclick="setServer('/SA/ProductWords')"></div></td></tr>
<tr><td><div id="addWordsAdmin"></div></td></tr>
<tr><td></td></tr>
<tr><td></td></tr>
<tr><td></td></tr>
<tr><td></td></tr>
<tr><td></td></tr>
<tr><td></td></tr>
<tr><td></td></tr>
<tr><td></td></tr>
<tr><td></td></tr>
</td></tr>
<tr><td>
<div id="scrollup">
  <div class="headline">
  </div>

</div>

<tr><td></td></tr>
<tr><td></td></tr>
<tr><td></td></tr>
<tr><td></td></tr>
<tr><td></td></tr>
<tr><td></td></tr>
<tr><td></td></tr>
<tr><td></td></tr>
<tr><td></td></tr>
<tr><td></td></tr>
<tr><td></td></tr>
<tr><td></td></tr>
<tr><td></td></tr>
<tr><td></td></tr>
<tr><td></td></tr>
<tr><td></td></tr>
<tr><td></td></tr>
</td></tr>
</table>
 
</form></body></html>
