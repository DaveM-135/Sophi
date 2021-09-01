package com.sophi.app.models.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "CLIENTES")
public class Cliente implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cod_cliente")
	private Long codCliente;
	
	@NotEmpty(message = "Este dato no debe estar vacío")
	@Column(name = "desc_cliente")
	private String descCliente;
	
	@Column(name = "desc_codigo_cliente")
	private String descCodigoCliente;
	
	@Column(name = "desc_cliente_anterior")
	private String descClienteAnterior;
	
	@Column(name = "fec_actualizacion")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fecActualizacion;
	
	@Column(name = "desc_direccion_fiscal")
	private String descDireccionFiscal;
	
	@Column(name = "desc_grupo_empresarial")
	private String descGrupoEmpresarial;
	
	@Range(min=0, max=99999999, message="Ingresa un monto positivo y no tan largo")
	@Column(name = "imp_facturacion_anual")
	private Float impFacturacionAnual;
	
	@Column(name = "desc_rango_recursos")
	private String descRangoRecursos;
	
	@Column(name = "desc_rfc")
	private String descRFC;
	
//	@Column(name = "cod_sector")
//	private Long codSector;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="cod_sector")
	private Sector sector;
	
	@Column(name = "fec_registro")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date fecRegistro;
	
	@OneToMany(mappedBy = "cliente")
    List<DetalleClienteInfraestructura> clientesInfraestructura;
	
	@OneToMany(mappedBy = "agenda")
	List<DetalleProyectoContacto> detalleProyectoContacto;

	@PrePersist
	public void prePersist() {
		fecRegistro = new Date();
	}
	
	@Column(name = "val_activo")
	private Long valActivo;

	public Long getCodCliente() {
		return codCliente;
	}

	public void setCodCliente(Long codCliente) {
		this.codCliente = codCliente;
	}

	public String getDescCliente() {
		return descCliente;
	}

	public void setDescCliente(String descCliente) {
		this.descCliente = descCliente;
	}

	public String getDescCodigoCliente() {
		return descCodigoCliente;
	}

	public void setDescCodigoCliente(String descCodigoCliente) {
		this.descCodigoCliente = descCodigoCliente;
	}

	public Date getFecRegistro() {
		return fecRegistro;
	}

	public void setFecRegistro(Date fecRegistro) {
		this.fecRegistro = fecRegistro;
	}

	public String getDescClienteAnterior() {
		return descClienteAnterior;
	}

	public void setDescClienteAnterior(String descClienteAnterior) {
		this.descClienteAnterior = descClienteAnterior;
	}

	public Date getFecActualizacion() {
		return fecActualizacion;
	}

	public void setFecActualizacion(Date fecActualizacion) {
		this.fecActualizacion = fecActualizacion;
	}

	public String getDescDireccionFiscal() {
		return descDireccionFiscal;
	}

	public void setDescDireccionFiscal(String descDireccionFiscal) {
		this.descDireccionFiscal = descDireccionFiscal;
	}

	public String getDescGrupoEmpresarial() {
		return descGrupoEmpresarial;
	}

	public void setDescGrupoEmpresarial(String descGrupoEmpresarial) {
		this.descGrupoEmpresarial = descGrupoEmpresarial;
	}

	public Float getImpFacturacionAnual() {
		return impFacturacionAnual;
	}

	public void setImpFacturacionAnual(Float impFacturacionAnual) {
		this.impFacturacionAnual = impFacturacionAnual;
	}

	public String getDescRangoRecursos() {
		return descRangoRecursos;
	}

	public void setDescRangoRecursos(String descRangoRecursos) {
		this.descRangoRecursos = descRangoRecursos;
	}

	public String getDescRFC() {
		return descRFC;
	}

	public void setDescRFC(String descRFC) {
		this.descRFC = descRFC;
	}

//	public Long getCodSector() {
//		return codSector;
//	}
//
//	public void setCodSector(Long codSector) {
//		this.codSector = codSector;
//	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<DetalleClienteInfraestructura> getClientesInfraestructura() {
		return clientesInfraestructura;
	}

	public void setClientesInfraestructura(List<DetalleClienteInfraestructura> clientesInfraestructura) {
		this.clientesInfraestructura = clientesInfraestructura;
	}
	
	public List<DetalleProyectoContacto> getDetalleProyectoContacto() {
		return detalleProyectoContacto;
	}

	public void setDetalleProyectoContacto(List<DetalleProyectoContacto> detalleProyectoContacto) {
		this.detalleProyectoContacto = detalleProyectoContacto;
	}

	public Sector getSector() {
		return sector;
	}

	public void setSector(Sector sector) {
		this.sector = sector;
	}

	public Long getValActivo() {
		return valActivo;
	}

	public void setValActivo(Long valActivo) {
		this.valActivo = valActivo;
	}
	
	
}
