package com.matching.common.utils;

import com.matching.common.dto.ResponseDto;
import com.matching.common.dto.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class ResponseUtil {
    public static <T>ResponseDto<T> SUCCESS (String message, T data) {
        return new ResponseDto(ResponseStatus.SUCCESS, message, data);
    }

    public static <T>ResponseDto<T> FAILURE (String message, T data) {
        return new ResponseDto(ResponseStatus.FAILURE, message, data);
    }

    public static <T>ResponseDto<T> ERROR (String message, T data) {
        return new ResponseDto(ResponseStatus.ERROR, message, data);
    }

}
