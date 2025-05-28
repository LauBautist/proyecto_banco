package edu.productos.diana.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.productos.diana.model.Cuota;

public interface CuotaRepository extends JpaRepository<Cuota, Integer> {

	List<Cuota> findByPrestamoIdOrderByNumeroAsc(Integer idPrestamo);

	List<Cuota> findByPagadaTrue();

}
