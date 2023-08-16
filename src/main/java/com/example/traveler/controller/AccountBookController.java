package com.example.traveler.controller;


import com.example.traveler.config.BaseException;
import com.example.traveler.config.BaseResponse;
import com.example.traveler.model.dto.*;
import com.example.traveler.model.entity.Travel;
import com.example.traveler.service.AccountBookService;
import com.example.traveler.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;


import static com.example.traveler.config.BaseResponseStatus.DELETE_ACCOUNTBOOK_FAIL;
import static com.example.traveler.config.BaseResponseStatus.DELETE_TRANSACTION_FAIL;




@RestController
@RequestMapping("/accountbook")
public class AccountBookController {
    private final AccountBookService accountBookService;
    private final TransactionService transactionService;


    @Autowired
    public AccountBookController(AccountBookService accountBookService, TransactionService transactionService) {
        this.accountBookService = accountBookService;
        this.transactionService = transactionService;
    }


    @GetMapping("/travel/{tId}")
    public List<AccountBookResponse> getAllAccountBookByTravel(@PathVariable Travel tId) throws BaseException {
        return accountBookService.getAllAccountBookByTravel(tId);
    }


    // AccountBook

    // 새로운 가계부 정보 저장
    @PostMapping("/{tId}")
    public BaseResponse<AccountBookResponse> saveAccountBook(@PathVariable Travel tId) {
        try {
            AccountBookResponse accountBookResponse = accountBookService.saveAccountBook(tId);
            return new BaseResponse<>(accountBookResponse);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    // 특정 가계부 정보 조회
    @GetMapping("/{accountId}")
    public BaseResponse<AccountBookResponse> getAccountBook(@PathVariable("accountId") int accountId) {
        try {
            AccountBookResponse accountBookResponse = accountBookService.getAccountBook(accountId);
            return new BaseResponse<>(accountBookResponse);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    // 가계부 수정
    @PatchMapping("/{accountId}")
    public BaseResponse<AccountBookResponse> patchAccountBook(@PathVariable("accountId") Long accountId, @RequestBody AccountBookRequest accountBookRequest) {
        try {
            AccountBookResponse accountBookResponse = accountBookService.patchAccountBook(accountId, accountBookRequest.getAccountName());
            return new BaseResponse<>(accountBookResponse);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    // 가계부 삭제
    @DeleteMapping("/{accountId}")
    public BaseResponse<String> deleteAccountBook(
            @PathVariable("accountId") Long accountId) {
        try {
            int result = accountBookService.deleteAccountBook(accountId);
            if (result != 1) {
                throw new BaseException(DELETE_ACCOUNTBOOK_FAIL);
            } else {
                return new BaseResponse<>("가계부 삭제에 성공했습니다.");
            }
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }


    // Transaction(내역) 관련 기능
    @PostMapping("/{accountId}/transaction")
    public BaseResponse<TransactionResponse> saveTransaction(@PathVariable("accountId") int accountId, @RequestBody TransactionRequest transactionRequest) {
        try {
            TransactionResponse transactionResponse = transactionService.saveTransaction(accountId, transactionRequest);
            return new BaseResponse<>(transactionResponse);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }


    @GetMapping("/transactions/accountbook/{accountId}")
    public BaseResponse<List<TransactionResponse>> getAllTransactionByAccountBook(@PathVariable("accountId") int accountId) {
        try {
            List<TransactionResponse> transactionResponseList = transactionService.getAllTransactionByAccountBook(accountId);
            return new BaseResponse<>(transactionResponseList);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }


    @GetMapping("/transactions/{transactionId}")
    public BaseResponse<TransactionResponse> getTransaction(@PathVariable("transactionId") int transactionId) {
        try {
            TransactionResponse transactionResponse = transactionService.getTransaction(transactionId);
            return new BaseResponse<>(transactionResponse);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }




    @PatchMapping("/transactions/{transactionId}")
    public BaseResponse<TransactionResponse> patchTransaction(@PathVariable("transactionId") int transactionId, @RequestBody TransactionRequest transactionRequest) {
        try {
            TransactionResponse transactionResponse = transactionService.patchTransaction(transactionId, transactionRequest);
            return new BaseResponse<>(transactionResponse);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }


    @DeleteMapping("/transactions/{transactionId}/{num}")
    public BaseResponse<String> deleteTransaction(@PathVariable("transactionId") int transactionId, @PathVariable("num") int num) {
        try {
            int result = transactionService.deleteTransaction(transactionId, num);
            if (result != 1) {
                throw new BaseException(DELETE_TRANSACTION_FAIL);
            } else {
                return new BaseResponse<>("내역 삭제에 성공했습니다.");
            }
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }


// // DateEntity(날자) 관련 기능
// @PostMapping("/date-entities")
// public BaseResponse<DateResponse> createDate(@PathVariable("accountId") int accountId, @RequestBody DateRequest dateRequest) {
// try {
// DateResponse dateResponse = dateService.createDate(accountId, dateRequest);
// return new BaseResponse<>(dateResponse);
// } catch (BaseException exception) {
// return new BaseResponse<>(exception.getStatus());
// }
// }
//
// @GetMapping("/date-entities/{dateId}")
// public BaseResponse<DateResponse> getDate(@PathVariable("dateId") int dateId) {
// try {
// DateResponse dateResponse = dateService.getDate(dateId);
// return new BaseResponse<>(dateResponse);
// } catch (BaseException exception) {
// return new BaseResponse<>(exception.getStatus());
// }
// }
//
// @GetMapping("/{accountId}/date-entities")
// public BaseResponse<List<DateResponse>> getAllDateByAccountBook(@PathVariable("accountId") int accountId) {
// try {
// List<DateResponse> dateResponse = dateService.getAllDateByAccountBook(accountId);
// return new BaseResponse<>(dateResponse);
// } catch (BaseException exception) {
// return new BaseResponse<>(exception.getStatus());
// }
// }
}