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
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "PROYECTOS")
public class Proyecto implements Serializable  {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cod_proyecto")
	private Long codProyecto;
	
	@Column(name = "cod_cliente")
	private Long codCliente;
	
	@Column(name = "cod_estatus_proyecto")
	private Long codEstatusProyecto;
	
	@NotEmpty
	@Column(name = "desc_proyecto")
	private String descProyecto;
	
	@Column(name = "desc_proyecto_texto")
	private String descProyectoTexto;	

	//	@Range(min=0, max=99999999, message="Ingresa un monto positivo válido")
	@Column(name = "imp_presupuesto")
	private String impPresupuesto;
	
	@NotEmpty
	@Column(name = "desc_codigo_proyecto")
	private String 	descCodigoProyecto;
	
	@Column(name = "cod_clasificacion_proyecto")
	private Long codClasificacionProyecto;
	
	@Column(name = "cod_area_comercial")
	private Long codAreaComercial;
	
	@Column(name = "val_total_horas_vendidas")
	private String valTotalHorasVendidas;
	
	@Column(name = "imp_precio_propuesta")
	private String impPrecioPropuesta;
	
	@Column(name = "fec_incio_proyecto")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fecIncioProyecto;
	
	@Column(name = "fec_fin_proyecto")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fecFinProyecto;

	@Column(name = "cod_tipo_proyecto")
	private Long codTipoProyecto;
	
	@Column(name = "val_porcentaje_riesgo")
	private String valPorcentajeRiesgo;
	
	@Column(name = "val_porcentaje_margen")
	private String valPorcentajeMargen;
	
	@Column(name = "val_total_horas_proyecto")
	private String valTotalHorasProyecto;
	
	@Column(name = "imp_costo_proyecto")
	private String impCostoProyecto;
	
	@Column(name = "imp_gasto_proyecto")
	private String impGastoProyecto;
	
	@Column(name = "fec_registro")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	private Date fecRegistro;
	
	@Column(name = "fec_cambio_estatus")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	private Date fecCambioEstatus;
	
	@Column(name = "cod_recurso_lider")
	private Long codRecursoLider;
	
	@Transient
	private String nombreRecursoLider;
	
	@Column(name = "cod_recurso_aprobador")
	private Long codRecursoAprobador;
	
	@Transient
	private String nombreRecursoAprobador;
	
	@Column(name = "cod_tipo_facturacion")
	private Long codTipoFacturacion;
	
	@Column(name = "val_evaluacion")
	private Long valEvaluacion;
	
	@Column(name = "fec_cierre_evaluacion")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	private Date fecCierreEvaluacion;
	
	@Column(name= "val_plan")
	private Long valPlan;
	
	@Column(name = "val_evaluar")
	private Long valEvaluar;

	@Transient
	private Long numAct;
	
	@Column(name = "cod_recurso_aprobador_bkp")
	private Long codRecursoAprobadorBKP;

	@PrePersist
	public void prePersist() {
		fecRegistro = new Date();
	}

	public Long getCodProyecto() {
		return codProyecto;
	}

	public void setCodProyecto(Long codProyecto) {
		this.codProyecto = codProyecto;
	}

	public Long getCodCliente() {
		return codCliente;
	}

	public void setCodCliente(Long codCliente) {
		this.codCliente = codCliente;
	}

	public Long getCodEstatusProyecto() {
		return codEstatusProyecto;
	}

	public void setCodEstatusProyecto(Long codEstatusProyecto) {
		this.codEstatusProyecto = codEstatusProyecto;
	}

	public String getDescProyecto() {
		return descProyecto;
	}

	public void setDescProyecto(String descProyecto) {
		this.descProyecto = descProyecto;
	}
	
	public String getDescProyectoTexto() {
		return descProyectoTexto;
	}

	public void setDescProyectoTexto(String descProyectoTexto) {
		this.descProyectoTexto = descProyectoTexto;
	}

	public String getImpPresupuesto() {
		return impPresupuesto;
	}

	public void setImpPresupuesto(String impPresupuesto) {
		this.impPresupuesto = impPresupuesto;
	}

	public String getDescCodigoProyecto() {
		return descCodigoProyecto;
	}

	public void setDescCodigoProyecto(String descCodigoProyecto) {
		this.descCodigoProyecto = descCodigoProyecto;
	}

	public Long getCodClasificacionProyecto() {
		return codClasificacionProyecto;
	}

	public void setCodClasificacionProyecto(Long codClasificacionProyecto) {
		this.codClasificacionProyecto = codClasificacionProyecto;
	}

	public Long getCodAreaComercial() {
		return codAreaComercial;
	}

	public void setCodAreaComercial(Long codAreaComercial) {
		this.codAreaComercial = codAreaComercial;
	}

	public String getValTotalHorasVendidas() {
		return valTotalHorasVendidas;
	}

