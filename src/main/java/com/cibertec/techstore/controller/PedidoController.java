package com.cibertec.techstore.controller;

import com.cibertec.techstore.model.Pedido;
import com.cibertec.techstore.repository.PedidoRepository;
import com.cibertec.techstore.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // 1. Mostrar la lista de pedidos
    @GetMapping("/pedidos")
    public String listarPedidos(Model model) {
        model.addAttribute("pedidos", pedidoRepository.findAll());
        return "pedidos"; // Renderiza templates/pedidos.html
    }

    // 2. Mostrar formulario para Nuevo Pedido (Manual)
    @GetMapping("/pedido/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("pedido", new Pedido());
        model.addAttribute("usuarios", usuarioRepository.findAll()); // Para elegir el cliente
        return "formulario-pedido"; // Renderiza templates/formulario-pedido.html
    }

    // 3. Guardar o Actualizar Pedido
    @PostMapping("/pedido/guardar")
    public String guardarPedido(@ModelAttribute Pedido pedido) {
        // Si no se envía estado, por defecto será PENDIENTE
        if (pedido.getEstado() == null || pedido.getEstado().isEmpty()) {
            pedido.setEstado("PENDIENTE");
        }
        pedidoRepository.save(pedido);
        return "redirect:/pedidos";
    }

    // 4. Mostrar formulario para Editar Pedido
    @GetMapping("/pedido/editar/{id}")
    public String editarPedido(@PathVariable("id") Integer id, Model model) {
        Pedido pedido = pedidoRepository.findById(id).orElse(null);
        if (pedido == null) {
            return "redirect:/pedidos";
        }
        model.addAttribute("pedido", pedido);
        model.addAttribute("usuarios", usuarioRepository.findAll());
        return "formulario-pedido";
    }

    // 5. Eliminar Pedido
    @GetMapping("/pedido/eliminar/{id}")
    public String eliminarPedido(@PathVariable("id") Integer id) {
        pedidoRepository.deleteById(id);
        return "redirect:/pedidos";
    }
}