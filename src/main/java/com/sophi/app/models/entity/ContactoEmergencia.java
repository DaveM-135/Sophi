package com.sophi.app.models.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.sophi.app.Utiles;

@Entity
@Table(name = "RECURSOS_CONTACTO_EMERGENCIA")
public class ContactoEmergencia implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cod_contacto_emergencia")
	private Long codContactoEmergencia;
	
	@Column(name = "cod_recurso")
	private Long codRecurso;
	
	@Column(name = "desc_nombre_contacto")
	private String descNombreContacto;
	
	@Column(name = "desc_tel_contacto_emergencia")
	private String descTelContactoEmergencia;
	
	@Column(name = "cod_parentesco")
	private Long codParentesco;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cod_parentesco",insertable = false, updatable = false)
	private Parentesco parentesco;
	
	@Column(name = "fec_registro")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date fecRegistro;
	
	@Column(name = "val_dependiente_economico")
	private Long valDependienteEconomico;

	@PrePersist
	public void prePersist() {
		fecRegistro = new Utiles().getFechaActual();
	}
	
	public Long getCodContactoEmergencia() {
		return codContactoEmergencia;
	}

	public void setCodContactoEmergencia(Long codContactoEmergencia) {
		this.codContactoEmergencia = codContactoEmergencia;
	}

	public Long getCodRecurso() {
		return codRecurso;
	}

	public void setCodRecurso(Long codRecurso) {
		this.codRecurso = codRecurso;
	}

	public String getDescNombreContacto() {
		return descNombreContacto;
	}

	public void setDescNombreContacto(String descNombreContacto) {
		this.descNombreContacto = descNombreContacto;
	}

	public String getDescTelContactoEmergencia() {
		return descTelContactoEmergencia;
	}

	public void setDescTelContactoEmergencia(String descTelContactoEmergencia) {
		this.descTelContactoEmergencia = descTelContactoEmergencia;
	}

	public Long getCodParentesco() {
		return codParentesco;
	}

	public void setCodParentesco(Long codParentesco) {
		this.codParentesco = codParentesco;
	}

	public Parentesco getParentesco() {
		return parentesco;
	}

	public void setParentesco(Parentesco parentesco) {
		this.parentesco = parentesco;
	}

	public Date getFecRegistro() {
		return fecRegistro;
	}

	public void setFecRegistro(Date fecRegistro) {
		this.fecRegistro = fecRegistro;
	}

	public Long getValDependienteEconomico() {
		return valDependienteEconomico;
	}

	public void setValDependienteEconomico(Long valDependienteEconomico) {
		this.valDependienteEconomico = valDependienteEconomico;
	}
	
	
}