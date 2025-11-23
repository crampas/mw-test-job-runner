package org.example.api;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ApiResponse<DATA> {
    private String error;
    private DATA data;

    public static <DATA> ApiResponse<DATA> ofData(DATA data) {
        return new ApiResponse<DATA>().setData(data);
    }

    public static <DATA> ApiResponse<DATA> ofError(String error) {
        return new ApiResponse<DATA>().setError(error);
    }

}
