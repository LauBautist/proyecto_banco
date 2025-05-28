package edu.productos.diana.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cliente")
public class Cliente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "nombre", nullable = false)
	private String nombre;

	@Column(name = "apellido", nullable = false)
	private String apellido;

	@Column(name = "curp", unique = true, nullable = false, length = 18)
	private String curp;

	@Column(name = "direccion")
	private String direccion;

	@Column(name = "telefono")
	private String telefono;

	@Column(name = "correo")
	private String correo;

	@Enumerated(EnumType.STRING)
	@Column(name = "tipoCliente")
	private TipoCliente tipoCliente = TipoCliente.no_socio;

	@Enumerated(EnumType.STRING)
	@Column(name = "estadoCliente")
	private EstadoCliente estadoCliente = EstadoCliente.activo;

	@Column(name = "fotoCliente")
	private String fotoCliente = "no-foto.png";

	@OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
	private java.util.List<Cuenta> cuentas;

	public java.util.List<Cuenta> getCuentas() {
		return cuentas;
	}

	public void setCuentas(java.util.List<Cuenta> cuentas) {
		this.cuentas = cuentas;
	}

	// Enums adaptados para que coincidan con los valores en minúsculas de MySQL
	public enum TipoCliente {
		socio, no_socio
	}

	public enum EstadoCliente {
		activo, inactivo
	}

	// Getters y setters

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getCurp() {
		return curp;
	}

	public void setCurp(String curp) {
		this.curp = curp;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public TipoCliente getTipoCliente() {
		return tipoCliente;
	}

	public void setTipoCliente(TipoCliente tipoCliente) {
		this.tipoCliente = tipoCliente;
	}

	public EstadoCliente getEstadoCliente() {
		return estadoCliente;
	}

	public void setEstadoCliente(EstadoCliente estadoCliente) {
		this.estadoCliente = estadoCliente;
	}

	public String getFotoCliente() {
		return fotoCliente;
	}

	public void setFotoCliente(String fotoCliente) {
		this.fotoCliente = fotoCliente;
	}

	@Override
	public String toString() {
		return "Cliente [id=" + id + ", nombre=" + nombre + ", apellido=" + apellido + ", curp=" + curp + ", direccion="
				+ direccion + ", telefono=" + telefono + ", correo=" + correo + ", tipoCliente=" + tipoCliente
				+ ", estadoCliente=" + estadoCliente + ", fotoCliente=" + fotoCliente + "]";
	}
}
