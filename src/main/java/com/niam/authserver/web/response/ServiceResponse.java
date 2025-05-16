package com.niam.authserver.web.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.niam.authserver.web.exception.ResultResponseStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ServiceResponse extends BaseService {
    private ResultResponse resultResponse;
    private Object data;

    @JsonProperty
    public void setResultResponse(ResultResponse resultResponse) {
        this.resultResponse = resultResponse;
    }

    public void setResultResponse(ResultResponseStatus resultStatus) {
        if (resultStatus == null) {
            return;
        }
        ResultResponse resultResponse = new ResultResponse();
        resultResponse.setResultStatus(resultStatus);
        resultResponse.setMessage(resultStatus.getDescription());
        resultResponse.setStatus(resultStatus.getStatus());
        resultResponse.setLevel(resultResponse.getResultLevel(resultStatus));
        this.resultResponse = resultResponse;
    }

    public void setResultResponse(ResultResponseStatus resultStatus, String message) {
        ResultResponse resultResponse = new ResultResponse();
        resultResponse.setResultStatus(resultStatus);
        resultResponse.setMessage(message);
        resultResponse.setStatus(resultStatus.getStatus());
        resultResponse.setLevel(resultResponse.getResultLevel(resultStatus));
        this.resultResponse = resultResponse;
    }

    public void setResultResponse(ResultResponseStatus resultStatus, boolean isExternal) {
        if (resultStatus == null) {
            return;
        }
        ResultResponse resultResponse = new ResultResponse();
        resultResponse.setMessage(resultStatus.getDescription());
        resultResponse.setStatus(resultStatus.getStatus());
        if (!isExternal) {
            resultResponse.setResultStatus(resultStatus);
        }
        resultResponse.setLevel(resultResponse.getResultLevel(resultStatus));
        this.resultResponse = resultResponse;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}