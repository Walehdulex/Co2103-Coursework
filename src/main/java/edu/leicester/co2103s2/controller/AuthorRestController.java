package edu.leicester.co2103s2.controller;

import edu.leicester.co2103s2.domain.Author;
import edu.leicester.co2103s2.domain.Book;
import edu.leicester.co2103s2.repo.AuthorRepository;
import edu.leicester.co2103s2.repo.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@CrossOrigin
public class AuthorRestController {
    private final AuthorRepository authorRepo;
    private final BookRepository bookRepo;

    public AuthorRestController(AuthorRepository authorRepo, BookRepository bookRepo) {
        this.authorRepo = authorRepo;
        this.bookRepo = bookRepo;
    }

    //List all Authors
    @GetMapping("/authors")
    public ResponseEntity<List<Author>> getAllAuthors() {
        List<Author> result = authorRepo.findAll();
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //Creating a specific author
    @PostMapping("/authors")
    public ResponseEntity<Author> createAuthor(@RequestBody Author author) {
        if (authorRepo.existsByNameAndBirthyearAndNationality(author.getName(),author.getBirthyear(),author.getNationality())) {
            return new ResponseEntity<>(author, HttpStatus.CONFLICT);
        }
        Author savedAuthor = authorRepo.save(author);
       return new ResponseEntity<>(savedAuthor, HttpStatus.OK);
    }

    //Retrieving a specific author from the List
    @GetMapping("/authors/{id}")
    public ResponseEntity<Author> getAuthorById(@PathVariable("id") long id) {
        if (authorRepo.findById(id).isPresent()) {
            return new ResponseEntity<>(authorRepo.findById(id).get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    //Updating a Specific Author
    @PutMapping("/authors/{id}")
    public ResponseEntity<Author> updateAuthor(@PathVariable("id") long id, @RequestBody Author newAuthor) {
        if (authorRepo.findById(id).isPresent()) {
            Author existingAuthor = authorRepo.findById(id).get();
            existingAuthor.setName(newAuthor.getName());
            existingAuthor.setBirthyear(newAuthor.getBirthyear());
            existingAuthor.setNationality(newAuthor.getNationality());
            existingAuthor.getBooks().clear();
            existingAuthor.getBooks().addAll(newAuthor.getBooks());
            authorRepo.save(existingAuthor);
            return new ResponseEntity<>(existingAuthor, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //Deleting a Specific Author
    @DeleteMapping("/authors/{id}")
    public ResponseEntity<?> deleteAuthor(@PathVariable("id") long id) {
        if (authorRepo.existsById(id)) {
            authorRepo.deleteById(id);
            return new ResponseEntity<>("Author with id:" + id + " Deleted succesfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Author with id: " + id + " does not exist", HttpStatus.NOT_FOUND);
    }

    //List All Books Written By a Specific Author
    @GetMapping("/authors/{id}/books")
    public ResponseEntity<List<Book>> getBooksByAuthor(@PathVariable("id") long id) {
        if (authorRepo.findById(id).isPresent()) {
            return new ResponseEntity<>(authorRepo.findById(id).get().getBooks(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}