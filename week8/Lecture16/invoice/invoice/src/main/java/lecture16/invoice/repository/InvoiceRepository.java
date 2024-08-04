package lecture16.invoice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import lecture16.invoice.model.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
}
