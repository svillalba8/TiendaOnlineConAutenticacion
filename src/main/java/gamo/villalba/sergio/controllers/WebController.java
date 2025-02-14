package gamo.villalba.sergio.controllers;

import gamo.villalba.sergio.models.BookModel;
import gamo.villalba.sergio.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class WebController {

    @Autowired
    private BookService bookService;

    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/catalogoLibro")
    public String catalogoLibro(Model model) {
        model.addAttribute("listadoLibros", bookService.getBooks());
        return "catalogo-libro";
    }

    @GetMapping("/buscarLibros")
    public String buscarLibros(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "isbn", required = false) String isbn,
            @RequestParam(value = "publisher", required = false) String publisher,
            Model model) {

        ArrayList<BookModel> resultadoBusqueda = bookService.findBooks(title, author, isbn, publisher);

        model.addAttribute("listadoLibros", resultadoBusqueda);

        return "formulario-busqueda-book";
    }

    @GetMapping("/editarLibro/{id}")
    public String mostrarFormularioEdicion(@PathVariable("id") long id, Model model) {
        Optional<BookModel> libro = bookService.getBookById(id);

        if (libro.isPresent())
        {
            model.addAttribute("libro", libro.get());
            return "formulario-edicion-book";
        }

        return "redirect:/catalogoLibro";
    }

    @PostMapping("/editarLibro/{id}")
    public String actualizarLibro(@ModelAttribute("libro") BookModel libroActualizado) {
        bookService.updateBook(libroActualizado);
        return "redirect:/catalogoLibro";
    }

    @GetMapping("/crearLibro")
    public String mostrarFormularioCreacion(Model model) {
        model.addAttribute("libro", new BookModel());
        return "formulario-creacion-book";
    }


    @PostMapping("/crearLibro")
    public String crearLibro(@ModelAttribute("libro") BookModel nuevoLibro) {
        bookService.addBook(nuevoLibro);
        return "redirect:/catalogoLibro";
    }

    @PostMapping("/borrarLibro/{id}")
    public String deleteBook(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            bookService.delete(id);
            redirectAttributes.addFlashAttribute("message", "Libro eliminado con éxito");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Hubo un problema al eliminar el libro");
        }
        return "redirect:/catalogoLibro";
    }
}
