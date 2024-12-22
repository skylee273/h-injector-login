package com.example.hinjectlogin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * LoginViewModel은 UI와 데이터 계층 간의 중간 역할을 수행하며,
 * 사용자 로그인 로직과 관련된 상태 관리 및 동작을 처리합니다.
 *
 * ### 주요 구성 요소
 *
 * 1. **ViewModel**:
 *    - 안드로이드 아키텍처 컴포넌트의 일부로, UI 관련 데이터를 관리하며,
 *      화면 회전 등 라이프사이클 변경에도 데이터를 유지합니다.
 *    - `viewModelScope`를 사용하여 ViewModel 내에서 코루틴을 안전하게 실행할 수 있습니다.
 *
 * 2. **MutableStateFlow & StateFlow**:
 *    - **`MutableStateFlow`**:
 *      - 상태를 저장하고, 업데이트하며, 다른 컴포넌트와 데이터를 공유하는 데 사용됩니다.
 *      - 내부적으로 값을 변경할 수 있도록 사용됩니다.
 *    - **`asStateFlow()`**:
 *      - 외부에서 읽기 전용으로 `StateFlow`를 제공하여 UI와 상태를 연결합니다.
 *
 * 3. **`_uiState` & `uiState`**:
 *    - **`_uiState`**: 내부적으로 상태를 관리하기 위한 `MutableStateFlow` 객체입니다.
 *    - **`uiState`**: 외부에 읽기 전용으로 제공되는 상태입니다.
 *
 * ### 주요 메서드 설명
 *
 * 1. **init 블록**:
 *    - ViewModel이 생성될 때 호출되며, 초기 상태를 설정합니다.
 *    - 사용자가 이미 로그인 상태인지 확인하여 상태를 업데이트합니다.
 *    - **`viewModelScope.launch`**:
 *      - 비동기 작업을 수행하며, ViewModel의 라이프사이클에 따라 코루틴이 자동 취소됩니다.
 *
 * 2. **login()**:
 *    - **역할**:
 *      - 저장소(`repository`)를 통해 사용자 로그인을 처리합니다.
 *    - **동작**:
 *      - 사용자가 입력한 ID와 PW를 `repository.login()`에 전달하여 로그인을 시도합니다.
 *      - 로그인 성공 여부에 따라 UI 상태를 업데이트합니다.
 *      - **`Dispatchers.IO`**:
 *        - 네트워크 작업과 같은 I/O 작업을 처리하기 위한 코루틴 디스패처를 지정합니다.
 *
 * 3. **onIdChange(value: String)**:
 *    - **역할**:
 *      - 사용자가 입력한 ID 값을 `uiState`에 업데이트합니다.
 *    - **동작**:
 *      - `_uiState.update { it.copy(id = value) }`를 통해 상태를 갱신합니다.
 *
 * 4. **onPwChange(value: String)**:
 *    - **역할**:
 *      - 사용자가 입력한 비밀번호 값을 `uiState`에 업데이트합니다.
 *    - **동작**:
 *      - `_uiState.update { it.copy(pw = value) }`를 통해 상태를 갱신합니다.
 *
 * ### LoginUiState 및 UserState 사용
 * - **`LoginUiState`**:
 *   - UI의 상태를 표현하는 데이터 클래스.
 *   - 예: 사용자 ID, 비밀번호, 로그인 상태 등을 포함.
 * - **`UserState`**:
 *   - 사용자 상태를 나타내는 Enum 클래스.
 *   - 예: `LOGGED_IN`, `FAILED` 등.
 *
 * ### 사용 예시
 * ```kotlin
 * val viewModel = LoginViewModel(repository)
 *
 * // ID와 비밀번호 업데이트
 * viewModel.onIdChange("user123")
 * viewModel.onPwChange("password123")
 *
 * // 로그인 시도
 * viewModel.login()
 *
 * // 상태 관찰 (UI에 반영)
 * viewModel.uiState.collect { state ->
 *     when (state.userState) {
 *         UserState.LOGGED_IN -> println("User logged in")
 *         UserState.FAILED -> println("Login failed")
 *         else -> println("Waiting for user input")
 *     }
 * }
 * ```
 *
 * ### 역할
 * - `LoginViewModel`은 로그인 상태와 사용자 입력값을 관리하며,
 *   UI와 데이터를 연결하는 중간 다리 역할을 합니다.
 * - UI 상태를 업데이트하고, 데이터 계층(UserDataRepository)과 상호작용을 조정합니다.
 */
class LoginViewModel(
    private val repository: UserDataRepository,
): ViewModel() {

    private val _uiState = MutableStateFlow(
        LoginUiState(id = "test", pw = "test1234")
    )
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            if(repository.isLoggedIn()) {
                val isLoggedInBefore = repository.isLoggedIn()
                if(isLoggedInBefore) {
                    _uiState.update { it.copy(userState = UserState.LOGGED_IN) }
                }
            }
        }
    }

    fun login() {
        viewModelScope.launch(Dispatchers.IO) {
            val isLoggedIn = repository.login(
                _uiState.value.id,
                _uiState.value.pw
            )
            val token = repository.getCurrentToken()
            _uiState.update {
                it.copy(userState = if(isLoggedIn) UserState.LOGGED_IN else UserState.FAILED)
            }
        }
    }

    fun onIdChange(value: String) {
        _uiState.update { it.copy(id = value) }
    }

    fun onPwChange(value: String) {
        _uiState.update { it.copy(pw = value) }
    }
}
