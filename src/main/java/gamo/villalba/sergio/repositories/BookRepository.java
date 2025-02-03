package gamo.villalba.sergio.repositories;

import gamo.villalba.sergio.models.BookModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends CrudRepository<BookModel, Long> {
}
