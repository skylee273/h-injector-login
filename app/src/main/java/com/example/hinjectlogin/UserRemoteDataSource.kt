package com.example.hinjectlogin

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * (4)
 * UserRemoteDataSource 클래스는 원격 데이터 소스를 처리하며, 서버와 통신하여 사용자 인증(로그인)을 수행합니다.
 *
 * ### 주요 구성 요소
 *
 * 1. **생성자 (constructor)**:
 *    - `private val service: LoginRetrofitService`:
 *      - 서버와 통신하기 위해 `LoginRetrofitService` 인터페이스를 주입받습니다.
 *      - 의존성 주입(DI)을 통해 서비스 객체를 관리합니다.
 *
 * 2. **suspend fun login(id: String, pw: String): String?**:
 *    - 사용자 ID와 비밀번호를 입력받아 로그인 요청을 처리하는 함수입니다.
 *    - 비동기 함수로 코루틴을 통해 실행되며, 네트워크 작업 중 메인 스레드가 차단되지 않습니다.
 *
 * ### 상세 동작 설명
 *
 * 1. **요청 데이터 생성**:
 *    - `val param = Json.encodeToString(LoginParam(id, pw))`:
 *      - 사용자 ID와 비밀번호를 포함하는 `LoginParam` 객체를 JSON 문자열로 직렬화합니다.
 *      - `kotlinx.serialization`의 `encodeToString`을 사용합니다.
 *
 * 2. **요청 본문(RequestBody) 변환**:
 *    - `param.toRequestBody()`:
 *      - JSON 문자열을 HTTP 요청 본문으로 변환합니다.
 *      - Retrofit 서비스에서 사용할 수 있는 `RequestBody` 형식으로 변환됩니다.
 *
 * 3. **서버 호출**:
 *    - `service.login(param.toRequestBody()).data`:
 *      - `LoginRetrofitService`의 `login` 메서드를 호출하여 서버에 로그인 요청을 보냅니다.
 *      - 응답으로 받은 `NetworkResponse` 객체의 `data` 속성을 반환합니다.
 *
 * 4. **에러 처리**:
 *    - `try-catch` 블록:
 *      - 네트워크 요청 중 발생할 수 있는 예외를 처리합니다.
 *      - 예외 발생 시 스택 트레이스를 출력하고, `null`을 반환하여 실패를 나타냅니다.
 *
 * ### 사용 예시
 * ```kotlin
 * val loginResult = userRemoteDataSource.login("user123", "password123")
 * if (loginResult != null) {
 *     println("Login successful: $loginResult")
 * } else {
 *     println("Login failed")
 * }
 * ```
 *
 * ### 역할
 * - 이 클래스는 서버와의 로그인 요청 및 응답 처리를 책임집니다.
 * - 네트워크 계층을 추상화하여 상위 계층에서 네트워크 로직을 직접 다루지 않도록 설계되었습니다.
 */
class UserRemoteDataSource(
    private val service: LoginRetrofitService
) {
    suspend fun login(id: String, pw: String): String? {
        return try {
            val param = Json.encodeToString(LoginParam(id, pw))
            service.login(param.toRequestBody()).data
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
