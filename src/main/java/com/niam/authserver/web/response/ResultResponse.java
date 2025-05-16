package com.niam.authserver.web.response;

import com.niam.authserver.web.exception.ResultResponseStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class ResultResponse implements Serializable {
    private static final long serialVersionUID = 6091567334208093240L;
    private int status;
    private String message;
    private ResultLevel level;
    private ResultResponseStatus resultStatus;

    public ResultResponse(ResultResponseStatus resultStatus) {
        this.resultStatus = resultStatus;
        this.status = resultStatus.getStatus();
        this.message = resultStatus.getDescription();
        this.level = getResultLevel(resultStatus);
    }

    public ResultResponse(ResultResponseStatus resultStatus, ResultLevel level) {
        this.resultStatus = resultStatus;
        this.status = resultStatus.getStatus();
        this.message = resultStatus.getDescription();
        this.level = level;
    }

    public ResultLevel getResultLevel(ResultResponseStatus resultStatus) {
        if (resultStatus == ResultResponseStatus.SUCCESS) {
            return ResultLevel.INFO;
        } else if (resultStatus == ResultResponseStatus.FAILURE) {
            return ResultLevel.BLOCKER;
        } else {
            return ResultLevel.WARN;
        }
    }
}