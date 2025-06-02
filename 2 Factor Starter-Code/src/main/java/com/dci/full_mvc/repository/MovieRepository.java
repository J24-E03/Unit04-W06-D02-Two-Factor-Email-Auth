package com.dci.full_mvc.repository;

import com.dci.full_mvc.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie,Long>, JpaSpecificationExecutor<Movie> {

//    List<Movie> findByGenreNot(String genreName);


    List<Movie> findByReleaseYearBetween(int start, int end);


//    writing native queries
    @Query(value="SELECT * FROM movie WHERE director = :director",nativeQuery = true)
    List<Movie> findMovieByDirector(@Param("director") String director);
}
