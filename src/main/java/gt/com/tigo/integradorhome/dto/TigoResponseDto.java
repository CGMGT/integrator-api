package gt.com.tigo.integradorhome.dto;

import org.springframework.http.HttpStatus;

public class TigoResponseDto {

    private String code;
    private String message;
    private Object data;

    public TigoResponseDto(Object data) {
        this.code = "0";
        this.message = "";
        this.data = data;
    }

    public TigoResponseDto(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public TigoResponseDto(String code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public TigoResponseDto(int code, String message) {
        this.code = String.valueOf(code);
        this.message = message;
    }

    public TigoResponseDto(HttpStatus code, String message) {
        this.code = code.toString();
        this.message = message;
    }

    public TigoResponseDto(int code, String message, Object data) {
        this.code = String.valueOf(code);
        this.message = message;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
