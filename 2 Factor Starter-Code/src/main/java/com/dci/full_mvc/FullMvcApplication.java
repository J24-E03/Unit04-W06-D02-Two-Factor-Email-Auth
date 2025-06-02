package com.dci.full_mvc;

import com.dci.full_mvc.model.Director;
import com.dci.full_mvc.model.Movie;
import com.dci.full_mvc.repository.DirectorRepository;
import com.dci.full_mvc.repository.MovieRepository;
import com.dci.full_mvc.repository.UserRepository;
import com.dci.full_mvc.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import java.time.LocalDate;

@SpringBootApplication
@RequiredArgsConstructor
//@EnableCaching
public class FullMvcApplication implements CommandLineRunner {

	private final MovieRepository movieRepository;
	private final DirectorRepository directorRepository;

	private final MovieService movieService;
	private final UserRepository userRepository;



    public static void main(String[] args) {
		SpringApplication.run(FullMvcApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

//		Director hasan = new Director(null,"Hasan","Rezai", LocalDate.now(),"German",true,null);
//
//		Director savedHasan = directorRepository.save(hasan);
//
//
//		Movie hasanMovie = new Movie(null,"Hasan Movie",2025,120,"Adventure","English",true,savedHasan);
//
//
//		movieRepository.save(hasanMovie);


//		System.out.println(movieRepository.findAll());

//		System.out.println(LocalDate.now().getYear());

//		System.out.println(movieService.searchMovies(null,null,null));

		System.out.println(userRepository.findByEmail("omar1" +
				"" +
				"@gmail.com").get());


	}


}
