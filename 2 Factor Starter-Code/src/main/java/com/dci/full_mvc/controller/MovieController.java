package com.dci.full_mvc.controller;

import com.dci.full_mvc.model.Director;
import com.dci.full_mvc.model.Genre;
import com.dci.full_mvc.model.Movie;
import com.dci.full_mvc.repository.DirectorRepository;
import com.dci.full_mvc.repository.GenreRepository;
import com.dci.full_mvc.repository.MovieRepository;
import com.dci.full_mvc.service.MovieService;
import com.dci.full_mvc.service.PdfService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {

    private final DirectorRepository directorRepository;
    private final GenreRepository genreRepository;
    private final MovieService movieService;
    private final PdfService pdfService;



//    @GetMapping
//    public String allMovies(Model model){
//        model.addAttribute("movies", movieService.findAll());
//        return "movies/movies-list";
//    }

//
//    @GetMapping
//    public String listMovies(@RequestParam(required = false) String title,
//                             @RequestParam(required = false) Integer startYear,
//                             @RequestParam(required = false) Integer endYear,
//                             @RequestParam(required = false) String directorName,
//                             Model model){
//        List<Movie> movies = movieService.searchMovies(title,startYear,endYear,directorName);
//
//        model.addAttribute("movies",movies);
//        model.addAttribute("title",title);
//        model.addAttribute("startYear",startYear);
//        model.addAttribute("endYear",endYear);
//        model.addAttribute("directorName",directorName);
//
//        return "movies/movies-list";
//    }

    @GetMapping
    public String listMovies(@RequestParam(required = false) String title,
                             @RequestParam(required = false) Integer startYear,
                             @RequestParam(required = false) Integer endYear,
                             @RequestParam(required = false) String directorName,
                             @RequestParam(defaultValue = "0") int page,
                             Model model) {

        int size = 10; // show 10 movies per page

        Page<Movie> moviePage = movieService.searchMovies(title, startYear, endYear, directorName, page, size);

        model.addAttribute("moviePage", moviePage);
        model.addAttribute("currentPage", page);
        model.addAttribute("hasNext", moviePage.hasNext());
        model.addAttribute("hasPrevious", moviePage.hasPrevious());
        model.addAttribute("title", title);
        model.addAttribute("startYear", startYear);
        model.addAttribute("endYear", endYear);
        model.addAttribute("directorName", directorName);

        System.out.println(moviePage.get());

        return "movies/movies-list"; // Thymeleaf view
    }






    @GetMapping("/{id}")
    public String getMovieById(@PathVariable Long id,Model model){
        model.addAttribute("movie",movieService.findById(id));
        return "movies/movie-details";
    }


    @GetMapping("/new")
    public String createMovie(Model model){
        model.addAttribute("movie", new Movie());
        model.addAttribute("directors", directorRepository.findAll());
        model.addAttribute("genres", genreRepository.findAll());
        model.addAttribute("enctype", "multipart/form-data");
        return "movies/movie-form";
    }

    /*
    1. Add validation to your model
    2. Add the @Valid on the posting to the model coming in to validate that it passes the model validation
    3. Add the BindingResult right after the model to capture the errors
    4. Check if there are errors with bindingResult.hasErrors() and if there are render the same page with the same attributes
    * */

    @PostMapping("/create")
    public String createNewMovie(@Valid @ModelAttribute Movie movie, BindingResult bindingResult,
                                 @RequestParam List<Long> genreIds,
                                 @RequestParam(required = false) MultipartFile posterImage,
                                 Model model) throws IOException {

        if (bindingResult.hasErrors()) {
            model.addAttribute("directors", directorRepository.findAll());
            model.addAttribute("genres", genreRepository.findAll());
            return "movies/movie-form";
        }

        Movie createdMovie = movieService.createMovie(movie, genreIds, posterImage);
        return "redirect:/movies/" + createdMovie.getId();
    }

    @GetMapping("/update/{id}")
    public String updateMovieForm(@PathVariable Long id, Model model){

        model.addAttribute("movie",movieService.findById(id));
        model.addAttribute("directors",directorRepository.findAll());
        model.addAttribute("genres", genreRepository.findAll());
        model.addAttribute("enctype", "multipart/form-data");

        return "movies/movie-form";
    }

    @PostMapping("/update/{id}")
    public String updateMovie(@ModelAttribute Movie movie,
                              @RequestParam(required = false) MultipartFile posterImage) throws IOException {
        Movie updatedMovie = movieService.updateMovie(movie);
        if (posterImage != null && !posterImage.isEmpty()) {
            movieService.uploadMoviePoster(updatedMovie, posterImage);
        }
        return "redirect:/movies/" + movie.getId();
    }

//
    @PostMapping("/delete/{id}")
    public String deleteMovie(@PathVariable Long id){
        movieService.deleteById(id);
        return "redirect:/movies";
    }


    @GetMapping(value="/generate",produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> downloadPdf(){
        List<Movie> movies = movieService.findAll();

        byte[] pdfBytes = pdfService.generatePdf(movies);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.builder("attachment").filename("movies.pdf").build());


        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }


}
