package vn.jpringboot.cinemaBooking.dto.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import lombok.Getter;

public class ResponseHandler extends ResponseEntity<ResponseHandler.Payload> {
    // PUT, PATCH, DELETE
    public ResponseHandler(HttpStatusCode status, String message) {
        super(new Payload(status.value(), message), HttpStatus.OK);
    }

    // GET, POST
    public ResponseHandler(HttpStatusCode status, String message, Object data) {
        super(new Payload(status.value(), message, data), HttpStatus.OK);
    }

    @Getter
    public static class Payload {
        private final int status;
        private final String message;
        private Object data;

        public Payload(int status, String message) {
            this.status = status;
            this.message = message;
        }

        public Payload(int status, String message, Object data) {
            this.status = status;
            this.message = message;
            this.data = data;
        }

    }
}
