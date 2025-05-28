package edu.productos.diana.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "movimiento")
public class Movimiento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Enumerated(EnumType.STRING)
	@Column(name = "tipo", nullable = false)
	private TipoMovimiento tipo;

	@Column(name = "monto", nullable = false)
	private BigDecimal monto;

	@Column(name = "fecha", nullable = false)
	private LocalDate fecha;

	@Enumerated(EnumType.STRING)
	@Column(name = "estado", nullable = false)
	private EstadoMovimiento estado = EstadoMovimiento.Completado;

	@ManyToOne
	@JoinColumn(name = "idCuenta", nullable = false)
	private Cuenta cuenta;

	// ENUMS internos
	public enum TipoMovimiento {
		Deposito, Retiro
	}

	public enum EstadoMovimiento {
		Completado, Cancelado
	}

	@ManyToOne
	@JoinColumn(name = "registradoPor")
	private Empleado registradoPor;

	@ManyToOne
	@JoinColumn(name = "idUsuario")
	private Usuario usuario;

	// Getters y Setters

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Empleado getRegistradoPor() {
		return registradoPor;
	}

	public void setRegistradoPor(Empleado registradoPor) {
		this.registradoPor = registradoPor;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public TipoMovimiento getTipo() {
		return tipo;
	}

	public void setTipo(TipoMovimiento tipo) {
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

	public EstadoMovimiento getEstado() {
		return estado;
	}

	public void setEstado(EstadoMovimiento estado) {
		this.estado = estado;
	}

	public Cuenta getCuenta() {
		return cuenta;
	}

	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}

	@Override
	public String toString() {
		return "Movimiento [id=" + id + ", tipo=" + tipo + ", monto=" + monto + ", fecha=" + fecha + ", estado="
				+ estado + ", cuenta=" + cuenta + ", registradoPor=" + registradoPor + "]";
	}
}
