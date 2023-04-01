package org.zerock.mreview.mreview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {

    // review num
    private Long reviewnum;

    // Movie mno
    private Long mno;

    // Member id
    private Long mid;

    // Memeber String nickname
    private String nickname;

    // Member email
    private String email;

    private int grade;

    private String text;
    private LocalDateTime regDate, modDate;

}
