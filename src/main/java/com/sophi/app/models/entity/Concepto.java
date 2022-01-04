package com.sophi.app.models.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "CAT_CONCEPTO")
public class Concepto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "cod_concepto")
	private Long codConcepto; 
	
	@Column(name = "desc_concepto")
	private String descConcepto; 
	
	@Column(name = "ruta_template")
	private String rutaTemplate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cod_etapa")
	private Etapa etapa;

	public Long getCodConcepto() {
		return codConcepto;
	}

	public void setCodConcepto(Long codConcepto) {
		this.codConcepto = codConcepto;
	}

	public String getDescConcepto() {
		return descConcepto;
	}

	public void setDescConcepto(String descConcepto) {
		this.descConcepto = descConcepto;
	}

	public String getRutaTemplate() {
		return rutaTemplate;
	}

	public void setRutaTemplate(String rutaTemplate) {
		this.rutaTemplate = rutaTemplate;
	}

	public Etapa getEtapa() {
		return etapa;
	}

	public void setEtapa(Etapa etapa) {
		this.etapa = etapa;
	}
	
	

}
