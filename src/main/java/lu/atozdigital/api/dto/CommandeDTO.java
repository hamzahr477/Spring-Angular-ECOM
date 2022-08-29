package lu.atozdigital.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lu.atozdigital.api.entity.Commande;
import lu.atozdigital.api.entity.ProductCommande;
import lu.atozdigital.api.enums.PayementMethod;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommandeDTO {
    Long id;
    @NotNull(message = "Please provide a valid product commande")
    @Valid
    List<ProductCommandeDTO> productCommandeList;

    @NotNull(message = "Please provide a valid payement method")
    PayementMethod payementMethod;
    String numOrder;
    LocalDateTime orderDate;
    Double total=  0.0;


    public Commande toEntity(){
        return Commande.builder()
                .payementMethod(this.payementMethod)
                .productCommandeList(productCommandeList.stream().map(ProductCommandeDTO::toEntity).collect(Collectors.toList()))
                .total(0.0)
                .build();
    }
}
