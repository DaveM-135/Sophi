$(document).ready(function() {
	
	calculaPromedioGeneralInit();
	
	$(".porcentajeCompetencia").on('keyup', function (e) {
	    if (e.key === 'Enter' || e.keyCode === 13) {
	    	registrar(this);
	    }
	});
	

	
});

//evaluacion unica
function registrar(inp){
	var btn = inp.id;
	var id = btn.substring(5);
	var resultado = $("#eval_" + id).val();
	if(resultado.length == 0){
		alert("No hay evaluación");
	} else {
		$(inp).parent().parent().parent().parent().next().find('.porcentajeCompetencia').focus();  
		$.ajax({
		    url: '/guardarRespuestaCompetencia',
		    type: 'GET',
		    data:{ arc : id,
		    	resultado : resultado
		    },
		    success: function(result) {
	        	if(result == "1"){
	        		$("#save_" + id).removeClass("pendiente");
	        		$("#save_" + id).addClass("ok");
	        		habilitaEnvio();
	        	} else {
	        		$("#save_" + id).removeClass("pendiente");
	        		$("#save_" + id).addClass("nok");
	        	}
	          }
		});
	}
}

function validarCalificacion(inp){
	var id = inp.substring(5);
	var resultado = parseInt($("#eval_"+id).val(), 10);
	if(resultado > 100){
		$("#eval_"+id).val(100);
	}
	calculaPromedioGeneral();
	$("#save_" + id).addClass("pendiente"); 
	$("#save_" + id).removeClass("ok"); 
	habilitaEnvio();
}

function registrarMeta(btn){
	var id = btn.substring(5);
	var resultado = $("#eval_" + id).val();
	if(resultado.length == 0){
		alert("No hay evaluación");
	} else {
		$.ajax({
		    url: '/guardarRespuestaMeta',
		    type: 'GET',
		    data:{ erm : id,
		    	resultado : resultado
		    },
		    success: function(result) {
	        	if(result == "1"){
	        		$("#save_" + id).removeClass("pendiente");
	        		$("#save_" + id).addClass("ok");
	        		habilitaEnvio();
	        	} else {
	        		$("#save_" + id).removeClass("pendiente");
	        		$("#save_" + id).addClass("nok");
	        	}
	          }
		});
	}
}

function validarCalificacionMeta(inp){
	var id = inp.substring(5);
	var resultado = parseInt($("#eval_"+id).val(), 10);
	if(resultado > 100){
		$("#eval_"+id).val(100);
	}
	$("#save_" + id).addClass("pendiente"); 
	$("#save_" + id).removeClass("ok"); 
}

function calculaPromedioGeneralInit(){
	var suma = 0;
	var totalReactivos= $("input[name='porcentajeCompetencia[]']").length;
	$("input[name='porcentajeCompetencia[]']").each(function() {
		if((this.value).length != 0){
		suma += parseInt(this.value, 10);
		var id = (this.id).substring(5);
		$("#save_" + id).addClass("ok");
		}
	}); 
	var promedioGeneral = suma / totalReactivos;
	$("#promedioGeneralHidden").val(promedioGeneral.toFixed(2));
	$("#promedioGeneral").html('Resultado: ' + promedioGeneral.toFixed(2) + '%');
	habilitaEnvio();
}

function calculaPromedioGeneral(){
	var suma = 0;
	var totalReactivos= $("input[name='porcentajeCompetencia[]']").length;
	$("input[name='porcentajeCompetencia[]']").each(function() {
		if((this.value).length != 0){
		suma += parseInt(this.value, 10);
		}
	}); 
	var promedioGeneral = suma / totalReactivos;
	$("#promedioGeneralHidden").val(promedioGeneral.toFixed(2));
	$("#promedioGeneral").html('Resultado: ' + promedioGeneral.toFixed(2) + '%');
}

function habilitaEnvio(){
	if ($("input[name='porcentajeCompetencia[]']").length == $(".ok").length){
		$("#btnCerrarAutoevaluacion").removeClass("d-none");
	} else {
		$("#btnCerrarAutoevaluacion").addClass("d-none");
	}
}

