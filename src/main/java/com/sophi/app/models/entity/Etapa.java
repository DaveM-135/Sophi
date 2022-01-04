package com.sophi.app.models.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "CAT_ETAPA")
public class Etapa implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "cod_etapa")
	private Long codEtapa;
	
	@Column(name = "desc_etapa")
	private String descEtapa; 
	
	@Column(name = "desc_corta")
	private String descCorta;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "cod_etapa")
	private List<Concepto> conceptos;

	public Long getCodEtapa() {
		return codEtapa;
	}

	public void setCodEtapa(Long codEtapa) {
		this.codEtapa = codEtapa;
	}

	public String getDescEtapa() {
		return descEtapa;
	}

	public void setDescEtapa(String descEtapa) {
		this.descEtapa = descEtapa;
	}

	public String getDescCorta() {
		return descCorta;
	}

	public void setDescCorta(String descCorta) {
		this.descCorta = descCorta;
	}

	public List<Concepto> getConceptos() {
		return conceptos;
	}

	public void setConceptos(List<Concepto> conceptos) {
		this.conceptos = conceptos;
	}
	
	
}
