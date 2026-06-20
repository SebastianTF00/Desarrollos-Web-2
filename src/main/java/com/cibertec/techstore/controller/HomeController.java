package com.cibertec.techstore.controller;

import com.cibertec.techstore.repository.CategoriaRepository;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.cibertec.techstore.model.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cibertec.techstore.service.IProductoService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private IProductoService productoService;

    @GetMapping("/")
    public String index(Model model) {
        // SIMULAMOS LA LISTA DE PRODUCTOS BASADA EN EL SQL
        List<Producto> destacados = new ArrayList<>();
        
        Producto p1 = new Producto();
        p1.setIdProducto(1);
        p1.setNombre("AMD Ryzen 7 5700X");
        p1.setDescripcion("8 núcleos, 16 hilos, 3.4GHz base, socket AM4");
        p1.setPrecio(new BigDecimal("195.00")); // Usamos BigDecimal según tu modelo
        p1.setStock(15);
        p1.setImagenUrl("https://img.freepik.com/vector-gratis/chip-procesador-tecnologia-informatica-vector-microchip-cpu_53876-172551.jpg"); // Imagen referencial

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

        // Enviamos la lista a la vista
        model.addAttribute("productos", destacados);
        model.addAttribute("tituloPagina", "TechStore - Inicio");
        
        return "inicio"; // Renderiza inicio.html
    }
    @GetMapping("/inventario")
    public String gestionInventario(Model model) {

        List<Producto> listaProductos = productoService.listarTodos();

        model.addAttribute("productos", listaProductos);

        long totalProductos = listaProductos.size();
        long bajoStock = listaProductos.stream().filter(p -> p.getStock() < 5).count();

        model.addAttribute("totalProductos", totalProductos);
        model.addAttribute("bajoStock", bajoStock);

        return "inventario"; // Renderiza inventario.html dentro de templates
    }

    // 1. Mostrar la página de Login
    @GetMapping("/login")
    public String mostrarLogin() {
        return "login"; // Renderiza templates/login.html
    }

    // 2. Procesar el formulario de Login
    @PostMapping("/login")
    public String procesarLogin(@RequestParam("correo") String correo, 
                                @RequestParam("contrasenia") String contrasenia, 
                                Model model) {
        
        // Simulación de validación para este avance (luego se conectará a la BD real)
        if ("admin@techstore.com".equals(correo) && "admin123".equals(contrasenia)) {
            // Si es correcto, lo enviamos de frente a la vista de inventario
            return "redirect:/inventario";
        }
        
        // Si falla, recargamos el login enviando un mensaje de error
        model.addAttribute("error", "Credenciales incorrectas. Acceso denegado.");
        return "login";
    }

    // Mostrar la vista del carrito de compras
    @GetMapping("/carrito")
    public String mostrarCarrito() {
        return "carrito"; // Renderiza templates/carrito.html
    }



    @Autowired
    private CategoriaRepository categoriaRepository;

    // --- NUEVAS RUTAS PARA EL CRUD DE INVENTARIO ---

    // 1. Mostrar el formulario para un Nuevo Producto
    @GetMapping("/producto/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        // Enviamos un objeto Producto vacío para que el formulario lo llene
        model.addAttribute("producto", new Producto());
        // Enviamos la lista de categorías para el <select> del formulario
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "nuevo-producto"; // Renderiza templates/nuevo-producto.html
    }

    // 2. Recibir los datos del formulario y guardar en la BD
    @PostMapping("/producto/guardar")
    public String guardarProducto(@ModelAttribute Producto producto) {
        // El servicio guarda el producto en la base de datos (SQL)
        productoService.guardar(producto);
        // Redirigimos de vuelta a la tabla del inventario para ver el nuevo producto
        return "redirect:/inventario";
    }

    // 3. Mostrar el formulario para Editar Producto
    @GetMapping("/producto/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") Integer id, Model model) {
        // Buscamos el producto en la BD por su ID
        Producto producto = productoService.obtenerPorId(id);
        
        if (producto == null) {
            return "redirect:/inventario"; // Si alguien pone un ID falso, lo regresamos
        }
        
        // Enviamos el producto encontrado y las categorías al formulario
        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categoriaRepository.findAll());
        
        return "editar-producto"; // Renderiza templates/editar-producto.html
    }

    // 4. Eliminar Producto
    @GetMapping("/producto/eliminar/{id}")
    public String eliminarProducto(@PathVariable("id") Integer id) {
        productoService.eliminar(id);
        // Recargamos la tabla
        return "redirect:/inventario";
    }
}
