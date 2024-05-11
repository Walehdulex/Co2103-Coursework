package edu.leicester.co2103s2;

import edu.leicester.co2103s2.domain.Author;
import edu.leicester.co2103s2.domain.Book;
import edu.leicester.co2103s2.domain.OrderEntity;
import edu.leicester.co2103s2.repo.AuthorRepository;
import edu.leicester.co2103s2.repo.BookRepository;
import edu.leicester.co2103s2.repo.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {
    private final AuthorRepository authorRepo;
    private final BookRepository bookRepo;
    private final OrderRepository orderRepo;

    @Autowired
    public DataLoader(AuthorRepository authorRepo, BookRepository bookRepo, OrderRepository orderRepo) {
        this.authorRepo = authorRepo;
        this.bookRepo = bookRepo;
        this.orderRepo = orderRepo;

    }

    @Override
    public void run(String... args) throws Exception {
        //Create and save Authors and books
        createAndSaveAuthors();
    }

    private void createAndSaveAuthors() {
        //Create Authors
        Author author = new Author();
        author.setName("WALE Dulex");
        author.setBirthyear(1987);
        author.setNationality("Nigerian");
        authorRepo.save(author);

        Author author1 = new Author();
        author1.setName("Jane Collins");
        author1.setBirthyear(1965);
        author1.setNationality("British");
        authorRepo.save(author1);

        Author author2 = new Author();
        author2.setName("Musa Jacobs");
        author2.setBirthyear(1974);
        author2.setNationality("German");
        authorRepo.save(author2);


        //Creating Books
        Book book = new Book();
        book.setISBN("97050");
        book.setTitle("Clean Code: A Handbook of Agile Software Craftsmanship");
        book.setPublicationYear(2002);
        book.setPrice(50.00);
        book.setAuthor(author);
        bookRepo.save(book);

        Book book1 = new Book();
        book1.setISBN("86746");
        book1.setTitle("Refactoring: Improving the Design of Existing Code");
        book1.setPublicationYear(2008);
        book1.setPrice(34.99);
        book1.setAuthor(author1);
        bookRepo.save(book1);

        Book book2 = new Book();
        book2.setISBN("566231");
        book2.setTitle("Design Patterns: Elements of Reusable Object-Oriented Software");
        book2.setPublicationYear(1999);
        book2.setPrice(44.99);
        book2.setAuthor(author2);
        bookRepo.save(book2);

        Book book3 = new Book();
        book3.setISBN("13579");
        book3.setTitle("Effective Java");
        book3.setPublicationYear(2001);
        book3.setPrice(45.00);
        book3.setAuthor(author);
        bookRepo.save(book3);

        Book book4 = new Book();
        book4.setISBN("24680");
        book4.setTitle("Refactoring to Patterns");
        book4.setPublicationYear(2004);
        book4.setPrice(38.00);
        book4.setAuthor(author);
        bookRepo.save(book4);


        //Create list for Books by authors
        List<Book> authorBooks = new ArrayList<>();
        authorBooks.add(book);
        authorBooks.add(book1);
        authorBooks.add(book2);
        author1.setBooks(authorBooks);

        //Create Orders
        OrderEntity orderEntity1 = new OrderEntity();
        orderEntity1.setDatetime(new Timestamp(System.currentTimeMillis()));
        orderEntity1.setCustomerName("Dulexis");
        orderEntity1.getBooks().add(book);
        orderRepo.save(orderEntity1);

        OrderEntity orderEntity2 = new OrderEntity();
        orderEntity2.setDatetime(new Timestamp(System.currentTimeMillis()));
        orderEntity2.setCustomerName("Chisom");
        orderEntity2.getBooks().add(book1);
        orderRepo.save(orderEntity2);

        OrderEntity orderEntity3 = new OrderEntity();
        orderEntity3.setDatetime(new Timestamp(System.currentTimeMillis()));
        orderEntity3.setCustomerName("Smith");
        orderEntity3.getBooks().add(book2);
        orderRepo.save(orderEntity3);
    }
}
