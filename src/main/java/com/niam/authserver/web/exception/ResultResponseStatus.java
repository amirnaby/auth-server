package com.niam.authserver.web.exception;

public enum ResultResponseStatus {
    SUCCESS(200, "success"),
    UNKNOWN(504, "unknown.error"),
    UNAUTHORIZED(401, "Unauthorized"),
    TOKEN_EXPIRED(403, "token.expired"),
    TOKEN_SIGNATURE_EXCEPTION(403, "token.signature.exception"),
    INVALID_TOKEN_EXCEPTION(403, "invalid.token.exception"),
    USER_MUST_CHANGE_PASSWORD(417, "user.must.change.password"),
    REFRESH_TOKEN_IS_NOT_IN_DATABASE(501, "refresh.token.is.not.in.database"),
    FAILURE(505, "failure"),
    CHANGE_PASSWORD_EXCEPTION(506, "invalid.password.exception"),
    DATABASE_EXCEPTION(1001, "database.error"),
    END_POINT_TIMEOUT(1001, "endpoint.connection.failure"),
    MONGO_DB_TIMEOUT(1003, "db.connection.failure"),
    BAD_REQUEST(400, "bad.request"),
    UNSUPPORTED_API_VERSION(6005, "unsupported.api.version.operation");

    private final String description;

    private final Integer status;

    ResultResponseStatus(int status, String description) {
        this.status = status;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public Integer getStatus() {
        return status;
    }
}