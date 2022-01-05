package org.zerock.sb.repository;

import com.google.common.collect.Sets;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.sb.dto.DiaryDTO;
import org.zerock.sb.entity.Diary;
import org.zerock.sb.entity.DiaryPicture;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class DiaryRepositoryTests {

    @Autowired
    DiaryRepository diaryRepository;

    @Autowired
    ModelMapper modelMapper;

    @Test
    public void testInsert() {

        IntStream.rangeClosed(1, 100).forEach(i -> {

            Set<String> tags = IntStream.rangeClosed(1, 3).mapToObj(j -> i + "_tag" + j).collect(Collectors.toSet());

            Set<DiaryPicture> pictures = IntStream.rangeClosed(1, 3).mapToObj(j -> {
                DiaryPicture picture = DiaryPicture.builder()
                        .uuid(UUID.randomUUID().toString())
                        .savePath("2021/10/18")
                        .fileName("img" + j + ".jpg")
                        .idx(j)
                        .build();
                return picture;
            }).collect(Collectors.toSet());

            Diary diary = Diary.builder()
                    .title("sample.." + i)
                    .content("sample..." + i)
                    .writer("user00")
                    .tags(tags)
                    .pictures(pictures)
                    .build();

            diaryRepository.save(diary);
        });
    }

    @Test
    public void testSelectOne() {

        Long dno = 1L;

        Optional<Diary> optionalDiary = diaryRepository.findById(dno);

        Diary diary = optionalDiary.orElseThrow();

        log.info(diary);
        log.info(diary.getTags());
        log.info(diary.getPictures());
    }

    @Test
    public void testSelectOne2() {

        Long dno = 1L;

        Optional<Diary> optionalDiary = diaryRepository.findById(dno);

        Diary diary = optionalDiary.orElseThrow();

        DiaryDTO dto = modelMapper.map(diary, DiaryDTO.class);

        log.info(dto);


    }

    @Test
    public void testSearchTag() {
        String tag = "1";
        Pageable pageable = PageRequest.of(0, 10, Sort.by("dno").descending());

        Page<Diary> result = diaryRepository.searchTags(tag, pageable);

        result.get().forEach(diary -> {
            log.info(diary);
            log.info(diary.getTags());
            log.info(diary.getPictures());
            log.info("-----------------------------");
        });

    }

    @Test
    public void testDelete(){

        Long dno = 104L;

        diaryRepository.deleteById(dno);

    }

    @Test
    @Transactional
    @Commit
    public void testUpdate(){

        //title content tags files 일괄로 변경되는 값이므로 메서드 하나로 묶기
        //엔티티의 상태도 JPA에서의 작업도 두 번 이루어짐
        Set<String> updateTags
                = Sets.newHashSet("aa","bb","cc");

        Set<DiaryPicture> updatePictures
                = IntStream.rangeClosed(10,15).mapToObj(i->{
                    DiaryPicture picture = DiaryPicture.builder()
                            .uuid(UUID.randomUUID().toString())
                            .savePath("2021/10/19")
                            .fileName("Test"+i+".jpg")
                            .idx(i)
                            .build();

                    return picture;

        }).collect(Collectors.toSet());

        Optional<Diary> optionalDiary = diaryRepository.findById(103L);

        // change
        Diary diary = optionalDiary.orElseThrow();

        diary.setTitle("Updated title 103");
        diary.setContent("Updated content 103");
        diary.setTags(updateTags);
        diary.setPictures(updatePictures);

        //Test code는 항상 commit, 트랜잭션을 걸어주어야만 롤백이 되지 앟는다.
        diaryRepository.save(diary);


    }
}