function cerrarOkAutoevaluacion(){
	$.ajax({
	    url: '/cerrarAutoevaluacion',
	    type: 'GET',
	    data: { re: $("#codRecursoEvaluacion").val(),
	    	pca: $("#promedioGeneralHidden").val()} ,
	    success: function(result) {
        	window.location.href = '/misEvaluaciones';
          }
	}); 
}


function cerrarAutoevaluacion(){
	var url = "/getLideresEvaluacion/?re=" + $("#codRecursoEvaluacion").val();
	$("#divModalCierreEvaluacion").load(url, function(){
		$("#modalCierreAutoevaluacion").modal("toggle");
	});
}

function cerrarEvaluacion(){
	if (confirm('\xbfCerrar la evaluación?')) {
	$.ajax({
	    url: '/cerrarEvaluacion',
	    type: 'GET',
	    data: { re: $("#codRecursoEvaluacion").val(),
	    	pca: $("#promedioGeneralHidden").val()} ,
	    success: function(result) {
        	window.location.href = '/misEvaluados';
          }
	}); 
	}
}

function verDetalleMetas(id){
	jsShowWindowLoad();
	var codEvaluacion = id.split("-")[0];
	var codRecurso = id.split("-")[1];
	url="/getRespuestasMetas/?re="+ codRecurso + "&ev=" + codEvaluacion;
	$("#divModalDetalleMetas").load(url, function(){
		$("#modalDetalleMetas").modal("toggle");
		jsRemoveWindowLoad();
	});
}

function verDetalleMetasEdicion(id){
	jsShowWindowLoad();
	var codEvaluacion = id.split("-")[0];
	var codRecurso = id.split("-")[1];
	url="/getRespuestasMetasEdicion/?re="+ codRecurso + "&ev=" + codEvaluacion;
	$("#divModalDetalleMetas").load(url, function(){
		$("#modalDetalleMetasEdicion").modal("toggle");
		jsRemoveWindowLoad();
	});
}

function actualizarDetalleMetas(){
	jsShowWindowLoad();
	var r = $("#re").val();
	var ce = $("#ev").val();
	$.ajax({
	    url: '/getPorcentajeMetasRecurso',
	    type: 'GET',
	    data: { r:r,
	    	ce: ce} ,
	    success: function(result) {
        	console.log(result.toFixed(1));
        	$("#modalDetalleMetasEdicion").modal("toggle");
        	$("#"+ce+"-"+r).html(result.toFixed(1)+"%");
        	jsRemoveWindowLoad();
          }
	}); 
}

// 

function verDetalleCompetencias(id){
	jsShowWindowLoad();
	var codEvaluacion = id.split("-")[0];
	var codRecurso = id.split("-")[1];
	console.log("evaluacion des: " + id.split("-")[0]);
	console.log("codRecurso: " +id.split("-")[1]);
	url="/getRespuestasEvaluacion/?re="+ codRecurso + "&ev=" + codEvaluacion;
	$("#divModalDetalleRespuestas").load(url, function(){
		$("#modalDetalleRespuestas").modal("toggle");
		jsRemoveWindowLoad();
	});
}

// Admin de evaluaciones 

function actualizarPage(){
	jsShowWindowLoad();
	window.location.reload();
}

function verDetalleEvaluacion(cod){
	jsShowWindowLoad();
	var url = "/verDetalleEvaluacion/?e="+cod;
	$("#divDetalleEvaluacion").load(url, function (){
		$("#modalDetalleEvaluacion").modal('toggle');
		jsRemoveWindowLoad();
	});
}

function descargarResultados(id){
	var id_array = id.split('-');
	var id_eval = id_array[0];
	var id_recurso = id_array[1];

	$.ajax({
		type: "GET",
		url: "/descargarResultadosEvalDesempeno",
		data: {codEvalDesc: id_eval, codRecurso: id_recurso},
		success: function(link){
			window.open(link);
		}
	});
}

function verEvaluadores(cod){
	jsShowWindowLoad();
	var ids = cod.split("-");
	var url = "/verEvaluadores/?e="+ids[0]+"&r="+ids[1];
	$("#divEvaluadores").load(url, function (){
		$("#modalEvaluadoresRecurso").modal('toggle');
		jsRemoveWindowLoad();
	});
}

