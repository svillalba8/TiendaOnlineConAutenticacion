package gamo.villalba.sergio.controllers;

import gamo.villalba.sergio.config.auth.TokenProvider;
import gamo.villalba.sergio.dtos.SignInDto;
import gamo.villalba.sergio.dtos.SignUpDto;
import gamo.villalba.sergio.enums.FormatMovie;
import gamo.villalba.sergio.models.BookModel;
import gamo.villalba.sergio.models.DiscModel;
import gamo.villalba.sergio.models.MovieModel;
import gamo.villalba.sergio.models.UserModel;
import gamo.villalba.sergio.services.AuthService;
import gamo.villalba.sergio.services.BookService;
import gamo.villalba.sergio.services.DiscService;
import gamo.villalba.sergio.services.MovieService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @Autowired
    private MovieService movieService;
    @Autowired
    private DiscService discService;
    @Autowired
    private AuthService authService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenProvider tokenProvider;

    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }

    // REGISTRO E INCIO DE USUARIO

    @GetMapping("/auth/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("signupDto", new SignUpDto());
        return "auth/singup";
    }

    @PostMapping("/auth/signup")
    public String handleSignup(
            @ModelAttribute("signupDto") SignUpDto signUpDto,
            RedirectAttributes redirectAttributes) {

        try {
            authService.signUp(signUpDto);
            redirectAttributes.addFlashAttribute("success", "¡Registro exitoso!");
            return "redirect:/auth/signin";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/auth/signup";
        }
    }

    @GetMapping("/auth/signin")
    public String showSignInForm(Model model) {
        model.addAttribute("signinDto", new SignInDto());
        return "auth/singin";
    }

    @PostMapping("/auth/signin")
    public String handleSignIn(
            @ModelAttribute("signinDto") SignInDto signinDto,
            RedirectAttributes redirectAttributes,
            HttpServletResponse response) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            signinDto.getUsername(),
                            signinDto.getPassword()
                    )
            );

            UserModel user = (UserModel) authentication.getPrincipal();

            String token = tokenProvider.generateAccessToken(user);

            Cookie jwtCookie = new Cookie("jwt", token);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(2 * 60 * 60);
            response.addCookie(jwtCookie);

            redirectAttributes.addFlashAttribute("success", "¡Bienvenido!");
            return "redirect:/";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Credenciales inválidas");
            return "redirect:/auth/signin";
        }
    }

    @PostMapping("/auth/logout")
    public String logout(HttpServletResponse response, HttpServletRequest request) {
        SecurityContextHolder.clearContext();

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        Cookie jwtCookie = new Cookie("jwt", null);
        jwtCookie.setMaxAge(0);
        jwtCookie.setPath("/");
        response.addCookie(jwtCookie);

        return "redirect:/";
    }

    // CATALOGOS
    @RequestMapping(value = "/catalogoLibro")
    public String catalogoLibro(Model model) {
        model.addAttribute("listadoLibros", bookService.getBooks());
        return "book/catalogo-libro";
    }

    @RequestMapping(value = "/catalogoPelicula")
    public String catalogoPeliculas(Model model) {
        model.addAttribute("listadoPeliculas", movieService.getMovies());
        return "movie/catalogo-pelicula";
    }

    @RequestMapping(value = "/catalogoDisco")
    public String catalogoDisco(Model model) {
        model.addAttribute("listadoDiscos", discService.getDiscs());
        return "disc/catalogo-disco";
    }

    //BUSQUEDAS
    @GetMapping("/buscarLibros")
    public String buscarLibros(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "isbn", required = false) String isbn,
            @RequestParam(value = "publisher", required = false) String publisher,
            Model model) {

        ArrayList<BookModel> resultadoBusqueda = bookService.findBooks(title, author, isbn, publisher);

        model.addAttribute("listadoLibros", resultadoBusqueda);

        return "book/formulario-busqueda-book";
    }

    @GetMapping("/buscarPeliculas")
    public String buscarPeliculas(@RequestParam(value = "title", required = false) String title,
                                  @RequestParam(value = "format", required = false) FormatMovie formatMovie,
                                  @RequestParam(value = "year", required = false) Integer year,
                                  @RequestParam(value = "director", required = false) String director,
                                  Model model) {

        ArrayList<MovieModel> resultadoBusqueda = movieService.findMovies(title, formatMovie, year, director);

        model.addAttribute("listadoPeliculas", resultadoBusqueda);

        return "movie/formulario-buscar-movie";
    }

    @GetMapping("/buscarDiscos")
    public String buscarDiscos(@RequestParam(value = "title", required = false) String title,
                               @RequestParam(value = "tracks", required = false) Integer tracks,
                               @RequestParam(value = "author", required = false) String author,
                               @RequestParam(value = "year", required = false) Integer year,
                               Model model) {

        ArrayList<DiscModel> resultadoBusqueda = discService.findDiscs(title, tracks, author, year);

        model.addAttribute("listadoDiscos", resultadoBusqueda);

        return "disc/formulario-buscar-disc";
    }

    //EDITOR
    @GetMapping("/editarLibro/{id}")
    public String mostrarFormularioEdicion(@PathVariable("id") long id, Model model) {
        Optional<BookModel> libro = bookService.getBookById(id);

        if (libro.isPresent())
        {
            model.addAttribute("libro", libro.get());
            return "book/formulario-edicion-book";
        }

        return "redirect:/catalogoLibro";
    }

    @PostMapping("/editarLibro/{id}")
    public String actualizarLibro(@ModelAttribute("libro") BookModel libroActualizado) {
        bookService.updateBook(libroActualizado);
        return "redirect:/catalogoLibro";
    }

    @GetMapping("/editarPelicula/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") long id, Model model) {
        Optional<MovieModel> pelicula = movieService.getMovieById(id);

        if (pelicula.isPresent())
        {
            model.addAttribute("pelicula", pelicula.get());
            return "movie/formulario-editar-movie";
        }
        return "redirect:/catalogoPelicula";
    }

    @PostMapping("/editarPelicula/{id}")
    public String actualizarPelicula(@PathVariable("id") long id, MovieModel peliculaActualizada) {
        movieService.updateMovie(peliculaActualizada);
        return "redirect:/catalogoPelicula";
    }

    @GetMapping("/editarDisco/{id}")
    public String mostrarFormularioDiscos(@PathVariable("id") long id, Model model) {
        Optional<DiscModel> disco = discService.getDiscById(id);

        if (disco.isPresent())
        {
            model.addAttribute("disco", disco.get());
            return "disc/formulario-editar-disc";
        }
        return "redirect:/catalogoDisco";
    }

    @PostMapping("/editarDisco/{id}")
    public String actualizarDisco(@PathVariable("id") long id, DiscModel discActualizada) {
        discService.updateDisc(discActualizada);
        return "redirect:/catalogoDisco";
    }

    //CREADOR
    @GetMapping("/crearLibro")
    public String mostrarFormularioCreacion(Model model) {
        model.addAttribute("libro", new BookModel());
        return "book/formulario-creacion-book";
    }

    @PostMapping("/crearLibro")
    public String crearLibro(@ModelAttribute("libro") BookModel nuevoLibro) {
        bookService.addBook(nuevoLibro);
        return "redirect:/catalogoLibro";
    }

    @GetMapping("/crearPelicula")
    public String mostrarFormularioCreacionPelicula(Model model) {
        model.addAttribute("pelicula", new MovieModel());
        return "movie/formulario-crear-movie";
    }

    @PostMapping("/crearPelicula")
    public String crearPelicula(@ModelAttribute("pelicula") MovieModel pelicula) {
        movieService.addMovie(pelicula);
        return "redirect:/catalogoPelicula";
    }

    @GetMapping("/crearDisco")
    public String mostrarFormularioCrearDisco(Model model) {
        model.addAttribute("disco", new DiscModel());
        return "disc/formulario-crear-disc";
    }

    @PostMapping("/crearDisco")
    public String crearDisco(@ModelAttribute("disco") DiscModel disco) {
        discService.addDisc(disco);
        return "redirect:/catalogoDisco";
    }

    //BORRADOR
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

    @PostMapping("/borrarPelicula/{id}")
    public String deletePelicula(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            movieService.deleteMovie(id);
            redirectAttributes.addFlashAttribute("message", "Película eliminada con exito");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Hubo un problema al eliminar la película");
        }
        return "redirect:/catalogoPelicula";
    }

    @PostMapping("/borrarDisco/{id}")
    public String deleteDisco(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            discService.deleteDiscById(id);
            redirectAttributes.addFlashAttribute("message", "Disco eliminado con exito");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Hubo un problema al eliminar la disco");
        }
        return "redirect:/catalogoDisco";
    }
}
