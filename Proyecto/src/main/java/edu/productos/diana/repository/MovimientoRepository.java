package edu.productos.diana.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.productos.diana.model.Cuenta;
import edu.productos.diana.model.Movimiento;

public interface MovimientoRepository extends JpaRepository<Movimiento, Integer> {
	
	List<Movimiento> findByCuenta(Cuenta cuenta);
}
