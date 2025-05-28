package edu.productos.diana.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import edu.productos.diana.model.Cliente;
import edu.productos.diana.model.Cuenta;
import edu.productos.diana.repository.CuentaRepository;

@Service
@Primary
public class CuentaServiceJpa implements CuentaService {

	@Autowired
	private CuentaRepository cuentaRepository;

	@Override
	public List<Cuenta> buscarTodas() {
		return cuentaRepository.findAll();
	}

	@Override
	public Cuenta buscarPorId(Integer idCuenta) {
		Optional<Cuenta> optional = cuentaRepository.findById(idCuenta);
		return optional.orElse(null);
	}

	@Override
	public void guardarCuenta(Cuenta cuenta) {
		cuentaRepository.save(cuenta);
	}

	@Override
	public void eliminarCuenta(Integer idCuenta) {
		cuentaRepository.deleteById(idCuenta);
	}

	@Override
	public List<Cuenta> buscarPorIdCliente(Integer idCliente) {
		return cuentaRepository.findByClienteId(idCliente);
	}

	@Override
	public List<Cuenta> buscarPorCliente(Cliente cliente) {
		return cuentaRepository.findByCliente(cliente);
	}
}
