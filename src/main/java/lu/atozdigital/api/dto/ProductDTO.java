package lu.atozdigital.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    Long id;
    @NotNull(message="Please provide a valid product name")
    @NotBlank(message="Please provide a valid product name")
    String name;
    String description;
    @Min(value = 0,  message = "Quantity should not be less than 0")
    int quantity;
    @Min(value = 0,  message = "Price should not be less than 0")
    Double price;
    String urlImg;
}
