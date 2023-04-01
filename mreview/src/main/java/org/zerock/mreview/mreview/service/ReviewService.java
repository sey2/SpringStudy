package org.zerock.mreview.mreview.service;

import org.zerock.mreview.mreview.dto.ReviewDTO;
import org.zerock.mreview.mreview.entity.Member;
import org.zerock.mreview.mreview.entity.Movie;
import org.zerock.mreview.mreview.entity.Review;

import java.util.List;

public interface ReviewService {

    // 영화의 모든 영화 리뷰를 가져온다.
    List<ReviewDTO> getListOfMovie(Long mno);

    // 영화 리뷰를 추가
    Long register(ReviewDTO movieReviewDTO);

    // 특정한 영화리뷰 수정
    void modify(ReviewDTO movieReviewDTO);

    // 영화 리뷰 삭제
    void remove(Long reviewnum);

    default Review dtoToEntity(ReviewDTO movieReviewDTO){
        return  Review.builder()
                .reviewnum(movieReviewDTO.getReviewnum())
                .movie(Movie.builder().mno(movieReviewDTO.getMno()).build())
                .member(Member.builder().mid(movieReviewDTO.getMid()).build())
                .grade(movieReviewDTO.getGrade())
                .text(movieReviewDTO.getText()).build();
    }

    default ReviewDTO entityToDTO(Review moviewReview){
        return  ReviewDTO.builder()
                .reviewnum(moviewReview.getReviewnum())
                .mno(moviewReview.getMovie().getMno())
                .mid(moviewReview.getMember().getMid())
                .nickname(moviewReview.getMember().getNickname())
                .email(moviewReview.getMember().getEmail())
                .grade(moviewReview.getGrade())
                .text(moviewReview.getText())
                .regDate(moviewReview.getRegDate())
                .modDate(moviewReview.getModDate())
                .build();
    }
}
