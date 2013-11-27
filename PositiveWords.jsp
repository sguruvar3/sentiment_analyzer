<html>
<head>
<link rel="stylesheet" type="text/css" href="listWords.css" />
<script src="jquery.js" type="text/javascript"></script>
<script src="jquery.scrollTo-min.js" type="text/javascript"></script>
<script language="javascript" type="text/javascript">
$(document).ready(function() {
         //below code is for high-lighting the link and scroll to particular DOM Element as well
         $(".firstUL li").each(function() {
                $(this).click(function() { //On click of any Alphabet
                        $(".firstUL li").removeClass("selected"); //Initially remove "selected" class if any
                        $(this).addClass("selected"); //Add "selected" class for the clicked one
                        elementClick = $(this).attr("id"); //get respective 'Id' for example 'a','b','c'.. etc.,
                        $(".content-container").scrollTo($("#content-for-"+elementClick), 800); //scrollTo particular DOM Element
                        $(".content-container div").css({'background-color' : '#ffffff'}); //set the background color to default, that is white
                        $(".content-container #content-for-"+elementClick).css({'background-color' : '#d2e2fc'}); //set the background color to light-blue to that div
                });
         });

         //When "Return to Top" is clicked highlight the first Alphabet that 'A' and scroll to top.
         $('.return-to-top').click(function(){
                $(".firstUL li").each(function() {
                        $(".firstUL li").removeClass("selected"); //Remove classname "selected"
                });
                $("#a").addClass("selected"); //Add a class named "selected" to the first Alphabet
                $(".content-container").scrollTo($("#content-for-a"), 800); //This is for scrolling to particular element that is "A" here...
                $(".content-container div").css({'background-color' : '#ffffff'}); //set the background color to default, that is white
                $(".content-container #content-for-a").css({'background-color' : '#d2e2fc'}); //set the background color to light-blue to that div
         });
});
</script>

</head>
<body background="images/backg_digi.gif">
<div id="finalStatus" align="center"><font size="6" color="blue"><i>Positive Words</i></font></div>

<div id="body-container">

<%= request.getAttribute("finalString") %>
</div>
</body>
</html>
