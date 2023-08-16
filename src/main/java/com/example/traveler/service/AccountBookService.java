package com.example.traveler.service;

import com.example.traveler.config.BaseException;
import com.example.traveler.model.dto.AccountBookResponse;
import com.example.traveler.model.entity.AccountBook;
import com.example.traveler.model.entity.Travel;
import com.example.traveler.repository.AccountBookRepository;
import com.example.traveler.repository.ChecklistRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.traveler.config.BaseResponseStatus.*;

@Service
@AllArgsConstructor
public class AccountBookService {
    @Autowired
    private ChecklistRepository travelRepository;
    private AccountBookRepository accountBookRepository;

    public AccountBookResponse saveAccountBook(Travel travel) throws BaseException {
        // 해당 여행에 대한 가계부 조회
        AccountBook createdAccountBook = accountBookRepository.findByTravel(travel);

        // 만약 해당 여행에 대한 가계부가 없으면 새로운 가계부 생성하여 저장
        if (createdAccountBook == null) {
            AccountBook newAccountBook = new AccountBook();
            newAccountBook.setAccountName("새로운 가계부");
            newAccountBook.setTravel(travel);

            // 새로운 가계부 데이터베이스에 저장
            try {
                createdAccountBook = accountBookRepository.save(newAccountBook);
            } catch (Exception e) {
                throw new BaseException(SAVE_ACCOUNTBOOK_FAIL);
            }
        }

        // 계산된 지출 퍼센트를 설정
        double totalExpense = createdAccountBook.getTotalExpense();
        if (totalExpense > 0) {
            createdAccountBook.setFoodExpensePercentage(createdAccountBook.getFoodExpense() / createdAccountBook.getTotalExpense() * 100);
            createdAccountBook.setTransportExpensePercentage(createdAccountBook.getTransportationExpense() / createdAccountBook.getTotalExpense() * 100);
            createdAccountBook.setSightseeingExpensePercentage(createdAccountBook.getSightseeingExpense() / createdAccountBook.getTotalExpense() * 100);
            createdAccountBook.setShoppingExpensePercentage(createdAccountBook.getShoppingExpense() / createdAccountBook.getTotalExpense() * 100);
            createdAccountBook.setOtherExpensePercentage(createdAccountBook.getOtherExpense() / createdAccountBook.getTotalExpense() * 100);
        }
        // 저장된 가계부 정보를 AccountBookResponse 형태로 반환
        AccountBookResponse accountBookResponse = new AccountBookResponse();
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

    public AccountBookResponse patchAccountBook(Long accountId, String newName) throws BaseException {
        // 가계부 정보를 업데이트하고 업데이트된 가계부를 반환하는 로직 구현
        AccountBook accountBook = accountBookRepository.findById(accountId)
                .orElseThrow(() -> new BaseException(ACCOUNTBOOK_IS_EMPTY));
        accountBook.setAccountName(newName);

        AccountBookResponse response = new AccountBookResponse();
        response.setAccountId(accountBook.getAccountId());
        response.setAccountName(accountBook.getAccountName());
        return response;
    }

    public int deleteAccountBook(Long accountId) throws BaseException {
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
}
