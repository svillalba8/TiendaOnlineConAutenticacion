package gamo.villalba.sergio.services;

import gamo.villalba.sergio.enums.FormatMovie;
import gamo.villalba.sergio.models.MovieModel;
import gamo.villalba.sergio.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class MovieService {

    @Autowired
    MovieRepository movieRepository;

    public ArrayList<MovieModel> getMovies() {
        return (ArrayList<MovieModel>) movieRepository.findAll();
    }

    public Optional<MovieModel> getMovieById(long id) {
        return movieRepository.findById(id);
    }

    public ArrayList<MovieModel> findMovies(String title, FormatMovie formatMovie,
                                            Integer year, String director) {
        if (year == null) year = 0;

        Integer finalYear = year;
        return new ArrayList<>(getMovies().stream()
                .filter(movie ->
                        (title == null || title.isEmpty() || movie.getTitle().toLowerCase().contains(title.toLowerCase())) &&
                        (formatMovie == null || movie.getFormat().equals(formatMovie)) &&
                        (finalYear == 0 || finalYear.equals(movie.getYear())) &&
                        (director == null || director.isEmpty() || movie.getDirector().toLowerCase().contains(director.toLowerCase()))
                ).toList());
    }

    public MovieModel addMovie(MovieModel movieModel) {
        return movieRepository.save(movieModel);
    }

    public MovieModel updateMovie(MovieModel movieModel) {
        return movieRepository.save(movieModel);
    }

    public boolean deleteMovie(long id) {
        try {
            movieRepository.deleteById(id);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}
