'use strict';

//Public Globals
const days = ['Domingo', 'Lunes', 'Martes', 'Miercoles', 'Jueves', 'Viernes', 'Sabado'];
const months = ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"];

let c_date = new Date();
let day = c_date.getDay();
let month = c_date.getMonth();
let year = c_date.getFullYear();

(function App() {
    const calendar = `<div class="row">
                <div class="col-sm-6 col-12 d-flex">
                    <div class="card border-0 flex-fill">
                        <div class="card-header card-header-cal py-3 d-flex justify-content-between">
                            <span class="prevMonth">&#10096;</span>
                            <span><strong id="s_m"></strong></span>
                            <span class="nextMonth">&#10097;</span>
                        </div>
                        <div class="card-body px-1 py-3" style="background: #DDDFEB">
                            <div class="table-responsive">
                                <table class="table table-sm table-borderless">
                                    <thead class="days text-center">
                                        <tr>
                                            ${Object.keys(days).map(key => (
                                                `<th><span>${days[key].substring(0,3)}</span></th>`
                                            )).join('')}                                            
                                        </tr>
                                    </thead>
                                    <tbody id="dates" class="dates text-center"></tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-sm-6 col-12 d-flex pa-sm">
                    <div class="card border-0 flex-fill" id="event">
                        <div class="card-header card-header-cal py-3 text-center">
                            <strong>Dias seleccionados</strong>
                            <button type="button" class="close hide">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="card-body" style="background: #DDDFEB" >
                            <!-- <div class="text-center">
                                <span class="event-date">06 June 2020</span><br>
                                <span class="event-day">Monday</span>
                            </div> --> 
                            <div class="events-today">
                               
                            </div> 
                             <div class="input-group events-input mb-3 col-10 mx-auto mt-2">
                             	<button class="btn btn-primary d-none" style="width:100%;" id="btn-solicitar" onclick="solicitar();">Solicitar</button>
                            </div>                      
                        </div>
                    </div>                            
                </div>
            </div>`;
    document.getElementById('app').innerHTML = calendar;   
})()

