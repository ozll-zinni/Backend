package com.example.traveler.service;

import static com.example.traveler.config.BaseResponseStatus.ACCOUNTBOOK_IS_EMPTY;
import static com.example.traveler.config.BaseResponseStatus.DELETE_TRANSACTION_FAIL;
import static com.example.traveler.config.BaseResponseStatus.INVALID_JWT;
import static com.example.traveler.config.BaseResponseStatus.SAVE_ACCOUNTBOOK_FAIL;
import static com.example.traveler.config.BaseResponseStatus.SAVE_TRANSACTION_FAIL;
import static com.example.traveler.config.BaseResponseStatus.TRANSACTION_NOT_FOUND;
import static com.example.traveler.config.BaseResponseStatus.TRAVEL_IS_EMPTY;
import static com.example.traveler.config.BaseResponseStatus.UPDATE_TRANSACTION_FAIL;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.example.traveler.config.BaseException;
import com.example.traveler.model.dto.TransactionRequest;
import com.example.traveler.model.dto.TransactionResponse;
import com.example.traveler.model.entity.AccountBook;
import com.example.traveler.model.entity.Transaction;
import com.example.traveler.model.entity.Travel;
import com.example.traveler.model.entity.User;
import com.example.traveler.repository.AccountBookRepository;
import com.example.traveler.repository.TransactionRepository;
import com.example.traveler.repository.TravelRepository;

@Service
public class TransactionService {
    private final AccountBookRepository accountBookRepository;
    private final TransactionRepository transactionRepository;
    private final TravelRepository travelRepository;
    @Autowired
    private UserService userService;

    @Autowired
    public TransactionService(TravelRepository travelRepository, AccountBookRepository accountBookRepository, TransactionRepository transactionRepository) {
    	this.travelRepository = travelRepository;
        this.accountBookRepository = accountBookRepository;
        this.transactionRepository = transactionRepository;
    }

    // 내역 추가 기능
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public TransactionResponse saveTransaction(String accessToken, int accountId, TransactionRequest transactionRequest) throws BaseException {
        User user = userService.getUserByToken(accessToken);
        if (user == null) {
            throw new BaseException(INVALID_JWT);
        }
        AccountBook accountBook = accountBookRepository.findById((long) accountId)
                .orElseThrow(() -> new BaseException(ACCOUNTBOOK_IS_EMPTY));

        Transaction transaction = new Transaction();
        transaction.setAccountBook(accountBook);
        transaction.setExpenseCategory(transactionRequest.getExpenseCategory());
        transaction.setExpenseDetail("");
        transaction.setAmount(transactionRequest.getAmount());

        try {
            Transaction savedTransaction = transactionRepository.save(transaction);
            return new TransactionResponse(savedTransaction);
        } catch (Exception e) {
            throw new BaseException(SAVE_TRANSACTION_FAIL);
        }
    }


    // 해당 가계부의 모든 내역 조회 기능
    public List<TransactionResponse> getAllTransactionByAccountbook(int accountId) throws BaseException {
        AccountBook accountBook = accountBookRepository.findById((long) accountId)
                .orElseThrow(() -> new BaseException(ACCOUNTBOOK_IS_EMPTY));

        List<Transaction> transactions = transactionRepository.findAllByAccountBook(accountBook);

        return mapTransactionListToTransactionList(transactions);
    }
    private List<TransactionResponse> mapTransactionListToTransactionList(List<Transaction> transactions) {
        List<TransactionResponse> transactionResponses = new ArrayList<>();
        for (Transaction transaction : transactions) {
            TransactionResponse transactionResponse = new TransactionResponse(transaction.getTransactionId(), transaction.getExpenseCategory(), transaction.getExpenseDetail(), transaction.getAmount());
            transactionResponses.add(transactionResponse);
        }
        return transactionResponses;
    }
    // 특정 내역 조회 기능
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public TransactionResponse getTransaction(int transactionId) throws BaseException {
        Transaction transaction = transactionRepository.findById((long) transactionId)
                .orElseThrow(() -> new BaseException(TRANSACTION_NOT_FOUND));

        return new TransactionResponse(transaction.getTransactionId(), transaction.getExpenseCategory(), transaction.getExpenseDetail(), transaction.getAmount());
    }

