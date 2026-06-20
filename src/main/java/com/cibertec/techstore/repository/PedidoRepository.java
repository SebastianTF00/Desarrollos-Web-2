package com.cibertec.techstore.repository;

import com.cibertec.techstore.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    // JpaRepository ya te incluye: findAll(), save(), findById(), deleteById()
}