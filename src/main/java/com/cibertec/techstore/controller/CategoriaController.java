package com.cibertec.techstore.controller;

import com.cibertec.techstore.model.Categoria;
import com.cibertec.techstore.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    // 1. Mostrar la lista de categorías
    @GetMapping("/categorias")
    public String listarCategorias(Model model) {
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "categorias"; // Renderiza templates/categorias.html
    }

    // 2. Mostrar formulario para Nueva Categoría
    @GetMapping("/categoria/nueva")
    public String mostrarFormularioNueva(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "formulario-categoria"; // Renderiza templates/formulario-categoria.html
    }

    // 3. Guardar o Actualizar Categoría
    @PostMapping("/categoria/guardar")
    public String guardarCategoria(@ModelAttribute Categoria categoria) {
        categoriaRepository.save(categoria);
        return "redirect:/categorias";
    }

    // 4. Mostrar formulario para Editar Categoría
    @GetMapping("/categoria/editar/{id}")
    public String editarCategoria(@PathVariable("id") Integer id, Model model) {
        Categoria categoria = categoriaRepository.findById(id).orElse(null);
        if (categoria == null) {
            return "redirect:/categorias";
        }
        model.addAttribute("categoria", categoria);
        return "formulario-categoria";
    }

    // 5. Eliminar Categoría
    @GetMapping("/categoria/eliminar/{id}")
    public String eliminarCategoria(@PathVariable("id") Integer id) {
        categoriaRepository.deleteById(id);
        return "redirect:/categorias";
    }
}