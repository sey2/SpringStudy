package org.zerock.mreview.mreview.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.mreview.mreview.entity.Movie;
import org.zerock.mreview.mreview.entity.MovieImage;

public interface MovieImageRepository extends JpaRepository<MovieImage, Long> {


}
