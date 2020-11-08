package Restapi.Repository;

import Restapi.Model.Variation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VariationRepository extends CrudRepository<Variation, Long> {
}
