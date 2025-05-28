package edu.productos.diana.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "cuota")
public class Cuota {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private int numero;

	private BigDecimal capital;
	private BigDecimal interes;
	private BigDecimal total;
	@Column(precision = 10, scale = 2)
	private BigDecimal montoPagado = BigDecimal.ZERO;

	@Column(name = "fecha_pago")
	private LocalDate fechaPago;

	@Enumerated(EnumType.STRING)
	private EstadoCuota estado = EstadoCuota.PENDIENTE;

	@Column(name = "forma_pago")
	private String formaPago = "MENSUAL";

	private String alerta = "-";

	private boolean pagada = false;

	@ManyToOne
	@JoinColumn(name = "id_prestamo")
	private Prestamo prestamo;

	// Enum interno
	public enum EstadoCuota {
		PENDIENTE, PAGADO
	}

	@Column(precision = 10, scale = 2)
	private BigDecimal interesMora = BigDecimal.ZERO;

	// Getters y setters

	public BigDecimal getInteresMora() {
		return interesMora;
	}

	public void setInteresMora(BigDecimal interesMora) {
		this.interesMora = interesMora;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public BigDecimal getCapital() {
		return capital;
	}

	public void setCapital(BigDecimal capital) {
		this.capital = capital;
	}

	public BigDecimal getInteres() {
		return interes;
	}

	public void setInteres(BigDecimal interes) {
		this.interes = interes;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public LocalDate getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(LocalDate fechaPago) {
		this.fechaPago = fechaPago;
	}

	public EstadoCuota getEstado() {
		return estado;
	}

	public void setEstado(EstadoCuota estado) {
		this.estado = estado;
	}

	public String getFormaPago() {
		return formaPago;
	}

	public void setFormaPago(String formaPago) {
		this.formaPago = formaPago;
	}

	public String getAlerta() {
		return alerta;
	}

	public void setAlerta(String alerta) {
		this.alerta = alerta;
	}

	public boolean isPagada() {
		return pagada;
	}

	public void setPagada(boolean pagada) {
		this.pagada = pagada;
	}

	public Prestamo getPrestamo() {
		return prestamo;
	}

	public void setPrestamo(Prestamo prestamo) {
		this.prestamo = prestamo;
	}

	public BigDecimal getMontoPagado() {
		return montoPagado;
	}

	public void setMontoPagado(BigDecimal montoPagado) {
		this.montoPagado = montoPagado;
	}

}
