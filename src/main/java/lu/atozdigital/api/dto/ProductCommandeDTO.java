package lu.atozdigital.api.dto;

import com.fasterxml.jackson.databind.cfg.MapperBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lu.atozdigital.api.entity.Commande;
import lu.atozdigital.api.entity.Product;
import lu.atozdigital.api.entity.ProductCommande;
import org.modelmapper.ModelMapper;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCommandeDTO {
    Long id;
    @NotNull(message = "Please provide a valid product id")
    ProductDTO product;
    @NotNull(message = "Please provide a valid quantity")
    @Min(value = 1, message = "Quantity should not be less than 1")
    int quantity;
    public ProductCommande toEntity(){
        ModelMapper modelMapper = new ModelMapper();
        return ProductCommande.builder()
                .product(modelMapper.map(product,Product.class))
                .quantity(this.quantity)
                .build();
    }
}
