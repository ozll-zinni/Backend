package com.example.traveler.controller;


import com.example.traveler.config.BaseException;
import com.example.traveler.config.BaseResponse;
import com.example.traveler.config.BaseResponseStatus;
import com.example.traveler.model.dto.*;
import com.example.traveler.model.entity.Travel;
import com.example.traveler.service.AccountBookService;
import com.example.traveler.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://traveler-smoky.vercel.app"})
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
    public BaseResponse<List<AccountBookResponse>> getAllAccountBookByTravel(@PathVariable Integer tId) throws BaseException{
        List<AccountBookResponse> accountBookResponses = accountBookService.getAllAccountBookByTravel(tId);
        return new BaseResponse<>(accountBookResponses);
    }


    // AccountBook
    // 새로운 여행에 대한 가계부 생성
    @PostMapping("/{tId}")
    public BaseResponse<List<DailyAccountBookResponse>> saveAccountBook(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable("tId") Integer tId,
            @RequestBody AccountBookRequest accountBookRequest) {

        try {
            List<DailyAccountBookResponse> accountBookResponses = accountBookService.saveAccountBook(
                    accessToken, tId, accountBookRequest);
            return new BaseResponse<>(accountBookResponses);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @PatchMapping("/{tId}")
    public BaseResponse<List<DailyAccountBookResponse>> createAccountBookEntries(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable("tId") Integer tId,
            @RequestBody AccountBookRequest accountBookRequest) {
        try {
            List<DailyAccountBookResponse> accountBookResponses = accountBookService.saveAccountBook(
                    accessToken, tId, accountBookRequest);

            return new BaseResponse<>(accountBookResponses);
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

    // 가계부 수정 (필요 없을듯)
//    @PatchMapping("/{accountId}")
//    public BaseResponse<AccountBookResponse> patchAccountBook(@RequestHeader("Authorization") String accessToken, @PathVariable("accountId") Long accountId, @RequestBody AccountBookRequest accountBookRequest) {
//        try {
//            AccountBookResponse accountBookResponse = accountBookService.patchAccountBook(accessToken, accountId, accountBookRequest);
//            return new BaseResponse<>(accountBookResponse);
//        } catch (BaseException exception) {
//            return new BaseResponse<>(exception.getStatus());
//        }
//    }

    // 새로운 내역을 특정 accountbook에 저장
    @PostMapping("/{accountId}/transactions")
    public BaseResponse<TransactionResponse> saveTransaction(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable("accountId") int accountId,
            @RequestBody TransactionRequest transactionRequest) {
        try {
            TransactionResponse transactionResponse = transactionService.saveTransaction(accessToken, accountId, transactionRequest);
            return new BaseResponse<>(transactionResponse);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    // 모든 transaction 정보 조회
    @GetMapping("/transactions")
    public BaseResponse<List<TransactionResponse>> getAllTransaction() {
        List<TransactionResponse> transactionResponses = transactionService.getAllTransaction();
        return new BaseResponse<>(transactionResponses);
    }

    // 특정 날짜 가계부에 포함된 내역 조회
    @GetMapping("/{accountId}/transactions/{transactionId}")
    public BaseResponse<TransactionResponse> getTransactionFromAccountbook(@PathVariable("accountId") int accountId, @PathVariable("transactionId") int transactionId) {
        try {
            TransactionResponse transactionResponse = transactionService.getTransactionFromAccountbook(accountId, transactionId);
            return new BaseResponse<>(transactionResponse);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    // 특정 날짜 가계부에 포함된 모든 내역 조회
    @GetMapping("/{accountId}/transactions")
    public BaseResponse<List<TransactionResponse>> getallTransactionByAccountbook(@PathVariable("accountId") int accountId) {
        try {
            List<TransactionResponse> transactionResponses = transactionService.getAllTransactionByAccountbook(accountId);
            return new BaseResponse<>(transactionResponses);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @PatchMapping("/{accountId}/transactions/{transactionId}")
    public BaseResponse<TransactionResponse> patchTransaction(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable("accountId") int accountId,
            @PathVariable("transactionId") TransactionRequest transactionId) {
        try {
            TransactionResponse transactionResponse = transactionService.patchTransaction(accessToken, accountId, transactionId);
            return new BaseResponse<>(transactionResponse);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

//    @PatchMapping("/{accountId}/transactions/{transactionId}/detail")
//    public BaseResponse<TransactionResponse> patchTransactionDetail(
//            @RequestHeader("Authorization") String accessToken,
//            @PathVariable("accountId") int accountId,
//            @PathVariable("transactionId") int transactionId,
//            @RequestBody TransactionDetailRequest transactionDetailRequest) {
//        try {
//            TransactionResponse transactionResponse = transactionService.patchTransactionDetail(accessToken, accountId, transactionId, transactionDetailRequest);
//            return new BaseResponse<>(transactionResponse);
//        } catch (BaseException exception) {
//            return new BaseResponse<>(exception.getStatus());
//        }
//    }


    // 특정 accountbook에 포함된 transaction 삭제
    @DeleteMapping("/{accountId}/transactions/{transactionId}")
    public BaseResponse<String> deleteTransactionFromAccountbook(@RequestHeader("Authorization") String accessToken,
                                                                 @PathVariable("accountId") int accountId, @PathVariable("transactionId") int transactionId) {
        try {
            int result = transactionService.deleteTransaction(accessToken, accountId, transactionId);
            if (result != 1) {
                throw new BaseException(BaseResponseStatus.DELETE_TRANSACTION_FAIL);
            } else {
                return new BaseResponse<>("가계부 내역 삭제에 성공했습니다.");
            }
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());

        }
    }
    // 가계부 요약 관련 기능
    @GetMapping("/{accountId}/summary")
    public BaseResponse<SummaryResponse> getAccountBookSummary(@PathVariable("tId") Long tId) {
        try {
            SummaryResponse summaryResponse = accountBookService.getAccountBookSummary(tId);
            return new BaseResponse<>(summaryResponse);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
