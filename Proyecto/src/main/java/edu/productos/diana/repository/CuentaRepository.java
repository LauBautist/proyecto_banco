package edu.productos.diana.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.productos.diana.model.Cliente;
import edu.productos.diana.model.Cuenta;

public interface CuentaRepository extends JpaRepository<Cuenta, Integer> {

	List<Cuenta> findByClienteId(Integer idCliente);

	List<Cuenta> findByCliente(Cliente cliente);

}
