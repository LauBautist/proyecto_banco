package edu.productos.diana.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.productos.diana.model.Cliente;
import edu.productos.diana.model.Prestamo;

public interface PrestamoRepository extends JpaRepository<Prestamo, Integer> {

	// Buscar préstamos por cliente
	List<Prestamo> findByCliente(Cliente cliente);
}
