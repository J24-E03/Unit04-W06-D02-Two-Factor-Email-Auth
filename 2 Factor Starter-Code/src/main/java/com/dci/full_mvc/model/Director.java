package com.dci.full_mvc.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Director implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long directorId;

    private String firstName;

    private String lastName;

    private LocalDate birthDate;

    private String nationality;

    private boolean wonOscars;

    @OneToMany(mappedBy = "director", fetch = FetchType.LAZY)
    private List<Movie> movies;


}
