package edu.leicester.co2103s2.controller;

import edu.leicester.co2103s2.domain.Book;
import edu.leicester.co2103s2.domain.OrderEntity;
import edu.leicester.co2103s2.repo.OrderRepository;
import edu.leicester.co2103s2.repo.AuthorRepository;
import edu.leicester.co2103s2.repo.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
public class OrderRestController {
    private final BookRepository bookRepo;
    private final AuthorRepository authorRepo;

    private final OrderRepository orderRepo;

    public OrderRestController(BookRepository bookRepo, AuthorRepository authorRepo, OrderRepository orderRepo) {
        this.bookRepo = bookRepo;
        this.authorRepo = authorRepo;
        this.orderRepo = orderRepo;
    }

    //List All Orders
    @GetMapping("/orders")
    private ResponseEntity<List<OrderEntity>> getAllOrders() {
        List<OrderEntity> orders = orderRepo.findAll();
        if (!orders.isEmpty()) {
            return new ResponseEntity<>(orders, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //Creating a Specific Order


    @PostMapping("/orders")
    public ResponseEntity<OrderEntity> createOrder(@RequestBody OrderEntity order) {
        try {
            OrderEntity savedOrder = orderRepo.save(order);
            return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //Retrieving A Specific Order
    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderEntity> getOrderById(@PathVariable("id") Long id) {
        Optional<OrderEntity> order = orderRepo.findById(id);
        if (order.isPresent()) {
            return new ResponseEntity<>(order.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //Updating A Specific Order
    @PutMapping("/orders/{id}")
    public ResponseEntity<OrderEntity> updateOrder(@PathVariable("id") Long id, @RequestBody OrderEntity updateOrder) {
        if (orderRepo.existsById(id)) {
            updateOrder.setId(id);
            orderRepo.save(updateOrder);
            return new ResponseEntity<>(updateOrder, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //List all Books in An Order
    @GetMapping("/orders/{id}/books")
    public ResponseEntity<List<Book>> getBooksInOrder(@PathVariable("id") Long id) {
        Optional<OrderEntity> order = orderRepo.findById(id);
        if (order.isPresent()) {
            return new ResponseEntity<>(order.get().getBooks(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //Add A Book To A Existing Order
    @PostMapping("orders/{id}/books")
    public ResponseEntity<?> addBookToOrder(@PathVariable("id") Long id, @RequestBody Book book) {
        Optional<OrderEntity> orderOptional = orderRepo.findById(id);
        if (orderOptional.isPresent()) {
            OrderEntity order = orderOptional.get();
            if (!order.getBooks().contains(book)) {
                order.getBooks().add(book);
                orderRepo.save(order);
                return new ResponseEntity<>(book, HttpStatus.OK);
            }else {
                return new ResponseEntity<>("Book already exists in the order", HttpStatus.BAD_REQUEST );
            }

        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //Removing A Book From An Existing Order

    @DeleteMapping("/orders/{id}/books/{ISBN}")
    public ResponseEntity<?> removeBookFromOrder(@PathVariable("id") Long id, @PathVariable("ISBN") String ISBN) {
        Optional<OrderEntity> orderOptional = orderRepo.findById(id);
        if (!orderOptional.isPresent()) {
            return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);
        }
        OrderEntity order = orderOptional.get();
        List<Book> books = new ArrayList<>(order.getBooks());
        Book removedBook = books.stream()
                .filter(book -> book.getISBN().equals(ISBN))
                .findFirst()
                .orElse(null);
        if (removedBook != null) {
            order.getBooks().remove(removedBook);
            orderRepo.save(order);
            return new ResponseEntity<>(order, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Book not found in order", HttpStatus.NOT_FOUND);
        }
    }
}


