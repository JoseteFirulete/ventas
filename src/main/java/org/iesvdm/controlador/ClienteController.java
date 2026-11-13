package org.iesvdm.controlador;

import java.util.List;
import java.util.Set;
import java.time.LocalDate;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.iesvdm.modelo.Cliente;
import org.iesvdm.modelo.Comercial;
import org.iesvdm.servicios.ClienteComercialService;
import org.iesvdm.servicios.ClienteService;
import org.iesvdm.servicios.ComercialService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;


@Controller 
public class ClienteController {

    private final ClienteService clienteService;
    private final ComercialService comercialService;
    private final ClienteComercialService clienteComercialService;

    public ClienteController(ClienteService clienteService, ComercialService comercialService,
            ClienteComercialService clienteComercialService) {
        this.clienteService = clienteService;
        this.comercialService = comercialService;
        this.clienteComercialService = clienteComercialService;
    }

    @GetMapping("/clientes")
    public String listar(Model model) {
        List<Cliente> clientes = clienteService.listAll();
        model.addAttribute("clientes", clientes);
        return "clientes";
    }

    @GetMapping("/clientes/{id}")
    public String detalle(Model model, @PathVariable int id) {
        return clienteService.find(id)
                .map(cliente -> {
                    model.addAttribute("cliente", cliente);
                    model.addAttribute("pedidos", clienteService.listPedidosByCliente(id));
                    return "detalle-cliente";
                })
                .orElse("redirect:/clientes");
    }

    @GetMapping("/clientes/crear")
    public String crear(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "crear-cliente";
    }

    @PostMapping("/clientes/crear")
    public RedirectView submitCrear(@ModelAttribute("cliente") Cliente cliente) {
        clienteService.create(cliente);
        return new RedirectView("/clientes");
    }

    @GetMapping("/clientes/editar/{id}")
    public String editar(Model model, @PathVariable int id) {
        return clienteService.find(id)
                .map(cliente -> {
                    model.addAttribute("cliente", cliente);
                    cargarComerciales(model, id);
                    return "editar-cliente";
                })
                .orElse("redirect:/clientes");
    }

    private void cargarComerciales(Model model, int clienteId) {
        var vinculados = clienteComercialService.listByClienteId(clienteId);
        Set<Integer> idsVinculados = vinculados.stream()
                .map(detalle -> detalle.comercialId())
                .collect(Collectors.toSet());
        List<Comercial> disponibles = comercialService.listAll().stream()
                .filter(comercial -> !idsVinculados.contains(comercial.getId()))
                .toList();
        model.addAttribute("comercialesVinculados", vinculados);
        model.addAttribute("comercialesDisponibles", disponibles);
    }

    @PostMapping("/clientes/editar/{id}")
    public RedirectView submitEditar(@PathVariable int id, @ModelAttribute("cliente") Cliente cliente) {
        cliente.setId(id);
        clienteService.update(cliente);
        return new RedirectView("/clientes");
    }

    @PostMapping("/clientes/editar/{id}/comerciales")
    public RedirectView añadirComercial(@PathVariable int id,
            @RequestParam int comercialId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam int prioridad,
            RedirectAttributes avisos) {
        if (clienteService.find(id).isEmpty() || comercialService.find(comercialId).isEmpty()) {
            avisos.addFlashAttribute("error", "El cliente o el comercial seleccionado no existe.");
        } else if (prioridad < 1) {
            avisos.addFlashAttribute("error", "La prioridad debe ser mayor que cero.");
        } else if (!clienteComercialService.add(id, comercialId, fecha, prioridad)) {
            avisos.addFlashAttribute("error", "El comercial ya está vinculado a este cliente.");
        }
        return new RedirectView("/clientes/editar/" + id);
    }

    @PostMapping("/clientes/editar/{clienteId}/comerciales/{comercialId}/borrar")
    public RedirectView borrarComercial(@PathVariable int clienteId, @PathVariable int comercialId) {
        clienteComercialService.delete(clienteId, comercialId);
        return new RedirectView("/clientes/editar/" + clienteId);
    }

    @PostMapping("/clientes/borrar/{id}")
    public RedirectView submitBorrar(@PathVariable int id, RedirectAttributes avisos) {
        try {
            clienteService.delete(id);
        } catch (DataIntegrityViolationException ex) {
            avisos.addFlashAttribute("error", "No se puede eliminar el cliente porque tiene pedidos o comerciales asociados.");
        }
        return new RedirectView("/clientes");
    }
}
