package edu.productos.diana.service;

import java.util.List;
import edu.productos.diana.model.Empleado;

public interface EmpleadoService {
	List<Empleado> buscarTodos();

	Empleado buscarPorId(Integer idEmpleado);

	void guardar(Empleado empleado);

	void eliminar(Integer idEmpleado);

	Empleado buscarPorUsuarioUsername(String username);

}
