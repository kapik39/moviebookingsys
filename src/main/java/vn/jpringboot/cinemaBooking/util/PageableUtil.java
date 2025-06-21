package vn.jpringboot.cinemaBooking.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableUtil {
    public static Pageable buildPageable(int pageNo, int pageSize, String... sortsBy) {
        int pageTemp = 0;
        if (pageNo > 0) {
            pageTemp = pageNo - 1;
        }
        List<Sort.Order> orders = new ArrayList<>();
        for (String sortBy : sortsBy) {
            Pattern pattern = Pattern.compile("\\w+:(asc|desc)");
            Matcher matcher = pattern.matcher(sortBy);
            if (matcher.matches()) {
                String field = matcher.group(0).split(":")[0];
                if (matcher.group(1).equalsIgnoreCase("asc")) {
                    orders.add(new Sort.Order(Sort.Direction.ASC, field));
                } else if (matcher.group(1).equalsIgnoreCase("desc")) {
                    orders.add(new Sort.Order(Sort.Direction.DESC, field));
                }
            }
        }
        return PageRequest.of(pageTemp, pageSize, Sort.by(orders));
    }
}
