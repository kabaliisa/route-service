package io.javabrains.moviecatalogservice.controller;

import io.javabrains.moviecatalogservice.domain.CatalogItem;
import io.javabrains.moviecatalogservice.domain.Movie;
import io.javabrains.moviecatalogservice.domain.Rating;
import io.javabrains.moviecatalogservice.domain.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {


        UserRating ratings = restTemplate.getForObject("http://localhost:8083/ratingsdata/user/" + userId, UserRating.class);
        System.out.println(ratings.getRatings());
        return ratings.getRatings().stream()
                .map(rating -> {
                    Movie movie = restTemplate.getForObject("http://localhost:8081/movies/" + rating.getMovieId(), Movie.class);
                    return new CatalogItem(movie.getName(), "Test", rating.getRating());
                }).collect(Collectors.toList());
    }

}
