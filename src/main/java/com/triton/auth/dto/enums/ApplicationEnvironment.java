package com.triton.auth.dto.enums;

import java.util.stream.Stream;

public enum ApplicationEnvironment implements CodeBasedEnum {
    DEV("dev"),
    QA("qa"),
    STG("stg"),
    PROD("prod");

    private final String code;

    ApplicationEnvironment(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }

    public static ApplicationEnvironment instanceOf(String name) {
        if (name != null) {
            for (ApplicationEnvironment u : values()) {
                if (u.name().equals(name))
                    return u;
            }
        }
        return null;
    }

    public static ApplicationEnvironment of(String code) {
        return Stream.of(values())
                .filter(t -> t.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
