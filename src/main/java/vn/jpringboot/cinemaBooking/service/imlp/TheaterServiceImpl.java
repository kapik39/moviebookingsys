package vn.jpringboot.cinemaBooking.service.imlp;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.jpringboot.cinemaBooking.dto.request.TheaterPatchDTO;
import vn.jpringboot.cinemaBooking.dto.request.TheaterRequestDTO;
import vn.jpringboot.cinemaBooking.dto.response.PageResponse;
import vn.jpringboot.cinemaBooking.dto.response.TheaterDetailResponse;
import vn.jpringboot.cinemaBooking.exception.ResourceNotFoundException;
import vn.jpringboot.cinemaBooking.model.Theater;
import vn.jpringboot.cinemaBooking.repository.TheaterRepository;
import vn.jpringboot.cinemaBooking.service.TheaterService;
import vn.jpringboot.cinemaBooking.util.PageableUtil;

@Service
@RequiredArgsConstructor
@Slf4j
public class TheaterServiceImpl implements TheaterService {
    private final TheaterRepository theaterRepository;

    @Override
    public Long saveTheater(TheaterRequestDTO request) {
        Theater theater = Theater.builder()
                .theaterName(request.getTheaterName())
                .theaterLocation(request.getTheaterLocation())
                .numberOfScreens(0)
                .build();
        theaterRepository.save(theater);
        log.info("Theater saved successfully with ID: {}", theater.getTheaterId());
        return theater.getTheaterId();
    }

    @Override
    public void updateTheater(Long theaterId, TheaterRequestDTO request) {
        Theater theater = getTheaterById(theaterId);
        theater.setTheaterName(request.getTheaterName());
        theater.setTheaterLocation(request.getTheaterLocation());
        theaterRepository.save(theater);
        log.info("Theater updated successfully with ID: {}", theaterId);
    }

    @Override
    public void deleteTheater(Long theaterId) {
        theaterRepository.deleteById(theaterId);
        log.info("Theater deleted successfully with ID: {}", theaterId);
    }

    @Override
    public TheaterDetailResponse getTheater(Long theaterId) {
        Theater theater = getTheaterById(theaterId);
        return TheaterDetailResponse.builder()
                .theaterId(theater.getTheaterId())
                .theaterName(theater.getTheaterName())
                .theaterLocation(theater.getTheaterLocation())
                .numberOfScreens(theater.getNumberOfScreens())
                .build();
    }

    public void updatePatchTheater(Long theaterId, TheaterPatchDTO request) {
        Theater theater = getTheaterById(theaterId);
        if (request.getTheaterName() != null) {
            theater.setTheaterName(request.getTheaterName());
        }
        if (request.getTheaterLocation() != null) {
            theater.setTheaterLocation(request.getTheaterLocation());
        }
        theaterRepository.save(theater);
        log.info("Theater patched successfully with ID: {}", theaterId);
    }

    @Override
    public PageResponse<?> getAllTheaters(int pageNo, int pageSize, String... sortsBy) {
        Pageable pageable = PageableUtil.buildPageable(pageNo, pageSize, sortsBy);

        Page<Theater> theaters = theaterRepository.findAll(pageable);

        log.info("Total theaters found:");
        List<TheaterDetailResponse> response = theaters.stream()
                .map(theater -> TheaterDetailResponse.builder()
                        .theaterId(theater.getTheaterId())
                        .theaterName(theater.getTheaterName())
                        .theaterLocation(theater.getTheaterLocation())
                        .numberOfScreens(theater.getNumberOfScreens())
                        .build())
                .toList();

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(theaters.getTotalPages())
                .items(response)
                .build();
    }

    @Override
    public Theater getTheaterById(Long theaterId) {
        return theaterRepository.findById(theaterId)
                .orElseThrow(() -> new ResourceNotFoundException("Theater not found with ID: " + theaterId));
    }
}
