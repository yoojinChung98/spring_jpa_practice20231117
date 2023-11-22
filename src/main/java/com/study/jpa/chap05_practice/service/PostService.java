package com.study.jpa.chap05_practice.service;

import com.study.jpa.chap05_practice.dto.*;
import com.study.jpa.chap05_practice.entity.HashTag;
import com.study.jpa.chap05_practice.entity.Post;
import com.study.jpa.chap05_practice.repository.HashTagRepository;
import com.study.jpa.chap05_practice.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional // JPA 레파지토리는 트랜잭션 단위로 동작하므로 반드시 해당 아노테이션이 필요함.
public class PostService {

    private final PostRepository postRepository;
    private final HashTagRepository hashTagRepository;


    public PostListResponseDTO getPosts(PageDTO dto) {

        // Pageable 객체 생성
        Pageable pageable = PageRequest.of(
                dto.getPage() - 1, //pageno는 제로베이스라서 -1 필수!
                dto.getSize(),
                Sort.by("createDate").descending()
        );

        // 데이터베이스에서 게시물 목록 조회
        Page<Post> posts = postRepository.findAll(pageable);


        // 게시물 정보만 꺼내기
        List<Post> postList = posts.getContent();

        // 리스트를 하나 더 선언한 이유 -> postList를 PostDetailResponseDTO 리스트로 바꿔야 하기 때문
        // 게시물 정보를 DTO의 형태에 맞게 변환 (stream을 이용하여 객체마다 일괄 처리)
        List<PostDetailResponseDTO> detailList
                = postList.stream()
                .map(PostDetailResponseDTO::new)
                .collect(Collectors.toList());



        // DB에서 조회한 정보를 JSON 형태에 맞는 DTO로 변환 -> PostListResponseDTO 요 형태가 JSON이 될 것임
        return PostListResponseDTO.builder()
                .count(detailList.size()) // 총 게시물 수가 아니라 조회된 게시물의 개수
                .pageInfo(new PageResponseDTO(posts)) // 페이지 정보가 담긴 객체를 DTO에게 전달해서 그쪽에서 값을 처리하도록 할 것.
                .posts(detailList)
                .build();
    }

    public PostDetailResponseDTO getDetail(Long id) throws Exception{

        Post postEntity = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(id + "번 게시물이 존재하지 않습니다!"));

        return new PostDetailResponseDTO(postEntity);
    }

    public PostDetailResponseDTO insert(PostCreateDTO dto)
        throws Exception{ //save()메서드는 null 값이 오면 예외가 발생함 -> 걍 컨트롤러로 던져버리겠음.(컨트롤러 쪽에서 한번에 처리하겠음)
        // PostCreateDTO에 dto를 entity 로 바꾸는 로직을 dto클래스에 생성하겠음

        // 게시물 저장 (아직 해시태그는 insert 되지 않음)
        Post saved = postRepository.save(dto.toEntity());
        // 위의 메서드에서는 HashTag 를 넣어줄 수 없었음 (왜냐하면 PostRepository에 HashTag를 저장할 수 있는 곳이 없었음(우리가 본 양방향 필드 걔는 읽기전용이라서)
        // )-> HashTag를 넣는 단계가 추가적으로 필요함
        // HashTag를 담는 테이블은 따로 있었대! 근데 기억이 안나@

        // 해시태그 따로 저장
        List<String> hashTags = dto.getHashTags();
        if(hashTags != null && hashTags.size() > 0) {
            hashTags.forEach(ht -> {
                HashTag savedTag = hashTagRepository.save(
                        HashTag.builder()
                                .tagName(ht)
                                .post(saved) // 아까 위에서 만들어놓은 게 Post 객체임.
                                .build()
                );
                /*
                - 하단의 코드saved를 작성해야하는 이유!
                Post Entity는 DB에 save를 진행할 때, HashTag에 대한 내용을 갱신하지 않습니다. (왜? Post는 HashTag라는 정보 자체가 없는걸...? 컬럼도 없어,,,)
                애초에 HashTag Entity는 따로 save를 진행합니다 (애초에 둘은 테이블이 각각 나뉘어 있음)
                HashTag는 양방향 매핑이 되어있는 연관고나계의 주인이기 때문에 , save를 진행할 때 Post 를 전달하므로
                DB와 Entity와의 상태가 동일라하지만,
                반대로 Post는 HashTag의 정보가 비어있는 상태입니다!!

                Post Entity에 연관관계 편의 메서드를 작성하여 HashTag의 내용을 동기화 해야 추후에 진행되는 과정에서 문제가 발생하지 않습니당!
                (Post를 화면단으로 return 하고 있음 => HashTag들도 같이 가야함 => 직접 갱신을 해야함)
                (Post를 다시 SELECT해서 가져온다?? 의미없음. 왜냐면 insert는 select 트랜잭션이 마무리된 후에 진행되므로, select절 몽땅 끝내기 전까지는 어차피 insert는 하지도 않음 (머 커밋을 하면 가능))
                 */
                // 세이브가 완전히 마무리 되었다면 , 해시태그의 내용을 다른 리스트에도 저장하자!
                saved.addHashTag((savedTag));
            });
        }




        return new PostDetailResponseDTO(saved);
    }
}
