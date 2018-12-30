/**
 * 
 */

$(document).ready(function() {
	//alert("Enfin on pourra tester un peu de Ajax");
});

/*function enreg(){
	alert("ici");
}*/

function mykeyPress(e){
	//alert("bien "+$('#t').val());
	/*var keynum;
	if(window.event){
		keynum = e.keyCode;
	}
	else if(e.key){
		keynum = e.key;
	}
	
	*/
	//e.which = le code de la touche qui a été saisi. 9 = touche tabulation
	//e.key = donne en chaine de caractère la touche qui a été saisi par exemple Tab pour la touche tabulation. 
	var keynum = (e.which)?e.which:e.keyCode;
	if(keynum == 13){
		//alert("on a appuyer la touche de code "+keynum+"  et on soumet ainsi la valeur: "+$('#t').val());
	}
	/*alert("keynum == "+keynum);
	if(keynum == "Tab") alert("tabulation");
	var charStr = String.fromCharCode(keynum);
	alert("charStr == "+charStr);*/
	
}

function returnfalseifTab(event){
	alert("on key press");
}

//$(function() {
	/*function submitClasseConcerne(){
		alert("je vous comprend tres bien");*/
		//var text = document.getElementById('idClasse').selectedIndex;
		/*var text = $('#idClasse option:selected').val();
		alert("texte== "+text);*/
		/*
		$('#form').submit();
	}*/
	
	/*function modifierLienRecherche(){
		var text = $('#idClasse option:selected').val();
		var href= $('#lienRecherche').attr('href');
		alert("idClasse select== "+text+" valeurHref== "+href);
	}*/
	
//});
