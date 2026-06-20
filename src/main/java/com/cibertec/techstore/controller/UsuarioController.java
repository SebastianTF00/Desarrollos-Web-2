package com.cibertec.techstore.controller;

import com.cibertec.techstore.model.Usuario;
import com.cibertec.techstore.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // 1. Mostrar la lista de usuarios
    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioRepository.findAll());
        return "usuarios"; // Renderiza templates/usuarios.html
    }

    // 2. Mostrar formulario para Nuevo Usuario
    @GetMapping("/usuario/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "formulario-usuario"; // Renderiza templates/formulario-usuario.html
    }

    // 3. Guardar o Actualizar Usuario
    @PostMapping("/usuario/guardar")
    public String guardarUsuario(@ModelAttribute Usuario usuario) {
        // Asignar rol por defecto si no se seleccionó ninguno
        if (usuario.getRol() == null || usuario.getRol().isEmpty()) {
            usuario.setRol("CLIENTE");
        }
        usuarioRepository.save(usuario);
        return "redirect:/usuarios";
    }

    // 4. Mostrar formulario para Editar Usuario
    @GetMapping("/usuario/editar/{id}")
    public String editarUsuario(@PathVariable("id") Integer id, Model model) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario == null) {
            return "redirect:/usuarios";
        }
        model.addAttribute("usuario", usuario);
        return "formulario-usuario";
    }

    // 5. Eliminar Usuario
    @GetMapping("/usuario/eliminar/{id}")
    public String eliminarUsuario(@PathVariable("id") Integer id) {
        usuarioRepository.deleteById(id);
        return "redirect:/usuarios";
    }
}