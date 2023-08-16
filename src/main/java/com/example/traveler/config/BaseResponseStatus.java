package com.example.traveler.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    INVALID_JWT(false, 2000, "유효하지 않은 JWT입니다."),
    EMPTY_JWT(false,2001,"JWT를 입력하세요"),

    //login
    INVALID_AUTHORIZATION_CODE(false, 2010, "유효하지 않은 Authorization code입니다."),

    // [Patch] /users/profile_image
    PATCH_NULL_FILE(false, 2021, "파일을 선택해주세요."),
    PATCH_FAIL_UPLOAD_S3(false, 2022, "업로드에 실패하였습니다."),
    PATCH_INVALID_FILE_TYPE(false, 2023, "지원하지 않는 파일 형식입니다."),


    /**
     * 3000 : Response 오류
     */
    // Common
    SAVE_TRAVEL_FAIL(false, 3001, "여행 생성에 실패했습니다."),
    TRAVEL_IS_EMPTY(false, 3002, "여행이 존재하지 않습니다."),
    DELETE_TRAVEL_FAIL(false, 3003, "여행 삭제에 실패했습니다."),
    PATCH_TRAVEL_FAIL(false, 3004, "여행 수정에 실패했습니다."),
    TRAVEL_USER_NOT_MATCH(false, 3005, "여행의 유저 정보가 일치하지 않습니다."),
    SAVE_DAYCOURSE_FAIL(false, 3006, "코스 생성에 실패했습니다."),
    DAYCOURSE_IS_EMPTY(false, 3007, "코스가 존재하지 않습니다."),
    DAYCOURSE_EXISTS(false, 3008, "코스가 이미 존재합니다."),
    SPOT_IS_FULL(false, 3009, "장소를 추가할 수 없습니다. 장소는 최대 4개까지 생성할 수 있습니다."),
    SAVE_SPOT_FAIL(false, 3010, "장소 생성에 실패했습니다."),
    DELETE_SPOT_FAIL(false, 3011, "장소 삭제에 실패했습니다."),

    // Post
    DELETE_POST_FAIL(false, 5000, "삭제에 실패했습니다."),

    /**
     * 4000 : Database, Server 오류
     */

//    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),

    DATABASE_ERROR(false, 9000, "데이터베이스 연결에 실패하였습니다."),

    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다."),

    POST_IS_EMPTY(false,5003, "게시물이 존재하지 않습니다."),
    POST_SEARCH_FAIL(false, 5004, "게시물 검색에 실패했습니다."),
    POST_GET_FAIL(false, 5005, "게시물 조회 실패했습니다."),
    POST_LIKE_FAIL(false, 5006, "게시물 좋아요에 실패했습니다."),
    POST_LIKE_CANCEL_FAIL(false, 5007, "게시물 좋아요 취소에 실패했습니다."),
    POST_LIKE_GET_FAIL(false, 5008, "게시물 좋아요 상태 조회에 실패했습니다."),
    POST_SCRAP_FAIL(false, 5009, "게시물 찜에 실패했습니다."),
    POST_SCRAP_CANCEL_FAIL(false, 5010, "게시물 찜에 실패했습니다."),
    POST_SCRAP_GET_FAIL(false, 5011, "게시물 찜에 실패했습니다."),
    POST_LIKE_COUNT_FAIL(false, 5012, "게시물 좋아요 개수 조회에 실패했습니다."),
    POST_SCRAP_COUNT_FAIL(false, 5013, "게시물 찜 개수 조회에 실패했습니다."),
    SAVE_COMMENT_FAIL(false, 5500, "댓글 생성에 실패했습니다."),

    /**
     * 6000 : Checklist 오류
     */
    SAVE_CATEGORY_FAIL(false, 6001, "체크리스트 생성 실패했습니다."),
    UPDATE_CATEGORYNAME_FAIL( false, 6002, "체크리스트 수정 실패하였습니다."),
    CHECKLIST_IS_EMPTY(false,6003, "체크리스트가 존재하지 않습니다."),
    DELETE_CATEGORY_FAIL(false, 6004, "체크리스트 삭제 실패하였습니다"),
    CATEGORY_NOT_FOUND(false, 6005, "체크리스트 조회 실패했습니다."),
    SAVE_ITEM_FAIL(false, 6011, "준비물 생성에 실패했습니다."),
    UPDATE_ITEM_FAIL(false, 6012, "준비물 수정에 실패했습니다."),
    DELETE_ITEM_FAIL(false, 6013, "준비물 삭제에 실패했습니다."),
    ITEM_NOT_FOUND(false,6014, "준비물 조회 실패했습니다."),


    /**
     * 7000 : 가계부 오류
     */
    REQUEST_IS_EMPTY(false, 7001, "요청을 찾을 수 없습니다."),
    REQUEST_IS_INVALID(false, 7002, "요청을 찾을 수 없습니다."),
    SAVE_DATE_FAIL(false, 7003, "날짜 생성 실패했습니다."),
    DATE_NOT_FOUND(false, 7004, "날짜를 찾을 수 없습니다."),
    UPDATE_DATE_FAIL(false, 7005, "날자 수정 실패했습니다."),
    DELETE_DATE_FAIL(false, 7006, "널짜 삭제 실패했습니다."),
    SAVE_ACCOUNTBOOK_FAIL(false, 7011, "가계부 생성 실패했습니다."),
    ACCOUNTBOOK_NOT_FOUND(false, 7012, "가계부를 찾을 수 없습니다."),
    ACCOUNTBOOK_USER_NOT_MATCH(false, 7013,"가계부 사용자가 맞지 않습니다."),
    UPDATE_ACCOUNTBOOK_FAIL(false, 7014, "가계부 수정에 실패했습니다."),
    DELETE_ACCOUNTBOOK_FAIL(false, 7015, "가계부 삭제에 실패했습니다."),
    ACCOUNTBOOK_IS_EMPTY(false, 7016, "가계부가 존재하지 않습니다."),
    SAVE_TRANSACTION_FAIL(false, 7021, "내역 생성에 실패했습니다."),
    TRANSACTION_NOT_FOUND(false, 7022, "내역을 찾을 수 없습니다."),
    UPDATE_TRANSACTION_FAIL(false, 7023, "내역 수정에 실패했습니다."),
    DELETE_TRANSACTION_FAIL(false, 7025, "내역 삭제에 실패했습니다.");



    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) { //BaseResponseStatus 에서 각 해당하는 코드를 생성자로 맵핑
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
