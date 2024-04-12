package com.example.traveler.service;

import com.example.traveler.config.BaseException;
import com.example.traveler.model.dto.*;
import com.example.traveler.model.entity.*;
import com.example.traveler.repository.AccountBookRepository;
import com.example.traveler.repository.TransactionRepository;
import com.example.traveler.repository.TravelRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.example.traveler.config.BaseResponseStatus.*;

@Service
@AllArgsConstructor
public class AccountBookService {

    @Autowired
    private AccountBookRepository accountBookRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private TravelRepository travelRepository;
    @Autowired
    private UserService userService;

    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public List<DailyAccountBookResponse> saveAccountBook(String accessToken, Integer tId,
                                                          AccountBookRequest accountBookRequest) throws BaseException {

        User user = userService.getUserByToken(accessToken);
        if (user == null) {
            throw new BaseException(INVALID_JWT);
        }

        // 주어진 tId에 해당하는 여행 정보를 데이터베이스에서 조회
        Travel travel = travelRepository.findById(tId)
                .orElseThrow(() -> new BaseException(TRAVEL_IS_EMPTY));

        // 가계부 응답을 저장할 리스트 생성
        List<DailyAccountBookResponse> accountBookResponses = new ArrayList<>();

        // 각 일에 대해 가계부를 생성
        for (DailyAccountBookRequest dailyAccountBookRequest : accountBookRequest.getDailyAccountBooks()) {
            // 새로운 가계부 생성
            AccountBook newAccountBook = new AccountBook();
            newAccountBook.setDate(dailyAccountBookRequest.getDate());
            newAccountBook.setTotalBudget(accountBookRequest.getTotalBudget());

            // 가계부 저장
            try {
                newAccountBook = accountBookRepository.save(newAccountBook);
            } catch (Exception e) {
                throw new BaseException(SAVE_ACCOUNTBOOK_FAIL);
            }

            // 전달받은 거래 내역을 가계부에 추가
            for (TransactionRequest transactionRequest : dailyAccountBookRequest.getTransactions()) {
                // 새로운 거래 내역 생성 및 저장
                Transaction newTransaction = new Transaction();
                newTransaction.setExpenseCategory(transactionRequest.getExpenseCategory());
                newTransaction.setExpenseDetail(transactionRequest.getExpenseDetail());
                newTransaction.setAmount(transactionRequest.getAmount());
                newTransaction.setAccountBook(newAccountBook); // 거래 내역과 가계부 연결

                try {
                    newTransaction = transactionRepository.save(newTransaction);
                } catch (Exception e) {
                    throw new BaseException(SAVE_TRANSACTION_FAIL);
                }

                // 각 카테고리별로 해당하는 가계부 항목에 거래 내역의 금액을 추가
                switch (transactionRequest.getExpenseCategory()) {
                    case "식비":
                        newAccountBook.setFoodExpense(newAccountBook.getFoodExpense() + transactionRequest.getAmount());
                        break;
                    case "교통":
                        newAccountBook.setTransportationExpense(newAccountBook.getTransportationExpense() + transactionRequest.getAmount());
                        break;
                    case "숙박":
                        newAccountBook.setLodgingExpense(newAccountBook.getLodgingExpense() + transactionRequest.getAmount());
                        break;
                    case "관광":
                        newAccountBook.setSightseeingExpense(newAccountBook.getSightseeingExpense() + transactionRequest.getAmount());
                        break;
                    case "쇼핑":
                        newAccountBook.setShoppingExpense(newAccountBook.getShoppingExpense() + transactionRequest.getAmount());
                        break;
                    case "기타":
                        newAccountBook.setOtherExpense(newAccountBook.getOtherExpense() + transactionRequest.getAmount());
                        break;
                }
            }

            // 변경된 가계부 정보를 저장
            accountBookRepository.save(newAccountBook);

            // 응답을 위한 가계부 정보 생성
            DailyAccountBookResponse accountBookResponse = new DailyAccountBookResponse();
            accountBookResponse.setAccountId(newAccountBook.getAccountId());

            accountBookResponses.add(accountBookResponse);
        }

        return accountBookResponses;
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
    public List<AccountBookResponse> getAllAccountBookByTravel(int tId) throws BaseException {
        Travel travel = travelRepository.findById(tId)
                .orElseThrow(() -> new BaseException(TRAVEL_IS_EMPTY));
        
        List<Date> dates = getDatesBetweenTwoDates(travel.getStartDate(), travel.getEndDate()); 
        List<AccountBookResponse> accountBookResponses = new ArrayList<>();
        
        for(Date date : dates) {
        	AccountBookResponse response = new AccountBookResponse();
        	response.setDate(date);
        	
        	accountBookResponses.add(response);
        }
        
        List<AccountBook> accountBooks = accountBookRepository.findAllByTravelOrderByDateAsc(travel);
        
        for (AccountBookResponse accountBookResponse : accountBookResponses) {
	        for (AccountBook accountBook : accountBooks) {
	        	if(accountBookResponse.getDateStr().equals(sdf.format(accountBook.getDate()))){
	        		accountBookResponse.setAccountBook(accountBook);
		            List<TransactionResponse> transactionResponses = new ArrayList<>();
		            for (Transaction transaction : accountBook.getAccountbookTransaction()) {
		                TransactionResponse transactionResponse = new TransactionResponse();
		                
		                transactionResponse.setTransactionId(transaction.getTransactionId());
		                transactionResponse.setExpenseCategory(transaction.getExpenseCategory());
		                transactionResponse.setExpenseDetail(transaction.getExpenseDetail());
		                transactionResponse.setAmount(transaction.getAmount());
		                transactionResponses.add(transactionResponse); // 수정된 부분
		            }
		            accountBookResponse.setTransactions(transactionResponses);
	        	}
	        }
        }
        return accountBookResponses;
    }


//    // 특정 가계부의 날짜 정보 조회 로직 구현
//    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
//    public List<Date> getDatesForAccountBook(Long accountId) throws BaseException {
//        // accountBookRepository를 이용하여 accountId에 해당하는 가계부 정보 조회
//        AccountBook accountBook = accountBookRepository.findById(accountId)
//                .orElseThrow(() -> new BaseException(ACCOUNTBOOK_NOT_FOUND));
//
//        // 해당 가계부의 DateEntity들을 조회하여 날짜 정보 추출
//        List<Date> dates = new ArrayList<>();
//        for (DateEntity dateEntity : accountBook.getDateEntities()) {
//            dates.add(dateEntity.getDate());
//        }
//
//        return dates;
//    }


//    // 날짜별 지출 계산 메서드
//    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
//    public DailyExpensesResponse getDailyExpenses(Long accountId) throws BaseException {
//        try {
//            AccountBook accountBook = accountBookRepository.findById(accountId)
//                    .orElseThrow(() -> new BaseException(ACCOUNTBOOK_IS_EMPTY));
//
//            List<Transaction> transactions = transactionRepository.findAllByAccountBook(accountBook);
//
//            Map<Date, Double> dailyExpenses = new HashMap<>();
//            for (Transaction transaction : transactions) {
//                Date date = transaction.getDate();
//                dailyExpenses.put(date, dailyExpenses.getOrDefault(date, 0.0) + transaction.getAmount());
//            }
//
//            // DailyExpensesResponse를 생성하여 반환
//            Map<String, Double> formattedDailyExpenses = convertDateToString(dailyExpenses);
//            return new DailyExpensesResponse(formattedDailyExpenses);
//        } catch (BaseException e) {
//            throw new BaseException(ACCOUNTBOOK_NOT_FOUND);
//        }
//    }

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

        double budget = accountBook.getTotalBudget(); // 예산 정보를 가져옵니다.
        double totalExpense = accountBook.getTotalExpense(); // 총 지출 정보를 가져옵니다.
        double budgetUsagePercentage = (totalExpense / budget) * 100; // 예산 대비 지출 비율을 계산합니다.

        double foodExpense = accountBook.getFoodExpense();
        double transportationExpense = accountBook.getTransportationExpense();
        double lodgingExpense = accountBook.getLodgingExpense();
        double sightseeingExpense = accountBook.getSightseeingExpense();
        double shoppingExpense = accountBook.getShoppingExpense();
        double otherExpense = accountBook.getOtherExpense();

        double foodExpensePercentage = (foodExpense / totalExpense) * 100;
        double transportationExpensePercentage = (transportationExpense / totalExpense) * 100;
        double lodgingExpensePercentage = (lodgingExpense / totalExpense) * 100;
        double sightseeingExpensePercentage = (sightseeingExpense / totalExpense) * 100;
        double shoppingExpensePercentage = (shoppingExpense / totalExpense) * 100;
        double otherExpensePercentage = (otherExpense / totalExpense) * 100;

        SummaryResponse summaryResponse = new SummaryResponse();
        summaryResponse.setTotalBudget(budget);
        summaryResponse.setTotalExpense(totalExpense);
        summaryResponse.setBudgetUsagePercentage(budgetUsagePercentage);
        summaryResponse.setFoodExpense(foodExpense);
        summaryResponse.setTransportationExpense(transportationExpense);
        summaryResponse.setSightseeingExpense(sightseeingExpense);
        summaryResponse.setShoppingExpense(shoppingExpense);
        summaryResponse.setOtherExpense(otherExpense);
        summaryResponse.setFoodExpensePercentage(foodExpensePercentage);
        summaryResponse.setLodgingExpensePercentage(lodgingExpensePercentage);
        summaryResponse.setTransportationExpensePercentage(transportationExpensePercentage);
        summaryResponse.setSightseeingExpensePercentage(sightseeingExpensePercentage);
        summaryResponse.setShoppingExpensePercentage(shoppingExpensePercentage);
        summaryResponse.setOtherExpensePercentage(otherExpensePercentage);

        return summaryResponse;
    }
    
    
    // 	요약(추가)
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public SummaryResponse getAccountBookSummary(int tId) throws BaseException {
    	Travel travel = travelRepository.findById(tId)
                .orElseThrow(() -> new BaseException(TRAVEL_IS_EMPTY));
    	
        List<AccountBook> accountBooks = accountBookRepository.findAllByTravel(travel);
        
        double budget = 0;
        double totalExpense = 0;
        double budgetUsagePercentage = 0;

        double foodExpense = 0;
        double transportationExpense = 0;
        double lodgingExpense = 0;
        double sightseeingExpense = 0;
        double shoppingExpense = 0;
        double otherExpense = 0;

        double foodExpensePercentage = 0;
        double transportationExpensePercentage = 0;
        double lodgingExpensePercentage = 0;
        double sightseeingExpensePercentage = 0;
        double shoppingExpensePercentage = 0;
        double otherExpensePercentage = 0;
        
        for(AccountBook accountBook : accountBooks) {

	        budget 						= Double.sum(budget, accountBook.getTotalBudget()); 						// 예산 정보 총합
	        totalExpense 				= Double.sum(totalExpense, accountBook.getTotalExpense()); 					// 총 지출 총합
	
	        foodExpense 				= Double.sum(foodExpense, accountBook.getFoodExpense());					//식비 총합
	        transportationExpense 		= Double.sum(transportationExpense, accountBook.getTransportationExpense());//교통비 총합
	        lodgingExpense 				= Double.sum(lodgingExpense, accountBook.getLodgingExpense());				//숙박비 총합
	        sightseeingExpense 			= Double.sum(sightseeingExpense, accountBook.getSightseeingExpense());		//관광 총합
	        shoppingExpense 			= Double.sum(shoppingExpense, accountBook.getShoppingExpense());			//쇼핑 총합
	        otherExpense 				= Double.sum(otherExpense, accountBook.getOtherExpense());					//기타총합
	        
        }

        budgetUsagePercentage 			= (totalExpense / budget) * 100; // 예산 대비 지출 비율을 계산합니다.
        foodExpensePercentage 			= (foodExpense / totalExpense) * 100;
        transportationExpensePercentage = (transportationExpense / totalExpense) * 100;
        lodgingExpensePercentage 		= (lodgingExpense / totalExpense) * 100;
        sightseeingExpensePercentage 	= (sightseeingExpense / totalExpense) * 100;
        shoppingExpensePercentage 		= (shoppingExpense / totalExpense) * 100;
        otherExpensePercentage 			= (otherExpense / totalExpense) * 100;
        
        SummaryResponse summaryResponse = new SummaryResponse();
        summaryResponse.setTotalBudget(budget);
        summaryResponse.setTotalExpense(totalExpense);
        summaryResponse.setBudgetUsagePercentage(budgetUsagePercentage);
        summaryResponse.setFoodExpense(foodExpense);
        summaryResponse.setTransportationExpense(transportationExpense);
        summaryResponse.setSightseeingExpense(sightseeingExpense);
        summaryResponse.setShoppingExpense(shoppingExpense);
        summaryResponse.setOtherExpense(otherExpense);
        summaryResponse.setFoodExpensePercentage(foodExpensePercentage);
        summaryResponse.setLodgingExpensePercentage(lodgingExpensePercentage);
        summaryResponse.setTransportationExpensePercentage(transportationExpensePercentage);
        summaryResponse.setLodgingExpensePercentage(lodgingExpensePercentage);
        summaryResponse.setSightseeingExpensePercentage(sightseeingExpensePercentage);
        summaryResponse.setShoppingExpensePercentage(shoppingExpensePercentage);
        summaryResponse.setOtherExpensePercentage(otherExpensePercentage);

        return summaryResponse;
    }

    
    /**
     * 시작 날짜와 종료 날짜 사이의 날들을 List로 저장
     * @param startDate
     * @param endDate
     * @return List<Date>
     */
    public static List<Date> getDatesBetweenTwoDates(Date startDate, Date endDate) {
    	List<Date> dates = new ArrayList<>();
    	
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();

		/* Calendar 타입으로 변경 add()메소드로 1일씩 추가해 주기위해 변경 */
		cal1.setTime( startDate );
		cal2.setTime( endDate );
		
		
		while( cal1.compareTo( cal2 ) !=1 ){

			dates.add(cal1.getTime());

		 /* 시작날짜 + 1 일 */
			cal1.add(Calendar.DATE, 1);
		 }
		
		return dates;
    }
}
