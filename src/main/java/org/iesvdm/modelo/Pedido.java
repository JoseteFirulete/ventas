package org.iesvdm.modelo;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Data;

@Data
public class Pedido {
    private int id;
    private Double total;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fecha;
    private Integer clienteId;
    private Integer comercialId;
}
