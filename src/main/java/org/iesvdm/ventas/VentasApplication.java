package org.iesvdm.ventas;

import org.iesvdm.dao.ComercialDAO;
import org.iesvdm.modelo.Comercial;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication(scanBasePackages = "org.iesvdm")
@Slf4j
public class VentasApplication implements CommandLineRunner {

    private final ComercialDAO comercialDAO;

    public VentasApplication(ComercialDAO comercialDAO) {
        this.comercialDAO = comercialDAO;
    }

    public static void main(String[] args) {
        SpringApplication.run(VentasApplication.class, args);
    }

    @Override
    public void run(String... args) {
        // Pruebas solicitadas para ComercialDAO mediante CommandLineRunner
        log.info("=== PRUEBAS DE ComercialDAO EN COMMANDLINERUNNER ===");
        
        int totalComerciales = comercialDAO.getAll().size();
        log.info("Devueltos {} comerciales.", totalComerciales);

        for (Comercial comercial : comercialDAO.getAll()) {
            log.info("Comercial -> ID: {}, Nombre: {} {}, Comisión: {}", 
                    comercial.getId(), comercial.getNombre(), comercial.getApellido1(), comercial.getComision());
        }
    }
}