package com.example.hinjectlogin

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * (5)
 * UserLocalDataSource 클래스는 로컬 데이터 저장소(DataStore)를 사용하여 사용자 데이터를 관리합니다.
 *
 * ### 주요 구성 요소
 *
 * 1. **Android `Context`**:
 *    - `Context`는 안드로이드에서 앱 환경에 대한 정보를 제공하는 클래스입니다.
 *    - 주요 역할:
 *      - 앱 리소스(예: 문자열, 이미지) 및 파일에 접근.
 *      - SharedPreferences, DataStore 등 데이터 저장소 접근.
 *      - 시스템 서비스(예: 알림, 위치, 클립보드) 관리.
 *    - 이 클래스에서 `Context`는 DataStore에 접근하기 위해 사용됩니다.
 *
 * 2. **DataStore**:
 *    - **DataStore란?**:
 *      - Jetpack 라이브러리로, 키-값 쌍 또는 타입 기반 데이터를 저장하는 데 사용됩니다.
 *      - SharedPreferences를 대체하기 위해 설계된 안정적이고 비동기적인 데이터 저장소입니다.
 *    - **특징**:
 *      - Kotlin Coroutines 기반으로 비동기 작업 지원.
 *      - 데이터 무결성을 보장하기 위해 `Flow`를 사용.
 *      - 파일 크기 제한이 없으며 데이터는 프로토콜 버퍼(Proto DataStore) 또는 기본 키-값 쌍(Preferences DataStore) 형식으로 저장.
 *    - **사용 이유**:
 *      - SharedPreferences보다 데이터 접근 속도가 더 빠르고, 데이터 변조 위험이 적음.
 *      - 비동기 방식으로 데이터를 처리해 UI 스레드가 차단되지 않음.
 *
 * 3. **DataStore 정의**:
 *    - `private const val USER_PREFERENCES_NAME = "user_preferences"`:
 *      - 데이터 저장소의 이름을 정의합니다. 저장소 파일이 `user_preferences`로 생성됩니다.
 *    - `private val Context.dataStore`:
 *      - `preferencesDataStore`를 사용하여 `Context`에 확장 프로퍼티를 추가합니다.
 *      - DataStore는 앱 내 데이터를 영구적으로 저장하기 위해 사용됩니다.
 *
 * 4. **companion object**:
 *    - `private val KEY_TOKEN = stringPreferencesKey("token")`:
 *      - DataStore에서 사용할 키를 정의합니다.
 *      - `KEY_TOKEN`은 "token"이라는 문자열을 식별자로 사용하여 데이터를 저장하고 불러옵니다.
 *
 * 5. **getToken()**:
 *    - **역할**: DataStore에서 저장된 사용자 토큰을 비동기로 가져옵니다.
 *    - **구현**:
 *      - `context.dataStore.data`: DataStore에 저장된 모든 데이터를 Flow로 제공합니다.
 *      - `.map { it[KEY_TOKEN] }`: `KEY_TOKEN`에 저장된 값을 추출합니다.
 *      - `.first()`: Flow에서 첫 번째 값을 가져옵니다. (suspend 함수)
 *    - **반환값**: 저장된 토큰 값(`String?`). 값이 없으면 `null` 반환.
 *
 * 6. **setToken(token: String)**:
 *    - **역할**: 사용자 토큰을 DataStore에 저장합니다.
 *    - **구현**:
 *      - `context.dataStore.edit { it[KEY_TOKEN] = token }`: DataStore에 `KEY_TOKEN` 키와 함께 `token` 값을 저장합니다.
 *
 * 7. **clear()**:
 *    - **역할**: DataStore에 저장된 모든 데이터를 삭제합니다.
 *    - **구현**:
 *      - `context.dataStore.edit { it.clear() }`: 저장소를 초기화하여 모든 데이터 삭제.
 *
 * ### 사용 예시
 * ```kotlin
 * val userLocalDataSource = UserLocalDataSource(context)
 *
 * // 토큰 저장
 * userLocalDataSource.setToken("exampleToken123")
 *
 * // 토큰 가져오기
 * val token = userLocalDataSource.getToken()
 * println("Stored Token: $token")
 *
 * // 데이터 초기화
 * userLocalDataSource.clear()
 * ```
 *
 * ### 역할
 * - 이 클래스는 앱의 로컬 데이터 저장 및 관리 기능을 추상화합니다.
 * - DataStore를 활용하여 사용자 데이터를 안전하게 저장하고 불러옵니다.
 * - 비동기 작업을 지원하며, 코루틴 기반으로 데이터 접근을 제공합니다.
 */
private const val USER_PREFERENCES_NAME = "user_preferences"
private val Context.dataStore by preferencesDataStore(name = USER_PREFERENCES_NAME)
class UserLocalDataSource constructor(
    private val context: Context
) {

    companion object {
        private val KEY_TOKEN = stringPreferencesKey("token")
    }

    suspend fun getToken(): String? {
        return context.dataStore.data.map { it[KEY_TOKEN] }.first()
    }

    suspend fun setToken(token: String) {
        context.dataStore.edit { it[KEY_TOKEN] = token }
    }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}
