package com.sophi.app.models.entity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

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

import com.sophi.app.Utiles;

@Entity
@Table(name = "DETALLE_RESPUESTAS_CLIMA")
public class RespuestaRecursoClima implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cod_respuesta_clima")
	private Long codRespuestaRecursoClima;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cod_pregunta_respuesta")
	private PreguntaRespuestaClima preguntaRespuestaClima;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cod_recurso")
	private Recurso recurso;
	
	@Column(name = "fec_respuesta")
	private Date fecRespuesta;
	
	@PrePersist
	public void prePersist() {
		fecRespuesta = new Utiles().getFechaActual();
	}

	public Long getCodRespuestaRecursoClima() {
		return codRespuestaRecursoClima;
	}

	public void setCodRespuestaRecursoClima(Long codRespuestaRecursoClima) {
		this.codRespuestaRecursoClima = codRespuestaRecursoClima;
	}

	public PreguntaRespuestaClima getPreguntaRespuestaClima() {
		return preguntaRespuestaClima;
	}

	public void setPreguntaRespuestaClima(PreguntaRespuestaClima preguntaRespuestaClima) {
		this.preguntaRespuestaClima = preguntaRespuestaClima;
	}

	public Recurso getRecurso() {
		return recurso;
	}

	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}

	public Date getFecRespuesta() {
		return fecRespuesta;
	}

	public void setFecRespuesta(Date fecRespuesta) {
		this.fecRespuesta = fecRespuesta;
	}
	
}