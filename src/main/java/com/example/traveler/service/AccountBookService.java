package com.example.traveler.service;

import com.example.traveler.config.BaseException;
import com.example.traveler.model.dto.AccountBookResponse;
import com.example.traveler.model.entity.AccountBook;
import com.example.traveler.model.entity.DateEntity;
import com.example.traveler.model.entity.Travel;
import com.example.traveler.repository.AccountBookRepository;
import com.example.traveler.repository.ChecklistRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.example.traveler.config.BaseResponseStatus.*;

@Service
@AllArgsConstructor
public class AccountBookService {
    @Autowired
    private ChecklistRepository travelRepository;
    private AccountBookRepository accountBookRepository;

    @Transactional
    public AccountBookResponse saveAccountBook(
            String accessToken, Travel travel,
            double totalBudget,
            double foodExpense,
            double transportationExpense,
            double sightseeingExpense,
            double shoppingExpense,
            double otherExpense) throws BaseException {
        AccountBook createdAccountBook = accountBookRepository.findByTravel(travel);

        if (createdAccountBook == null) {
            AccountBook newAccountBook = new AccountBook();
            newAccountBook.setAccountName("새로운 가계부");
            newAccountBook.setTravel(travel);

            // 날짜 정보 추가
            newAccountBook.addDateEntities();

            // 총 예산 설정
            newAccountBook.setTotalBudget(totalBudget);

            try {
                createdAccountBook = accountBookRepository.save(newAccountBook);
            } catch (Exception e) {
                throw new BaseException(SAVE_ACCOUNTBOOK_FAIL);
            }
        }

        // 식비, 교통비, 관광비, 쇼핑 비용, 기타 비용 설정
        createdAccountBook.setFoodExpense(foodExpense);
        createdAccountBook.setTransportationExpense(transportationExpense);
        createdAccountBook.setSightseeingExpense(sightseeingExpense);
        createdAccountBook.setShoppingExpense(shoppingExpense);
        createdAccountBook.setOtherExpense(otherExpense);

        // 나머지 필요한 정보 설정 및 저장
        // ...

        // 계산된 지출 퍼센트를 설정
        double totalExpense = createdAccountBook.getTotalExpense();
        if (totalExpense > 0) {
            createdAccountBook.setFoodExpensePercentage(createdAccountBook.getFoodExpense() / totalExpense * 100);
            createdAccountBook.setTransportExpensePercentage(createdAccountBook.getTransportationExpense() / totalExpense * 100);
            createdAccountBook.setSightseeingExpensePercentage(createdAccountBook.getSightseeingExpense() / totalExpense * 100);
            createdAccountBook.setShoppingExpensePercentage(createdAccountBook.getShoppingExpense() / totalExpense * 100);
            createdAccountBook.setOtherExpensePercentage(createdAccountBook.getOtherExpense() / totalExpense * 100);
        }

        accountBookRepository.save(createdAccountBook);

        AccountBookResponse accountBookResponse = new AccountBookResponse();
        accountBookResponse.setAccountId(createdAccountBook.getAccountId());
        accountBookResponse.setAccountName(createdAccountBook.getAccountName());
        return accountBookResponse;
    }



    public AccountBookResponse getAccountBook(int accountId) throws BaseException {
        // 가계부 ID를 기반으로 가계부를 조회하고 반환하는 로직 구현
        Optional<AccountBook> getAccountBook = accountBookRepository.findByAccountId(accountId);
        if (getAccountBook == null) {
            throw new BaseException(ACCOUNTBOOK_IS_EMPTY);
        }
        AccountBookResponse accountBookResponse = new AccountBookResponse(getAccountBook.get(), getAccountBook.get());
        return accountBookResponse;
    }

    public AccountBookResponse patchAccountBook(String accesstoken, Long accountId, String newName) throws BaseException {
        // 가계부 정보를 업데이트하고 업데이트된 가계부를 반환하는 로직 구현
        AccountBook accountBook = accountBookRepository.findById(accountId)
                .orElseThrow(() -> new BaseException(ACCOUNTBOOK_IS_EMPTY));
        accountBook.setAccountName(newName);

        AccountBookResponse response = new AccountBookResponse();
        response.setAccountId(accountBook.getAccountId());
        response.setAccountName(accountBook.getAccountName());
        return response;
    }

    public int deleteAccountBook(String accessToken, Long accountId) throws BaseException {
        // 가계부를 삭제하는 로직 구현
        AccountBook accountBook = accountBookRepository.findById(accountId)
                .orElseThrow(() -> new BaseException(ACCOUNTBOOK_IS_EMPTY));
        try {
            accountBookRepository.delete(accountBook);
        } catch (Exception e) {
            throw new BaseException(DELETE_ACCOUNTBOOK_FAIL);
        }
        return 1;
    }

    public List<AccountBookResponse> getAllAccountBookByTravel(Travel travel) throws BaseException {
        // 해당 여행에 대한 모든 가계부를 데이터베이스에서 조회
        List<AccountBook> accountBooks = accountBookRepository.findAllByTravel(travel);

        // 조회된 가계부들을 AccountBookResponse 형태로 변환하여 리스트로 반환
        List<AccountBookResponse> accountBookResponses = new ArrayList<>();
        for (AccountBook accountBook : accountBooks) {
            AccountBookResponse response = new AccountBookResponse(accountBook.getAccountName(), accountBook.getAccountId());
            accountBookResponses.add(response);
        }
        return accountBookResponses;
    }

    // 특정 가계부의 날짜 정보 조회 로직 구현
    public List<Date> getDatesForAccountBook(Long accountId) throws BaseException {
        // accountBookRepository를 이용하여 accountId에 해당하는 가계부 정보 조회
        AccountBook accountBook = accountBookRepository.findById(accountId)
                .orElseThrow(() -> new BaseException(ACCOUNTBOOK_NOT_FOUND));

        // 해당 가계부의 DateEntity들을 조회하여 날짜 정보 추출
        List<Date> dates = new ArrayList<>();
        for (DateEntity dateEntity : accountBook.getDateEntities()) {
            dates.add(dateEntity.getDate());
        }

        return dates;
    }

}
