package gamo.villalba.sergio.repositories;

import gamo.villalba.sergio.models.MovieModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends CrudRepository<MovieModel, Long> {
}
