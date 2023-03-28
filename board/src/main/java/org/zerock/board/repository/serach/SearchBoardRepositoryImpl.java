package org.zerock.board.repository.serach;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.QBoard;
import org.zerock.board.entity.QMember;
import org.zerock.board.entity.QReply;

import java.util.List;
import java.util.stream.Collectors;

/***
 *
 * SearchBoardRepositoryImpl 클래스에서 중요한 점은 QuerydslRepositorySupport를 상속해야 한다는 점이다.
 * QuerydslRepositorySupport는 생성자가 존재하므로 클래스 내에서 super()를 이용해서 호출 해야한다.
 * 이때 도메인 클래스를 지정하는데 null 값을 넣을 수 없습니다.
 * 실제 구현은 조금 뒤로 미루고 log.info()를 이용해서 동작하는지를 확인하는 목적으로 사용한다.
 * BoardRepository는 선언한 SearchBoardRepository 인터페이스를 상속하는 형태로 변경한다.
 *
 */
@Log4j2
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements SearchBoardRepository {

    public SearchBoardRepositoryImpl(){
        super(Board.class);
    }

    @Override
    public Board search1() {
        log.info("search1 ...............");

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        QMember member = QMember.member;

        JPQLQuery<Board> jpqlQuery = from(board);

        // select test Code
        // jpqlQuery.select(board).where(board.bno.eq(1L));

        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

        // 객체 단위로 가져오기
        // jpqlQuery.select(board, member, member.email, reply.count()).groupBy(board);

        // 각각의 데이터를 튜플 단위로 추출
        JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member.email, reply.count());
        tuple.groupBy(board);

        log.info("---------------------");

        List<Tuple> result = tuple.fetch();

        // log.info(jpqlQuery);
        // List<Board> result = jpqlQuery.fetch();

        log.info(result);
        log.info("----------------------");


        return null;
    }

    @Override
    public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {
        log.info("searchPage.....................");

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        QMember member = QMember.member;

        JPQLQuery<Board> jpqlQuery = from(board);
        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

        // SELECT b, w, count(r) FROM Board b
        // LEFT JOIN b.writer w LEFT JOIN REPLY r ON r board = b
        JPQLQuery<Tuple> tuple = jpqlQuery.select(board,member, reply.count());

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        BooleanExpression expression = board.bno.gt(0L);

        booleanBuilder.and(expression);

        if(type != null){
            String[] typeArr = type.split(" ");

            // 검색 조건을 작성하기
            BooleanBuilder conditionBuilder = new BooleanBuilder();

            for(String t: typeArr){
                switch (t){
                    case "t":
                        conditionBuilder.or(board.title.contains(keyword));
                        break;
                    case "w":
                        conditionBuilder.or(member.email.contains(keyword));
                        break;
                    case "c":
                        conditionBuilder.or(board.content.contains(keyword));
                        break;
                }
            }

            booleanBuilder.and(conditionBuilder);
        }

        tuple.where(booleanBuilder);

        // order by
        Sort sort = pageable.getSort();

        // tuple orderby(board.bno.desc());

        sort.stream().forEach(order ->{
            Order direction = order.isAscending()? Order.ASC : Order.DESC;
            String prop = order.getProperty();

            PathBuilder orderByExpression = new PathBuilder(Board.class, "board");

            tuple.orderBy(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });

        tuple.groupBy(board);

        // page 처리
        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());

        List<Tuple> result = tuple.fetch();

        log.info(result);

        long count = tuple.fetchCount();

        log.info("COUNT: " + count);

        return new PageImpl<Object[]>(result.stream().map(t -> t.toArray()).collect(Collectors.toList()),
                pageable, count);
    }
}
