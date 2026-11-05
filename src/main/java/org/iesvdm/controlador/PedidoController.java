package org.iesvdm.controlador;

import org.iesvdm.modelo.Pedido;
import org.iesvdm.servicios.ClienteService;
import org.iesvdm.servicios.ComercialService;
import org.iesvdm.servicios.PedidoService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/pedidos")
public class PedidoController {
    private final PedidoService pedidos;
    private final ClienteService clientes;
    private final ComercialService comerciales;

    public PedidoController(PedidoService pedidos, ClienteService clientes, ComercialService comerciales) {
        this.pedidos = pedidos;
        this.clientes = clientes;
        this.comerciales = comerciales;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("pedidos", pedidos.listAll());
        return "pedidos";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable int id, Model model) {
        return pedidos.find(id).map(detalle -> {
            model.addAttribute("detalle", detalle);
            return "detalle-pedido";
        }).orElse("redirect:/pedidos");
    }

    private String formulario(Model model, boolean editar) {
        model.addAttribute("clientes", clientes.listAll());
        model.addAttribute("comerciales", comerciales.listAll());
        return editar ? "editar-pedido" : "crear-pedido";
    }

    @GetMapping("/crear")
    public String crear(Model model) {
        model.addAttribute("pedido", new Pedido());
        return formulario(model, false);
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable int id, Model model) {
        return pedidos.find(id).map(detalle -> {
            model.addAttribute("pedido", detalle.pedido());
            return formulario(model, true);
        }).orElse("redirect:/pedidos");
    }

    @PostMapping("/crear")
    public String submitCrear(@ModelAttribute Pedido pedido, BindingResult errores, Model model) {
        return guardar(pedido, errores, model, false);
    }

    @PostMapping("/editar/{id}")
    public String submitEditar(@PathVariable int id, @ModelAttribute Pedido pedido, BindingResult errores, Model model) {
        if (pedidos.find(id).isEmpty()) return "redirect:/pedidos";
        pedido.setId(id);
        return guardar(pedido, errores, model, true);
    }

    private String guardar(Pedido pedido, BindingResult errores, Model model, boolean editar) {
        if (pedido.getTotal() == null || !Double.isFinite(pedido.getTotal())) {
            errores.rejectValue("total", "invalido", "Introduce un importe válido.");
        }
        if (pedido.getClienteId() == null || clientes.find(pedido.getClienteId()).isEmpty()) {
            errores.rejectValue("clienteId", "invalido", "Selecciona un cliente existente.");
        }
        if (pedido.getComercialId() == null || comerciales.find(pedido.getComercialId()).isEmpty()) {
            errores.rejectValue("comercialId", "invalido", "Selecciona un comercial existente.");
        }
        if (errores.hasErrors()) return formulario(model, editar);
        try {
            if (editar) pedidos.update(pedido);
            else pedidos.create(pedido);
        } catch (DataIntegrityViolationException ex) {
            errores.reject("integridad", "No se pudo guardar. Comprueba los datos y las selecciones.");
            return formulario(model, editar);
        }
        return "redirect:/pedidos";
    }

    @PostMapping("/borrar/{id}")
    public String borrar(@PathVariable int id) {
        pedidos.delete(id);
        return "redirect:/pedidos";
    }
}
