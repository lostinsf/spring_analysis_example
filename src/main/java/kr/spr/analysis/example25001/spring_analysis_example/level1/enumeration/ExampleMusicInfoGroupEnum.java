package kr.spr.analysis.example25001.spring_analysis_example.level1.enumeration;

public enum ExampleMusicInfoGroupEnum {

    // 키
    KPOP("한국팝"),
    JPOP("일본팝"),
    ETC("기타");

    // 값
    private final String description;

    // 생성자
    ExampleMusicInfoGroupEnum(String description) {

        this.description = description;
    }

    // 메서드
    public static ExampleMusicInfoGroupEnum fromDescription(String description) {

        for (ExampleMusicInfoGroupEnum exampleMusicInfoGroupEnum : ExampleMusicInfoGroupEnum.values()) {
            if (exampleMusicInfoGroupEnum.getDescription().equals(description)) {
                return exampleMusicInfoGroupEnum;
            }
        }
        return ETC;
    }

    // 게더
    public String getDescription() {

        return description;
    }
}
