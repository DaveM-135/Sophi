$(document).ready(function() {

	$('#undo_redo').multiselect();
	$('#pr').hide();
	$('#thp').hide();
	$('#cp').hide();
	$( "#preventa" ).prop( "checked", true );
  
	$( "#aceptarProyecto" ).click(function() {
		$( "#preventa" ).prop( "disabled", true );
		$('#pr').show('500');
		$('#thp').show('500');
		$('#cp').show('500');
 	});
  
	$( "#cancelarProyecto" ).click(function() {
		$( "#preventa" ).prop( "checked", true );
	});
  
	$("#nombreProyecto").keydown(function(event){
		var codigo = $("#cliente").val();
		codigo = codigo +"-"+$("#areaComercial").val();
		codigo= codigo + "-" +$("#nombreProyecto").val().substring(-1,3);
		$("#codigoProyecto").val(codigo);
	}); 
	
	$( "#cliente" ).click(function() {
		var codigo = $("#cliente").val();
		codigo = codigo +"-"+$("#areaComercial").val();
		codigo= codigo + "-" +$("#nombreProyecto").val().substring(-1,3);
		$("#codigoProyecto").val(codigo);
	});
	
	$( "#areaComercial" ).click(function() {
		var codigo = $("#cliente").val();
		codigo = codigo +"-"+$("#areaComercial").val();
		codigo= codigo + "-" +$("#nombreProyecto").val().substring(-1,3);
		$("#codigoProyecto").val(codigo);
	});
	
	$( "#guardarInfra" ).click(function() {
		var codCliente=$('#codCliente').val();
		var codPRoyecto=$('#codProyecto').val();
		var codEstatusProyecto=$('#codEstatusProyecto').val();
		//alert("codCliente "+codCliente);
		//alert("codPRoyecto "+codPRoyecto);
		//alert("codEstatusProyecto "+codEstatusProyecto);
		var texto="";
		$.ajax({ 
			url: "/eliminarInfra/" + codPRoyecto+ "/" +codEstatusProyecto+"/"+codCliente,
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

});

function enviarCostoRecurso(){
	$.ajax({
		type: "POST",
		url: "/formCostoRecursoProyecto",
		data: $("#costoRecursoProyectoForm").serialize(),
		success: function(){
			$("#costoRecursosModal").modal('hide');
		}
	});
}

function cargaformRecursosProyecto(){
	var url="/recursosProyecto/"+$('#codProyecto').val();
	$("#contentFormRecursoProyecto").load(url);
}

function guardarCom() {
		var codCliente=$('#codCliente').val();
		var codPRoyecto=$('#codProyecto').val();
		var codEstatusProyecto=$('#codEstatusProyecto').val();
		var codContacto=$('#contacto').val();
		var codClasificacionProyecto=1;
		
		$.ajax({ 
			url: "/guardarComplemento/" + codPRoyecto+"/"+codContacto+"/"+codClasificacionProyecto+"/"+codEstatusProyecto+"/"+codCliente,
        	success: function(resE){
        		location.href = '/preventaProyectoConsulta/'+codPRoyecto+'/'+codEstatusProyecto+"/"+codCliente;
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