$(document).ready(function() {
	switch (new Date().getDay()){
			case 0:
				$("#da0").show();
				$("#da1").hide();
				$("#da2").hide();
				$("#da3").hide();
				$("#da4").hide();
				$("#da5").hide();
				$("#da6").hide();
				break;
			case 1:
				$("#da0").hide();
				$("#da1").show();
				$("#da2").hide();
				$("#da3").hide();
				$("#da4").hide();
				$("#da5").hide();
				$("#da6").hide();
				break;
			case 2:
				$("#da0").hide();
				$("#da1").hide();
				$("#da2").show();
				$("#da3").hide();
				$("#da4").hide();
				$("#da5").hide();
				$("#da6").hide();
				break;
			case 3:
				$("#da0").hide();
				$("#da1").hide();
				$("#da2").hide();
				$("#da3").show();
				$("#da4").hide();
				$("#da5").hide();
				$("#da6").hide();
				break;
			case 4:
				$("#da0").hide();
				$("#da1").hide();
				$("#da2").hide();
				$("#da3").hide();
				$("#da4").show();
				$("#da5").hide();
				$("#da6").hide();
				break;
			case 5:
				$("#da0").hide();
				$("#da1").hide();
				$("#da2").hide();
				$("#da3").hide();
				$("#da4").hide();
				$("#da5").show();
				$("#da6").hide();
				break;
			case 6:
				$("#da0").hide();
				$("#da1").hide();
				$("#da2").hide();
				$("#da3").hide();
				$("#da4").hide();
				$("#da5").hide();
				$("#da6").show();
				break;
		}
		
	var fh = new Date();
	formatoFechaLarga(fh);
	completaSemana(fh);
	semanaInicioFin(fh);

	$("#sigDia").click(function() {
		limpiaActive();
		formatoFechaLarga(sumarDias(fh, 1));
		completaSemana(fh);
		cargaActividadDia();
		semanaInicioFin(fh);
	});

	$("#antDia").click(function() {
		limpiaActive();
		formatoFechaLarga(sumarDias(fh, -1));
		completaSemana(fh);
		cargaActividadDia();
		semanaInicioFin(fh);
	});
	
	$("#selectProyecto").click(function() {
		cargarActividadesPrimariasProyecto();
	});
	
	
	$("a[id^='da']").click(function(){
		limpiaActive();
		$(this).addClass("active");
		
		cargaActividadDia();
		var fech = $("#semanaDias .active span").text().split("-");
		fh = new Date(+fech[2], fech[1]-1, +fech[0]);
		formatoFechaLarga(fh);
		semanaInicioFin(fh);
	});
	
	$(document).on('click', '.borrar', function (event) {
	    event.preventDefault();
	    $(this).closest('tr').remove();
	});
	
	cargaActividadDia();
	
//	cargarActividadesPrimariasProyecto();
//	filtraActPorFase();
//	altaCapHoraActividad();
	
	$("[type='number']").keypress(function (evt) {
	    evt.preventDefault();
	});

});

function valorInicio(valInicio){
	$("#fecFinReporte").attr("min",valInicio);
}

function valorFinal(valFinal){
	$("#fecInicioReporte").attr("max",valFinal);
}


function dwlPdf(){
	var fecInicioReporte = $("#fecInicioReporte").val();
	var fecFinReporte = $("#fecFinReporte").val();
	var fInicioArray = fecInicioReporte.split('-');
	var fInicio = fInicioArray[1]+'/'+fInicioArray[2]+'/'+fInicioArray[0];
	var fFinArray = fecFinReporte.split('-');
	var fFin = fFinArray[1]+'/'+fFinArray[2]+'/'+fFinArray[0];
	var rec= $("#authGetName").val();
	if(fecInicioReporte.length > 0 && fecFinReporte.length > 0){
		$.ajax({
		    type: "GET",
		    url: "/getReporteHorasCapturadasPdf",
		    data: {fInicio: fInicio, 
		    		fFin: fFin,
		    		rec:rec},
			success: function(link){
		        window.open(link);
		    }
		});
	} else {
		alert("Selecciona una fecha de inicio y fecha fin correctamente");
	}
	
	
}

