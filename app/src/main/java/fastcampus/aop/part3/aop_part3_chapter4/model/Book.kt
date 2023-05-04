package fastcampus.aop.part3.aop_part3_chapter4.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

//https://docs.google.com/document/d/1cFdIS5kAE33J7UpbMGKpNtumELO7YhVhEiEBx7ZQe_0/edit#
//https://developers.naver.com/docs/serviceapi/search/book/book.md

// json 데이터 맵핑할 데이터 클래스 생성 , Parcelize -> 직렬화 (intent로 객체를 넘기기 위해서)
@Parcelize
data class Book(
    @SerializedName("isbn") val id: String, // ISBN -> 책 구분 하기 위해서 받아옴 (리뷰 저장하고 불러올때 이용)
    @SerializedName("title") val title: String, // 책 제목
    @SerializedName("description") val description: String, //	네이버 도서의 책 소개
    @SerializedName("price") val priceSales: String, // 판매 가격 -> discount로 변경된듯? postman으로 돌려봐야할듯..
    @SerializedName("image") val coverSmallUrl: String, // 섬네일 이미지의 url
    @SerializedName("link") val mobileLink: String // 네이버 도서 정보 url
): Parcelable