package gamo.villalba.sergio.controllers;

import gamo.villalba.sergio.models.BookModel;
import gamo.villalba.sergio.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping(value = "/bookstore")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public ArrayList<BookModel> getBooks() {
        return bookService.getBooks();
    }

    @GetMapping(value = "/{id}")
    public Optional<BookModel> getBookById(@PathVariable("id") long id) {
        return bookService.getBookById(id);
    }

    @PostMapping
    public BookModel createBook(@RequestBody BookModel book) {
        return bookService.addBook(book);
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") long id) {
        boolean deleted = bookService.delete(id);

        if (deleted) return "Libro eliminado";
        else return "No se ha podido eliminar el libro";
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookModel> updateBook(
            @PathVariable("id") long id,
            @RequestBody BookModel bookDetails) {

        Optional<BookModel> bookOptional = bookService.getBookById(id);

        if (bookOptional.isEmpty()) return ResponseEntity.notFound().build();

        BookModel existingBook = bookOptional.get();
        existingBook.setTitle(bookDetails.getTitle());
        existingBook.setAuthor(bookDetails.getAuthor());
        existingBook.setIsbn(bookDetails.getIsbn());
        existingBook.setPublisher(bookDetails.getPublisher());
        existingBook.setPrice(bookDetails.getPrice());

        BookModel updatedBook = bookService.updateBook(existingBook);
        return ResponseEntity.ok(updatedBook);
    }
}
