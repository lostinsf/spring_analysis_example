package kr.spr.analysis.example25001.spring_analysis_example.level2._tool.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@MappedSuperclass
public abstract class GenericEntity {


    // 파라미터
    // 생성한 날짜시간
    @Column(updatable = false)
    private LocalDateTime createdDateTime = null;

    // 수정한 날짜시간
    @Column()
    private LocalDateTime updatedDateTime = null;

    // 삭제한 날짜시간
    @Column()
    private LocalDateTime deletedDateTime = null;

    // 공통 태그
    @Column()
    private String tag = null;

    // 공통 여유 비고
    @Lob()
    private String etc = null;


    // 매서드
    // 등록시 날짜 추가
    @PrePersist
    protected void onCreateDateTime() {

        if (createdDateTime == null) {

            createdDateTime = LocalDateTime.now();
        }
    }

}
