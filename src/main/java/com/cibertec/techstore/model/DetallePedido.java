package com.cibertec.techstore.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "detalles_pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Integer idDetalle;

    @ManyToOne
    @JoinColumn(name = "id_pedido", referencedColumnName = "id_pedido", foreignKey = @ForeignKey(name = "FK_detalle_pedido"))
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "id_producto", referencedColumnName = "id_producto", foreignKey = @ForeignKey(name = "FK_detalle_producto"))
    private Producto producto;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;
}
