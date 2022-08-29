package lu.atozdigital.api.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lu.atozdigital.api.enums.PayementMethod;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @OneToMany(fetch = FetchType.EAGER,cascade =CascadeType.ALL)
    @JoinColumn(name = "commande_id")
    List<ProductCommande> productCommandeList;
    @Column(unique = true,nullable = false)
    String numOrder;
    LocalDateTime orderDate;
    Double total=  0.0;
    @Enumerated(EnumType.STRING)
    PayementMethod payementMethod;


}
