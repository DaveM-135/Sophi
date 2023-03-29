package com.sophi.app.models.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "CAT_METAS_PERFILES")
public class MetasPerfiles implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cod_perfil_meta")
	private Long codPerfilMeta;
	
	@Column(name = "val_porcentaje")
	private Long valPorcentaje;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "cod_meta")
	Metas meta;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "cod_perfil")
	PerfilRecurso perfil;
	
	@Column(name = "fec_registro")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fecRegistro;

	public Long getCodPerfilMeta() {
		return codPerfilMeta;
	}

	public void setCodPerfilMeta(Long codPerfilMeta) {
		this.codPerfilMeta = codPerfilMeta;
	}

	public Long getValPorcentaje() {
		return valPorcentaje;
	}

	public void setValPorcentaje(Long valPorcentaje) {
		this.valPorcentaje = valPorcentaje;
	}

	public Metas getMeta() {
		return meta;
	}

	public void setMeta(Metas meta) {
		this.meta = meta;
	}

	public PerfilRecurso getPerfil() {
		return perfil;
	}

	public void setPerfil(PerfilRecurso perfil) {
		this.perfil = perfil;
	}

	public Date getFecRegistro() {
		return fecRegistro;
	}

	public void setFecRegistro(Date fecRegistro) {
		this.fecRegistro = fecRegistro;
	}
	
	

}
