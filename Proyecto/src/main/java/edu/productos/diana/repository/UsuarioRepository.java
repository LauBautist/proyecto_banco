package edu.productos.diana.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.productos.diana.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

	Usuario findByUsername(String username);

}
