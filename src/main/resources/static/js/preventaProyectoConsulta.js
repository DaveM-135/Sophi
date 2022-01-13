$(document).ready(function() {
	let anio = new Date().getYear()+1900;
	let mes = new Date().getMonth()+1;
	let dia = new Date().getDate();

	mes = (mes.length !== 1) ? '0'+mes:mes;
	dia = (dia.length !== 1) ? '0'+dia:dia;

	let sysdate = anio+'-'+mes+'-'+dia;
	
	if($("#cancelado").attr("checked")){
		$("input").prop("disabled", true);
		$("textarea").prop("disabled", true);
		$("select").prop("disabled", true);
	}

	if($('#fechaFin').val() > sysdate){
		$('#modalProyectoACerrado .modal-body').html('<p>Cambiar&aacute;s el estatus del proyecto a cerrado, esta operaci&oacute;n no se podr&aacute; recuperar. ¿Est&aacute;s de acuerdo?</p><p>Cerrar&aacute;s el proyecto antes de su fecha de finalización, ¿Tambi&eacute; est&aacute;s de acuerdo?</p>');
	}
	
	$("#presupuesto").on({
		"focus": function(event) {
					$(event.target).select();
				},
		"keyup": function(event) {
					$(event.target).val(function(index, value) {
						return value.replace(/\D/g, "")
						.replace(/([0-9])([0-9]{2})$/, '$1.$2')
						.replace(/\B(?=(\d{3})+(?!\d)\.?)/g, ",");
					});
		  		}
	});
	
	$("#precio").on({
		"focus": function(event) {
					$(event.target).select();
				},
		"keyup": function(event) {
					$(event.target).val(function(index, value) {
						return value.replace(/\D/g, "")
						.replace(/([0-9])([0-9]{2})$/, '$1.$2')
						.replace(/\B(?=(\d{3})+(?!\d)\.?)/g, ",");
					});
		  		}
	});
	
	$("#gasto").on({
		"focus": function(event) {
					$(event.target).select();
				},
		"keyup": function(event) {
					$(event.target).val(function(index, value) {
						return value.replace(/\D/g, "")
						.replace(/([0-9])([0-9]{2})$/, '$1.$2')
						.replace(/\B(?=(\d{3})+(?!\d)\.?)/g, ",");
					});
					calculoPrecio();
		  		}
	});
	
	$("#costoProyecto").on({
		"focus": function(event) {
					$(event.target).select();
				},
		"keyup": function(event) {
					$(event.target).val(function(index, value) {
						return value.replace(/\D/g, "")
						.replace(/([0-9])([0-9]{2})$/, '$1.$2')
						.replace(/\B(?=(\d{3})+(?!\d)\.?)/g, ",");
					});
		  		}
	});
	
	$("#thorasProyecto").on({
		"focus": function(event) {
					$(event.target).select();
				},
		"keyup": function(event) {
					$(event.target).val(function(index, value) {
						return value.replace(/\D/g, "")
						.replace(/([0-9])([0-9]{2})$/, '$1.$2')
						.replace(/\B(?=(\d{3})+(?!\d)\.?)/g, ",");
					});
		  		}
	});
	
	
	
	$("#totalHoras").on({
		"focus": function(event) {
					$(event.target).select();
				},
		"keyup": function(event) {
					$(event.target).val(function(index, value) {
						return value.replace(/\D/g, "")
						.replace(/([0-9])([0-9]{2})$/, '$1.$2')
						.replace(/\B(?=(\d{3})+(?!\d)\.?)/g, ",");
					});
		  		}
	});
	
	$("#btnPreventaCancelado").hide();

	$('#undo_redo').multiselect();
	
	var checkBox = document.getElementById("proyecto");
	
	if (checkBox.checked == false){
    	$('#pr').hide();
    	$('#pm').hide();		
		$('#ig').hide();
		$('#pc').hide();
		$('#thp').hide();
		$('#cp').hide();
		$('#pp').hide();
		$('#thv').hide();
		$('#fi').hide();
		$('#ff').hide();
		$("#evaluado").hide();
		$('#complementoProyecto').hide();
	} 
	
	var checkBoxCierre = document.getElementById("cierre");
	
	if (checkBoxCierre.checked == true){
		$('#btnRegresarCierre').show('500');
		$('#pr').show();
		$('#pc').show();
		$('#pm').show();		
		$('#ig').show();		
		$('#thp').show();
		$('#cp').show();
		$('#pp').show();
		$('#thv').show();
		$('#fi').show();
		$('#ff').show();
		document.getElementById("fi").disabled = true;
		$('#complementoProyecto').show('500');
	} else {
		$('#btnRegresarCierre').hide();
	}
	
	
  
	$( "#aceptarProyecto" ).click(function() {
		console.log("entra a cambiar a proyecto");
		$( "#preventa" ).prop( "disabled", true );
		$('#pr').show();
		$('#pc').show();
		$('#pm').show();		
		$('#ig').show('500');	
		$('#thp').show('500');
		$('#cp').show('500');
		$('#pp').show('500');
		$('#thv').show('500');
		$('#fi').show('500');
		$('#ff').show('500');
		$("#evaluado").show();
		$("#presupuesto").hide();
		//$('#complementoProyecto').show('500');
		$('#codEstatusProyecto').val(2);
		$('#btnEnviar').val("Guardar y continuar");
 	});
 	
 	$("#aceptarCancelado").click(function(){
		console.log("Entra a cancelar la preventa");
		$( "#preventa" ).prop( "disabled", true );
		$( "#proyecto" ).prop( "disabled", true );
		$( "#cierre" ).prop( "disabled", true );
		$( "#cancelado" ).prop( "disabled", false);
		$("#btnEnviar").val("Guardar y Salir");
	});
	
	$( "#aceptarCierre" ).click(function() {
		$( "#proyecto" ).prop( "disabled", true );
		//$('#complementoProyecto').show('500');
		$('#codEstatusProyecto').val(3);
		$('#btnEnviar').hide();
		$('#btnRegresarProyecto').hide();
		$('#btnRegresarCierre').show('500');
 	});
  
	$("#cancelarProyecto").click(function() {
		$( "#preventa" ).prop( "checked", true );
	});
	
	$( "#cancelarCancelado" ).click(function() {
		$( "#preventa" ).prop( "checked", true );
	});
	
	$( "#cancelarCierre" ).click(function() {
		$( "#proyecto" ).prop( "checked", true );
	});
	
	$("#fechaInicio").change(function(){
		  var fehchaMin = $("#fechaInicio").val();
		  document.getElementById('fechaFin').setAttribute("min", fehchaMin);
	});
	
  
	$("#nombreProyecto").keydown(function(event){
		var codigo = $("#valCliente").val();
		codigo = codigo +"-"+$("#areaComercial").val();
		codigo= codigo + "-" +$("#nombreProyecto").val().substring(-1,3);
		$("#codigoProyecto").val(codigo);
	}); 
	
//	$( "#areaComercial" ).click(function() {
//		var codigo = $("#valCliente").val();
//		codigo = codigo +"-"+$("#areaComercial").val();
//		codigo= codigo + "-" +$("#nombreProyecto").val().substring(-1,3);
//		$("#codigoProyecto").val(codigo);
//	});
	
	$( "#guardarInfra" ).click(function() {
		var codCliente=$("#cliente").val();//$('#codCliente').val();
		var codPRoyecto=$('#codProyecto').val();
		var codEstatusProyecto=$("#proyecto").val();//$('#codEstatusProyecto').val();
		//alert("codCliente "+codCliente);
		//alert("codPRoyecto "+codPRoyecto);
		//alert("codEstatusProyecto "+codEstatusProyecto);
		var texto="";
		$.ajax({ 
			url: "/eliminarInfra/" + codPRoyecto+ "/" +codEstatusProyecto+ "/" + codCliente,
        	success: function(resE){
        		//alert("exito ");
        		$("#undo_redo_to option").each(function(){
					//alert('opcion '+$(this).text()+' valor '+ $(this).attr('value'));
					texto=texto+$(this).text()+", ";
					$.ajax({ 
						url: "/agregarInfra/" + codCliente + "/" + codPRoyecto + "/" + $(this).attr('value') + "/" +codEstatusProyecto,
            			success: function(res){
                			console.log(res)
            			}
        			});
				});
        		
				$( "#tecnologia" ).val(texto);
        	}
    	})
	});
	
	$( "#contacto" ).change(function() {
		var codCliente=$('#codCliente').val();
		var codPRoyecto=$('#codProyecto').val();
		var codEstatusProyecto=$('#codEstatusProyecto').val();
		var codContacto=$('#contacto').val();
		$.ajax({ 
			url: "/eliminarContacto/" + codPRoyecto+"/"+codEstatusProyecto+"/"+codCliente,
        	success: function(resE){
        		//alert("elimina contacto");
        		$.ajax({ 
					url: "/guardarContactoProyecto/" + codPRoyecto+"/"+codContacto+"/"+codEstatusProyecto+"/"+codCliente,
        			success: function(resE){
        				console.log(resE);
        				//alert("agregaContacto");
        				//location.href = '/preventaProyectoConsulta/'+codPRoyecto;
        			}
    			});
        		
        	}
    	})
	});
	
	calculoPrecio();
	
	$("#layout").hide();
	
	$("#muestraLayout").click(function(){
		$("#layout").show();
	});
	
	$("#ocultarLayout").click(function(){
		$("#layout").hide();
	});
	
	$("#NoCargarPlan").click(function(){
		$("#layout").hide();
	});
	
	$("#cerrarModalPlan").click(function(){
		$("#layout").hide();
	});

});


