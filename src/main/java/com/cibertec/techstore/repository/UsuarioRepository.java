package com.cibertec.techstore.repository;

import com.cibertec.techstore.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    // Listo, JpaRepository nos regala todos los métodos CRUD automáticamente
    Usuario findByCorreo(String correo);
}