	public void setValTotalHorasVendidas(String valTotalHorasVendidas) {
		this.valTotalHorasVendidas = valTotalHorasVendidas;
	}

	public String getImpPrecioPropuesta() {
		return impPrecioPropuesta;
	}

	public void setImpPrecioPropuesta(String impPrecioPropuesta) {
		this.impPrecioPropuesta = impPrecioPropuesta;
	}

	public Date getFecIncioProyecto() {
		return fecIncioProyecto;
	}

	public void setFecIncioProyecto(Date fecIncioProyecto) {
		this.fecIncioProyecto = fecIncioProyecto;
	}

	public Date getFecFinProyecto() {
		return fecFinProyecto;
	}

	public void setFecFinProyecto(Date fecFinProyecto) {
		this.fecFinProyecto = fecFinProyecto;
	}

	public Long getCodTipoProyecto() {
		return codTipoProyecto;
	}

	public void setCodTipoProyecto(Long codTipoProyecto) {
		this.codTipoProyecto = codTipoProyecto;
	}

	public String getValPorcentajeRiesgo() {
		return valPorcentajeRiesgo;
	}

	public void setValPorcentajeRiesgo(String valPorcentajeRiesgo) {
		this.valPorcentajeRiesgo = valPorcentajeRiesgo;
	}

	public String getValPorcentajeMargen() {
		return valPorcentajeMargen;
	}

	public void setValPorcentajeMargen(String valPorcentajeMargen) {
		this.valPorcentajeMargen = valPorcentajeMargen;
	}

	public String getValTotalHorasProyecto() {
		return valTotalHorasProyecto;
	}

	public void setValTotalHorasProyecto(String valTotalHorasProyecto) {
		this.valTotalHorasProyecto = valTotalHorasProyecto;
	}

	public String getImpCostoProyecto() {
		return impCostoProyecto;
	}

	public void setImpCostoProyecto(String impCostoProyecto) {
		this.impCostoProyecto = impCostoProyecto;
	}

	public String getImpGastoProyecto() {
		return impGastoProyecto;
	}

	public void setImpGastoProyecto(String impGastoProyecto) {
		this.impGastoProyecto = impGastoProyecto;
	}

	public Date getFecRegistro() {
		return fecRegistro;
	}

	public void setFecRegistro(Date fecRegistro) {
		this.fecRegistro = fecRegistro;
	}
	
	public Date getFecCambioEstatus() {
		return fecCambioEstatus;
	}

	public void setFecCambioEstatus(Date fecCambioEstatus) {
		this.fecCambioEstatus = fecCambioEstatus;
	}

	public Long getCodRecursoLider() {
		return codRecursoLider;
	}

	public String getNombreRecursoLider() {
		return nombreRecursoLider;
	}

	public void setNombreRecursoLider(String nombreRecursoLider) {
		this.nombreRecursoLider = nombreRecursoLider;
	}

	public String getNombreRecursoAprobador() {
		return nombreRecursoAprobador;
	}

	public void setNombreRecursoAprobador(String nombreRecursoAprobador) {
		this.nombreRecursoAprobador = nombreRecursoAprobador;
	}

	public void setCodRecursoLider(Long codRecursoLider) {
		this.codRecursoLider = codRecursoLider;
	}

	public Long getCodRecursoAprobador() {
		return codRecursoAprobador;
	}

	public void setCodRecursoAprobador(Long codRecursoAprobador) {
		this.codRecursoAprobador = codRecursoAprobador;
	}

	public Long getCodTipoFacturacion() {
		return codTipoFacturacion;
	}

	public void setCodTipoFacturacion(Long codTipoFacturacion) {
		this.codTipoFacturacion = codTipoFacturacion;
	}

	public Long getNumAct() {
		return numAct;
	}

	public void setNumAct(Long numAct) {
		this.numAct = numAct;
	}
	
	public Long getValEvaluacion() {
		return valEvaluacion;
	}

	public void setValEvaluacion(Long valEvaluacion) {
		this.valEvaluacion = valEvaluacion;
	}

	public Date getFecCierreEvaluacion() {
		return fecCierreEvaluacion;
	}

	public void setFecCierreEvaluacion(Date fecCierreEvaluacion) {
		this.fecCierreEvaluacion = fecCierreEvaluacion;
	}
	
	public Long getValPlan() {
		return valPlan;
	}

	public void setValPlan(Long valPlan) {
		this.valPlan = valPlan;
	}

	public Long getValEvaluar() {
		return valEvaluar;
	}

	public void setValEvaluar(Long valEvaluar) {
		this.valEvaluar = valEvaluar;
	}
	
	public Long getCodRecursoAprobadorBKP() {
		return codRecursoAprobadorBKP;
	}

	public void setCodRecursoAprobadorBKP(Long codRecursoAprobadorBKP) {
		this.codRecursoAprobadorBKP = codRecursoAprobadorBKP;
	}
	
}
