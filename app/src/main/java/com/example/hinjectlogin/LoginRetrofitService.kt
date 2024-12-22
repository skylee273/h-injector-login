package com.example.hinjectlogin

import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * (2)
 * LoginRetrofitService는 Retrofit을 사용하여 서버와 통신하기 위한 인터페이스입니다.
 * 이 인터페이스는 로그인 요청을 처리하는 API를 정의합니다.
 *
 * ### 주요 구성 요소 설명
 *
 * 1. **@Headers**:
 *    - HTTP 요청에 헤더를 추가합니다.
 *    - 이 경우, 요청 본문의 Content-Type을 `application/json;charset=UTF-8`로 설정하여 JSON 데이터를 서버에 전송하도록 명시합니다.
 *
 * 2. **@POST**:
 *    - HTTP POST 요청을 나타냅니다.
 *    - `users/login`은 요청을 보낼 서버의 엔드포인트(URL 경로)를 정의합니다.
 *
 * 3. **suspend fun**:
 *    - `suspend` 키워드는 함수가 코루틴에서 호출될 수 있음을 나타냅니다.
 *    - 코루틴은 Kotlin에서 비동기 프로그래밍을 간단하고 효율적으로 처리하기 위한 구조입니다.
 *    - **일반 `fun`과의 차이점**:
 *      - 일반 `fun`: 동기적으로 실행되며, 호출이 완료될 때까지 메인 스레드 또는 호출 스레드를 차단합니다.
 *      - `suspend fun`: 비동기적으로 실행되며, 호출 시 메인 스레드를 차단하지 않고 중단(suspend) 지점에서 다른 작업을 실행할 수 있습니다.
 *    - 이 함수는 네트워크 요청과 같은 시간이 오래 걸리는 작업에 적합하며, 비동기 처리로 UI 응답성을 유지할 수 있습니다.
 *
 * 4. **@Body**:
 *    - HTTP 요청 본문(body)에 데이터를 전달합니다.
 *    - 이 경우, `RequestBody` 타입의 데이터를 전송합니다.
 *
 * 5. **NetworkResponse<String>**:
 *    - 서버로부터의 응답을 나타냅니다.
 *    - `NetworkResponse`는 이전에 정의된 제네릭 데이터 클래스로, 응답 데이터(`data`) 타입은 `String`으로 설정되었습니다.
 *
 * - 클라이언트에서 로그인 요청을 서버로 전달하고, 응답 데이터를 관리합니다.
 */
interface LoginRetrofitService {

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("users/login")
    suspend fun login (
        @Body requestBody: RequestBody
    ) : NetworkResponse<String>
}
