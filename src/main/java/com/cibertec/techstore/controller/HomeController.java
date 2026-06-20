package com.cibertec.techstore.controller;

import com.cibertec.techstore.model.Categoria;
import com.cibertec.techstore.model.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.cibertec.techstore.service.IProductoService;
import com.cibertec.techstore.service.ICategoriaService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private IProductoService productoService;

    @Autowired
    private ICategoriaService categoriaService;

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
    // 1. Mostrar la tabla del Inventario
    @GetMapping("/inventario")
    public String gestionInventario(Model model) {
        List<Producto> listaProductos = productoService.listarTodos();
        model.addAttribute("productos", listaProductos);

        model.addAttribute("totalProductos", listaProductos.size());
        model.addAttribute("bajoStock", listaProductos.stream().filter(p -> p.getStock() < 5).count());
        return "inventario";
    }

    // 2. Mostrar la página con el formulario para el Nuevo Producto
    @GetMapping("/inventario/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        Producto producto = new Producto();
        List<Categoria> listaCategorias = categoriaService.listarTodos(); // Jalamos las categorías de la BD

        model.addAttribute("producto", producto);
        model.addAttribute("categorias", listaCategorias); // Enviamos las categorías al select del HTML
        return "nuevo-producto"; // Buscará nuevo-producto.html dentro de templates
    }

    // 3. Procesar el formulario y guardar en la Base de Datos
    @PostMapping("/inventario/guardar")
    public String guardarProducto(@ModelAttribute("producto") Producto producto) {
        productoService.guardar(producto); // Inserta el producto en MySQL Workbench
        return "redirect:/inventario"; // Nos regresa automáticamente a la tabla actualizada
    }

}
