package Restapi.Repository;

import Restapi.Model.Variation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VariationRepository extends CrudRepository<Variation, Long> {
    List<Variation> findAllByCodeContaining(String code);

    List<Variation> findAllByNameContaining(String name);

    List<Variation> findAllByNameContainingOrCodeContaining(String name, String code);

}
