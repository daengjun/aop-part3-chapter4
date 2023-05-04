package fastcampus.aop.part3.aop_part3_chapter4

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import fastcampus.aop.part3.aop_part3_chapter4.adapter.BookAdapter
import fastcampus.aop.part3.aop_part3_chapter4.adapter.HistoryAdapter
import fastcampus.aop.part3.aop_part3_chapter4.api.BookAPI
import fastcampus.aop.part3.aop_part3_chapter4.databinding.ActivityMainBinding
import fastcampus.aop.part3.aop_part3_chapter4.model.BestSellerDto
import fastcampus.aop.part3.aop_part3_chapter4.model.History
import fastcampus.aop.part3.aop_part3_chapter4.model.SearchBooksDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/** 인터파크 api 사용 중지로 인해서 네이버 api 사용
 *  베스트셀러 기능 X , 검색 구현만 가능함
 *
 * RecyclerView 사용하기
 * View Binding 사용하기
 * Retrofit 사용하기 (API 호출)
 * Android Room 사용하기 (복습 파트2, 챕터4 계산기, Local DB)
 * Glide 사용하기 (이미지 로딩)
 *
 * **/

//todo oncreate에 순서 상관없이 코드가 구현되어있는데 각 기능에따라 매서드로 따로 분류 하는게 좋을것같아보임


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: BookAdapter
    private lateinit var historyAdapter: HistoryAdapter

    private lateinit var service: BookAPI

    private lateinit var db: AppDatabase

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "historyDB"
        )
            .build()

        // BookAdapter 초기화 , 콜백 기능 구현
        adapter = BookAdapter(clickListener = {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("bookModel", it)
            startActivity(intent)
        })

        // HistoryAdapter 초기화 , 콜백 기능 구현 (history recyclerView x버튼 클릭 했을때)
        historyAdapter = HistoryAdapter(historyDeleteClickListener = {
            deleteSearchKeyword(it)
        })


        // retrofit 생성
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // bookApi 사용
        service = retrofit.create(BookAPI::class.java)
//        service.getBestSeller(getString(R.string.interpark_apikey))
//            .enqueue(object: Callback<BestSellerDto> {
//                override fun onFailure(call: Call<BestSellerDto>, t: Throwable) {
//
//                }
//
//                override fun onResponse(call: Call<BestSellerDto>, response: Response<BestSellerDto>) {
//                    if (response.isSuccessful.not()) {
//                        return
//                    }
//
//                    response.body()?.let {
//                        adapter.submitList(it.books)
//                    }
//                }
//
//            })
//


        // 리사이클러뷰 초기화
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        // 어뎁터 연결
        binding.recyclerView.adapter = adapter

        // 검색 onclick 기능 구현
        binding.searchEditText.setOnKeyListener { v, keyCode, event ->
            // keyCode가 엔터 입력과 키눌림 일때 동작
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
               // 에딧텍스트에 입력한 값을 인자로 전달해서 책 검색
                search(binding.searchEditText.text.toString())
                // 이벤트 처리가 되면 true
                return@setOnKeyListener true
            }
            return@setOnKeyListener false

        }

        // 검색창 클릭 했을때 구현 (최근 입력값 보여주기 위해서)
        binding.searchEditText.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                showHistoryView()
            }

            return@setOnTouchListener false
        }


        // 레이아웃 매니저 설정
        binding.historyRecyclerView.layoutManager = LinearLayoutManager(this)
        // 히스토리 리사이클러뷰 초기화
        binding.historyRecyclerView.adapter = historyAdapter



    }

    // 검색 할때 사용
    private fun search(text: String) {

        // bookapi에 정의한 GET 매서드 사용
        service.getBooksByName(
            getString(R.string.naver_id),
            getString(R.string.naver_secret_key),
            text
        )
            .enqueue(object : Callback<SearchBooksDto> {
                override fun onFailure(call: Call<SearchBooksDto>, t: Throwable) {
                    // History 숨기기
                    hideHistoryView()
                }

                override fun onResponse(
                    call: Call<SearchBooksDto>,
                    response: Response<SearchBooksDto>
                ) {

                    // History 숨기기
                    hideHistoryView()

                    // 키워드 저장
                    saveSearchKeyword(text)

                    if (response.isSuccessful.not()) {
                        return
                    }

                    response.body()
                        ?.let {
                            // listAdapter 에 값 전달 해서 RecyclerView 에 표시
                            adapter.submitList(it.books)
                        }
                }

            })
    }

    // 히스토리창 보이게 , DB에 저장된 값 불러오기 (맨아래값부터 하나씩 불러오기)

//    reverse() : 배열을 뒤집은 후 호출한 배열에 적용하는 Unit 함수
//    reversed() : 배열을 뒤집은 후 새로운 객체에 할당하는 List 반환형 함수
//    reversedArray() : 배열을 뒤집은 후 새로운 객체에 할당하는 Array 반환형 함수

    // reversed 사용하는이유는 showHistory에서만 반대로 보여주기 위함
    // reverse 해버리면 리스트값 안이 전체 반대로 데이터가 변경되기때문에 안됨
    private fun showHistoryView() {
        Thread(Runnable {
            db.historyDao()
                .getAll() //리스트를 반환
                .reversed() // 리스트 순서를 변경
                .run {
                    runOnUiThread {
                        binding.historyRecyclerView.isVisible = true
                        historyAdapter.submitList(this) //어뎁터 리스트 데이터 벼
                    }
                }

        }).start()

    }

    // 히스토리창 안보이게
    private fun hideHistoryView() {
        binding.historyRecyclerView.isVisible = false
    }

    // 히스토리 db에 방금입력한 값 저장하기
    private fun saveSearchKeyword(keyword: String) {

        // 키워드 값이 담긴 history 객체 전달
        Thread(Runnable {
            db.historyDao()
                .insertHistory(History(null, keyword))
        }).start()
    }

    // x 버튼 눌렀을때 룸 db에서 키워드값 제거
    private fun deleteSearchKeyword(keyword: String) {
        Thread(Runnable {
            db.historyDao()
                .delete(keyword)
            showHistoryView()
        }).start()
    }

    companion object {
        // 태그 ID
        private const val TAG = "MainActivity"

        // API 주소
        private const val BASE_URL = "https://openapi.naver.com/"
    }
}