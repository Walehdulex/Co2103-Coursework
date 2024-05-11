package edu.leicester.co2103s2.controller;

import edu.leicester.co2103s2.domain.Book;
import edu.leicester.co2103s2.domain.OrderEntity;
import edu.leicester.co2103s2.repo.OrderRepository;
import edu.leicester.co2103s2.repo.AuthorRepository;
import edu.leicester.co2103s2.repo.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
public class BookRestController {
    private final BookRepository bookRepo;
    private final AuthorRepository authorRepo;

    private final OrderRepository orderRepo;

    public BookRestController(BookRepository bookRepo, AuthorRepository authorRepo, OrderRepository orderRepo){
        this.bookRepo = bookRepo;
        this.authorRepo = authorRepo;
        this.orderRepo = orderRepo;

    }

    //List All Books

    @GetMapping("books")
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> result = bookRepo.findAll();
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //Creating A Specific Book
    @PostMapping("/books")
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        // Check if a book with the same ISBN already exists
        Optional<Book> existingBook = bookRepo.findById(book.getISBN());
        if (existingBook.isPresent()) {
            // Return a conflict response if the book already exists
            return new ResponseEntity<>(book, HttpStatus.CONFLICT);
        }

        // Save the new book
        Book savedBook = bookRepo.save(book);
        return new ResponseEntity<>(savedBook, HttpStatus.OK);
    }



    //Retrieving A Specific Book
    @GetMapping("/books/{ISBN}")
    public ResponseEntity<Book> getBookByISBN(@PathVariable("ISBN") String ISBN) {
        if (bookRepo.findById(ISBN).isPresent()) {
            return new ResponseEntity<>(bookRepo.findById(ISBN).get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //Updating A Specific Book

    @PutMapping("/books/{ISBN}")
    public ResponseEntity<Book> updateBook(@PathVariable("ISBN") String ISBN, @RequestBody Book newBook) {
        Optional<Book> optionalExistingBook = bookRepo.findById(ISBN);
        if (optionalExistingBook.isPresent()) {
            Book existingBook = optionalExistingBook.get();

            // Update the existing book with values from newBook
            existingBook.setTitle(newBook.getTitle());
            existingBook.setPublicationYear(newBook.getPublicationYear());
            existingBook.setPrice(newBook.getPrice());
            // Set other fields as needed

            // Save the updated book back to the repository
            Book updatedBook = bookRepo.save(existingBook);

            // Return the updated book with a success response
            return new ResponseEntity<>(updatedBook, HttpStatus.OK);
        }

        // Return newBook with a not found response if the book with given ISBN does not exist
        return new ResponseEntity<>(newBook, HttpStatus.NOT_FOUND);
    }


    //Deleting A Specific Book
    @DeleteMapping("/books/{ISBN}")
    public ResponseEntity<String> deleteBook(@PathVariable("ISBN") String ISBN) {
        if (bookRepo.existsById(ISBN)) {
            bookRepo.deleteById(ISBN);
            return new ResponseEntity<>("Book deleted successfully",HttpStatus.OK);
        }
        return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
    }

    //List All Authors Of A Book
    @GetMapping("books/{ISBN}/authors")
    public ResponseEntity<?> getAuthorsofBook(@PathVariable("ISBN") String ISBN) {
        Optional<Book> book = bookRepo.findById(ISBN);
        if (book.isPresent()) {
            return new ResponseEntity<>(book.get().getAuthor(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //List All Orders Containing A Specific Book (endpoint #13)
    @GetMapping("/books/{ISBN}/orders")
    public ResponseEntity<List<OrderEntity>> getOrderContainingBook(@PathVariable("ISBN") String ISBN) {
        List<OrderEntity> orders = orderRepo.findByBooks_ISBN(ISBN);
        if (!orders.isEmpty()) {
            return new ResponseEntity<>(orders, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


//      if (!orders.isEmpty()) {
//        return new ResponseEntity<>(orders, HttpStatus.OK);
//    }
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);






}
