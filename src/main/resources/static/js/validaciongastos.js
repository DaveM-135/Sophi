function validacion(){
	
	var date = new Date();

	var day = date.getDate();
	var month = date.getMonth() + 1;
	var year = date.getFullYear();
	
	var fecha = new Object();
	
	if(month < 10){
		fecha = year.toString().concat("-0", month.toString(), "-", day.toString());
		console.log(fecha);
		console.log(`${year}-0${month}-${day}`);
	}else{
		fecha = year.toString().concat("-", month.toString(), "-", day.toString());
		console.log(fecha);
		console.log(`${year}-${month}-${day}`);
	}
	
	var input = document.querySelectorAll("#check");
	console.log(input.length);
	
	var RecVal = document.getElementById("codRecVal").value;
	var target = document.querySelectorAll("#RecVal");
	console.log(RecVal);
	
	for(var i=0 ; i<input.length ; i++){
		console.log(RecVal);
		if(input[i].checked){
			input[i].value = fecha;
			target[i].value = RecVal;
		}
		
		else{
			input[i].value = "";
			target[i].value = "";
		}
		
		console.log(target[i]);
	}
	
}