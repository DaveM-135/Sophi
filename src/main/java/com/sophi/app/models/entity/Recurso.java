package com.sophi.app.models.entity;

import java.io.Serializable;
import java.util.ArrayList;
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
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "RECURSOS")
public class Recurso implements Serializable  {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cod_recurso")
	private Long codRecurso;
	
	@Lob
    @Column(name = "desc_foto_personal", columnDefinition="BLOB")
	private byte[] foto;
	
	@Lob
    @Column(name = "desc_foto_cv", columnDefinition="BLOB")
    private byte[] fotoCv;
	
//	@NotEmpty(message = "Este dato no debe estar vacío")
	@Column(name = "desc_recurso")
	private String descRecurso;
	
//	@NotEmpty(message = "Este dato no debe estar vacío")
	@Column(name = "desc_apellido_paterno")
	private String descApellidoPaterno;
	
	@Column(name = "desc_apellido_materno")
	private String descApellidoMaterno;
	
	@Column(name = "val_aprobador")
	private Long valAprobador;
	
	@Column(name = "val_responsable")
	private Long valResponsable;
	
//	@NotEmpty(message = "Este dato no debe estar vacío")
	@Email(message = "No es un email correcto")
	@Column(name = "desc_correo_electronico")
	private String descCorreoElectronico;
	
	@Email(message = "No es un email correcto")
	@Column(name = "desc_correo_personal")
	private String descCorreoPersonal;
	
	@NotEmpty(message = "Este dato no debe estar vacío")
	@Column(name = "desc_tel_celular")
	private String descTelCelular;
	
	@Column(name = "desc_tel_empresa")
	private String descTelEmpresa;
	
//	@NotEmpty(message = "Este dato no debe estar vacío")
	@Column(name = "desc_rfc")
	private String descRfc;
	
	@Column(name = "desc_tel_ext")
	private String descTelExt;
	
//	@NotEmpty(message = "Este dato no debe estar vacío")
	@Column(name = "desc_curp")
	private String descCurp;
	
//	@NotNull(message = "Este dato no debe estar vacío")
	@Column(name = "fec_nacimiento")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fecNacimiento;
	
//	@NotEmpty(message = "Este dato no debe estar vacío")
	@Column(name = "dir_calle")
	private String dirCalle;
	
	@Column(name = "dir_numero_ext")
	private String dirNumeroExt;
	
	@Column(name = "dir_numero_int")
	private String dirNumeroInt;
	
//	@NotEmpty(message = "Este dato no debe estar vacío")
	@Column(name = "dir_colonia")
	private String dirColonia;
	
//	@NotEmpty(message = "Este dato no debe estar vacío")
	@Column(name = "dir_cp")
	private String dirCp;
	
//	@NotEmpty(message = "Este dato no debe estar vacío")
	@Column(name = "dir_alcaldia_municipio")
	private String dirAlcaldiaMunicipio;
	
//	@NotEmpty(message = "Este dato no debe estar vacío")
	@Column(name = "dir_estado")
	private String dirEstado;
	
//	@NotEmpty(message = "Este dato no debe estar vacío")
	@Column(name = "desc_nss")
	private String descNss;

	@Column(name = "cod_estado_civil")
	private Long codEstadoCivil;
	
	@Column(name = "cod_tipo_sangre")
	private Long codTipoSangre;
	
	@Column(name = "desc_genero")
	private String descGenero;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cod_estado_civil",insertable = false, updatable = false)
	private EstadoCivil estadoCivil;
	
	@Column(name = "val_numero_hijos")
	private Long valNumeroHijos;
	
//	@NotNull(message = "Este dato no debe estar vacío")
	@Column(name = "fec_ingreso_empresa")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fecIngresoEmpresa;
	
	@Column(name = "val_activo")
	private Long valActivo;
	
	@Column(name = "cod_area_recurso")
	private Long codAreaRecurso;
	
