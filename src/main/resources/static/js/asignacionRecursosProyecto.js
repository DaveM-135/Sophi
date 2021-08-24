function enviarAsignacionRecurso(){
//  var items = $("#list2 input:checked:not('.all')");
//  var items = $("#list2 input[type='checkbox']");
  var items = $("#select2 option");
  var ids=[];
  var n = items.length;
	if (n > 0) {
		items.each(function(idx, item) {
			var choice = $(item);
			var rec = choice.attr('value');
			ids[idx]= rec;
		});
		asignaRecursos(ids);
	}
}

function cargaAsignacionRecursosProyecto(){
	var url="/recursosDispProyecto/"+$('#codProyecto').val();
	$("#contentAsignacionRecursoProyecto").load(url);
}

function asignaRecursos(ids){
	document.getElementById("aceptarAsignacionBtn").disabled = true;
//	document.getElementById("aceptarAsignacionBtnName").innerHTML = 'Guardando...';
console.log(ids);
	$.ajax({
		url: "/guardaAsignacionRecursosProyecto",
		data: {ids:ids,
			   codProyecto:$('#codProyecto').val()},
		success: function(){
			document.getElementById("aceptarAsignacionBtn").disabled = false;
			document.getElementById("aceptarAsignacionBtnName").innerHTML = 'Aceptar';
			//location.reload();
			$("#modalAsignacion").modal('hide');
		}
	});
}

