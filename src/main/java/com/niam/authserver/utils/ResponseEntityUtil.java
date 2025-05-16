package com.niam.authserver.utils;

import com.niam.authserver.web.exception.ResultResponseStatus;
import com.niam.authserver.web.response.ResultLevel;
import com.niam.authserver.web.response.ResultResponse;
import com.niam.authserver.web.response.ServiceResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ResponseEntityUtil {

    private static ResponseEntity<ServiceResponse> getServiceResponseResponseEntity(ServiceResponse response, ResultResponse resultResponse) {
        resultResponse.setStatus(HttpStatus.OK.value());
        resultResponse.setResultStatus(ResultResponseStatus.SUCCESS);
        resultResponse.setMessage(ResultResponseStatus.SUCCESS.getDescription());
        resultResponse.setLevel(ResultLevel.INFO);
        response.setResultResponse(resultResponse);
        return ResponseEntity.ok(response);
    }

    public org.springframework.http.ResponseEntity<ServiceResponse> ok(Object data) {
        ServiceResponse response = new ServiceResponse();
        response.setData(data);
        ResultResponse resultResponse = new ResultResponse();
        return getServiceResponseResponseEntity(response, resultResponse);
    }

    public org.springframework.http.ResponseEntity<ServiceResponse> ok() {
        ServiceResponse response = new ServiceResponse();
        ResultResponse resultResponse = new ResultResponse();
        return getServiceResponseResponseEntity(response, resultResponse);
    }

    private org.springframework.http.ResponseEntity<ServiceResponse> badRequest(Object data) {
        ServiceResponse response = new ServiceResponse();
        response.setData(data);
        ResultResponse resultResponse = new ResultResponse();
        resultResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        resultResponse.setResultStatus(ResultResponseStatus.BAD_REQUEST);
        resultResponse.setMessage(ResultResponseStatus.BAD_REQUEST.getDescription());
        resultResponse.setLevel(ResultLevel.WARN);
        response.setResultResponse(resultResponse);
        return org.springframework.http.ResponseEntity.ok(response);
    }
}