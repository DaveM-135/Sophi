package com.sophi.app.models.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

@Entity
@Table(name = "RECURSOS_TRAYECTORIA_NIVEL")
public class RecursoTrayectoria implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cod_nivel_trayectoria")
	private Long codNivelTrayectoria;
	
	@Column(name = "cod_nivel_experiencia")
	private Long codNivelExperiencia;
	
	@Column(name = "cod_recurso")
	private Long codRecurso;
	
	@Column(name = "cod_conocimiento")
	private Long codConocimiento;
	
	@Column(name = "porcentaje_experiencia")
	private Long porcentajeExperiencia;

	public Long getCodNivelTrayectoria() {
		return codNivelTrayectoria;
	}

	public void setCodNivelTrayectoria(Long codNivelTrayectoria) {
		this.codNivelTrayectoria = codNivelTrayectoria;
	}

	public Long getCodNivelExperiencia() {
		return codNivelExperiencia;
	}

	public void setCodNivelExperiencia(Long codNivelExperiencia) {
		this.codNivelExperiencia = codNivelExperiencia;
	}

	public Long getCodRecurso() {
		return codRecurso;
	}

	public void setCodRecurso(Long codRecurso) {
		this.codRecurso = codRecurso;
	}

	public Long getCodConocimiento() {
		return codConocimiento;
	}

	public void setCodConocimiento(Long codConocimiento) {
		this.codConocimiento = codConocimiento;
	}

	public Long getPorcentajeExperiencia() {
		return porcentajeExperiencia;
	}

	public void setPorcentajeExperiencia(Long porcentajeExperiencia) {
		this.porcentajeExperiencia = porcentajeExperiencia;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
