var borrar_cliente;

function desactivar(id) {
	borrar_cliente = id;
	$("#desactivarRegistro").modal("show");
}

function desactivarRegistro(){
	$.ajax({
		type: "GET",
		url:"/desactivarCliente/"+borrar_cliente,
		success: function(){
			location.reload();
			$("#eliminarRegistro").modal("hide");
		}
	})
}

function activar(id) {
	borrar_cliente = id;
	$("#activarRegistro").modal("show");
}

function activarRegistro(){
	$.ajax({
		type: "GET",
		url:"/activarCliente/"+borrar_cliente,
		success: function(){
			location.reload();
			$("#eliminarRegistro").modal("hide");
		}
	})
}