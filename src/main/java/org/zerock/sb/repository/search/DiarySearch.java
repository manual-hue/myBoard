package org.zerock.sb.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zerock.sb.entity.Diary;

public interface DiarySearch {

    Page<Object[]> getSearchList(char[] typeArr, String keyword, Pageable pageable);
}