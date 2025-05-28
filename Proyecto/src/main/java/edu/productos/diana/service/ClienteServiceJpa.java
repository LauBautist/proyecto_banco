package edu.productos.diana.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import edu.productos.diana.model.Cliente;
import edu.productos.diana.repository.ClienteRepository;

@Service
@Primary
public class ClienteServiceJpa implements ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;

	@Override
	public List<Cliente> buscarTodos() {
		return clienteRepository.findAll();
	}

	@Override
	public Cliente buscarPorId(Integer idCliente) {
		Optional<Cliente> optional = clienteRepository.findById(idCliente);
		return optional.orElse(null);
	}

	@Override
	public void guardarCliente(Cliente cliente) {
		clienteRepository.save(cliente);
	}

	@Override
	public void eliminarCliente(Integer idCliente) {
		clienteRepository.deleteById(idCliente);
	}
}
