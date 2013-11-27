function setServer(val){
parent.rightframe.location=val;//"http://10.230.110.101:8008/dbGuru/dbs.cgi?selectedServer="+dbserver;
}

function selectNode (node,str, so)
{
var selection, range, doc, win;
if (so != 'so'){
if ((doc = node.ownerDocument) && (win = doc.defaultView) && typeof
win.getSelection != 'undefined' && typeof doc.createRange !=
'undefined' && (selection = window.getSelection()) && typeof
selection.removeAllRanges != 'undefined')
{
range = doc.createRange();
range.selectNode(node);

selection.removeAllRanges();
selection.addRange(range);
}
else if (document.body && typeof document.body.createTextRange !=
'undefined' && (range = document.body.createTextRange()))
{
range.moveToElementText(node);
range.select();
}
document.getElementById('text1').value=str;
}


}


function clearSelection ()
{
if (document.selection)
document.selection.empty();
else if (window.getSelection)
window.getSelection().removeAllRanges();
}


