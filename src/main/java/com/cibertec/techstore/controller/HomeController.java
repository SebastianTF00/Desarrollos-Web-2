package com.cibertec.techstore.controller;

import com.cibertec.techstore.model.Producto;
import com.cibertec.techstore.model.Usuario; // ¡Importación agregada!
import com.cibertec.techstore.repository.CategoriaRepository;
import com.cibertec.techstore.repository.UsuarioRepository;
import com.cibertec.techstore.service.IProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private IProductoService productoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    // --- CATÁLOGO PRINCIPAL (TIENDA) ---
    @GetMapping("/")
    public String index(Model model) {
        // Simulamos la lista de productos basada en el SQL para la vista de inicio
        List<Producto> destacados = new ArrayList<>();

        Producto p1 = new Producto();
        p1.setIdProducto(1);
        p1.setNombre("AMD Ryzen 7 5700X");
        p1.setDescripcion("8 núcleos, 16 hilos, 3.4GHz base, socket AM4");
        p1.setPrecio(new BigDecimal("195.00"));
        p1.setStock(15);
        p1.setImagenUrl("https://img.freepik.com/vector-gratis/chip-procesador-tecnologia-informatica-vector-microchip-cpu_53876-172551.jpg");

        Producto p2 = new Producto();
        p2.setIdProducto(2);
        p2.setNombre("NVIDIA RTX 4060 Ti 8GB");
        p2.setDescripcion("Arquitectura Ada Lovelace, DLSS 3, Ray Tracing");
        p2.setPrecio(new BigDecimal("399.99"));
        p2.setStock(8);
        p2.setImagenUrl("https://img.freepik.com/vector-gratis/tarjeta-video-realista-ilustracion-aislada_1284-60195.jpg");

        Producto p3 = new Producto();
        p3.setIdProducto(3);
        p3.setNombre("Mouse Gamer Inalámbrico Pro");
        p3.setDescripcion("Sensor de alta precisión 16000 DPI, tasa 1000Hz, batería recargable");
        p3.setPrecio(new BigDecimal("75.50"));
        p3.setStock(30);
        p3.setImagenUrl("https://img.freepik.com/vector-gratis/raton-ordenador-estilo-realista_1284-14227.jpg");

        destacados.add(p1);
        destacados.add(p2);
        destacados.add(p3);

        model.addAttribute("productos", destacados);
        model.addAttribute("tituloPagina", "TechStore - Inicio");

        return "inicio";
    }

    // --- CONTROL DE LOGIN ÚNICO ---
    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam("correo") String correo,
                                @RequestParam("password") String password,
                                Model model) {

        // Buscamos el usuario en la base de datos por su correo
        Usuario usuario = usuarioRepository.findByCorreo(correo);

        // Validación real de credenciales
        if (usuario != null && usuario.getContrasenia().equals(password)) {
            // Si el usuario es ADMIN, va al inventario de frente
            if ("ADMIN".equals(usuario.getRol())) {
                return "redirect:/inventario";
            }
            // Si es un cliente común, lo mandamos al catálogo de la tienda
            return "redirect:/";
        }

        // Si falla, volvemos a pintar el login con el mensaje de error personalizado
        model.addAttribute("error", "Credenciales incorrectas. Acceso denegado.");
        return "login";
    }

    @GetMapping("/inventario")
    public String gestionInventario(Model model) {
        List<Producto> listaProductos = productoService.listarTodos();

        // Si la lista de la BD viene completamente vacía, evitamos que rompa las tarjetas superiores
        if (listaProductos == null) {
            listaProductos = new ArrayList<>();
        }

        model.addAttribute("productos", listaProductos);

        // KPIs protegidos contra campos NULL en la base de datos de Railway
        long totalProductos = listaProductos.size();

        long bajoStock = listaProductos.stream()
                .filter(p -> p != null && p.getStock() != null && p.getStock() < 5)
                .count();

        model.addAttribute("totalProductos", totalProductos);
        model.addAttribute("bajoStock", bajoStock);

        return "inventario";
    }

    @GetMapping("/carrito")
    public String mostrarCarrito() {
        return "carrito";
    }

    // --- CRUD DE PRODUCTOS ---
    @GetMapping("/producto/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "nuevo-producto";
    }

    @PostMapping("/producto/guardar")
    public String guardarProducto(@ModelAttribute Producto producto) {
        productoService.guardar(producto);
        return "redirect:/inventario";
    }

    @GetMapping("/producto/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") Integer id, Model model) {
        Producto producto = productoService.obtenerPorId(id);

        if (producto == null) {
            return "redirect:/inventario";
        }

        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "editar-producto";
    }

    @GetMapping("/producto/eliminar/{id}")
    public String eliminarProducto(@PathVariable("id") Integer id) {
        productoService.eliminar(id);
        return "redirect:/inventario";
    }
}