package cobook.buddywisdom.global.domain;

import lombok.*;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
public class Response<T> {

    private int returnCode;
    private String returnMessage;
    private T data;

    public static <T> Response<T> of(String message, T data) {
        return Response.<T>builder()
                .returnCode(HttpStatus.OK.value())
                .returnMessage(message)
                .data(data)
                .build();
    }
}