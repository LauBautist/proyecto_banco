package edu.productos.diana.service;

import java.util.List;

import edu.productos.diana.model.Cliente;
import edu.productos.diana.model.Cuenta;

public interface CuentaService {

	List<Cuenta> buscarTodas();

	Cuenta buscarPorId(Integer idCuenta);

	void guardarCuenta(Cuenta cuenta);

	void eliminarCuenta(Integer idCuenta);

	List<Cuenta> buscarPorIdCliente(Integer idCliente);

	List<Cuenta> buscarPorCliente(Cliente cliente);

}
