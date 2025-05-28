package edu.productos.diana.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "pago")
public class Pago {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TipoPago tipo;

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal monto;

	@Column(nullable = false)
	private LocalDate fecha;

	@ManyToOne
	@JoinColumn(name = "idPrestamo")
	private Prestamo prestamo;

	@ManyToOne
	@JoinColumn(name = "registradoPor")
	private Empleado registradoPor;

	// En Pago.java
	@ManyToOne
	@JoinColumn(name = "idUsuario")
	private Usuario usuario;

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	// Enum que coincide con los valores del ENUM en MySQL
	public enum TipoPago {
		Parcial, Total
	}

	// Getters y setters

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public TipoPago getTipo() {
		return tipo;
	}

	public void setTipo(TipoPago tipo) {
		this.tipo = tipo;
	}

	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public Prestamo getPrestamo() {
		return prestamo;
	}

	public void setPrestamo(Prestamo prestamo) {
		this.prestamo = prestamo;
	}

	public Empleado getRegistradoPor() {
		return registradoPor;
	}

	public void setRegistradoPor(Empleado registradoPor) {
		this.registradoPor = registradoPor;
	}

	@Override
	public String toString() {
		return "Pago [id=" + id + ", tipo=" + tipo + ", monto=" + monto + ", fecha=" + fecha + ", prestamo=" + prestamo
				+ ", registradoPor=" + registradoPor + "]";
	}
}
