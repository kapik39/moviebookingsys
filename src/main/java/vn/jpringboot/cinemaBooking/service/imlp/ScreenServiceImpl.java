package vn.jpringboot.cinemaBooking.service.imlp;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.jpringboot.cinemaBooking.dto.request.ScreenRequestDTO;
import vn.jpringboot.cinemaBooking.dto.request.SeatDTO;
import vn.jpringboot.cinemaBooking.dto.response.PageResponse;
import vn.jpringboot.cinemaBooking.dto.response.ScreenDetailResponse;
import vn.jpringboot.cinemaBooking.exception.ResourceNotFoundException;
import vn.jpringboot.cinemaBooking.model.Screen;
import vn.jpringboot.cinemaBooking.model.Seat;
import vn.jpringboot.cinemaBooking.model.Theater;
import vn.jpringboot.cinemaBooking.repository.ScreenRepository;
import vn.jpringboot.cinemaBooking.repository.SeatRepository;
import vn.jpringboot.cinemaBooking.repository.TheaterRepository;
import vn.jpringboot.cinemaBooking.service.ScreenService;
import vn.jpringboot.cinemaBooking.service.TheaterService;
import vn.jpringboot.cinemaBooking.util.PageableUtil;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScreenServiceImpl implements ScreenService {
    private final ScreenRepository screenRepository;
    private final SeatRepository seatRepository;
    private final TheaterRepository theaterRepository;
    private final TheaterService theaterService;

    @Override
    public Long saveScreen(ScreenRequestDTO request) {
        Theater theater = theaterService.getTheaterById(request.getTheaterId());
        Screen screen = Screen.builder()
                .screenName(request.getScreenName())
                .screenCapacity(request.getSeats().size())
                .theater(theater)
                .build();

        request.getSeats().forEach(a -> screen.saveSeat(Seat.builder()
                .seatRow(a.getSeatRow())
                .seatNumber(a.getSeatNumber())
                .seatType(a.getSeatType())
                .build()));

        theater.setNumberOfScreens(theater.getNumberOfScreens() + 1);
        theaterRepository.save(theater);
        screenRepository.save(screen);
        log.info("Screen saved successfully with ID: {}", screen.getScreenId());
        return screen.getScreenId();
    }

    @Override
    public void addSeat(Long screenId, SeatDTO request) {
        Screen screen = getScreenById(screenId);
        Seat addSeat = Seat.builder()
                .seatRow(request.getSeatRow())
                .seatNumber(request.getSeatNumber())
                .seatType(request.getSeatType())
                .build();
        screen.saveSeat(addSeat);
        screen.setScreenCapacity(Integer.valueOf(screen.getScreenCapacity() + 1));
        screenRepository.save(screen);
        log.info("Seat added successfully for screenId: {}", screenId);
    }

    @Override
    public void updateScreen(Long screenId, ScreenRequestDTO request) {
        Theater theater = theaterService.getTheaterById(request.getTheaterId());
        Screen screen = getScreenById(screenId);
        screen.setScreenName(request.getScreenName());
        screen.setTheater(theater);
        screenRepository.save(screen);
        log.info("Screen updated successfully for screenId: {}", screenId);
    }

    @Override
    public void updateSeat(Long screenId, Long seatId, SeatDTO request) {
        Screen screen = getScreenById(screenId);
        Seat seatToUpdate = screen.getSeats().stream()
                .filter(seat -> seat.getSeatId().equals(seatId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found with ID: " + seatId));
        seatToUpdate.setSeatRow(request.getSeatRow());
        seatToUpdate.setSeatNumber(request.getSeatNumber());
        seatToUpdate.setSeatType(request.getSeatType());
        screenRepository.save(screen);
        log.info("Seat updated successfully for screenId: {}", screenId);
    }

    @Override
    public void deleteScreen(Long screenId, Long theaterId) {
        Theater theater = theaterService.getTheaterById(theaterId);
        theater.setNumberOfScreens(theater.getNumberOfScreens() + 1);
        theaterRepository.save(theater);
        screenRepository.deleteById(screenId);
        log.info("Screen deleted successfully with ID: {}", screenId);
    }

    @Override
    public void deleteSeat(Long screenId, Long seatId) {
        Screen screen = getScreenById(screenId);
        seatRepository.deleteById(screenId);
        screen.setScreenCapacity(Integer.valueOf(screen.getScreenCapacity() - 1));
        screenRepository.save(screen);
        log.info("Seat deleted successfully with ID: {}", screenId);
    }

    @Override
    public ScreenDetailResponse getScreen(Long screenId) {
        Screen screen = getScreenById(screenId);
        return ScreenDetailResponse.builder()
                .screenId(screen.getScreenId())
                .screenName(screen.getScreenName())
                .screenCapacity(screen.getScreenCapacity())
                .theaterId(screen.getTheater().getTheaterId())
                .seats(convertToSeat(screen.getSeats()))
                .build();
    }

    @Override
    public PageResponse<?> getAllScreensByTheaterId(Long theaterId, int pageNo, int pageSize, String... sortsBy) {
        Pageable pageable = PageableUtil.buildPageable(pageNo, pageSize, sortsBy);

        Page<Screen> screenPage = screenRepository.findByTheater_TheaterId(theaterId, pageable);

        log.info("Total Screen found:");
        List<ScreenDetailResponse> screenDetails = screenPage.stream()
                .map(screen -> ScreenDetailResponse.builder()
                        .screenId(screen.getScreenId())
                        .screenName(screen.getScreenName())
                        .screenCapacity(screen.getScreenCapacity())
                        .theaterId(screen.getTheater().getTheaterId())
                        .build())
                .toList();
        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(screenPage.getTotalPages())
                .items(screenDetails)
                .build();
    }

    private Set<SeatDTO> convertToSeat(Set<Seat> seat) {
        Set<SeatDTO> result = new HashSet<>();
        seat.forEach(a -> result.add(SeatDTO.builder()
                .seatNumber(a.getSeatNumber())
                .seatRow(a.getSeatRow())
                .seatType(a.getSeatType())
                .build()));
        return result;
    }

    @Override
    public Screen getScreenById(Long screenId) {
        return screenRepository.findById(screenId)
                .orElseThrow(() -> new ResourceNotFoundException("Screen not found with ID: " + screenId));
    }
}
