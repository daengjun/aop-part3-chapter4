package fastcampus.aop.part3.aop_part3_chapter4.model

import com.google.gson.annotations.SerializedName

// interpark api 서비스 중단으로 인해 사용불가
data class BestSellerDto(
    //SerializedName 으로 선언된 변수명이 json object name과 달라도 맵핑 시켜줄수있음
    @SerializedName("title") val title: String,
    @SerializedName("item") val books: List<Book>
)