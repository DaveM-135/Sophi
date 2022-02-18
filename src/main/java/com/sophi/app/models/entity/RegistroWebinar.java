package com.sophi.app.models.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;

import com.sophi.app.Utiles;

@Entity
@Table(name = "REGISTROS_WEBINAR_SOPHITECH")
public class RegistroWebinar implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cod_registro")
	private Long codRegistro;
	
	@NotEmpty(message = "Este dato no debe estar vacío")
	@Column(name = "desc_nombre")
	private String descNombre;
	
	@NotEmpty(message = "Este dato no debe estar vacío")
	@Column(name = "desc_apellidos")
	private String descApellidos;
	
	@NotEmpty(message = "Este dato no debe estar vacío")
	@Column(name = "desc_email")
	private String descEmail;
	
	@NotEmpty(message = "Este dato no debe estar vacío")
	@Column(name = "desc_empresa")
	private String descEmpresa;
	
	@NotEmpty(message = "Este dato no debe estar vacío")
	@Column(name = "desc_cargo")
	private String descCargo;
	
	@Column(name = "desc_telefono")
	private String descTelefono;
	
	@NotEmpty(message = "Este dato no debe estar vacío")
	@Column(name = "desc_pais")
	private String descPais;
	
	@Column(name = "fec_registro")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fecRegistro;
	
	@PrePersist
	public void prePersist() {
		fecRegistro = new Utiles().getFechaActual();
	}

	public Long getCodRegistro() {
		return codRegistro;
	}

	public void setCodRegistro(Long codRegistro) {
		this.codRegistro = codRegistro;
	}

	public String getDescNombre() {
		return descNombre;
	}

	public void setDescNombre(String descNombre) {
		this.descNombre = descNombre;
	}

	public String getDescApellidos() {
		return descApellidos;
	}

	public void setDescApellidos(String descApellidos) {
		this.descApellidos = descApellidos;
	}

	public String getDescEmail() {
		return descEmail;
	}

	public void setDescEmail(String descEmail) {
		this.descEmail = descEmail;
	}

	public String getDescEmpresa() {
		return descEmpresa;
	}

	public void setDescEmpresa(String descEmpresa) {
		this.descEmpresa = descEmpresa;
	}

	public String getDescCargo() {
		return descCargo;
	}

	public void setDescCargo(String descCargo) {
		this.descCargo = descCargo;
	}

	public String getDescTelefono() {
		return descTelefono;
	}

	public void setDescTelefono(String descTelefono) {
		this.descTelefono = descTelefono;
	}

	public String getDescPais() {
		return descPais;
	}

	public void setDescPais(String descPais) {
		this.descPais = descPais;
	}

	public Date getFecRegistro() {
		return fecRegistro;
	}

	public void setFecRegistro(Date fecRegistro) {
		this.fecRegistro = fecRegistro;
	}
	
}
