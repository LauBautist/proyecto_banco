// EmpleadoServiceJpa.java
package edu.productos.diana.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import edu.productos.diana.model.Empleado;
import edu.productos.diana.repository.EmpleadoRepository;

@Service
@Primary
public class EmpleadoServiceJpa implements EmpleadoService {

	@Autowired
	private EmpleadoRepository empleadoRepository;

	@Override
	public List<Empleado> buscarTodos() {
		return empleadoRepository.findAll();
	}

	@Override
	public Empleado buscarPorId(Integer idEmpleado) {
		Optional<Empleado> opt = empleadoRepository.findById(idEmpleado);
		return opt.orElse(null);
	}

	@Override
	public void guardar(Empleado empleado) {
		empleadoRepository.save(empleado);
	}

	@Override
	public void eliminar(Integer idEmpleado) {
		empleadoRepository.deleteById(idEmpleado);
	}

	@Override
	public Empleado buscarPorUsuarioUsername(String username) {
		return empleadoRepository.findByUsuarioUsername(username);
	}

}
