package lecture16.invoice.model;

import lombok.Data;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Data
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invoiceId;

    @ElementCollection
    private List<Long> productIds;
}
