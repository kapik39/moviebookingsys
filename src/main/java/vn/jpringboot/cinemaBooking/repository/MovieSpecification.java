package vn.jpringboot.cinemaBooking.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import vn.jpringboot.cinemaBooking.dto.request.MovieSearch;
import vn.jpringboot.cinemaBooking.model.Movie;

public class MovieSpecification implements Specification<Movie> {
    private final MovieSearch search;

    public MovieSpecification(MovieSearch search) {
        this.search = search;
    }

    @Override
    public Predicate toPredicate(Root<Movie> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicate = new ArrayList<>();
        if (search.getTitle() != null && !search.getTitle().isEmpty()) {
            predicate.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("movieTitle")),
                    "%" + search.getTitle().toLowerCase() + "%"));
        }
        if (search.getGenre() != null && !search.getGenre().isEmpty()) {
            predicate.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("movieGenre")),
                    "%" + search.getGenre().toLowerCase() + "%"));
        }
        return criteriaBuilder.and(predicate.toArray(new Predicate[0]));
    }

}
