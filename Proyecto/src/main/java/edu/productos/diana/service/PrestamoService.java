package edu.productos.diana.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import edu.productos.diana.model.Cliente;
import edu.productos.diana.model.Cuota;
import edu.productos.diana.model.Empleado;
import edu.productos.diana.model.Prestamo;

public interface PrestamoService {

	List<Prestamo> buscarTodos();

	Prestamo buscarPorId(Integer id);

	void guardarPrestamo(Prestamo prestamo);

	void guardarPrestamo(Prestamo prestamo, Empleado aprobadoPor);

	void eliminarPrestamo(Integer id);

	List<Prestamo> buscarPorCliente(Cliente cliente);

	List<Cuota> calcularCuotas(BigDecimal monto, BigDecimal interes, Integer plazo, LocalDate fechaInicio);

}
