package com.example.traveler.service;

import com.example.traveler.config.BaseException;
import com.example.traveler.model.dto.AccountBookResponse;
import com.example.traveler.model.dto.DailyExpensesResponse;
import com.example.traveler.model.dto.MonthlyExpensesResponse;
import com.example.traveler.model.dto.SummaryResponse;
import com.example.traveler.model.entity.AccountBook;
import com.example.traveler.model.entity.DateEntity;
import com.example.traveler.model.entity.Transaction;
import com.example.traveler.model.entity.Travel;
import com.example.traveler.repository.AccountBookRepository;
import com.example.traveler.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.example.traveler.config.BaseResponseStatus.*;

@Service
@AllArgsConstructor
public class AccountBookService {
    private AccountBookRepository accountBookRepository;
    private TransactionRepository transactionRepository;

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
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


    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public AccountBookResponse getAccountBook(int accountId) throws BaseException {
        // 가계부 ID를 기반으로 가계부를 조회하고 반환하는 로직 구현
        Optional<AccountBook> getAccountBook = accountBookRepository.findByAccountId(accountId);
        if (getAccountBook == null) {
            throw new BaseException(ACCOUNTBOOK_IS_EMPTY);
        }
        AccountBookResponse accountBookResponse = new AccountBookResponse(getAccountBook.get(), getAccountBook.get());
        return accountBookResponse;
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
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

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
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

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
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
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
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

    // 월별 지출 계산 메서드
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public MonthlyExpensesResponse getMonthlyExpenses(Long accountId) throws BaseException {
        try {
            AccountBook accountBook = accountBookRepository.findById(accountId)
                    .orElseThrow(() -> new BaseException(ACCOUNTBOOK_IS_EMPTY));

            List<Transaction> transactions = transactionRepository.findAllByAccountBook(accountBook);

            Map<YearMonth, Double> monthlyExpenses = new HashMap<>();
            for (Transaction transaction : transactions) {
                YearMonth yearMonth = YearMonth.from(transaction.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                monthlyExpenses.put(yearMonth, monthlyExpenses.getOrDefault(yearMonth, 0.0) + transaction.getAmount());
            }

            // MonthlyExpensesResponse를 생성하여 반환
            Map<String, Double> formattedMonthlyExpenses = convertYearMonthToString(monthlyExpenses);
            return new MonthlyExpensesResponse(formattedMonthlyExpenses);
        } catch (Exception e) {
            throw new BaseException(ACCOUNTBOOK_IS_EMPTY); // 가계부가 없는 경우 예외 처리
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    private Map<String, Double> convertYearMonthToString(Map<YearMonth, Double> monthlyExpenses) {
        Map<String, Double> formattedExpenses = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        for (Map.Entry<YearMonth, Double> entry : monthlyExpenses.entrySet()) {
            String formattedDate = entry.getKey().format(formatter);
            formattedExpenses.put(formattedDate, entry.getValue());
        }

        return formattedExpenses;
    }

    // 날짜별 지출 계산 메서드
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public DailyExpensesResponse getDailyExpenses(Long accountId) throws BaseException {
        try {
            AccountBook accountBook = accountBookRepository.findById(accountId)
                    .orElseThrow(() -> new BaseException(ACCOUNTBOOK_IS_EMPTY));

            List<Transaction> transactions = transactionRepository.findAllByAccountBook(accountBook);

            Map<Date, Double> dailyExpenses = new HashMap<>();
            for (Transaction transaction : transactions) {
                Date date = transaction.getDate();
                dailyExpenses.put(date, dailyExpenses.getOrDefault(date, 0.0) + transaction.getAmount());
            }

            // DailyExpensesResponse를 생성하여 반환
            Map<String, Double> formattedDailyExpenses = convertDateToString(dailyExpenses);
            return new DailyExpensesResponse(formattedDailyExpenses);
        } catch (BaseException e) {
            throw new BaseException(ACCOUNTBOOK_NOT_FOUND);
        }
    }

    private Map<String, Double> convertDateToString(Map<Date, Double> dailyExpenses) {
        Map<String, Double> formattedExpenses = new HashMap<>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        for (Map.Entry<Date, Double> entry : dailyExpenses.entrySet()) {
            String formattedDate = formatter.format(entry.getKey());
            formattedExpenses.put(formattedDate, entry.getValue());
        }

        return formattedExpenses;
    }

    // 요약
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public SummaryResponse getAccountBookSummary(Long accountId) throws BaseException {
        AccountBook accountBook = accountBookRepository.findById(accountId)
                .orElseThrow(() -> new BaseException(ACCOUNTBOOK_NOT_FOUND));

        double totalExpense = accountBook.getTotalExpense();
        double foodExpense = accountBook.getFoodExpense();
        double transportationExpense = accountBook.getTransportationExpense();
        double sightseeingExpense = accountBook.getSightseeingExpense();
        double shoppingExpense = accountBook.getShoppingExpense();
        double otherExpense = accountBook.getOtherExpense();

        double foodExpensePercentage = (foodExpense / totalExpense) * 100;
        double transportationExpensePercentage = (transportationExpense / totalExpense) * 100;
        double sightseeingExpensePercentage = (sightseeingExpense / totalExpense) * 100;
        double shoppingExpensePercentage = (shoppingExpense / totalExpense) * 100;
        double otherExpensePercentage = (otherExpense / totalExpense) * 100;

        SummaryResponse summaryResponse = new SummaryResponse();
        summaryResponse.setTotalExpense(totalExpense);
        summaryResponse.setFoodExpense(foodExpense);
        summaryResponse.setTransportationExpense(transportationExpense);
        summaryResponse.setSightseeingExpense(sightseeingExpense);
        summaryResponse.setShoppingExpense(shoppingExpense);
        summaryResponse.setOtherExpense(otherExpense);
        summaryResponse.setFoodExpensePercentage(foodExpensePercentage);
        summaryResponse.setTransportationExpensePercentage(transportationExpensePercentage);
        summaryResponse.setSightseeingExpensePercentage(sightseeingExpensePercentage);
        summaryResponse.setShoppingExpensePercentage(shoppingExpensePercentage);
        summaryResponse.setOtherExpensePercentage(otherExpensePercentage);

        return summaryResponse;
    }

}
