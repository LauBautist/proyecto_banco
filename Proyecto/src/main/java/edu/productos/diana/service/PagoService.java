package edu.productos.diana.service;

import java.time.LocalDate;
import java.util.List;

import edu.productos.diana.model.Pago;

public interface PagoService {
	List<Pago> buscarTodos();

	Pago buscarPorId(Integer id);

	void guardar(Pago pago);

	void eliminar(Integer id);

	List<Pago> buscarPorPrestamoYFecha(Integer idPrestamo, LocalDate fecha);

}
