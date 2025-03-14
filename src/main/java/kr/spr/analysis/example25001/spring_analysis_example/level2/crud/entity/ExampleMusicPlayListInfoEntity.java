package kr.spr.analysis.example25001.spring_analysis_example.level2.crud.entity;

import jakarta.persistence.*;
import kr.spr.analysis.example25001.spring_analysis_example.level1.util.RandomDataUtil;
import kr.spr.analysis.example25001.spring_analysis_example.level2._tool.entity.GenericEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "example_youtube_music_play_list_info")
public class ExampleMusicPlayListInfoEntity extends GenericEntity {

    @Transient
    String entityName = this.getClass().getAnnotation(Table.class).name().toUpperCase();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Integer version;

    @Column(unique = true)
    private String uniqueCode;

    @Column(nullable = false)
    private String groupEnum = "";

    @Column()
    private int rankNumber;

    @Column()
    private String infoCode;

    @Column()
    private String infoTitle;

    @Column()
    private String playListIdCode;

    @Column()
    private String playListTitle;

    @Column()
    private String channelIdCode;

    @Column()
    private String channelTitle;

    @Lob()
    private String description;


    // uniqueCode 자동 생성
    @PostPersist
    public void generateUniqueCode() {

        if (uniqueCode == null) {
            uniqueCode = RandomDataUtil.getUniqueCode(entityName);
        }

    }

}
