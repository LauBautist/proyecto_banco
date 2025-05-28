package edu.productos.diana.service;

import java.util.List;

import edu.productos.diana.model.Cliente;

public interface ClienteService {

	List<Cliente> buscarTodos();

	Cliente buscarPorId(Integer idCliente);

	void guardarCliente(Cliente cliente);

	void eliminarCliente(Integer idCliente);

}