	@OneToOne
	@JoinColumn(name = "cod_area_recurso", referencedColumnName="cod_consultor", insertable=false, updatable=false)
	private AreaRecurso arearecurso;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "fec_salida_empresa")
	private Date fecSalidaEmpresa;
	
	@Column(name = "desc_motivo_salida")
	private String descMotivoSalida;
	
	@Column(name = "cod_puesto")
	private Long codPuesto;
	
	@OneToOne
	@JoinColumn(name = "cod_puesto", insertable = false, updatable = false)
	private Puesto puesto;
	
	@Column(name = "cod_jornada")
	private Long codJornada;
	
	@OneToOne
	@JoinColumn(name = "cod_jornada", insertable = false, updatable = false)
	private Jornada jornada;
	
	@Column(name = "cod_tipo_recurso")
	private Long codTipoRecurso;
	
	@OneToOne
	@JoinColumn(name = "cod_tipo_recurso", insertable = false, updatable = false)
	private TipoRecurso tiporecurso;
	
	@Column(name = "cod_proveedor")
	private Long codProveedor;
	
	@Column(name = "cod_perfil")
	private Long codPerfil;
	
	@OneToOne
	@JoinColumn(name = "cod_perfil", insertable = false, updatable = false)
	private PerfilRecurso perfilRecurso;
	
	@OneToOne
	@JoinColumn(name = "cod_proveedor", insertable = false, updatable = false)
	private Proveedor proveedor;
	
	@Column(name = "val_costo_minimo")
	private Float valCostoMinimo;
	
	@Column(name = "desc_edu_grado_escolaridad")
	private String descEduGradoEscolaridad;
	
	@Column(name = "desc_edu_etapa")
	private String descEduEtapa;
	
	@Column(name = "desc_edu_institucion_academica")
	private String descEduInstitucionAcademica;
	
	@Column(name = "desc_edu_documento_obtenido")
	private String descEduDocumentoObtenido;
	
	@Column(name = "desc_banco_nombre")
	private String descBancoNombre;
	
	@Column(name = "desc_banco_numero_cuenta")
	private String descBancoNumeroCuenta;
	
	@Column(name = "desc_banco_clabe")
	private String descBancoClabe;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cod_tipo_sangre",insertable = false, updatable = false)
	private TipoSangre tipoSangre;
	
	@Column(name = "desc_medicos_alergias")
	private String descMedicosAlergias;
	
	@Column(name = "val_medicos_alergias")
	public String valMedicosAlergias;
	
	@Column(name = "desc_medicos_embarazos")
	private String descMedicosEmbarazos;
	
	@Column(name = "desc_medicos_cirugias")
	private String descMedicosCirugias;
	
	@Column(name = "desc_medicos_enfermedades")
	private String descMedicosEnfermedades;
	
	@Column(name = "val_medicos_enfermedades")
	public String valMedicosEnfermedades;
	
	@Column(name = "fec_registro")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fecRegistro;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "cod_recurso")
	private List<Herramienta> herramientas;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "cod_recurso")
	private List<Dependiente> dependientes;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "cod_recurso")
	private List<ContactoEmergencia> contactosEmergencia;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "cod_recurso")
	private List<Sueldo> sueldos;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "cod_recurso")
	private List<Skill> skills;
	
	@Transient
	private List<Float> totalHorasForecast;
	
	public Recurso() {
		herramientas = new ArrayList<Herramienta>();
		dependientes = new ArrayList<Dependiente>();
		contactosEmergencia = new ArrayList<ContactoEmergencia>();
		sueldos = new ArrayList<Sueldo>();
		skills = new ArrayList<Skill>();
	}

	@PrePersist
	public void prePersist() {
		fecRegistro = new Date();
		valMedicosAlergias="No";
		valMedicosEnfermedades="No";
		descMedicosEmbarazos="No";
		descMedicosCirugias="No";
		valActivo=1L;
		codTipoSangre=0L;
		
	}
	
	public Long getCodTipoSangre() {
		return codTipoSangre;
	}

	public void setCodTipoSangre(Long codTipoSangre) {
		this.codTipoSangre = codTipoSangre;
	}

	public List<Float> getTotalHorasForecast() {
		return totalHorasForecast;
	}

	public void setTotalHorasForecast(List<Float> totalHorasForecast) {
		this.totalHorasForecast = totalHorasForecast;
	}

	public Long getCodRecurso() {
		return codRecurso;
	}

	public void setCodRecurso(Long codRecurso) {
		this.codRecurso = codRecurso;
	}

	public String getDescRecurso() {
		return descRecurso;
	}

	public void setDescRecurso(String descRecurso) {
		this.descRecurso = descRecurso;
	}

	public String getDescApellidoPaterno() {
		return descApellidoPaterno;
	}

	public void setDescApellidoPaterno(String descApellidoPaterno) {
		this.descApellidoPaterno = descApellidoPaterno;
	}

	public String getDescApellidoMaterno() {
		return descApellidoMaterno;
	}

	public void setDescApellidoMaterno(String descApellidoMaterno) {
		this.descApellidoMaterno = descApellidoMaterno;
	}

	public String getDescCorreoElectronico() {
		return descCorreoElectronico;
	}

	public void setDescCorreoElectronico(String descCorreoElectronico) {
		this.descCorreoElectronico = descCorreoElectronico;
	}

	public String getDescTelCelular() {
		return descTelCelular;
	}

	public void setDescTelCelular(String descTelCelular) {
		this.descTelCelular = descTelCelular;
	}

	public String getDescTelEmpresa() {
		return descTelEmpresa;
	}

	public void setDescTelEmpresa(String descTelEmpresa) {
		this.descTelEmpresa = descTelEmpresa;
	}

	public String getDescRfc() {
		return descRfc;
	}

	public void setDescRfc(String descRfc) {
		this.descRfc = descRfc;
	}

	public String getDescTelExt() {
		return descTelExt;
	}

	public void setDescTelExt(String descTelExt) {
		this.descTelExt = descTelExt;
	}

	public String getDescCurp() {
		return descCurp;
	}

	public void setDescCurp(String descCurp) {
		this.descCurp = descCurp;
	}

	public Date getFecNacimiento() {
		return fecNacimiento;
	}

	public void setFecNacimiento(Date fecNacimiento) {
		this.fecNacimiento = fecNacimiento;
	}

	public String getDirCalle() {
		return dirCalle;
	}

	public void setDirCalle(String dirCalle) {
		this.dirCalle = dirCalle;
	}

	public String getDirNumeroExt() {
		return dirNumeroExt;
	}

	public void setDirNumeroExt(String dirNumeroExt) {
		this.dirNumeroExt = dirNumeroExt;
	}

	public String getDirNumeroInt() {
		return dirNumeroInt;
	}

	public void setDirNumeroInt(String dirNumeroInt) {
		this.dirNumeroInt = dirNumeroInt;
	}

	public String getDirColonia() {
		return dirColonia;
	}

	public void setDirColonia(String dirColonia) {
		this.dirColonia = dirColonia;
	}

	public String getDirCp() {
		return dirCp;
	}

	public void setDirCp(String dirCp) {
		this.dirCp = dirCp;
	}

	public String getDirAlcaldiaMunicipio() {
		return dirAlcaldiaMunicipio;
	}

	public void setDirAlcaldiaMunicipio(String dirAlcaldiaMunicipio) {
		this.dirAlcaldiaMunicipio = dirAlcaldiaMunicipio;
	}

	public String getDirEstado() {
		return dirEstado;
	}

	public void setDirEstado(String dirEstado) {
		this.dirEstado = dirEstado;
	}

	public String getDescNss() {
		return descNss;
	}

	public void setDescNss(String descNss) {
		this.descNss = descNss;
	}
	
	public Long getCodEstadoCivil() {
		return codEstadoCivil;
	}

	public void setCodEstadoCivil(Long codEstadoCivil) {
		this.codEstadoCivil = codEstadoCivil;
	}

	public Long getValNumeroHijos() {
		return valNumeroHijos;
	}

	public void setValNumeroHijos(Long valNumeroHijos) {
		this.valNumeroHijos = valNumeroHijos;
	}

	public Date getFecIngresoEmpresa() {
		return fecIngresoEmpresa;
	}

	public void setFecIngresoEmpresa(Date fecIngresoEmpresa) {
		this.fecIngresoEmpresa = fecIngresoEmpresa;
	}

	public Long getValActivo() {
		return valActivo;
	}

	public void setValActivo(Long valActivo) {
		this.valActivo = valActivo;
	}

	public Long getCodAreaRecurso() {
		return codAreaRecurso;
	}

	public void setCodAreaRecurso(Long codAreaRecurso) {
		this.codAreaRecurso = codAreaRecurso;
	}

	public AreaRecurso getArearecurso() {
		return arearecurso;
	}

	public void setArearecurso(AreaRecurso arearecurso) {
		this.arearecurso = arearecurso;
	}

	public Date getFecSalidaEmpresa() {
		return fecSalidaEmpresa;
	}

	public void setFecSalidaEmpresa(Date fecSalidaEmpresa) {
		this.fecSalidaEmpresa = fecSalidaEmpresa;
	}

	public String getDescMotivoSalida() {
		return descMotivoSalida;
	}

	public void setDescMotivoSalida(String descMotivoSalida) {
		this.descMotivoSalida = descMotivoSalida;
	}

	public Long getCodPuesto() {
		return codPuesto;
	}

	public void setCodPuesto(Long codPuesto) {
		this.codPuesto = codPuesto;
	}

	public Long getCodJornada() {
		return codJornada;
	}

	public void setCodJornada(Long codJornada) {
		this.codJornada = codJornada;
	}

	public Long getCodTipoRecurso() {
		return codTipoRecurso;
	}

	public void setCodTipoRecurso(Long codTipoRecurso) {
		this.codTipoRecurso = codTipoRecurso;
	}

	public Long getCodProveedor() {
		return codProveedor;
	}

	public void setCodProveedor(Long codProveedor) {
		this.codProveedor = codProveedor;
	}

	public Float getValCostoMinimo() {
		return valCostoMinimo;
	}

	public void setValCostoMinimo(Float valCostoMinimo) {
		this.valCostoMinimo = valCostoMinimo;
	}

	public String getDescEduGradoEscolaridad() {
		return descEduGradoEscolaridad;
	}

	public void setDescEduGradoEscolaridad(String descEduGradoEscolaridad) {
		this.descEduGradoEscolaridad = descEduGradoEscolaridad;
	}

	public String getDescEduEtapa() {
		return descEduEtapa;
	}

	public void setDescEduEtapa(String descEduEtapa) {
		this.descEduEtapa = descEduEtapa;
	}

	public String getDescEduInstitucionAcademica() {
		return descEduInstitucionAcademica;
	}

	public void setDescEduInstitucionAcademica(String descEduInstitucionAcademica) {
		this.descEduInstitucionAcademica = descEduInstitucionAcademica;
	}

	public String getDescEduDocumentoObtenido() {
		return descEduDocumentoObtenido;
	}

	public void setDescEduDocumentoObtenido(String descEduDocumentoObtenido) {
		this.descEduDocumentoObtenido = descEduDocumentoObtenido;
	}

	public String getDescBancoNombre() {
		return descBancoNombre;
	}

	public void setDescBancoNombre(String descBancoNombre) {
		this.descBancoNombre = descBancoNombre;
	}

	public String getDescBancoNumeroCuenta() {
		return descBancoNumeroCuenta;
	}

	public void setDescBancoNumeroCuenta(String descBancoNumeroCuenta) {
		this.descBancoNumeroCuenta = descBancoNumeroCuenta;
	}

	public String getDescBancoClabe() {
		return descBancoClabe;
	}

	public void setDescBancoClabe(String descBancoClabe) {
		this.descBancoClabe = descBancoClabe;
	}

	
	public String getDescMedicosAlergias() {
		return descMedicosAlergias;
	}

	public TipoSangre getTipoSangre() {
		return tipoSangre;
	}

	public void setTipoSangre(TipoSangre tipoSangre) {
		this.tipoSangre = tipoSangre;
	}

	public void setDescMedicosAlergias(String descMedicosAlergias) {
		this.descMedicosAlergias = descMedicosAlergias;
	}

	public String getDescMedicosEmbarazos() {
		return descMedicosEmbarazos;
	}

	public void setDescMedicosEmbarazos(String descMedicosEmbarazos) {
		this.descMedicosEmbarazos = descMedicosEmbarazos;
	}

	public String getDescMedicosCirugias() {
		return descMedicosCirugias;
	}

	public void setDescMedicosCirugias(String descMedicosCirugias) {
		this.descMedicosCirugias = descMedicosCirugias;
	}

	public String getDescMedicosEnfermedades() {
		return descMedicosEnfermedades;
	}

	public void setDescMedicosEnfermedades(String descMedicosEnfermedades) {
		this.descMedicosEnfermedades = descMedicosEnfermedades;
	}

	public Date getFecRegistro() {
		return fecRegistro;
	}

	public void setFecRegistro(Date fecRegistro) {
		this.fecRegistro = fecRegistro;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<Herramienta> getHerramientas() {
		return herramientas;
	}

	public void setHerramientas(List<Herramienta> herramientas) {
		this.herramientas = herramientas;
	}
	
	public void addHerramienta(Herramienta herramienta) {
		herramientas.add(herramienta);
	}

	public List<Dependiente> getDependientes() {
		return dependientes;
	}

	public void setDependientes(List<Dependiente> dependientes) {
		this.dependientes = dependientes;
	}

	public List<ContactoEmergencia> getContactosEmergencia() {
		return contactosEmergencia;
	}

	public void setContactosEmergencia(List<ContactoEmergencia> contactosEmergencia) {
		this.contactosEmergencia = contactosEmergencia;
	}

	public List<Skill> getSkills() {
		return skills;
	}

	public void setSkills(List<Skill> skills) {
		this.skills = skills;
	}

	public List<Sueldo> getSueldos() {
		return sueldos;
	}

	public void setSueldos(List<Sueldo> sueldos) {
		this.sueldos = sueldos;
	}

	public byte[] getFoto() {
		return foto;
	}

	public void setFoto(byte[] foto) {
		this.foto = foto;
	}

	public byte[] getFotoCv() {
		return fotoCv;
	}

	public void setFotoCv(byte[] fotoCv) {
		this.fotoCv = fotoCv;
	}

	public Long getValAprobador() {
		return valAprobador;
	}

	public void setValAprobador(Long valAprobador) {
		this.valAprobador = valAprobador;
	}

	public Long getValResponsable() {
		return valResponsable;
	}

	public void setValResponsable(Long valResponsable) {
		this.valResponsable = valResponsable;
	}

	public Puesto getPuesto() {
		return puesto;
	}

	public void setPuesto(Puesto puesto) {
		this.puesto = puesto;
	}

	public Jornada getJornada() {
		return jornada;
	}

	public void setJornada(Jornada jornada) {
		this.jornada = jornada;
	}

	public TipoRecurso getTiporecurso() {
		return tiporecurso;
	}

	public void setTiporecurso(TipoRecurso tiporecurso) {
		this.tiporecurso = tiporecurso;
	}

	public Proveedor getProveedor() {
		return proveedor;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

	public EstadoCivil getEstadoCivil() {
		return estadoCivil;
	}

	public void setEstadoCivil(EstadoCivil estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	public String getDescCorreoPersonal() {
		return descCorreoPersonal;
	}

	public void setDescCorreoPersonal(String descCorreoPersonal) {
		this.descCorreoPersonal = descCorreoPersonal;
	}

	public String getDescGenero() {
		return descGenero;
	}

	public void setDescGenero(String descGenero) {
		this.descGenero = descGenero;
	}

	public Long getCodPerfil() {
		return codPerfil;
	}

	public void setCodPerfil(Long codPerfil) {
		this.codPerfil = codPerfil;
	}

	public PerfilRecurso getPerfilRecurso() {
		return perfilRecurso;
	}

	public void setPerfilRecurso(PerfilRecurso perfilRecurso) {
		this.perfilRecurso = perfilRecurso;
	}

	public String getValMedicosAlergias() {
		return valMedicosAlergias;
	}

	public void setValMedicosAlergias(String valMedicosAlergias) {
		this.valMedicosAlergias = valMedicosAlergias;
	}

	public String getValMedicosEnfermedades() {
		return valMedicosEnfermedades;
	}

	public void setValMedicosEnfermedades(String valMedicosEnfermedades) {
		this.valMedicosEnfermedades = valMedicosEnfermedades;
	}
	
}