function renderCalendar(m, y) {
    //Month's first weekday
    let firstDay = new Date(y, m, 1).getDay();  
    //Days in Month
    let d_m = new Date(y, m+1, 0).getDate();
    //Days in Previous Month
    let d_pm = new Date(y, m, 0).getDate();
    
    
    let table = document.getElementById('dates');
    table.innerHTML = '';
    let s_m = document.getElementById('s_m');
    s_m.innerHTML = months[m] + ' ' + y;
    let date = 1;
    //remaing dates of last month
    let r_pm = (d_pm-firstDay) +1;
    for (let i = 0; i < 6; i++) {
        let row = document.createElement('tr');
        for (let j = 0; j < 7; j++) {
            if (i === 0 && j < firstDay) {  
                let cell = document.createElement('td');
                let span = document.createElement('span');
                let cellText = document.createTextNode(r_pm);
                span.classList.add('ntMonth');
                span.classList.add('prevMonth');                  
                cell.appendChild(span).appendChild(cellText);
                row.appendChild(cell);
                r_pm++;
            }
            else if (date > d_m && j <7) {
                if (j!==0) {
                    let i = 0; 
                    for (let k = j; k < 7; k++) {
                         i++                                             
                        let cell = document.createElement('td');
                        let span = document.createElement('span');
                        let cellText = document.createTextNode(i);
                        span.classList.add('ntMonth');                    
                        span.classList.add('nextMonth');                    
                        cell.appendChild(span).appendChild(cellText);
                        row.appendChild(cell);          
                    };                  
                }                
               break;
            }
            else {
                let cell = document.createElement('td');
                let span = document.createElement('span');
                let cellText = document.createTextNode(date);
                span.classList.add('showEvent');
                if (date === c_date.getDate() && y === c_date.getFullYear() && m === c_date.getMonth()) {
                    span.classList.add('bg-danger');
                } 
                cell.appendChild(span).appendChild(cellText);
                row.appendChild(cell);
                date++;
            }
        }
        table.appendChild(row);
    }
}
renderCalendar(month, year)


    $(function(){
    	
    	//AGREGA dia seleccionado
        function showEvent(fecSolicitud, id){
//        	console.log("este es el id del dia :" + id);
        	var conteoDias = $("#conteoSolicitados").text();
        	var conteoDiasDisponibles = $("#conteoDisponibles").text();
        	if(conteoDiasDisponibles > 0){
        		if(conteoDias < 10){
		        	conteoDias++;
		        	conteoDiasDisponibles--;
		        	$("#conteoSolicitados").html(conteoDias);
		        	$("#conteoDisponibles").html(conteoDiasDisponibles);
		        	$('.events-today').append('<div class="alert alert-info alert-dismissible fade show" style="font-size:12px; margin-bottom:5px; padding:5px;" role="alert">' +
		        			fecSolicitud +
		              '<button type="button" style="font-size:16px; padding:5px;" class="close remove-event" data-dismiss="alert" aria-label="Close" id=' + id + '>'+
		                '<span aria-hidden="true">&times;</span>'+
		              '</button>'+
		            '</div>');
		        	if(conteoDias == 0){
		        		$('#btn-solicitar').addClass('d-none');
		        	} else {
		        		$('#btn-solicitar').removeClass('d-none');
		        	}
        		} else {
        			alert("El número máximo de días solicitados es 10.")
        		}
        	} else {
        		alert("No cuentas con días disponible.")
        	}
        }
        
        //Valida si ya existe el dia 
        function agrega(id){
    		var resultado = true;
    		$('.remove-event').each(function(){
    			if (id==$(this).attr('id')){
    				resultado = false;
    			}
    		});
    		return resultado;
    	}
        
        function completar(x){
        	
        	if (x.length == 1){
        		return '0'+ x;
        	} else {
        		return x;
        	}
        	 
        }
    	
        $(document).on('click', '.remove-event', function(){
            var conteoDias = $("#conteoSolicitados").text();
        	conteoDias--;
        	var conteoDiasDisponibles = $("#conteoDisponibles").text();
        	conteoDiasDisponibles++;
        	 $("#conteoSolicitados").html(conteoDias);
        	 $("#conteoDisponibles").html(conteoDiasDisponibles);
        	 if(conteoDias == 0){
        		 $('#btn-solicitar').addClass('d-none');
        	 } else {
        		 $('#btn-solicitar').removeClass('d-none');
        	 }
        })

        $(document).on('click', '.prevMonth', function(){
            year = (month === 0) ? year - 1 : year;
            month = (month === 0) ? 11 : month - 1;
            renderCalendar(month, year);
        })
        $(document).on('click', '.nextMonth', function(){
            year = (month === 11) ? year + 1 : year;
            month = (month + 1) % 12;
            renderCalendar(month, year);
        })
    
        $(document).on('click', '.showEvent', function(){
            $('.showEvent').removeClass('active');
            $('#event').removeClass('d-none');
            $(this).addClass('active');
            let todaysDate = $(this).text() +' '+ (months[month]) +' '+ year;
            let eventDay = days[new Date(year, month, $(this).text()).getDay()];
//            let eventDate = completar($(this).text()) + completar((month + 1).toString()) + year;
            let eventDate = year + completar((month + 1).toString()) +  completar($(this).text());
//            alert(eventDate);
            var hoy = $("#hoy").val();
//            console.log("hoy: " + hoy +" - select: " + eventDate);
            if (eventDate >= hoy){
	            if(agrega(eventDate)){
	            	if (!(eventDay == "Sabado" || eventDay == "Domingo")){
	            		$.ajax({
	            			url: "/validarDiaLaboralVacacionesAprobador",
	            			data: {codDia:eventDate,
								   codRecurso: $("#codRecurso").val()},
	            			success: function(result){
	            				if( result === "0"){
	    	            			showEvent(eventDay + ' ' +todaysDate, eventDate );
	    	            		} else if(result !== "0"){
									alert(result);
								} else {
	    	            			alert("Feriado Sophitech: "+ result);
	    	            		}
	            			}
	            		});
	                } else {
	                	alert("No disponible para vacaciones.");
	                }
	            } else {
	            	alert("Ya se encuentra seleccionado.");
	            }
        	} else {
        		alert("No disponible para vacaciones.");
        	}
            
        })
        $(document).on('click', '.hide', function(){
            $('#event').addClass('d-none');
        })
		
		$(document).on('click', '.showEvent', function(){
            $('.showEvent').removeClass('active');
            $('#event').removeClass('d-none');
            $(this).addClass('active');
            let todaysDate = $(this).text() +' '+ (months[month]) +' '+ year;
            let eventDay = days[new Date(year, month, $(this).text()).getDay()];
//            let eventDate = completar($(this).text()) + completar((month + 1).toString()) + year;
            let eventDate = year + completar((month + 1).toString()) +  completar($(this).text());
//            alert(eventDate);
            var hoy = $("#hoy").val();
//            console.log("hoy: " + hoy +" - select: " + eventDate);
			if (!(eventDay == "Sabado" || eventDay == "Domingo")){
				$.ajax({
					url: "/validarVacacionesAprobador",
		            data: {codDia:eventDate,
						  codRecurso: $("#codRecurso").val()},
		            success: function(result){
		            	if( result !== eventDate){
							console.log("Se puede pedir el día de vacaciones");
		    	        } else {
							alert(result);
		    	        }
		            }
		        });	
			}
        })
    })

            
