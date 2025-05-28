package edu.productos.diana.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import edu.productos.diana.model.Empleado;

public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {
	Empleado findByUsuarioUsername(String username);

}
