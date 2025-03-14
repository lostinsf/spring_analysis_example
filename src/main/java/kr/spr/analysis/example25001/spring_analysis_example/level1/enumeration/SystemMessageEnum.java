package kr.spr.analysis.example25001.spring_analysis_example.level1.enumeration;

public enum SystemMessageEnum {
    // 키
    INIT("초기화에 성공하였습니다."),
    INSERT("데이터 생성에 성공하였습니다."),
    STORE("데이터 보관에 성공하였습니다."),
    INFO(""),
    NULL("null"),
    WARN("경고가 발생하였습니다."),
    EMPTY("값이 비어 있습니다."),
    SUCCESS("요청에 성공하였습니다."),
    EXCEPTION("요청 중 예외가 발생하였습니다."),
    PARSE_EXCEPTION("내부 값 데이터 변환 중 에러가 발생하였습니다."),
    NOENUM_EXCEPTION("제한된 값 선택 부분에서 해당 범위에서 벗어난 값이 발생하였습니다."),
    NULL_EXCEPTION("해당 데이터 검색시 null 에러가 발생하였습니다."),
    FAIL("요청에 실패하였습니다."),
    CASTING_FAIL("데이터 변환 작업에 실패하였습니다."),
    PARAMETER_FAIL("요청한 파라미터값 형식이 잘못되어 실패하였습니다."),
    NETWORK_FAIL("네트워크 장애가 발생하여 실패하였습니다."),
    DATABASE_FAIL("데이터베이스 장애가 발생하여 실패하었습니다."),
    UNIQUE_FAIL("데이터베이스 저장 중 유니크값 중복 발생으로 실패하었습니다."),
    WAIT("잠시만 기다려 주세요."),
    TEST("현재 테스트 중 입니다."),
    UPDATE("업데이트가 완료되었습니다."),
    ETC("기타");

    // 값
    private final String description;

    // 생성자
    SystemMessageEnum(String description) {

        this.description = description;
    }

    // 메서드
    public static SystemMessageEnum fromDescription(String description) {

        for (SystemMessageEnum systemMessageEnum : SystemMessageEnum.values()) {
            if (systemMessageEnum.getDescription().equals(description)) {
                return systemMessageEnum;
            }
        }
        return ETC;
    }

    // 게더
    public String getDescription() {

        return description;
    }
}
