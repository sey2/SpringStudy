package org.zerock.mreview.mreview.service;

import org.zerock.mreview.mreview.dto.MovieDTO;
import org.zerock.mreview.mreview.dto.MovieImageDTO;
import org.zerock.mreview.mreview.dto.PageRequestDTO;
import org.zerock.mreview.mreview.dto.PageResultDTO;
import org.zerock.mreview.mreview.entity.Movie;
import org.zerock.mreview.mreview.entity.MovieImage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface MovieService {

    Long register(MovieDTO movieDTO);

    PageResultDTO<MovieDTO, Object[]> getList(PageRequestDTO requestDTO);

    MovieDTO getMovie(Long mno);

    default Map<String, Object> dtoTOEntity(MovieDTO movieDTO){
        Map<String, Object> entityMap = new HashMap<>();

        Movie movie = Movie.builder()
                .mno(movieDTO.getMno())
                .title(movieDTO.getTitle())
                .build();

        entityMap.put("movie", movie);

        List<MovieImageDTO> imageDTOList = movieDTO.getImageDTOList();

        // MovieImageDTO 처리

        if(imageDTOList != null && imageDTOList.size() > 0){
            List<MovieImage> movieImageList = imageDTOList.stream().map(movieImageDTO -> {
               MovieImage movieImage = MovieImage.builder()
                       .path(movieImageDTO.getPath())
                       .imgName(movieImageDTO.getImgName())
                       .uuid(movieImageDTO.getUuid())
                       .movie(movie)
                       .build();
               return movieImage;
            }).collect(Collectors.toList());

            entityMap.put("imgList", movieImageList);
        }
        return entityMap;
    }

    default MovieDTO entitiesToDTO(Movie movie, List<MovieImage> movieImages, Double avg, Long reviewCnt){
        MovieDTO movieDTO = MovieDTO.builder()
                .mno(movie.getMno())
                .title(movie.getTitle())
                .regDate(movie.getRegDate())
                .modDate(movie.getModDate())
                .build();

        List<MovieImageDTO> movieImageDTOList = movieImages.stream().map(movieImage -> {
            return MovieImageDTO.builder().imgName(movieImage.getImgName())
                    .path(movieImage.getPath())
                    .uuid(movieImage.getUuid())
                    .build();
        }).collect(Collectors.toList());

        movieDTO.setImageDTOList(movieImageDTOList);
        movieDTO.setAvg(avg);
        movieDTO.setReviewCnt(reviewCnt.intValue());



        return movieDTO;

    }
}
