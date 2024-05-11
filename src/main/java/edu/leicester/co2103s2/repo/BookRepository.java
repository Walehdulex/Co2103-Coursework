package edu.leicester.co2103s2.repo;

import edu.leicester.co2103s2.domain.Book;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, String>  {
    List<Book> findAll();

}
