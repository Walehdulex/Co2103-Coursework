package edu.leicester.co2103s2.repo;

import edu.leicester.co2103s2.domain.Author;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AuthorRepository extends CrudRepository<Author, Long> {
    List<Author> findAll();

    @Query("SELECT COUNT(a) > 0 FROM Author a WHERE a.name = :name AND a.birthyear = :birthyear AND a.nationality = :nationality")
    boolean existsByNameAndBirthyearAndNationality(@Param("name") String name, @Param("birthyear") int birthyear, @Param("nationality") String nationality);
}