function dwlXlsx(){
	var fecInicioReporte = $("#fecInicioReporte").val();
	var fecFinReporte = $("#fecFinReporte").val();
	var fInicioArray = fecInicioReporte.split('-');
	var fInicio = fInicioArray[1]+'/'+fInicioArray[2]+'/'+fInicioArray[0];
	var fFinArray = fecFinReporte.split('-');
	var fFin = fFinArray[1]+'/'+fFinArray[2]+'/'+fFinArray[0];
	var rec= $("#authGetName").val();
	if(fecInicioReporte.length > 0 && fecFinReporte.length > 0){
		$.ajax({
		    type: "GET",
		    url: "/getReporteHorasCapturadasXLSXIni",
		    data: {fInicio: fInicio, 
		    		fFin: fFin,
		    		rec:rec},
			success: function(link){
		        window.open(link);
		    }
		});
	} else {
		alert("Selecciona una fecha de inicio y fecha fin correctamente");
	}
}

function clicProyectoEdit(){
	cargarActividadesPrimariasProyectoEdit();
}

function clicProyectoCopia(){
	cargarActividadesPrimariasProyectoCopia();
}

function semanaInicioFin(fecha){

var curr = new Date(fecha);

var first = curr.getDate() - curr.getDay(); // First day is the day of the month - the day of the week 
var last = first + 6; // last day is the first day + 6 



var firstday = new Date(curr.setDate(first)).toUTCString();
var firstday2 = new Date(firstday);
//var lastday = new Date(curr.setDate(last)).toUTCString(); 
//var lastday2 = new Date(firstday2.setDate(firstday2.getDate() + 6)); 
var lastday =  new Date(firstday2.setDate(firstday2.getDate() + 6)).toUTCString();; 


$.ajax({
    type: "GET",
    url: "/horasTotalSemanaIni",
    data: {login: $("#authGetName").val() , fechaInicioSemana: firstday, fechaFinSemana: lastday },
	success: function(result){
        $("#dt").html(result);
    }
});

}

function cargaActividadDia(){
	$("#detalleHorasCapturadas").html('<div class="spinner-grow text-muted"></div>');
	var fech = $("#semanaDias .active span").text();
	var url="/cargarActividadCapturadasIni/"+$("#authGetName").val()+"/"+fech;
	$("#detalleHorasCapturadas").load(url);
}

function cargarActividadesPrimariasProyecto(){
	var url="/cargarActividadPrimariaIni/"+$("#authGetName").val()+"/"+$("#selectProyecto").val();
	$("#resultListActividadesPrimarias").load(url);
}

function clicProyectoEdit(){
	var url="/cargarActividadPrimariaEditIni/"+$("#authGetName").val()+"/"+$("#selectProyectoEdit").val();
	$("#resultListActividadesPrimariasEdit").load(url);
}

function clicProyectoCopia(){
	var url="/cargarActividadPrimariaCopiaIni"+$("#authGetName").val()+"/"+$("#selectProyectoCopia").val();
	$("#resultListActividadesPrimariasCopia").load(url);
}


function filtraActPorFase(){
	cargarActividadesSecundariasProyecto();
}

function filtraActPorFaseEdit(){
	cargarActividadesSecundariasProyectoEdit();
}

function filtraActPorFaseCopia(){
	cargarActividadesSecundariasProyectoCopia();
}

function cargarActividadesSecundariasProyecto(){
	var url="/cargarActividadSecundariaIni/"+$("#authGetName").val()+"/"+$("#selectProyecto").val()+"/"+encodeURIComponent($("#selectActividadesPrimarias").val());
	$("#resultListActividadesSecundarias").load(url);
}

function cargarActividadesSecundariasProyectoEdit(){
	var url="/cargarActividadSecundariaEditIni/"+$("#authGetName").val()+"/"+$("#selectProyectoEdit").val()+"/"+encodeURIComponent($("#selectActividadPrimariaEdit").val());
	$("#codActividad").load(url);
}

