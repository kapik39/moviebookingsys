package vn.jpringboot.cinemaBooking.dto.response;

public class ResponseError extends ResponseData<Void> {

    public ResponseError(int status, String message) {
        super(status, message);
    }

}
