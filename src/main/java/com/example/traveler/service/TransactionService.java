package com.example.traveler.service;

import com.example.traveler.config.BaseException;
import com.example.traveler.model.dto.ItemResponse;
import com.example.traveler.model.dto.TransactionRequest;
import com.example.traveler.model.dto.TransactionResponse;
import com.example.traveler.model.entity.AccountBook;
import com.example.traveler.model.entity.ItemEntity;
import com.example.traveler.model.entity.Transaction;
import com.example.traveler.model.entity.User;
import com.example.traveler.repository.AccountBookRepository;
import com.example.traveler.repository.TransactionRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.traveler.config.BaseResponseStatus.*;

@Service
public class TransactionService {
    private final AccountBookRepository accountBookRepository;
    private final TransactionRepository transactionRepository;
    @Autowired
    private UserService userService;

    @Autowired
    public TransactionService(AccountBookRepository accountBookRepository, TransactionRepository transactionRepository) {
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


}