function calculoPrecio(){
	var costo = parse($("#costoProyecto").val());
	var gasto = parse($("#gasto").val());
	var riesgoPorcentaje = parse($("#riesgo").val());
	var margenPorcentaje = parse($("#margen").val());
	
	var costogasto = gasto + costo;
	var riesgoValor = riesgoPorcentaje / 100 * costogasto ;
	var precio = costogasto + riesgoValor;
	var margenValor = margenPorcentaje / 100 * precio;
	var precioTotal = precio + margenValor;
	
	var precioPropuesto = parse($("#precio").val()) + margenPorcentaje / 100 * parse($("#precio").val()) + riesgoPorcentaje / 100 * parse($("#precio").val());
	
//	console.log(precioTotal.toLocaleString("en-US", {minimumFractionDigits: 2}));
	if(!isNaN(precioPropuesto)){
		$("#totalPropuesto").html("Precio propuesto: $" + precioPropuesto.toLocaleString("en-US", {minimumFractionDigits: 2})+" (MXN)");
	} else {
		$("#totalPropuesto").html("Precio propuesto: $0.00 (MXN)");
	}
	
	if (!isNaN(precioTotal)) {
		$("#totalProyecto").html("Precio piso: $" + precioTotal.toLocaleString("en-US", {minimumFractionDigits: 2})+" (MXN)");
	} else {
		$("#totalProyecto").html("Precio piso: $0.00 (MXN)");
	}
}

