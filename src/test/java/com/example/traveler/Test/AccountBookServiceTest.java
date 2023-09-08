package com.example.traveler.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.traveler.config.BaseException;
import com.example.traveler.model.dto.AccountBookResponse;
import com.example.traveler.model.dto.DailyExpensesResponse;
import com.example.traveler.model.dto.MonthlyExpensesResponse;
import com.example.traveler.model.entity.AccountBook;
import com.example.traveler.model.entity.DateEntity;
import com.example.traveler.model.entity.Transaction;
import com.example.traveler.model.entity.Travel;
import com.example.traveler.repository.AccountBookRepository;
import com.example.traveler.repository.TransactionRepository;
import com.example.traveler.service.AccountBookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;


public class AccountBookServiceTest {

    private AccountBookService accountBookService;
    private AccountBookRepository accountBookRepository;
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        accountBookRepository = mock(AccountBookRepository.class);
        transactionRepository = mock(TransactionRepository.class);
        accountBookService = new AccountBookService(accountBookRepository, transactionRepository);
    }

    @Test
    void testSaveAccountBook() throws BaseException {
        Travel travel = new Travel();
        String accessToken = "your_access_token";
        double totalBudget = 1000.0;
        double foodExpense = 200.0;
        double transportationExpense = 150.0;
        double sightseeingExpense = 300.0;
        double shoppingExpense = 100.0;
        double otherExpense = 50.0;
        AccountBook mockAccountBook = new AccountBook();
        when(accountBookRepository.findByTravel(travel)).thenReturn(null);
        when(accountBookRepository.save(any(AccountBook.class))).thenReturn(mockAccountBook);

        AccountBookResponse accountBookResponse = accountBookService.saveAccountBook(
                accessToken, travel, totalBudget, foodExpense, transportationExpense,
                sightseeingExpense, shoppingExpense, otherExpense);

        assertNotNull(accountBookResponse);
        assertEquals("새로운 가계부", accountBookResponse.getAccountName());
    }

    @Test
    void testGetAccountBook() throws BaseException {
        Long accountId = 1L;
        AccountBook mockAccountBook = new AccountBook();
        when(accountBookRepository.findByAccountId(Math.toIntExact(accountId))).thenReturn(Optional.of(mockAccountBook));

        AccountBookResponse accountBookResponse = accountBookService.getAccountBook(Math.toIntExact(accountId));

        assertNotNull(accountBookResponse);
    }

    @Test
    void testPatchAccountBook() throws BaseException {
        String accessToken = "your_access_token";
        Long accountId = 1L;
        String newName = "Updated Account Book Name";
        AccountBook mockAccountBook = new AccountBook();
        when(accountBookRepository.findById(accountId)).thenReturn(Optional.of(mockAccountBook));

        AccountBookResponse accountBookResponse = accountBookService.patchAccountBook(accessToken, accountId, newName);

        assertNotNull(accountBookResponse);
        assertEquals(newName, accountBookResponse.getAccountName());
    }

    @Test
    void testDeleteAccountBook() throws BaseException {
        String accessToken = "your_access_token";
        Long accountId = 1L;
        AccountBook mockAccountBook = new AccountBook();
        when(accountBookRepository.findById(accountId)).thenReturn(Optional.of(mockAccountBook));

        int result = accountBookService.deleteAccountBook(accessToken, accountId);

        assertEquals(1, result);
    }

    @Test
    void testGetAllAccountBookByTravel() throws BaseException {
        Travel travel = new Travel();
        AccountBook mockAccountBook1 = new AccountBook();
        AccountBook mockAccountBook2 = new AccountBook();
        List<AccountBook> mockAccountBooks = Arrays.asList(mockAccountBook1, mockAccountBook2);
        when(accountBookRepository.findAllByTravel(travel)).thenReturn(mockAccountBooks);

        List<AccountBookResponse> accountBookResponses = accountBookService.getAllAccountBookByTravel(travel);

        assertNotNull(accountBookResponses);
        assertEquals(2, accountBookResponses.size());
    }

    @Test
    void testGetMonthlyExpenses() throws BaseException {
        Long accountId = 1L;
        AccountBook mockAccountBook = new AccountBook();
        List<Transaction> mockTransactions = Arrays.asList(
                new Transaction(new Date(), 100.0),
                new Transaction(new Date(), 200.0)
        );
        mockAccountBook.setTransactions(mockTransactions);
        when(accountBookRepository.findById(accountId)).thenReturn(Optional.of(mockAccountBook));

        MonthlyExpensesResponse monthlyExpensesResponse = accountBookService.getMonthlyExpenses(accountId);

        assertNotNull(monthlyExpensesResponse);
    }

    @Test
    void testGetDailyExpenses() throws BaseException {
        Long accountId = 1L;
        AccountBook mockAccountBook = new AccountBook();
        List<Transaction> mockTransactions = Arrays.asList(
                new Transaction(new Date(), 100.0),
                new Transaction(new Date(), 200.0)
        );
        mockAccountBook.setTransactions(mockTransactions);
        when(accountBookRepository.findById(accountId)).thenReturn(Optional.of(mockAccountBook));

        DailyExpensesResponse dailyExpensesResponse = accountBookService.getDailyExpenses(accountId);

        assertNotNull(dailyExpensesResponse);
    }





}
