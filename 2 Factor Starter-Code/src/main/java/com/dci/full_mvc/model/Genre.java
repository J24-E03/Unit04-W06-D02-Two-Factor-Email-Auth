package com.dci.full_mvc.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "genres") //custom name for our DB table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Genre implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long genreId;


    private String name;


//    @ManyToMany(mappedBy = "genres")
//    private List<Movie> movies = new ArrayList<>();


}
