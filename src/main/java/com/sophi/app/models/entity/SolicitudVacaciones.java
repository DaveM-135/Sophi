package com.sophi.app.models.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "SOLICITUDES_VACACIONES")
public class SolicitudVacaciones implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cod_solicitud")
	private Long codSolicitud;

	@Column(name = "cod_recurso")
	private Long codRecurso;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "cod_solicitud")
	private List<DetalleSolicitud> DetallesSolicitud;
	
	@Column(name = "fec_solicitud")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fecSolicitud;
	
	@Column(name = "val_dias_solicitados")
	private Long valDiasSolicitados;
	
	@Column(name = "cod_recurso_aprobador")
	private Long codRecursoAprobador;
	
	@Column(name = "fec_aprobacion")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fecAprobacion;
	
	@Column(name = "fec_rechazo")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fecRechazo;
	
	@Column(name = "fec_cancelacion")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fecCancelacion;

	@Column(name = "val_periodo")
	private Long valPeriodo;
	
	@Transient
	private Long contador;

	public Long getCodSolicitud() {
		return codSolicitud;
	}

	public void setCodSolicitud(Long codSolicitud) {
		this.codSolicitud = codSolicitud;
	}

	public Long getCodRecurso() {
		return codRecurso;
	}

	public void setCodRecurso(Long codRecurso) {
		this.codRecurso = codRecurso;
	}

	public Date getFecSolicitud() {
		return fecSolicitud;
	}

	public void setFecSolicitud(Date fecSolicitud) {
		this.fecSolicitud = fecSolicitud;
	}

	public Long getValDiasSolicitados() {
		return valDiasSolicitados;
	}

	public void setValDiasSolicitados(Long valDiasSolicitados) {
		this.valDiasSolicitados = valDiasSolicitados;
	}

	public Long getCodRecursoAprobador() {
		return codRecursoAprobador;
	}

	public void setCodRecursoAprobador(Long codRecursoAprobador) {
		this.codRecursoAprobador = codRecursoAprobador;
	}

	public Date getFecAprobacion() {
		return fecAprobacion;
	}

	public void setFecAprobacion(Date fecAprobacion) {
		this.fecAprobacion = fecAprobacion;
	}

	public Date getFecRechazo() {
		return fecRechazo;
	}

	public void setFecRechazo(Date fecRechazo) {
		this.fecRechazo = fecRechazo;
	}

	public Date getFecCancelacion() {
		return fecCancelacion;
	}

	public void setFecCancelacion(Date fecCancelacion) {
		this.fecCancelacion = fecCancelacion;
	}

	public Long getValPeriodo() {
		return valPeriodo;
	}

	public void setValPeriodo(Long valPeriodo) {
		this.valPeriodo = valPeriodo;
	}

	public Long getContador() {
		return contador;
	}

	public void setContador(Long contador) {
		this.contador = contador;
	}

	public List<DetalleSolicitud> getDetallesSolicitud() {
		return DetallesSolicitud;
	}

	public void setDetallesSolicitud(List<DetalleSolicitud> detallesSolicitud) {
		DetallesSolicitud = detallesSolicitud;
	}
	

}
