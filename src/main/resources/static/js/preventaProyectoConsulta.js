$(document).ready(function() {
	
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

	$('#undo_redo').multiselect();
	
	var checkBox = document.getElementById("proyecto");
	
	if (checkBox.checked == false){
    	$('#pr').hide();
		$('#thp').hide();
		$('#cp').hide();
		$('#pp').hide();
		$('#thv').hide();
		$('#fi').hide();
		$('#ff').hide();
		$('#complementoProyecto').hide();
	} 
	
	var checkBoxCierre = document.getElementById("cierre");
	
	if (checkBoxCierre.checked == true){
		$('#btnRegresarCierre').show('500');
		$('#pr').show();
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
		$( "#preventa" ).prop( "disabled", true );
		$('#pr').show('500');
		$('#thp').show('500');
		$('#cp').show('500');
		$('#pp').show('500');
		$('#thv').show('500');
		$('#fi').show('500');
		$('#ff').show('500');
		//$('#complementoProyecto').show('500');
		$('#codEstatusProyecto').val(2);
		$('#btnEnviar').val("Guardar y continuar");
 	});
	
	$( "#aceptarCierre" ).click(function() {
		$( "#proyecto" ).prop( "disabled", true );
		//$('#complementoProyecto').show('500');
		$('#codEstatusProyecto').val(3);
		$('#btnEnviar').hide();
		$('#btnRegresarProyecto').hide();
		$('#btnRegresarCierre').show('500');
 	});
  
	$( "#cancelarProyecto" ).click(function() {
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
		var codCliente=$('#codCliente').val();
		var codPRoyecto=$('#codProyecto').val();
		var codEstatusProyecto=$('#codEstatusProyecto').val();
		alert("codCliente "+codCliente);
		alert("codPRoyecto "+codPRoyecto);
		alert("codEstatusProyecto "+codEstatusProyecto);
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
	
	

});
function guardarTodo(){
	//alert("Entra a funcion");
	var codCliente=$('#codCliente').val();
	var codPRoyecto=$('#codProyecto').val();
	var codEstatusProyecto;
	
	var checkBox = document.getElementById("proyecto");
	if (checkBox.checked == false){
		//alert("1");
    	var codEstatusProyecto=1;
	}else{
		//alert("2");
		var codEstatusProyecto=2;
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