function parse(texto){
	var valor = parseFloat(texto.replace(/,/g, ''));
	return valor;
}


function guardarTodo(){
	//alert("Entra a funcion");
	var codCliente=$('#codCliente').val();
	var codPRoyecto=$('#codProyecto').val();
	var codEstatusProyecto;
	
	var checkBox = document.getElementById("proyecto");
	var checkCancelado = $("#cancelado");
	if (checkBox.checked === false){
    	codEstatusProyecto=1;
	}else if(checkBox.checked === true){
		codEstatusProyecto=2;
	}else if(checkCancelado.checked === true){
		codEstatusProyecto=4;
	}
	
	var areaComercial=$('#areaComercial').val();
	var nombreProyecto=$('#nombreProyecto').val();
	var fechaInicio=$('#fechaInicio').val();
	var fechaFin=$('#fechaFin').val();
	var codigoProyecto=$('#codigoProyecto').val();
	var presupuesto=$('#presupuesto').val();
	var riesgo;
	var tipoFacturacion=$('#tipoFacturacion').val();
	var totalHoras=$('#totalHoras').val();
	var thorasProyecto;
	var tipoProyecto=$('#tipoProyecto').val();
	var precio=$('#precio').val();
	var costoProyecto;
	var clasificacionProyecto=$('#clasificacionProyecto').val();
	//alert("estatus "+codEstatusProyecto);
	if($('#riesgo').val()==""){
		riesgo=0;
	}else{
		riesgo=$('#riesgo').val();
	}
	if($('#thorasProyecto').val()==""){
		thorasProyecto=0;
	}else{
		thorasProyecto=$('#thorasProyecto').val();
	}
	if($('#costoProyecto').val()==""){
		costoProyecto=0;
	}else{
		costoProyecto=$('#costoProyecto').val();
	}
	
	
	$.ajax({
		url:'/guardaP',
		datatype:'json',
		type:'get',
		data:{codCliente:codCliente,
			codPRoyecto:codPRoyecto,
			codEstatusProyecto:codEstatusProyecto,
			areaComercial:areaComercial,
			nombreProyecto:nombreProyecto,
			fechaInicio:fechaInicio,
			fechaFin:fechaFin,
			codigoProyecto:codigoProyecto,
			presupuesto:presupuesto,
			riesgo:riesgo,
			tipoFacturacion:tipoFacturacion,
			totalHoras:totalHoras,
			thorasProyecto:thorasProyecto,
			tipoProyecto:tipoProyecto,
			precio:precio,
			costoProyecto:costoProyecto,
			clasificacionProyecto:clasificacionProyecto
		},
		success: function(data){
			alert("Todo ok");
		}
	});
}


function cargaAreasComerciales(){
	var cliente=$("#cliente").val();
		//alert("cliente "+cliente);
		$.ajax({ 
			url: "/areaComercialCliente/"+cliente,
        	success: function(res){
        		//alert(res);
        		document.getElementById("areaComercial").innerHTML = "";
        		document.getElementById("areaComercial").innerHTML = res;
        	}
    	});
}

function cargaContactos(){
	var cliente=$("#cliente").val();
		//alert("cliente "+cliente);
		$.ajax({ 
			url: "/contactoCliente/"+cliente,
        	success: function(res){
        		//alert(res);
        		document.getElementById("contacto").innerHTML = "";
        		document.getElementById("contacto").innerHTML = res;
        	}
    	});
}

var _gaq = _gaq || [];
  _gaq.push(['_setAccount', 'UA-36251023-1']);
  _gaq.push(['_setDomainName', 'jqueryscript.net']);
  _gaq.push(['_trackPageview']);

  (function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
  })();