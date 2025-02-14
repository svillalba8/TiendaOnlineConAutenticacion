package gamo.villalba.sergio.controllers;

import gamo.villalba.sergio.models.MovieModel;
import gamo.villalba.sergio.repositories.MovieRepository;
import gamo.villalba.sergio.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/moviestore")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping
    public ArrayList<MovieModel> getMovies() {
        return movieService.getMovies();
    }

    @GetMapping(value = "/{id}")
    public Optional<MovieModel> getMovieById(@PathVariable("id") long id) {
        return movieService.getMovieById(id);
    }

    @PostMapping
    public MovieModel createMovie(@RequestBody MovieModel movieModel) {
        if (movieService.getMovies().contains(movieModel)) return null;
        else return movieService.addMovie(movieModel);
    }

    @DeleteMapping("/{id}")
    public String deleteMovie(@PathVariable("id") long id) {
        boolean deleted = movieService.deleteMovie(id);

        if (deleted) return "Película eliminada";
        else return "No se ha podido eliminar la película";
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieModel> updateMovie(
            @PathVariable("id") long id,
            @RequestBody MovieModel movieModel) {

        Optional<MovieModel> movieOptional = movieService.getMovieById(id);

        if (movieOptional.isEmpty()) return ResponseEntity.notFound().build();

        MovieModel existingMovie = movieOptional.get();
        existingMovie.setTitle(movieModel.getTitle());
        existingMovie.setFormat(movieModel.getFormat());
        existingMovie.setYear(movieModel.getYear());
        existingMovie.setPrice(movieModel.getPrice());
        existingMovie.setDirector(movieModel.getDirector());

        MovieModel updatedMovie = movieService.updateMovie(existingMovie);
        return ResponseEntity.ok(updatedMovie);
    }
}
