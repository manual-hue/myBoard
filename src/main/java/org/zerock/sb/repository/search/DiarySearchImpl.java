package org.zerock.sb.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.sb.entity.*;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class DiarySearchImpl extends QuerydslRepositorySupport implements DiarySearch {

    public DiarySearchImpl() {
        super(Diary.class);
    }

    @Override
    public Page<Object[]> getListWithFavorite(char[] typeArr, String keyword, Pageable pageable) {

        log.info("getSearchList...............");

        QDiary qDiary = QDiary.diary;
        QFavorite qFavorite = QFavorite.favorite;
        QDiaryPicture qDiaryPicture = new QDiaryPicture("pic");

        JPQLQuery<Diary> jpqlQuery = from(qDiary);

        jpqlQuery.leftJoin(qDiary.tags);
        jpqlQuery.leftJoin(qDiary.pictures, qDiaryPicture);
        jpqlQuery.leftJoin(qFavorite).on(qFavorite.diary.eq(qDiary));

        jpqlQuery.groupBy(qDiary);

        jpqlQuery.select(qDiary.dno, qDiary.title, qDiaryPicture, qDiary.tags.any(), qFavorite.score.sum());

        getQuerydsl().applyPagination(pageable, jpqlQuery);

        log.info("query:" + jpqlQuery);

        jpqlQuery.fetch();

        //검색조건이 있다면
        if(typeArr != null && typeArr.length > 0){

            BooleanBuilder condition = new BooleanBuilder();

            for(char type: typeArr){
                if(type == 'T'){
                    condition.or(qDiary.title.contains(keyword));
                }else if(type =='C'){
                    condition.or(qDiary.content.contains(keyword));
                }else if(type == 'W'){
                    condition.or(qDiary.writer.contains(keyword));
                }
            }
            jpqlQuery.where(condition);
        }

        // 조회 대상 지정 = 글 번호, 제목, 첨부파일, 해시태그, 좋아요 수
        JPQLQuery<Tuple> selectQuery = jpqlQuery.select(qDiary.dno, qDiary.title, qDiaryPicture, qDiary.tags.any(), qFavorite.score.sum());

        this.getQuerydsl().applyPagination(pageable, selectQuery);

        List<Tuple> tupleList = selectQuery.fetch();
        long totalCount = selectQuery.fetchCount();

        List<Object[]> arr = tupleList.stream().map(tuple -> tuple.toArray()).
                collect(Collectors.toList());

        return new PageImpl<>(arr, pageable, totalCount);

    }
}