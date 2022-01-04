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
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.sophi.app.Utiles;

@Entity
@Table(name = "RESULTADOS_COMPETENCIAS")
public class RespuestaCompetencia implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cod_resultado_competencia")
	private Long codResultadoCompetencia; 
	
	@Column(name = "cod_evaluacion_desempeno")
	private Long codEvaluacionDesempeno; 
	
	@Column(name = "cod_recurso")
	private Long codRecurso; 
	
	@Column(name = "cod_competencias")
	private Long codCompetencias;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cod_competencias", insertable=false, updatable=false)
	Competencias competencia;
	
	@Column(name = "desc_definicion")
	private String descReactivo;
	
	@Column(name = "val_resultado")
	private Long valResultado;
	
	@Column(name = "cod_recurso_evaluador")
	private Long codRecursoEvaluador;
	
	@Column(name = "val_tipo_evaluador")
	private Long valTipoEvaluador;
	
	@Column(name = "fec_registro")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fecRegistro;
	
	@PrePersist
	public void prePersist() {
		fecRegistro = new Utiles().getFechaActual();
	}

	public Long getCodResultadoCompetencia() {
		return codResultadoCompetencia;
	}

	public void setCodResultadoCompetencia(Long codResultadoCompetencia) {
		this.codResultadoCompetencia = codResultadoCompetencia;
	}

	public Long getCodEvaluacionDesempeno() {
		return codEvaluacionDesempeno;
	}

	public void setCodEvaluacionDesempeno(Long codEvaluacionDesempeno) {
		this.codEvaluacionDesempeno = codEvaluacionDesempeno;
	}

	public Long getCodRecurso() {
		return codRecurso;
	}

	public void setCodRecurso(Long codRecurso) {
		this.codRecurso = codRecurso;
	}

	public Long getCodCompetencias() {
		return codCompetencias;
	}

	public void setCodCompetencias(Long codCompetencias) {
		this.codCompetencias = codCompetencias;
	}

	public Competencias getCompetencia() {
		return competencia;
	}

	public void setCompetencia(Competencias competencia) {
		this.competencia = competencia;
	}

	public String getDescReactivo() {
		return descReactivo;
	}

	public void setDescReactivo(String descReactivo) {
		this.descReactivo = descReactivo;
	}

	public Long getValResultado() {
		return valResultado;
	}

	public void setValResultado(Long valResultado) {
		this.valResultado = valResultado;
	}

	public Long getCodRecursoEvaluador() {
		return codRecursoEvaluador;
	}

	public void setCodRecursoEvaluador(Long codRecursoEvaluador) {
		this.codRecursoEvaluador = codRecursoEvaluador;
	}

	public Long getValTipoEvaluador() {
		return valTipoEvaluador;
	}

	public void setValTipoEvaluador(Long valTipoEvaluador) {
		this.valTipoEvaluador = valTipoEvaluador;
	}

	public Date getFecRegistro() {
		return fecRegistro;
	}

	public void setFecRegistro(Date fecRegistro) {
		this.fecRegistro = fecRegistro;
	}

	public RespuestaCompetencia(Long codEvaluacionDesempeno, Long codRecurso, Competencias competencia, String descReactivo, Long valResultado,
			Long codRecursoEvaluador, Long valTipoEvaluador) {
		super();
		this.codEvaluacionDesempeno = codEvaluacionDesempeno;
		this.codRecurso = codRecurso;
		this.competencia = competencia;
		this.descReactivo = descReactivo;
		this.valResultado = valResultado;
		this.codRecursoEvaluador = codRecursoEvaluador;
		this.valTipoEvaluador = valTipoEvaluador;
	}
	
	public RespuestaCompetencia(Long codCompetencias, String descReactivo, double valResultado) {
		super();
		this.codCompetencias = codCompetencias;
		this.descReactivo = descReactivo;
		this.valResultado = (long) valResultado;
	}
	
	public RespuestaCompetencia() {
		
	}
	

}
