package com.sophi.app.models.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

@Entity
@Table(name = "DETALLE_CONOCIMIENTOS_PROYECTOS")
public class DetalleConocimientosProyecto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cod_detalle_conocimiento")
	private Long codDetalleConocimiento;
	
	@ManyToOne
	@JoinColumn(name = "cod_trayectoria_proyecto", nullable = false)
	private RecursoTrayectoriaProyecto recursoTrayectoriaProyecto;
	
//	@Column(name = "cod_trayectoria_proyecto")
//	private Long codTrayectoriaProyecto;
	
	@ManyToOne
	@JoinColumn(name = "cod_conocimiento", nullable = false)
	private Conocimientos conocimiento;
	
//	@Column(name = "cod_conocimiento")
//	private Long codConocimiento;

	public Long getCodDetalleConocimiento() {
		return codDetalleConocimiento;
	}

	public void setCodDetalleConocimiento(Long codDetalleConocimiento) {
		this.codDetalleConocimiento = codDetalleConocimiento;
	}

	public RecursoTrayectoriaProyecto getRecursoTrayectoriaProyecto() {
		return recursoTrayectoriaProyecto;
	}

	public void setRecursoTrayectoriaProyecto(RecursoTrayectoriaProyecto recursoTrayectoriaProyecto) {
		this.recursoTrayectoriaProyecto = recursoTrayectoriaProyecto;
	}

	public Conocimientos getConocimiento() {
		return conocimiento;
	}

	public void setConocimiento(Conocimientos conocimiento) {
		this.conocimiento = conocimiento;
	}

	

}
