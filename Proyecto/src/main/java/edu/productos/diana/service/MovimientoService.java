package edu.productos.diana.service;

import java.util.List;

import edu.productos.diana.model.Cuenta;
import edu.productos.diana.model.Empleado;
import edu.productos.diana.model.Movimiento;

public interface MovimientoService {

	List<Movimiento> buscarTodos();

	Movimiento buscarPorId(Integer idMovimiento);

	void guardarMovimiento(Movimiento movimiento);

	void eliminarMovimiento(Integer idMovimiento);

	// Nuevo método para obtener los movimientos de una cuenta
	List<Movimiento> buscarPorCuenta(Cuenta cuenta);

	void guardarMovimiento(Movimiento movimiento, Empleado empleado);

}
