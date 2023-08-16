package com.example.traveler.service;

import com.example.traveler.config.BaseException;
import com.example.traveler.model.dto.TransactionRequest;
import com.example.traveler.model.dto.TransactionResponse;
import com.example.traveler.model.entity.AccountBook;
import com.example.traveler.model.entity.Transaction;
import com.example.traveler.repository.AccountBookRepository;
import com.example.traveler.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.traveler.config.BaseResponseStatus.*;

@Service
public class TransactionService {
    private final AccountBookRepository accountBookRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(AccountBookRepository accountBookRepository, TransactionRepository transactionRepository) {
        this.accountBookRepository = accountBookRepository;
        this.transactionRepository = transactionRepository;
    }

    // 내역 추가 기능
    public TransactionResponse saveTransaction(String accessToken, int accountId, TransactionRequest transactionRequest) throws BaseException {
        AccountBook accountBook = accountBookRepository.findById((long) accountId)
                .orElseThrow(() -> new BaseException(ACCOUNTBOOK_IS_EMPTY));

        Transaction transaction = new Transaction();
        transaction.setAccountBook(accountBook);
//        transaction.setDateId(transactionRequest.getDateId());
        transaction.setExpenseItem(transactionRequest.getExpenseItem());
        transaction.setDescription(transactionRequest.getDescription());
        transaction.setAmount(transactionRequest.getAmount());
        transaction.setTransactionType(transactionRequest.getTransactionType());

        try {
            Transaction savedTransaction = transactionRepository.save(transaction);
            return new TransactionResponse(savedTransaction);
        } catch (Exception e) {
            throw new BaseException(SAVE_TRANSACTION_FAIL);
        }
    }

    // 해당 가계부의 모든 내역 조회 기능
    public List<TransactionResponse> getAllTransactionByAccountBook(int accountId) throws BaseException {
        AccountBook accountBook = accountBookRepository.findById((long) accountId)
                .orElseThrow(() -> new BaseException(ACCOUNTBOOK_IS_EMPTY));

        List<Transaction> transactions = transactionRepository.findAllByAccountBook(accountBook);

        return transactions.stream()
                .map(TransactionResponse::new)
                .collect(Collectors.toList());
    }

    // 특정 내역 조회 기능
    public TransactionResponse getTransaction(int transactionId) throws BaseException {
        Transaction transaction = transactionRepository.findById((long) transactionId)
                .orElseThrow(() -> new BaseException(TRANSACTION_NOT_FOUND));

        return new TransactionResponse(transaction);
    }

    // 내역 수정 기능
    public TransactionResponse patchTransaction(String accessToken, int transactionId, TransactionRequest transactionRequest) throws BaseException {
        Transaction transaction = transactionRepository.findById((long) transactionId)
                .orElseThrow(() -> new BaseException(TRANSACTION_NOT_FOUND));

        // transactionRequest에 따라 내역 정보 업데이트
//        transaction.setDateId(transactionRequest.getDateId());
        transaction.setExpenseItem(transactionRequest.getExpenseItem());
        transaction.setDescription(transactionRequest.getDescription());
        transaction.setAmount(transactionRequest.getAmount());
        transaction.setTransactionType(transactionRequest.getTransactionType());

        try {
            Transaction updatedTransaction = transactionRepository.save(transaction);
            return new TransactionResponse(updatedTransaction);
        } catch (Exception e) {
            throw new BaseException(UPDATE_TRANSACTION_FAIL);
        }
    }

    // 내역 삭제 기능
    public int deleteTransaction(String accessToken, int transactionId, int num) throws BaseException {
        Transaction transaction = transactionRepository.findById((long) transactionId)
                .orElseThrow(() -> new BaseException(TRANSACTION_NOT_FOUND));

        // num 매개변수에 따라 삭제 로직 수행

        try {
            transactionRepository.delete(transaction);
            return 1;
        } catch (Exception e) {
            throw new BaseException(DELETE_TRANSACTION_FAIL);
        }
    }
}
