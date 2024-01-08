package com.sophi.app;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class Utiles {
	
	public Map<String, String> recursosEtapaList(){
		Map<String, String> etapaList = new HashMap<String, String>();
		etapaList.put("Trunca", "Trunca");
		etapaList.put("Terminada", "Terminada");
		return etapaList;
	}
	
	
	public Map<String, String> skillTipoList(){
		Map<String, String> tipoSkillList = new HashMap<String, String>();
		tipoSkillList.put("Skill", "Skill");
		tipoSkillList.put("Soft skill", "Soft skill");
		return tipoSkillList;
	}
	
	public Map<String, String> skillDominioList(){
		Map<String, String> dominioSkillList = new HashMap<String, String>();
		dominioSkillList.put("No aplica", "No aplica");
		dominioSkillList.put("Alto", "Alto");
		dominioSkillList.put("Medio", "Medio");
		dominioSkillList.put("Bajo", "Bajo");
		return dominioSkillList;
	}
	
	
	public Date getFechaActual() {

		Calendar currentDate = Calendar.getInstance();
		currentDate.setTimeZone(TimeZone.getTimeZone("America/Mexico_City"));

		//Date date = new Date(currentDate.get(Calendar.YEAR) - 1900, currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE));
		Date date = currentDate.getTime();
		
		return date;
	}

	public Calendar getFechaActualCal() {

		Calendar currentDate = Calendar.getInstance();
		currentDate.setTimeZone(TimeZone.getTimeZone("America/Mexico_City"));
		
		return currentDate;
	}
	
	
	public int getFechaViernes() {

		Calendar currentDate = Calendar.getInstance();
		currentDate.setTimeZone(TimeZone.getTimeZone("America/Mexico_City"));
		
		int diasViernes = currentDate.get(Calendar.DAY_OF_WEEK) + 1;
		currentDate.add(Calendar.DAY_OF_MONTH,  diasViernes * -1 );
		
		String anio = Integer.toString(currentDate.get(Calendar.YEAR));
		String mes = String.format("%02d", currentDate.get(Calendar.MONTH)+1);
		String dia = String.format("%02d", currentDate.get(Calendar.DATE));
		
		int fechaViernes = Integer.parseInt(anio+mes+dia);
		
		return fechaViernes;
		
	}
	
	public String getHostName() {
		return "localhost:8090";
	}

}
