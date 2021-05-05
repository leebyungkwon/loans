package com.loanscrefia.common.board.domain;


import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;

import lombok.Data;

@Data
@Alias("board")
public class BoardDomain extends BaseDomain{
   
   private long groupNo;            // 회원사그룹코드(key) (?)
   
   private int comCode;            //    회원사 코드
   private String comCodeNm;      //    회원사 코드명
   
   private String memberId;         //    ID
   
   private String password;         //    패스워드
   
   private String deptNm;            // 부서명
   
   private String memberName;      // 담당자이름
   
   private String positionNm;      // 직위명
   
   private String email;               // 이메일
   
   private String extensionNo;      // 회사전화번호
   
   private String mobileNo;         // 휴대폰번호
   
   private String joinDt;            //    가입일
   private String apprStat;         //    승인여부
   private int fileSeq;               // 첨부파일ID
   private int failCnt;               //    로그인 실패횟수
   private String creYn;            //    협회여부
   private String dropYn;            //    탈퇴여부
   private int updSeq;               //    수정자 시퀀스
   private String updTimestamp;   //    수정일시
   private Long memberSeq;         //    담당자 시퀀스
   
   private String tempMemberCheck;
   
   //가공
   private int[] memberSeqArr;
   
    
}