package vn.jpringboot.cinemaBooking.dto.response;

import org.springframework.http.HttpStatusCode;

public class ResponseFailure extends ResponseHandler {

    public ResponseFailure(HttpStatusCode status, String message) {
        super(status, message);
    }

}
