package com.gdsc.jpa.controller;


import com.gdsc.jpa.dto.MemberDTO;
import com.gdsc.jpa.entity.Member;
import com.gdsc.jpa.service.MemberService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor //초기화 되지않은 final 필드나, @NonNull 이 붙은 필드에 대해 생성자를 생성
//@RequestMapping("/api") // URL 을 컨트롤러의 메서드와 매핑할 때 사용하는 어노테이션
public class MemberController {

    private final MemberService memberService;

    //id(team)값으로 Member 객체를 build
    //@PathVariabled 어노테이션은 URL 처리를 위해 사용

    @PostMapping("/teams/{id}/members")
    public ResponseEntity<MemberDTO> saveByTeamId(@PathVariable("id")Long id,@RequestBody MemberDTO request)
    {
        MemberDTO response = memberService.saveByTeamId(id,request);
        return ResponseEntity.created(URI.create("/api/members/"+response.getId()))
                .body(response);
    }

    //id를 받아서 팀에 포함된 모든 멤버를 가져오고 List 에 findAllByTeamId(id) 값을 넣어줌
    //해당 Entity 필드 이름을 입력하면 검색 쿼리를 실행한 결과를 전달
    //List가 비어있을 경우 noContent()로 에러 발생
//    @GetMapping("teams/{id}/members")
//    public ResponseEntity<List<MemberDTO>> findAllByTeamId(@PathVariable("id") Long id){
//
//        List<MemberDTO> response = memberService.findAllByTeamId(id);
//
//        if(response.isEmpty()){
//            return ResponseEntity
//                    .noContent()
//                    .build();
//        }
//        return ResponseEntity.ok(response);
//    }

    @GetMapping("/teams/{id}/members")
    public ResponseEntity<Page<MemberDTO>> findAllByTeamIdWithPaging(
            @PathVariable("id") Long id,
            @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
        Page<MemberDTO> responses = memberService.findAllByTeamIdWithPaging(id, pageable);

        if(responses.isEmpty()){
            return ResponseEntity
                    .noContent()
                    .build();
        }
        return ResponseEntity.ok(responses);
    }
    /*@GetMapping("/members")
    public ResponseEntity<Page<MemberDTO>> findAllWithPaging(
            Pageable pageable) {
        Page<MemberDTO> responses = memberService.findAllWithPaging(pageable);

        if(responses.isEmpty()){
            return ResponseEntity
                    .noContent()
                    .build();
        }
        return ResponseEntity.ok(responses);
    }*/
    //Pagable만 사용하는 API
    @GetMapping("/members/pageable")
    public ResponseEntity<Page<MemberDTO>> findAllWithPaging(
            Pageable pageable){
        Page<MemberDTO> responses = memberService.findAllWithPaging(pageable);

        if (responses.isEmpty()) {
            return ResponseEntity
                    .noContent()
                    .build();
        }
        return ResponseEntity.ok(responses);
    }
    // @PageableDefault를 사용하는 API
    @GetMapping("/members/default")
    public ResponseEntity<Page<MemberDTO>> findAllWithPagingDefault(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        Page<MemberDTO> responses = memberService.findAllWithPaging(pageable);

        if (responses.isEmpty()) {
            return ResponseEntity
                    .noContent()
                    .build();
        }

        return ResponseEntity
                .ok(responses);
    }
    //RequestParam를 사용하는 API
    @GetMapping("/members/request")
    public ResponseEntity<Page<MemberDTO>> findAllWithRequest(@RequestParam("page") int page,
                                                              @RequestParam("size") int size,
                                                              Pageable pageable){
        Pageable paging = PageRequest.of(page, size);
        Page<MemberDTO> response = memberService.findAllWithPaging(paging);

        if(response.isEmpty()){
            return ResponseEntity
                    .noContent()
                    .build();
        }
        return ResponseEntity.ok(response);
    }
    //모든 멤버를 조회 하기위한 controller
    //List가 비어있을 경우 noContent()로 에러 발생
//    @GetMapping("/members")
//    public ResponseEntity<List<MemberDTO>> findAll() {
//        List<MemberDTO> response = memberService.findAll();
//
//        if (response.isEmpty()) {
//            return ResponseEntity
//                    .noContent()
//                    .build();
//        }
//        return ResponseEntity.ok(response);
//    }

    //한 명의 멤버를 조회하기 위한 controller
    //id값으로 검색
    @GetMapping("/members/{id}")
    public ResponseEntity<MemberDTO> findById(@PathVariable("id") Long id) {
        MemberDTO response = memberService.findById(id);

        return ResponseEntity.ok(response);
    }


    //한 명의 멤버를 조회 한 뒤 update 하는 controller
    @PatchMapping("/members/{id}")
    public ResponseEntity<MemberDTO> updateById(@PathVariable("id")Long id,@RequestBody MemberDTO request){

        MemberDTO response = memberService.updateById(id,request);

        return ResponseEntity.ok(response);
    }
    //한 명의 멤버를 조회 한 뒤 delete 하는 controller
    @DeleteMapping("/members/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Long id) {
        memberService.deleteById(id);

        return ResponseEntity.ok(null);
    }


}