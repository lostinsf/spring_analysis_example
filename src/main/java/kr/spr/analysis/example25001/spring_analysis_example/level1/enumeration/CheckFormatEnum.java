package kr.spr.analysis.example25001.spring_analysis_example.level1.enumeration;

public enum CheckFormatEnum {
    // 키
    EMAIL("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"),
    IPV4("^((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])\\.){3}" + "(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])$"),
    URL("^(https?:\\/\\/)?([a-zA-Z0-9.-]+)(:[0-9]+)?(\\/[a-zA-Z0-9._~:/?#[\\\\]@!$&'()*+,;=%-]*)?$"),
    ETC("기타");

    // 값
    private final String description;

    // 생성자
    CheckFormatEnum(String description) {

        this.description = description;
    }

    // 메서드
    public static CheckFormatEnum fromDescription(String description) {

        for (CheckFormatEnum checkFormatEnumItem : CheckFormatEnum.values()) {
            if (checkFormatEnumItem.getDescription().equals(description)) {
                return checkFormatEnumItem;
            }
        }
        return ETC;
    }

    // 게더
    public String getDescription() {

        return description;
    }
}
