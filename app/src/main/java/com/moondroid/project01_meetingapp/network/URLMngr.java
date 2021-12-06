package com.moondroid.project01_meetingapp.network;

public class URLMngr {
    public final static String ENV = "TEST";
    //public final static String ENV = "PROD";

    public final static String SERVER_BASE_URL_TEST = "http://moondroid.dothome.co.kr/damoim-test/";        //테스트 서버
    public final static String SERVER_BASE_ULR_PROD = "http://moondroid.dothome.co.kr/damoim/";             //운영 서버

    public final static String IMG_URL = SERVER_BASE_ULR_PROD;

    public static String BASE_URL_DEFAULT;

    static {
        switch (ENV) {
            case "PROD":
                BASE_URL_DEFAULT = SERVER_BASE_ULR_PROD;
                break;
            case "TEST":
                BASE_URL_DEFAULT = SERVER_BASE_URL_TEST;
                break;
        }
    }

    public static final String SAVE_USER = "saveUserBaseDB.php";                    // 회원가입
    public static final String UPDATE_USER = "updateUserBaseDB.php";                // 유저 정보 수정
    public static final String CHECK_ID = "checkUserId.php";                        // 회원가입 - 아이디 중복 체크
    public static final String CHECK_GROUP_NAME = "checkMeetName.php";              // 모임 이름 중복 체크
    public static final String UPDATE_INTEREST = "updateUserInterest.php";          // 관심사 업데이트
    public static final String USER_INFO = "loadUserBaseDBOnIntro.php";             // 유저 정보 로드
    public static final String SAVE_GROUP = "saveItemBaseDB.php";                   // 모임 만들기 - 저장
    public static final String GET_GROUP = "getItemBaseData.php";                   // 모임 내용 로드
    public static final String UPDATE_MEET = "updateItemBaseDB.php";                // 모임 설명 수정
    public static final String SAVE_USER_MEET_DT = "saveUserMeetData.php";          // 모임 참여 내용 저장
    public static final String CHECK_USER_MEET_DT = "checkUserMeetData.php";        // 모임 참여 목록 로드
    public static final String LOAD_MEMBERS = "loadMembers.php";                    // 모임 멤버 로드
    public static final String LOAD_USERS_MEET = "loadUserMeetItem.php";            // 참여 모임 로드
    public static final String SAVE_USER_KAKAO = "saveUserBaseDataToKakako.php";    // 회원가입 - 카카오
    public static final String SAVE_FCM = "saveFCMToken.php";                       // FCM 토큰 저장
    public static final String SEND_FCM = "sendFCMMessage.php";                     // 대화창 알림 전송
    public static final String SAVE_MOIM = "saveMoimInfo.php";                      // 모임 만들기
    public static final String LOAD_MOIM = "loadMoims.php";                         // 모임 불러오기
    public static final String LOAD_CHAT = "loadChatInfo.php";                      // 대화 내용 불러오기
    public static final String SEND_FCM_MOIM = "sendFCMMessageMoim.php";            // 정모 알림 메세지
    public static final String LOAD_MOIM_ALL = "loadMoimsAll.php";                  // 모든 모임 정보 로드
    public static final String INSERT_FAVOR = "insertFavor.php";                    // 관심 목록 추가
    public static final String DELETE_FAVOR = "deleteFavor.php";                    // 관심 목록 제거
    public static final String LOAD_USER_FAVOR = "loadUserFavoriteItem.php";        // 관심 모임 로드
    public static final String CHECK_USER_FAVOR = "checkUserFavorite.php";          // 관심 목록 로드
    public static final String SAVE_RECENT = "uploadRecentMoim.php";                // 최근 본 모임 저장
    public static final String LOAD_RECENT = "loadRecentMoim.php";                  // 최근 본 모임 불러오기
    public static final String LOAD_JOIN = "loadJoinMembers.php";                   // 정모 참여 멤버
    public static final String ADD_JOIN = "addJoinMember.php";                      // 정모 참여
    public static final String UPDATE_SETTING = "updateFCMSetting.php";             // 알림 설정
    public static final String LOAD_SETTING = "loadFCMSetting.php";                 // 알림 설정 로드
}
