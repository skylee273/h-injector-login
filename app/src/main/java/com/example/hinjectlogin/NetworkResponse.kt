package com.example.hinjectlogin

import kotlinx.serialization.Serializable

/**
 * (1)
 * NetworkResponse 클래스는 서버 응답을 처리하기 위해 설계된 제네릭 데이터 클래스입니다.
 *
 * ### Kotlin의 `data class`와 일반 `class`의 차이점
 *
 * 1. **`data class`**:
 *    - 데이터를 저장하고 관리하기 위한 클래스입니다.
 *    - 주요 기능:
 *      - 주 생성자에 정의된 프로퍼티를 기반으로 `toString`, `equals`, `hashCode`, `copy` 등의 메서드를 자동으로 생성합니다.
 *      - 데이터 중심의 객체를 간결하고 효율적으로 정의할 수 있습니다.
 *    - 주로 **데이터의 전달 및 저장**을 목적으로 사용됩니다.
 *
 * 2. **일반 `class`**:
 *    - 특정 로직이나 동작을 구현하는 클래스입니다.
 *    - `data class`와 달리 기본적으로 `toString`, `equals`, `hashCode` 등의 메서드가 자동으로 생성되지 않으며, 필요 시 직접 구현해야 합니다.
 *    - 주로 **동작, 상태 변경, 비즈니스 로직**을 처리하는 데 사용됩니다.
 *
 * ### NetworkResponse 설명
 *
 * @param T 제네릭 타입으로, 다양한 데이터 타입을 처리할 수 있습니다.
 * @property result 응답 결과를 나타내는 문자열로, 주로 "success" 또는 "failure"와 같은 값을 가집니다.
 * @property data 응답 데이터를 포함하며, 제네릭 타입으로 선언되어 다양한 데이터 구조를 유연하게 처리할 수 있습니다.
 * @property errorMessage 에러 발생 시 관련 메시지를 포함합니다. 에러가 없을 경우 빈 문자열일 수 있습니다.
 *
 * @Serializable 어노테이션을 사용하여 JSON과 같은 형식으로 직렬화 및 역직렬화가 가능합니다.
 * 이 클래스는 네트워크 요청 응답을 구조적으로 관리하고 처리하는 데 사용됩니다.
 */
@Serializable
data class NetworkResponse<T>(
    val result: String,
    val data: T,
    val errorMessage: String
)
