package edu.productos.diana.model;

public class CuentaDTO {

	private Integer id;
	private String numeroCuenta;
	private String tipoCuenta;

	public CuentaDTO() {
	}

	public CuentaDTO(Cuenta cuenta) {
		this.id = cuenta.getId();
		this.numeroCuenta = cuenta.getNumeroCuenta();
		this.tipoCuenta = cuenta.getTipoCuenta().name(); // Convierte el enum a String
	}

	// Getters y Setters
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNumeroCuenta() {
		return numeroCuenta;
	}

	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}

	public String getTipoCuenta() {
		return tipoCuenta;
	}

	public void setTipoCuenta(String tipoCuenta) {
		this.tipoCuenta = tipoCuenta;
	}
}
