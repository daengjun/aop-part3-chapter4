package fastcampus.aop.part3.aop_part3_chapter4.model

import com.google.gson.annotations.SerializedName

// items 리스트 받아와서 book 데이터 클래스에 맵핑
data class SearchBooksDto(
    @SerializedName("items") val books: List<Book>
)