function cargarActividadesSecundariasProyectoCopia(){
	var url="/cargarActividadSecundariaCopiaIni/"+$("#authGetName").val()+"/"+$("#selectProyectoCopia").val()+"/"+encodeURIComponent($("#selectActividadPrimariaCopia").val());
	$("#codActividad").load(url);
}

function altaCapHoraActividad(){
	var fech = $("#semanaDias .active span").text();
	var url="/cargarDetActividadIni/"+$("#selectActividadesSecundarias").val()+"/"+fech;
	$("#resultDetActividades").load(url);
}

function altaCapHoraActividadNoPlan(){
	var fech = $("#semanaDias .active span").text();
	var url="/cargarDetActividadNoPlanIni/"+$("#selectActividadesSecundarias").val()+"/"+fech+"/"+$("#authGetName").val();
	$("#resultDetActividades").load(url);
}

function altaCapHoraActividadFuera(){
	var fech = $("#semanaDias .active span").text();
	var url="/cargarDetActividadFueraIni/"+$("#selectActividadesSecundarias").val()+"/"+fech+"/"+$("#authGetName").val()+"/"+$("#selectProyecto").val();
	$("#resultDetActividades").load(url);
}

function limpiaActive() {
	for (var i = 0; i <= 6; i++) {
		$("#da" + i).removeClass("active");
	}
}

function handleChange(input) {
    if (input.value < 0) input.value = 0;
    if (input.value > 24) input.value = 24;
}

async function validaForm(){
	if(!$("#descDetalleHora").val()){
		$("#descDetalleHora").addClass("alert-danger");
		$("#divDescDetalleHora").html("<small class='form-text text-danger'>Este dato es requerido</small>");
	} else if ($("#valHoraCap").val() > 0 && $("#valHoraCap").val() <= 24 && $("#valHoraCap").val().match(/(^\d*\.{0,1}\d{0,1})$/)){
	    $('#capHorasForm').submit();
	    $('#capHoraModal').modal('hide');
	    $("#detalleHorasCapturadas").html('<div class="spinner-grow text-muted"></div>');
	    await sleep(1000);
	    
	    var fech = $("#semanaDias .active span").text().split("-");
		fh = new Date(+fech[2], fech[1]-1, +fech[0]);
		semanaInicioFin(fh);
		cargaActividadDia();
	    $('#selectProyecto').val("");
		$('#selectActividadesPrimarias').val("");
		$('#selectActividadesSecundarias').val("");
		$('#descDetalleHora').val("");
		$('#valHoraCap').val("");
		$('#resultListActividadesPrimarias').html("");
		$('#resultListActividadesSecundarias').html("");
		$('#resultDetActividades').html("");
	} else {
		$("#descDetalleHora").removeClass("alert-danger");
		$("#divDescDetalleHora").html("");
		
		$("#valHoraCap").addClass("alert-danger");
		$("#divValHoraCap").html("<small class='form-text text-danger'>1-24 hrs</small>");
	}
}

async function validaFormEdit(){
	if(!$("#descDetalleHoraEdit").val()){
		$("#descDetalleHoraEdit").addClass("alert-danger");
		$("#divDescDetalleHoraEdit").html("<small class='form-text text-danger'>Este dato es requerido</small>");
	} else if ($("#valHoraCapEdit").val() > 0 && $("#valHoraCapEdit").val() <= 24 && $("#valHoraCapEdit").val().match(/(^\d*\.{0,1}\d{0,1})$/)){
		$('#formEditCapHoraActividad').submit();
		$('#capHoraModalEdit').modal('hide');
		
		 $("#detalleHorasCapturadas").html('<div class="spinner-grow text-muted"></div>');
		    await sleep(1000);
		    
		    var fech = $("#semanaDias .active span").text().split("-");
			fh = new Date(+fech[2], fech[1]-1, +fech[0]);
			semanaInicioFin(fh);
			cargaActividadDia();
		
		$('#selectProyectoEdit').val("");
		$('#selectActividadSecundariaEdit').val("");
		$('#descDetalleHoraEdit').val("");
		$('#valHoraCapEdit').val("");
	} else {
		$("#descDetalleHoraEdit").removeClass("alert-danger");
		$("#divDescDetalleHoraEdit").html("");
		
		$("#valHoraCapEdit").addClass("alert-danger");
		$("#divValHoraCapEdit").html("<small class='form-text text-danger'>1-24 hrs</small>");
	}
}

