package lecture16.invoice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lecture16.invoice.dto.InvoiceDTO;
import lecture16.invoice.model.Invoice;
import lecture16.invoice.service.InvoiceService;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    // Default to using REST_TEMPLATE for the standard endpoints
    @GetMapping
    public List<InvoiceDTO> getAllInvoices() {
        return invoiceService.getAllInvoices();
    }

    @GetMapping("/{id}")
    public InvoiceDTO getInvoiceById(@PathVariable Long id) {
        return invoiceService.getInvoiceById(id);
    }

    @PostMapping
    public InvoiceDTO saveInvoice(@RequestBody Invoice invoice) {
        return invoiceService.saveInvoice(invoice);
    }

    // Feign client specific endpoints
    @GetMapping("/feign")
    public List<InvoiceDTO> getAllInvoicesFeign() {
        return invoiceService.getAllInvoicesFeign();
    }

    @GetMapping("/feign/{id}")
    public InvoiceDTO getInvoiceByIdFeign(@PathVariable Long id) {
        return invoiceService.getInvoiceByIdFeign(id);
    }

    @PostMapping("/feign")
    public InvoiceDTO saveInvoiceFeign(@RequestBody Invoice invoice) {
        return invoiceService.saveInvoiceFeign(invoice);
    }

    // WebClient specific endpoints
    @GetMapping("/webclient")
    public List<InvoiceDTO> getAllInvoicesWebClient() {
        return invoiceService.getAllInvoicesWebClient();
    }

    @GetMapping("/webclient/{id}")
    public InvoiceDTO getInvoiceByIdWebClient(@PathVariable Long id) {
        return invoiceService.getInvoiceByIdWebClient(id);
    }

    @PostMapping("/webclient")
    public InvoiceDTO saveInvoiceWebClient(@RequestBody Invoice invoice) {
        return invoiceService.saveInvoiceWebClient(invoice);
    }
}
