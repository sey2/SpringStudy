package org.zerock.mreview.mreview.repository;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.zerock.mreview.mreview.entity.Movie;
import org.zerock.mreview.mreview.entity.MovieImage;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootTest
public class MovieRepositoryTests {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieImageRepository imageRepository;

    @Commit
    @Transactional
    @Test
    public void insertMovies(){
        IntStream.rangeClosed(1, 100).forEach(i ->{
            Movie movie = Movie.builder().title("Movie ..... " + i).build();

            System.out.println("---------------------------");

            movieRepository.save(movie);

            int count = (int)(Math.random() * 5) + 1;

            for(int j=0; j<count; j++){
                MovieImage movieImage = MovieImage.builder()
                        .uuid(UUID.randomUUID().toString())
                        .movie(movie)
                        .imgName("test" + j + ".jpg").build();

                imageRepository.save(movieImage);
            }
            System.out.println("====================");
        });
    }

    // 1페이지에서 10개의 데이터를 가져옴
    @Test
    public void testListPage(){
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "mno"));

        Page<Object[]> result = movieRepository.getListPage(pageRequest);

        for(Object[] objects: result.getContent()){
            System.out.println(Arrays.toString(objects));
        }
    }

    //94번 영화 리뷰 이미지 데이터 조회
    @Test
    public void testGetMovieWithAll(){
        List<Object[]> result = movieRepository.getMovieWithAll(94L);

        System.out.println(result);

        for(Object[] arr: result)
            System.out.println(Arrays.toString(arr));
    }

}
