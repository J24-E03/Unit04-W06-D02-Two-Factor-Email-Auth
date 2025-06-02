package com.dci.full_mvc.service;

import com.dci.full_mvc.exceptions.ResourceNotFound;
import com.dci.full_mvc.model.Director;
import com.dci.full_mvc.model.Genre;
import com.dci.full_mvc.model.ImageMetaData;
import com.dci.full_mvc.model.Movie;
import com.dci.full_mvc.repository.DirectorRepository;
import com.dci.full_mvc.repository.GenreRepository;
import com.dci.full_mvc.repository.ImageMetaDataRepository;
import com.dci.full_mvc.repository.MovieRepository;
import com.dci.full_mvc.specification.MovieSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final DirectorRepository directorRepository;
    private final GenreRepository genreRepository;
    private final MovieRepository movieRepository;
    private final ImageUploadService imageUploadService;
    private final ImageMetaDataRepository imageMetaDataRepository;


    public Movie createMovie(Movie movie, List<Long> genreIds, MultipartFile posterImage) throws IOException {
        //        validation for the director
        Director director = directorRepository.findById(movie.getDirector().getDirectorId())
                .orElseThrow(() -> new RuntimeException("Director with Id not found"));

        movie.setDirector(director);

        //        setting the genres:
        List<Genre> genres = genreRepository.findAllById(genreIds);
        movie.setGenres(genres);

        Movie createdMovie = movieRepository.save(movie);

        if (posterImage != null && !posterImage.isEmpty()) {
            uploadMoviePoster(createdMovie, posterImage);
        }

        return createdMovie;
    }

    public List<Movie> findAll(){
        return movieRepository.findAll();
    }


    @Cacheable(value = "movies", key = "#id")
    public Movie findById(Long id){
        Movie foundMovie = movieRepository.findById(id)
                .orElseThrow(()->new ResourceNotFound("Movie not found"));

        return foundMovie;
    }


    public Movie updateMovie(Movie movie){
        System.out.println(movie);
        //        validation for the director
        Director director = directorRepository.findById(movie.getDirector().getDirectorId())
                .orElseThrow(()->new ResourceNotFound("Director with Id not found"));

        movie.setDirector(director);


        return movieRepository.save(movie);
    }

    public void deleteById(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new ResourceNotFound("Movie with id " + id + " not found");
        }
        movieRepository.deleteById(id);
    }

    public Movie uploadMoviePoster(Movie movie, MultipartFile posterImage) throws IOException {
        Map<String, String> uploadResult = imageUploadService.uploadImage(posterImage);

        ImageMetaData imageMetaData = new ImageMetaData();
        imageMetaData.setPath(uploadResult.get("url"));
        imageMetaData.setPublicId(uploadResult.get("publicId"));

        movie.setImage(imageMetaData);
        return movieRepository.save(movie);
    }




    public Page<Movie> searchMovies(String title, Integer startYear, Integer endYear, String directorName, int page, int size) {
        Specification<Movie> spec = Specification
                .where(MovieSpecification.titleContains(title))
                .and(MovieSpecification.releaseYearBetween(startYear, endYear))
                .and(MovieSpecification.directorNameContains(directorName));

        Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());
        return movieRepository.findAll(spec, pageable);
    }




}
