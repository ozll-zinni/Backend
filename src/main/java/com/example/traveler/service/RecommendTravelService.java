package com.example.traveler.service;

import com.example.traveler.model.dto.*;
import com.example.traveler.model.entity.*;
import com.example.traveler.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class RecommendTravelService {

    @Autowired
    private RecommendTravelRepository recommendTravelRepository;

    @Autowired
    private SpotRepository spotRepository;

    @Autowired
    private DayCourseRepository dayCourseRepository;

    @Autowired
    private TravelRepository travelRepository;

    @Autowired
    private DestinationRepository destinationRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private SpotService spotService;
    @Autowired
    private PostRepository postRepository;

    public List<RecommendTravel> getMatchingTravels(String accessToken, RecommendTravelRequest request) {
        int period = calculatePeriod(request.getStartDate(), request.getFinishDate());
        int code1 = request.getCityId() * 1000 + request.getWhat() * 100 + request.getHard() * 10 ;
        int choose = request.getHard();
        int withwho = request.getWithwho();

        List<RecommendTravel> finalTravels = new ArrayList<>();

        int num = 0;

        switch (choose) {
            case 1:
                num += 4;
                break;
            case 2:
                num += 3;
                break;
            case 3:
                num += 2;
                break;
        }

        for (int i = 1; i <= period; i++) {
            int code = code1 + i;

            List<RecommendTravel> travelcode1 = recommendTravelRepository.findByCode1(code);

            Collections.shuffle(travelcode1);

            List<RecommendTravel> randomResult = travelcode1.subList(0, num);

            finalTravels.addAll(randomResult);

        }

        List<Spot> spots = new ArrayList<>();
        List<DayCourse> daycourses = new ArrayList<>();

        int snum = 1;

        for (RecommendTravel travel : finalTravels) {

            Spot spot = new Spot(travel.getDestination(), travel.getLatitude(), travel.getLongitude());
            Spot check = spotRepository.save(spot);
            System.out.println(check.getTitle());
            System.out.println(check.getLatitude());

            spots.add(spot);

            if (snum%num == 0) {

                int numOfDay = travel.getCode1() % 10;

                DayCourse dayCourse = new DayCourse();

                switch (num) {
                    case 1:
                        dayCourse.setSpot1(spots.get(0));
                        break;
                    case 2:
                        dayCourse.setSpot1(spots.get(0));
                        dayCourse.setSpot2(spots.get(1));
                        break;
                    case 3:
                        dayCourse.setSpot1(spots.get(0));
                        dayCourse.setSpot2(spots.get(1));
                        dayCourse.setSpot3(spots.get(2));
                        break;
                    case 4:
                        dayCourse.setSpot1(spots.get(0));
                        dayCourse.setSpot2(spots.get(1));
                        dayCourse.setSpot3(spots.get(2));
                        dayCourse.setSpot4(spots.get(3));
                        break;
                }

                if (dayCourse.getSpot1() != null && dayCourse.getSpot2() != null) {
                    dayCourse.setFirst(spotService.distance(dayCourse.getSpot1().getLatitude(), dayCourse.getSpot1().getLongitude(), dayCourse.getSpot2().getLatitude(), dayCourse.getSpot2().getLongitude()));
                }
                else {
                    dayCourse.setFirst(null);
                }
                if (dayCourse.getSpot2() != null && dayCourse.getSpot3() != null) {
                    dayCourse.setSecond(spotService.distance(dayCourse.getSpot2().getLatitude(), dayCourse.getSpot2().getLongitude(), dayCourse.getSpot3().getLatitude(), dayCourse.getSpot3().getLongitude()));
                }
                else {
                    dayCourse.setSecond(null);
                }
                if (dayCourse.getSpot3() != null && dayCourse.getSpot4() != null) {
                    dayCourse.setThird(spotService.distance(dayCourse.getSpot3().getLatitude(), dayCourse.getSpot3().getLongitude(), dayCourse.getSpot2().getLatitude(), dayCourse.getSpot2().getLongitude()));
                }
                else {
                    dayCourse.setThird(null);
                }

                dayCourse.setNumOfDay(numOfDay);
                dayCourseRepository.save(dayCourse);
                daycourses.add(dayCourse);
                spots.clear();
                snum = 0;

            }

            snum += 1;

        }

        Destination t = destinationRepository.findBydId(request.getCityId());

        User user = userService.getUserByToken(accessToken);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Travel travel = new Travel();

        try {
            Date s = dateFormat.parse(request.getStartDate());
            travel.setStart_date(s);
            Date f = dateFormat.parse(request.getFinishDate());
            travel.setEnd_date(f);
        } catch (ParseException e) {
            System.out.println("");
        }

        travel.setTitle("추천여행");
        travel.setUser(user);
        travel.setWriteStatus(1);
        travel.setDestination(t.getCity());
        travel.setCourses(daycourses);
        travel.setCode(code1);
        travel.setState(1);
        travel.setWithWho(withwho);
        travel.setTimeStatus(0);
        travelRepository.save(travel);

        return finalTravels;
    }

    public TravelResponse getMatchingTravels2(String accessToken, RecommendTravelRequest request) {
        int period = calculatePeriod(request.getStartDate(), request.getFinishDate());
        int code1 = request.getCityId() * 1000 + request.getWhat() * 100 + request.getHard() * 10 ;
        int choose = request.getHard();
        int withwho = request.getWithwho();

        int total_target_num = choose * period;
        Destination t = destinationRepository.findBydId(request.getCityId());
        String keyword = t.getCountry() + " " + t.getCity();

        switch (request.getWhat()) {
            case 1:
                keyword += "힐링여행";
                break;
            case 2:
                keyword += "관광";
                break;
            case 3:
                keyword += "액티비티";
                break;
            case 4:
                keyword += "체험";
                break;
            case 5:
                keyword += "먹방";
                break;
            case 6:
                keyword += "카페";
                break;
        }

        URI uri = UriComponentsBuilder
                .fromUriString("http://15.164.232.95:8000")
                .queryParam("keyword", keyword)
                .encode()
                .build()
                .toUri();


        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<RecommendSpotResponse>> responseEntity = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<RecommendSpotResponse>>() {}
        );
        List<RecommendSpotResponse> spots = responseEntity.getBody();
        List<Spot> spotEntities = new ArrayList<>();

        for (int i = 0; i < total_target_num && i < spots.size(); i++) {
            RecommendSpotResponse spot = spots.get(i);
            Spot newSpot = new Spot(spot.getName(), spot.getLocation().get(0), spot.getLocation().get(1));
            Spot saveSpot = spotRepository.save(newSpot);
            spotEntities.add(saveSpot);
        }
        List<DayCourse> dayCourseEntities = new ArrayList<>();
        for (int i = 0; i < period; i++) {
            DayCourse dayCourse = new DayCourse();
            if (choose == 2) {
                dayCourse.setSpot1(spotEntities.get(i * 2));
                dayCourse.setSpot2(spotEntities.get(i * 2 + 1));
            } else if (choose == 3) {
                dayCourse.setSpot1(spotEntities.get(i * 3));
                dayCourse.setSpot2(spotEntities.get(i * 3 + 1));
                dayCourse.setSpot3(spotEntities.get(i * 3 + 2));
            } else if (choose == 4) {
                dayCourse.setSpot1(spotEntities.get(i * 4));
                dayCourse.setSpot2(spotEntities.get(i * 4 + 1));
                dayCourse.setSpot3(spotEntities.get(i * 4 + 2));
                dayCourse.setSpot4(spotEntities.get(i * 4 + 3));
            }
            if (dayCourse.getSpot1() != null && dayCourse.getSpot2() != null) {
                dayCourse.setFirst(spotService.distance(dayCourse.getSpot1().getLatitude(), dayCourse.getSpot1().getLongitude(), dayCourse.getSpot2().getLatitude(), dayCourse.getSpot2().getLongitude()));
            }
            else {
                dayCourse.setFirst(null);
            }
            if (dayCourse.getSpot2() != null && dayCourse.getSpot3() != null) {
                dayCourse.setSecond(spotService.distance(dayCourse.getSpot2().getLatitude(), dayCourse.getSpot2().getLongitude(), dayCourse.getSpot3().getLatitude(), dayCourse.getSpot3().getLongitude()));
            }
            else {
                dayCourse.setSecond(null);
            }
            if (dayCourse.getSpot3() != null && dayCourse.getSpot4() != null) {
                dayCourse.setThird(spotService.distance(dayCourse.getSpot3().getLatitude(), dayCourse.getSpot3().getLongitude(), dayCourse.getSpot2().getLatitude(), dayCourse.getSpot2().getLongitude()));
            }
            else {
                dayCourse.setThird(null);
            }

            dayCourse.setNumOfDay(period);
            DayCourse dc = dayCourseRepository.save(dayCourse);
            dayCourseEntities.add(dc);
        }


        User user = userService.getUserByToken(accessToken);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Travel travel = new Travel();

        try {
            Date s = dateFormat.parse(request.getStartDate());
            travel.setStart_date(s);
            Date f = dateFormat.parse(request.getFinishDate());
            travel.setEnd_date(f);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        travel.setTitle("추천여행");
        travel.setUser(user);
        travel.setWriteStatus(1);
        travel.setDestination(t.getCity());
        travel.setCourses(dayCourseEntities);
        travel.setCode(code1);
        travel.setState(1);
        travel.setWithWho(withwho);
        travel.setTimeStatus(0);
        Travel saveTravel = travelRepository.save(travel);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TravelResponse travelResponse = new TravelResponse(saveTravel.getTId(), saveTravel.getTitle(), saveTravel.getDestination(), formatter.format(saveTravel.getStart_date()) , formatter.format(saveTravel.getEnd_date()), formatter.format(saveTravel.getCreated_at()), saveTravel.getTimeStatus(), saveTravel.getWriteStatus(), saveTravel.getNoteStatus(), saveTravel.getCourses(), saveTravel.getUser().getId(), saveTravel.getCode());
        return travelResponse;
    }


    private int calculatePeriod(String startDate, String finishDate) {
        LocalDate s = LocalDate.parse(startDate);
        LocalDate f = LocalDate.parse(finishDate);

        long period = ChronoUnit.DAYS.between(s,f);

        return (int) period + 1;
    }

    public List<Destination> getAllregieon(){
        return destinationRepository.findAll();
    }

    public List<MainrecoResponse> getList() {
        List<Post> recoten = postRepository.findTop10ByOrderByLikesDesc();
        List<MainrecoResponse> a = new ArrayList<>();

        for(Post reco : recoten) {

            Date startDate = reco.getTravel().getStart_date();
            Date endDate = reco.getTravel().getEnd_date();

            LocalDate startLocalDate = Instant.ofEpochMilli(startDate.getTime())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            LocalDate endLocalDate = Instant.ofEpochMilli(endDate.getTime())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            long per = ChronoUnit.DAYS.between(startLocalDate, endLocalDate);

            MainrecoResponse main = new MainrecoResponse();

                    main.setPId(reco.getPId());
                    main.setTitle(reco.getTitle());
                    main.setDuration(Long.toString(per) + "박" + Long.toString(per + 1) + "일");
                    main.setDescription(reco.getOneLineReview());
                    if (reco.getImage_url() != null && !reco.getImage_url().isEmpty()) {
                        main.setUrl(reco.getImage_url().get(0));
                    }
            a.add(main);
        }

        return a;
    }

}
