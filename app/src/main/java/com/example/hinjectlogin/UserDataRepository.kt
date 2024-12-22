package com.example.hinjectlogin

/**
 * (6)
 * UserDataRepository는 로컬 및 원격 데이터 소스를 결합하여 사용자 데이터를 관리하는 클래스입니다.
 * 이 클래스는 앱의 데이터 계층을 추상화하여 데이터 소스에 대한 의존성을 줄이고, 데이터의 일관된 처리를 제공합니다.
 *
 * ### 주요 구성 요소
 *
 * 1. **생성자 (constructor)**:
 *    - `private val localDataSource: UserLocalDataSource`:
 *      - 로컬 저장소(DataStore)를 사용하여 사용자 데이터를 관리합니다.
 *    - `private val remoteDataSource: UserRemoteDataSource`:
 *      - 원격 서버와 통신하여 로그인 작업을 처리합니다.
 *
 * 2. **login(id: String, pw: String): Boolean**:
 *    - **역할**:
 *      - 사용자 ID와 비밀번호를 이용해 로그인 요청을 처리합니다.
 *    - **동작**:
 *      - `isLoggedIn()`을 호출하여 사용자가 이미 로그인 상태인지 확인합니다. 로그인되어 있다면 `true` 반환.
 *      - 로그인 상태가 아니라면 `remoteDataSource.login()`을 호출하여 서버로부터 토큰을 받아옵니다.
 *      - 토큰이 없으면 `false` 반환. 토큰을 받으면 `localDataSource.setToken(token)`을 호출해 로컬 저장소에 토큰을 저장합니다.
 *      - 성공적으로 토큰을 저장한 후 `true` 반환.
 *
 * 3. **isLoggedIn(): Boolean**:
 *    - **역할**:
 *      - 사용자가 로그인 상태인지 확인합니다.
 *    - **동작**:
 *      - `localDataSource.getToken()`으로 로컬 저장소에서 토큰을 가져옵니다.
 *      - 토큰이 `null`이거나 비어 있으면 `false`, 그렇지 않으면 `true` 반환.
 *
 * 4. **getCurrentToken(): String?**:
 *    - **역할**:
 *      - 현재 저장된 사용자 토큰을 반환합니다.
 *    - **동작**:
 *      - `localDataSource.getToken()`을 호출해 로컬 저장소에서 토큰을 가져옵니다.
 *      - 토큰이 존재하면 반환하고, 없으면 `null` 반환.
 *
 * 5. **logout()**:
 *    - **역할**:
 *      - 사용자 로그아웃을 처리합니다.
 *    - **동작**:
 *      - `localDataSource.clear()`를 호출하여 로컬 저장소의 모든 데이터를 초기화합니다.
 *
 * ### 사용 예시
 * ```kotlin
 * val repository = UserDataRepository(localDataSource, remoteDataSource)
 *
 * // 로그인 처리
 * val loginSuccess = repository.login("user123", "password123")
 * if (loginSuccess) {
 *     println("Login successful")
 * } else {
 *     println("Login failed")
 * }
 *
 * // 현재 로그인 상태 확인
 * if (repository.isLoggedIn()) {
 *     println("User is logged in")
 * } else {
 *     println("User is not logged in")
 * }
 *
 * // 로그아웃
 * repository.logout()
 * println("User logged out")
 * ```
 *
 * ### 역할
 * - UserDataRepository는 로컬과 원격 데이터 소스를 통합하여 데이터 처리 로직을 간소화합니다.
 * - 로그인, 로그아웃, 로그인 상태 확인, 현재 토큰 조회와 같은 사용자 관련 작업을 한 곳에서 처리할 수 있습니다.
 * - 데이터 계층의 단일 진입점을 제공하여 코드 재사용성과 유지보수성을 높입니다.
 */
class UserDataRepository(
    private val localDataSource: UserLocalDataSource,
    private val remoteDataSource: UserRemoteDataSource,
) {
    suspend fun login(id: String, pw : String) : Boolean {
        if(isLoggedIn()){
            return true
        }
        val token = remoteDataSource.login(id, pw) ?: return false
        localDataSource.setToken(token)
        return true
    }

    suspend fun isLoggedIn(): Boolean {
        return !localDataSource.getToken().isNullOrEmpty()
    }

    suspend fun getCurrentToken(): String? {
        return localDataSource.getToken()
    }

    suspend fun logout() {
        localDataSource.clear()
    }
}
