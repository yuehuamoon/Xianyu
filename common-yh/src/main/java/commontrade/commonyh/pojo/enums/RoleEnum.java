package commontrade.commonyh.pojo.enums;

public enum RoleEnum {
    NORMAL_USER(0, "普通用户"),
    ADMIN(1, "管理员"),
    OTHER(2, "其他");

    private final Integer code;
    private final String desc;

    RoleEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }
}