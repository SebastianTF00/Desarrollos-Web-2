package com.cibertec.techstore.service.impl;

import com.cibertec.techstore.model.Categoria;
import com.cibertec.techstore.repository.CategoriaRepository;
import com.cibertec.techstore.service.ICategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaServiceImpl implements ICategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public List<Categoria> listarTodos() {
        return categoriaRepository.findAll(); // Busca todas las categorías en MySQL Workbench
    }
}