    // 내역 수정 기능
    public TransactionResponse patchTransaction(String accessToken, int transactionId, TransactionRequest transactionRequest) throws BaseException {
        Transaction transaction = transactionRepository.findById((long) transactionId)
                .orElseThrow(() -> new BaseException(TRANSACTION_NOT_FOUND));

        transaction.setExpenseCategory(transactionRequest.getExpenseCategory());
        transaction.setExpenseDetail(transactionRequest.getExpenseDetail());
        transaction.setAmount(transactionRequest.getAmount());

        try {
            Transaction updatedTransaction = transactionRepository.save(transaction);
            return new TransactionResponse(updatedTransaction);
        } catch (Exception e) {
            throw new BaseException(UPDATE_TRANSACTION_FAIL);
        }
    }


//    public TransactionResponse patchTransactionDetail(String accessToken, int accountId, int transactionId, TransactionRequest transactionDetailRequest) throws BaseException {
//        User user = userService.getUserByToken(accessToken);
//        if (user == null) {
//            throw new BaseException(INVALID_JWT);
//        }
//        Transaction transaction = transactionRepository.findByTransactionIdAndAccountbook_accountId((long) transactionId, (long) accountId)
//                .orElseThrow(() -> new BaseException(TRANSACTION_NOT_FOUND));
//        transaction.setExpenseDetail(transactionDetailRequest.getExpenseDetail());
//
//        try {
//            transaction = transactionRepository.save(transaction);
//        } catch (Exception e) {
//            throw new BaseException(SAVE_ITEM_FAIL);
//        }
//
//        return new TransactionResponse(transaction.getTransactionId(), transaction.getExpenseCategory(), transaction.getExpenseDetail(), transaction.getAmount());
//    }


