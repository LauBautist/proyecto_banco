package edu.productos.diana.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.productos.diana.model.Pago;

public interface PagoRepository extends JpaRepository<Pago, Integer> {
	List<Pago> findByPrestamoIdAndFecha(Integer idPrestamo, LocalDate fecha);

}
