package gamo.villalba.sergio.repositories;

import gamo.villalba.sergio.models.DiscModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscRepository extends CrudRepository<DiscModel, Long> {
}
