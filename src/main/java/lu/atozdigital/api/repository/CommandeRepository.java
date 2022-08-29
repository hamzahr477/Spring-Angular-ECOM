package lu.atozdigital.api.repository;

import lu.atozdigital.api.entity.Commande;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommandeRepository extends JpaRepository<Commande,Long> {

    Page<Commande> findAllByNumOrderContains(String numOrder, Pageable pageable);

}
