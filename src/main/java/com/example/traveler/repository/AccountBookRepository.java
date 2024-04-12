package com.example.traveler.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.traveler.model.entity.AccountBook;
import com.example.traveler.model.entity.Travel;

public interface AccountBookRepository extends JpaRepository<AccountBook, Long> {
    AccountBook findByTravel(Travel travel);
    AccountBook findByTravelAndDate(Travel travel, Date date);

    Optional<AccountBook> findByAccountId(int accountId);

    List<AccountBook> findAllByTravel(Travel travel);
    List<AccountBook> findAllByTravelOrderByDateAsc(Travel travel);
    
}
