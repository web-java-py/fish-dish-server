package com.server.enums;

public enum ClassType {
    FISH("0", "鱼"), DISH("1", "菜");

    private final String code;
    private final String info;

    ClassType(String code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public String getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return info;
    }

    public static ClassType findByCode(String code) {
        for (ClassType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
