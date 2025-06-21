package vn.jpringboot.cinemaBooking.dto.response;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PageResponse<T> implements Serializable {
    private int pageNo;
    private int pageSize;
    private long totalPages;
    private T items;
}
