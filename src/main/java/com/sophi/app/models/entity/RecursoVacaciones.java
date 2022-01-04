package com.sophi.app.models.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "RECURSO_VACACIONES")
public class RecursoVacaciones implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "cod_recurso")
	private Long codRecurso;

	@Column(name = "val_acumulado")
	private Long valAcumulado;
	
	@Column(name = "val_recuperado")
	private Long valRecuperado;
	
	@Column(name = "val_contrato")
	private Long valContrato;

	@Column(name = "val_total")
	private Long valTotal;

	@Column(name = "val_aprobado")
	private Long valAprobado;

	@Column(name = "val_disponibles")
	private Long valDisponibles;

	@Column(name = "val_periodo")
	private Long valPeriodo;
	
	@Transient
	private String nombreRecurso;
	
	@Transient
	private Long valPendientes;
	
	public Long getCodRecurso() {
		return codRecurso;
	}


	public void setCodRecurso(Long codRecurso) {
		this.codRecurso = codRecurso;
	}


	public Long getValAcumulado() {
		return valAcumulado;
	}


	public void setValAcumulado(Long valAcumulado) {
		this.valAcumulado = valAcumulado;
	}


	public Long getValRecuperado() {
		return valRecuperado;
	}


	public void setValRecuperado(Long valRecuperado) {
		this.valRecuperado = valRecuperado;
	}


	public Long getValContrato() {
		return valContrato;
	}

	public void setValContrato(Long valContrato) {
		this.valContrato = valContrato;
	}

	public Long getValTotal() {
		return valTotal;
	}

	public void setValTotal(Long valTotal) {
		this.valTotal = valTotal;
	}

	public Long getValAprobado() {
		return valAprobado;
	}

	public void setValAprobado(Long valAprobado) {
		this.valAprobado = valAprobado;
	}

	public Long getValDisponibles() {
		return valDisponibles;
	}

	public void setValDisponibles(Long valDisponibles) {
		this.valDisponibles = valDisponibles;
	}

	public Long getValPeriodo() {
		return valPeriodo;
	}

	public void setValPeriodo(Long valPeriodo) {
		this.valPeriodo = valPeriodo;
	}

	public String getNombreRecurso() {
		return nombreRecurso;
	}

	public void setNombreRecurso(String nombreRecurso) {
		this.nombreRecurso = nombreRecurso;
	}

	public Long getValPendientes() {
		return valPendientes;
	}

	public void setValPendientes(Long valPendientes) {
		this.valPendientes = valPendientes;
	}
	


}
