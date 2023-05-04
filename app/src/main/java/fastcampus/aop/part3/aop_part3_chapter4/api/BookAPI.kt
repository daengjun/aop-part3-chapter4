package fastcampus.aop.part3.aop_part3_chapter4.api

import fastcampus.aop.part3.aop_part3_chapter4.model.SearchBooksDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface BookAPI {

    @GET("/v1/search/book.json")
    fun getBooksByName(
        // 네이버 api는 헤더에 전달해야한다고함
        @Header("X-Naver-Client-Id") id: String,
        @Header("X-Naver-Client-Secret") secretKey: String,

        // URL 파라미터로 전달
        @Query("query") keyword: String
    ): Call<SearchBooksDto>

}