function agregarEvaluador(){
	var codRecursoEvaluador = $("#selectPosiblesEvaluadores").val();
	var nombreRecurso =  $("#selectPosiblesEvaluadores option:selected").text();
	console.log(codRecursoEvaluador + " " + nombreRecurso);
	
	if(itemsHelper.hasEvaluador(codRecursoEvaluador)){
		alert("YA FUE AGREGADO");
		return false;
	} else {
		var input = "<tr><td>"+
			"<input type='checkbox' class='chkEvaluador' name='chkEvaluador' value='" +codRecursoEvaluador+ "' checked='checked'/>&nbsp;"+
			"<span>"+nombreRecurso+"</span>"+
			"</td></tr>";
		 $("#tableEvaluadoresRecurso tbody").append(input);
	}
}

function guardarEvaluadores(){
	
	var ids = [];
	$('input[name="chkEvaluador"]').each(function(){
		if ($(this).prop('checked')){
			ids.push($(this).val());
		}
	});
	if(ids.length == 0) {
		alert('Seleccione al menos un evaluador');
	} else {
		if (confirm('Se modificaran los evaluadores, \xbfEstás seguro?')) {
			jsShowWindowLoad();
			$.ajax({
			    url: '/modificarEvaluadores',
			    type: 'GET',
			    data: { evaluadores: ids,
			    	re: $("#re").val(),
			    	ev: $("#ev").val()} ,
			    success: function(result) {
			    	$("#modalEvaluadoresRecurso").modal('toggle');
			    	$("#modalDetalleEvaluacion").modal('toggle');
			    	verDetalleEvaluacion($("#ev").val());
		          }
			}); 
		}
	}
}


var itemsHelper = {
		hasEvaluador: function(id){
			var resultado = false;
			$('input[name="chkEvaluador"]').each(function(){
				if (parseInt(id)==parseInt($(this).val())){
					resultado = true;
				}
			});
			return resultado;
		}	
	}


function nuevaEvaluacion(){
	var url="/nuevaEvaluacion";
	$("#divNuevaEvaluacion").load(url, function (){
		$("#tablaCandidatos").hide();
		$("#validacionNombre").hide();
		$("#validacionInicioPeriodo").hide();
		$("#validacionFinPeriodo").hide();
		$("#modalNuevaEvaluacion").modal('toggle');
	});
}

function guardarEvaluacion(){
	var nombre = $("#descEvaluacionDesempeno").val();
	var pInicio = $("#fecPeriodoInicio").val();
	var pFin = $("#fecPeriodoFin").val();
	var err= 0;
	
	if(nombre.length == 0){
		err++;
		$("#validacionNombre").show();
	} else {
		$("#validacionNombre").hide();
	}
	
	if(pInicio.length == 0){
		err++;
		$("#validacionInicioPeriodo").show();
	} else {
		$("#validacionInicioPeriodo").hide();
	}
	
	if(pFin.length == 0){
		err++;
		$("#validacionFinPeriodo").show();
	} else {
		$("#validacionFinPeriodo").hide();
	}
	
	if($("#inicioEvaluacion").prop('checked')) {
		if($(".chkRecurso:checked").length == 0){
			err++;
			alert("No hay recursos seleccionados");
		}
	}
	
	if (err == 0){
		if($("#inicioEvaluacion").prop('checked')) {
			if (confirm('\xbfDeseas iniciar el periodo de evaluación?\nValida nuevamente la selección de recursos.\nEsta tarea no se podrá revertir.')) {
				jsShowWindowLoad();
				$("#formularioEvaluacion").submit();
			}
		} else {
			jsShowWindowLoad();
			$("#formularioEvaluacion").submit();
		}
		
	}
}

function editarEvaluacion(cod){
	var url = "/editarDetalleEvaluacion/?e="+cod;
	$("#divNuevaEvaluacion").load(url, function (){
		$("#tablaCandidatos").hide();
		$("#validacionNombre").hide();
		$("#validacionInicioPeriodo").hide();
		$("#validacionFinPeriodo").hide();
		$("#modalNuevaEvaluacion").modal('toggle');
	});
}

function borrarEvaluacion(cod){
	if (confirm('\xbfDeseas borrar la evaluación?')) {
		$.ajax({
		    url: '/borrarEvaluacion',
		    type: 'GET',
		    data: { e: cod} ,
		    success: function(result) {
	        	location.reload();
	          }
		}); 
		}
}

