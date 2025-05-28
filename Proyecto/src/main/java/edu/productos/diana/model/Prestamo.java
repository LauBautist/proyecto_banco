package edu.productos.diana.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "prestamo")
public class Prestamo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	private BigDecimal monto;

	@Column(nullable = false)
	private BigDecimal interes;

	@Column(nullable = false)
	private Integer plazo; // en meses

	@Column(nullable = false)
	private LocalDate fechaAprobacion;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private EstadoPrestamo estado;

	@ManyToOne
	@JoinColumn(name = "idCliente", nullable = false)
	private Cliente cliente;

	@ManyToOne
	@JoinColumn(name = "aprobadoPor")
	private Empleado aprobadoPor;

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@ManyToOne
	@JoinColumn(name = "idUsuario")
	private Usuario usuario;

	public enum EstadoPrestamo {
		Activo, Pagado, Vencido
	}

	@OneToMany(mappedBy = "prestamo", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Cuota> cuotas;

	public List<Cuota> getCuotas() {
		return cuotas;
	}

	public void setCuotas(List<Cuota> cuotas) {
		this.cuotas = cuotas;
	}

	// Getters y Setters

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	public BigDecimal getInteres() {
		return interes;
	}

	public void setInteres(BigDecimal interes) {
		this.interes = interes;
	}

	public Integer getPlazo() {
		return plazo;
	}

	public void setPlazo(Integer plazo) {
		this.plazo = plazo;
	}

	public LocalDate getFechaAprobacion() {
		return fechaAprobacion;
	}

	public void setFechaAprobacion(LocalDate fechaAprobacion) {
		this.fechaAprobacion = fechaAprobacion;
	}

	public EstadoPrestamo getEstado() {
		return estado;
	}

	public void setEstado(EstadoPrestamo estado) {
		this.estado = estado;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Empleado getAprobadoPor() {
		return aprobadoPor;
	}

	public void setAprobadoPor(Empleado aprobadoPor) {
		this.aprobadoPor = aprobadoPor;
	}

	@Override
	public String toString() {
		return "Prestamo [id=" + id + ", monto=" + monto + ", interes=" + interes + ", plazo=" + plazo
				+ ", fechaAprobacion=" + fechaAprobacion + ", estado=" + estado + ", cliente=" + cliente
				+ ", aprobadoPor=" + aprobadoPor + "]";
	}
}
