package kr.co.codewiki.shoppingmall.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class MemberFormDto {
     private String name;
     private String email;
     private String password;
     private String address;

     // 홍팍은 여기다가 toEntity 메소드 만들어서
     // new 해서 파라미터로 저기저거 선언되어져있는 저거 받아와서
     // entity 를 return 해줬었ㅇㅓ

     // 근데!(Member.java)
}
