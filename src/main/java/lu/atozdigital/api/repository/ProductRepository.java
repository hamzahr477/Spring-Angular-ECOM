package lu.atozdigital.api.repository;

import lu.atozdigital.api.dto.ProductDTO;
import lu.atozdigital.api.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    /**
     *Find all Product its name contain a search value
     * @param name Stirng contain search value
     * @param pageable a Pageable conatin paramter of number of page, size and sorting
     * @return Page of Products
     */
    Page<Product> findAllByNameContains(String name, Pageable pageable);
}
