package com.dci.full_mvc.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


//@NotNull          // Field must not be null
//@NotEmpty         // Field must not be null or empty
//@NotBlank         // String must not be null or whitespace
//@Size(min=, max=) // Size boundaries for strings, collections
//@Min(value=)      // Minimum value for numbers
//@Max(value=)      // Maximum value for numbers
//@Email            // Must be a well-formed email address
//@Pattern(regexp=) // Must match the regular expression pattern


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Movie implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private ImageMetaData image;

    @NotBlank(message = "Title for movie cannot be blank")
    @Size(min=5, max = 100, message = "Title must be between 5 and 100 characters")
    private String title;


    @Min(value=20, message = "Release Year must be after year 20")
    @Max(value=2050,message = "Release year must be before 2050")
    private int releaseYear;

    @Positive(message = "Duration must be postive number")
    @Max(value=1000, message = "duration must be below 1000 minutes")
    private int duration;

    @NotBlank(message = "Language is Required")
    private String language;
    
    private boolean wonOscars;

    @ManyToOne
    @JoinColumn(name="director_id")
    private Director director;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="movies_genres",
            joinColumns = @JoinColumn(name="movie_id"),
            inverseJoinColumns = @JoinColumn(name="genre_id")
    )
    private List<Genre> genres = new ArrayList<>();

    public void setImage(ImageMetaData image) {
        this.image = image;
        image.setMovie(this);
    }

    public void removeImage() {
        if (image != null) {
            image.setMovie(null);
            this.image = null;
        }
    }
}
