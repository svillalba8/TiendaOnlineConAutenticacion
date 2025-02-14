package gamo.villalba.sergio.services;

import gamo.villalba.sergio.models.BookModel;
import gamo.villalba.sergio.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public ArrayList<BookModel> getBooks() {
        return (ArrayList<BookModel>) bookRepository.findAll();
    }

    public Optional<BookModel> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public ArrayList<BookModel> findBooks(String title, String author, String isbn, String publisher) {
        return new ArrayList<>(getBooks().stream()
                .filter(libro ->
                        (title == null || title.isEmpty() || libro.getTitle().toLowerCase().contains(title.toLowerCase())) &&
                        (author == null || author.isEmpty() || libro.getAuthor().toLowerCase().contains(author.toLowerCase())) &&
                        (isbn == null || isbn.isEmpty() || libro.getIsbn().equals(isbn)) &&
                        (publisher == null || publisher.isEmpty() || libro.getPublisher().toLowerCase().contains(publisher.toLowerCase())))
                .toList());

    }

    public BookModel addBook(BookModel book) {
        return bookRepository.save(book);
    }

    public BookModel updateBook(BookModel book) {
        return bookRepository.save(book);
    }

    public boolean delete(long id) {
        try {
            bookRepository.deleteById(id);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}
