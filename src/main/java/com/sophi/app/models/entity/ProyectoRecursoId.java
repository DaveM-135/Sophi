package com.sophi.app.models.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ProyectoRecursoId implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "cod_proyecto")
	private Long codProyecto;
	
	@Column(name = "cod_recurso")
	private Long codRecurso;
	
	@Column(name = "cod_cliente")
	private Long codCliente;
	
	@Column(name = "cod_estatus_proyecto")
	private Long codEstatusProyecto;
	
	public ProyectoRecursoId() {
		
	}
	
	public ProyectoRecursoId( Long codProyecto, Long codRecurso, Long codCliente, Long codEstatusProyecto) {
		super();
		this.codProyecto = codProyecto;
		this.codRecurso = codRecurso;
		this.codCliente = codCliente;
		this.codEstatusProyecto = codEstatusProyecto;
	}

	@Override
	public int hashCode() {
		return Objects.hash(codProyecto,codRecurso,codCliente,codEstatusProyecto);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProyectoRecursoId RecursoGastoId = (ProyectoRecursoId) o;
        return codProyecto.equals(RecursoGastoId.codProyecto) &&
        		codRecurso.equals(RecursoGastoId.codRecurso) &&
        		codCliente.equals(RecursoGastoId.codCliente) &&
        		codEstatusProyecto.equals(RecursoGastoId.codEstatusProyecto);
	}

	public Long getCodProyecto() {
		return codProyecto;
	}

	public void setCodProyecto(Long codProyecto) {
		this.codProyecto = codProyecto;
	}

	public Long getCodRecurso() {
		return codRecurso;
	}

	public void setCodRecurso(Long codRecurso) {
		this.codRecurso = codRecurso;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