async function validaFormCopia(){
	$.ajax({
	    type: "POST",
	    url: "/formCopiaCapHoraIni",
	    data: $("#formCopiaCapHoraActividad").serialize(),
		success: function(){
			$('#capHoraModalCopiar').modal('hide');
	    }
	});
	$("#detalleHorasCapturadas").html('<div class="spinner-grow text-muted"></div>');
	await sleep(1000);
			    
	var fech = $("#semanaDias .active span").text().split("-");
	fh = new Date(+fech[2], fech[1]-1, +fech[0]);
	semanaInicioFin(fh);
	cargaActividadDia();
}

function formatoFechaLarga(fecha) {
	var meses = new Array("Enero", "Febrero", "Marzo", "Abril", "Mayo",
			"Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre",
			"Diciembre");
	var diasSemana = new Array("Domingo", "Lunes", "Martes", "Miércoles",
			"Jueves", "Viernes", "Sábado");
	var nombreFecha = diasSemana[fecha
		.getDay()]
		+ ", "
		+ fecha.getDate()
		+ " de "
		+ meses[fecha.getMonth()]
		+ " de " + fecha.getFullYear();
	document.getElementById("fechaModal").innerHTML = nombreFecha;
	document.getElementById("fechaDiaActual").innerHTML = nombreFecha;
	$("#da" + fecha.getDay()).addClass("active");
}

function sumarDias(fecha, dias) {
	fecha.setDate(fecha.getDate() + dias);
	return fecha;
}

function completaSemana(fechaActual) {
	diasArriba(fechaActual);
	diasAbajo(fechaActual);
}

function diasAbajo(fab) {
	var fecha = new Date(fab);
	for (var j = fab.getDay(); j >= 0; j--) {
		document.getElementById("d" + j).innerHTML = fecha.getDate() + "-"
				+ (fecha.getMonth() + 1) + "-" + fecha.getFullYear();
		fecha = sumarDias(fecha, -1)
	}
}

function diasArriba(far) {
	var fecha = new Date(far);
	for (var i = far.getDay(); i <= 6; i++) {
		document.getElementById("d" + i).innerHTML = fecha.getDate() + "-"
				+ (fecha.getMonth() + 1) + "-" + fecha.getFullYear();
		fecha = sumarDias(fecha, 1)
	}
}


function delCaptura(codCaptura){
	$.ajax({
	    type: "GET",
	    url: "/borrarCapHoraIni",
	    data: {codCaptura: codCaptura},
		success: function(result){
			var fech = $("#semanaDias .active span").text().split("-");
			fh = new Date(+fech[2], fech[1]-1, +fech[0]);
	        semanaInicioFin(fh);
	    }
	});
}


function editCaptura(codCaptura){
	var url="/editCapturaIni/"+codCaptura;
	$("#formEditCaptura").html('<div class="spinner-grow text-muted"></div>');
	$("#formEditCaptura").load(url);
	$('#capHoraModalEdit').modal('show');
//	$('#selectProyectoEdit').prop('selected', false);
//	$('#selectActividadSecundariaEdit').prop('selected', false);
}

function copiaCaptura(codCaptura){
	var url="/copiaCapturaIni/"+codCaptura;
	$("#formCopiarCaptura").html('<div class="spinner-grow text-muted"></div>');
	$("#formCopiarCaptura").load(url);
	$('#capHoraModalCopiar').modal('show');
}

function no_refresh(capHora){	
	$.ajax({
		type: 'POST',
		url: '/formCapHoraActividadIni',
		data: capHora,
		success: function(result){
			$('.table').html(result);
		}
	});
	return false;
}

function sleep(ms) {
	  return new Promise(resolve => setTimeout(resolve, ms));
	}