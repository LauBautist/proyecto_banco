package edu.productos.diana.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import edu.productos.diana.model.Cuenta;
import edu.productos.diana.model.Empleado;
import edu.productos.diana.model.Movimiento;
import edu.productos.diana.repository.MovimientoRepository;

@Service
@Primary
public class MovimientoServiceJpa implements MovimientoService {

	@Autowired
	private MovimientoRepository movimientoRepository;

	@Override
	public List<Movimiento> buscarTodos() {
		return movimientoRepository.findAll();
	}

	@Override
	public Movimiento buscarPorId(Integer idMovimiento) {
		Optional<Movimiento> optional = movimientoRepository.findById(idMovimiento);
		return optional.orElse(null);
	}

	@Override
	public void guardarMovimiento(Movimiento movimiento) {
		movimientoRepository.save(movimiento);
	}

	@Override
	public void eliminarMovimiento(Integer idMovimiento) {
		movimientoRepository.deleteById(idMovimiento);
	}

	@Override
	public List<Movimiento> buscarPorCuenta(Cuenta cuenta) {
		return movimientoRepository.findByCuenta(cuenta);
	}

	@Override
	public void guardarMovimiento(Movimiento movimiento, Empleado empleado) {
		movimiento.setRegistradoPor(empleado);
		movimientoRepository.save(movimiento);
	}
}
