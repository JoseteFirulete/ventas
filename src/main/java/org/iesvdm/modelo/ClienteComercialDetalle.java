package org.iesvdm.modelo;

import java.time.LocalDate;

public record ClienteComercialDetalle(int comercialId, String comercialNombre, LocalDate fecha, int prioridad) {}