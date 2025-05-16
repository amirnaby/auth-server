package com.niam.authserver.persistence.model;

public enum SimplePermission {
    READ(1, 'R'),
    WRITE(2, 'W'),
    CREATE(4, 'C'),
    DELETE(8, 'D');

    private final int code;
    private final char mask;

    SimplePermission(int code, char mask) {
        this.code = code;
        this.mask = mask;
    }

    public static SimplePermission valueOf(Integer code) {
        for (SimplePermission value : SimplePermission.values()) {
            if (value.getCode() == code) return value;
        }
        return null;
    }

    public int getCode() {
        return code;
    }
    public char getMask() {
        return mask;
    }
}