    // 내역 삭제 기능
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)

    public int deleteTransaction(String accessToken, int accountId, int transacrionId) throws BaseException {
        User user = userService.getUserByToken(accessToken);
        if (user == null) {
            throw new BaseException(INVALID_JWT);
        }
        Transaction transaction = (Transaction) transactionRepository.findByTransactionIdAndAccountBook_accountId((long) transacrionId, (long) accountId)
                .orElseThrow(() -> new BaseException(TRANSACTION_NOT_FOUND));

        try {
            transactionRepository.delete(transaction); // 수정된 부분
        } catch (Exception e) {
            throw new BaseException(DELETE_TRANSACTION_FAIL);
        }
        return accountId;
    }
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public List<TransactionResponse> getAllTransaction() {
        List<Transaction> transactions = transactionRepository.findAll();
        return mapTransactionListToTransactionList(transactions);
    }

    // 특정 가계부에 포함된 내역 조회 및 삭제
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public TransactionResponse getTransactionFromAccountbook(int accountId, int transactionId) throws BaseException {
        AccountBook accountBook = accountBookRepository.findByAccountId(accountId)
                .orElseThrow(() -> new BaseException(ACCOUNTBOOK_IS_EMPTY));
        Transaction transaction = transactionRepository.findByTransactionIdAndAccountBook((long) transactionId, accountBook)
                .orElseThrow(() -> new BaseException(TRANSACTION_NOT_FOUND));
        try {
            transactionRepository.delete(transaction);
        } catch (Exception e) {
            throw new BaseException(DELETE_TRANSACTION_FAIL);
        }
        return new TransactionResponse(transaction.getTransactionId(), transaction.getExpenseCategory(), transaction.getExpenseDetail(), transaction.getAmount());
    }

    
    
    /**
     *  Transaction 내역 추가 기능(신규)
     * @param accessToken
     * @param tId
     * @param transactionRequest
     * @return TransactionResponse
     * @throws BaseException
     */
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public TransactionResponse saveTransactionNew(String accessToken, int tId, TransactionRequest transactionRequest) throws BaseException {
    	
    	/*  //TODO 인증키 값 관련 프론트엔드단 로그인 이슈로 테스트 시 주석 처리 후 테스트
        User user = userService.getUserByToken(accessToken);
        if (user == null) {
            throw new BaseException(INVALID_JWT);
        }*/
    	
    	Travel travel = travelRepository.findById(tId)
                .orElseThrow(() -> new BaseException(TRAVEL_IS_EMPTY));
    	
    	//AccountId가 키값인데 Travel과 Date만 가지고 있기 때문에 키값을 찾거나 존재유무를 확인하기 위함
        AccountBook accountBook = accountBookRepository.findByTravelAndDate(travel, transactionRequest.getDate());
        
        if(Objects.isNull(accountBook)) {
        	accountBook = new AccountBook();
        	accountBook.setDate(transactionRequest.getDate());
            accountBook.setTravel(travel);
            accountBook.setAccountName("가계부");//TODO 현재 Table에 Account_Name이 NOT NULL 이라 넣어놓음 현재 받을 수 있는 부분이 없음
        }
        
        // 각 카테고리별로 해당하는 가계부 항목에 거래 내역의 금액을 추가
        switch (transactionRequest.getExpenseCategory()) {
            case "식비":
            	accountBook.setFoodExpense(accountBook.getFoodExpense() + transactionRequest.getAmount());
                break;
            case "교통비":
            	accountBook.setTransportationExpense(accountBook.getTransportationExpense() + transactionRequest.getAmount());
                break;
            case "숙박비":
            	accountBook.setLodgingExpense(accountBook.getLodgingExpense() + transactionRequest.getAmount());
                break;
            case "관광비":
            	accountBook.setSightseeingExpense(accountBook.getSightseeingExpense() + transactionRequest.getAmount());
                break;
            case "쇼핑비":
            	accountBook.setShoppingExpense(accountBook.getShoppingExpense() + transactionRequest.getAmount());
                break;
            case "기타":
            	accountBook.setOtherExpense(accountBook.getOtherExpense() + transactionRequest.getAmount());
                break;
        }
        Double totalBudget = transactionRequest.getTotalBudget();	//TODO 기존 예산을 전달 받을 곳이 없음.
        Double totalExpense = accountBook.getTotalExpense() +  transactionRequest.getAmount();//기존의 총 비용과 새로 추가된 비용을 합산
        
        accountBook.setTotalBudget(totalBudget);
        accountBook.setTotalExpense(totalExpense);
        accountBook.setBudgetUsagePercentage((totalExpense / totalBudget) * 100); // 예산 대비 지출 비율을 계산
        
        try {
        	//ACCOUNT_BOOK을 SAVE후 해당 키 값을 가져옴
        	accountBook = accountBookRepository.save(accountBook);
        } catch (Exception e) {
            throw new BaseException(SAVE_ACCOUNTBOOK_FAIL);
        }
        
        //Transaction 생성 후 데이터 바인딩
        Transaction transaction = new Transaction();

        //현재 Description, ExpenseItem 등 칼럼이 추가됨(기존 Category와 Detail 부분과 중복)
        transaction.setDate(transactionRequest.getDate());
        transaction.setDescription(transactionRequest.getExpenseDetail());
        transaction.setExpenseItem(transactionRequest.getExpenseCategory());
        transaction.setTransactionType("expense");	//TODO 해당 부분은 테이블을 참고하였으나 따로 참고할 부분이 없어서 둠.
        transaction.setExpenseCategory(transactionRequest.getExpenseCategory());
        transaction.setExpenseDetail(transactionRequest.getExpenseDetail());
        transaction.setAmount(transactionRequest.getAmount());
        transaction.setAccountBook(accountBook); // 거래 내역과 가계부 연결
        
        try {
            transaction = transactionRepository.save(transaction);
            return new TransactionResponse(transaction);
        } catch (Exception e) {
            throw new BaseException(SAVE_TRANSACTION_FAIL);
        }
    }
}
