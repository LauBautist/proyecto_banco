package edu.productos.diana.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.productos.diana.model.Perfil;

public interface PerfilRepository extends JpaRepository<Perfil, Integer> {

	Perfil findByPerfil(String perfil);

}
