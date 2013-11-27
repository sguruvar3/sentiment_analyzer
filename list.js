
function SelectSubCat(){
removeAllOptions(document.getElementById('SubCat'));
//addOption(document.getElementById("SubCat"), "", "SubCat", "");

if(document.getElementById('Category').value.toLowerCase() == 'WebCams'.toLowerCase()){
addOption(document.getElementById("SubCat"),"C600", "C600");
addOption(document.getElementById("SubCat"),"Pro9000", "Pro9000");
}
if(document.getElementById('Category').value.toLowerCase() == 'keyboard'.toLowerCase()){
addOption(document.getElementById("SubCat"),"illuminating", "illuminating");
addOption(document.getElementById("SubCat"),"diNovoMini", "diNovoMini");
}

}
function removeAllOptions(selectbox)
{
	var i;
	for(i=selectbox.options.length-1;i>=0;i--)
	{
		selectbox.remove(i);
	}
}


function addOption(selectbox, value, text )
{
	var optn = document.createElement("OPTION");
	optn.text = text;
	optn.value = value;

	selectbox.options.add(optn);
        selectbox.setAttribute("onChange", "selectedItem();" ); 
}
