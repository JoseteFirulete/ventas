package org.iesvdm.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Data;

@Data
public class Pedido {
    private int id;
    private BigDecimal total;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fecha;
    private Integer clienteId;
    private Integer comercialId;
}
