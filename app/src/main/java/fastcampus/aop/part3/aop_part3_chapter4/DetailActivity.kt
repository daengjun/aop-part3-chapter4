package fastcampus.aop.part3.aop_part3_chapter4

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.bumptech.glide.Glide
import fastcampus.aop.part3.aop_part3_chapter4.databinding.ActivityDetailBinding
import fastcampus.aop.part3.aop_part3_chapter4.model.Book
import fastcampus.aop.part3.aop_part3_chapter4.model.Review

// 아이템 클릭했을 때 자세히 보기 화면
class DetailActivity : AppCompatActivity() {

    // 뷰바인딩 사용
    private lateinit var binding: ActivityDetailBinding
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // db 불러 오기
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "historyDB"
        ).build()

        // book객체를 직렬화로 받아옴
        val bookModel = intent.getParcelableExtra<Book>("bookModel")

        // 제목 입력 , orEmpty() -> null이 아니면 문자열을 반환하고, 그렇지 않으면 빈 문자열을 반환합니다.
        binding.titleTextView.text = bookModel?.title.orEmpty()


        // 글라이드에 이미지 설정
        Glide
            .with(binding.coverImageView.context)
            .load(bookModel?.coverSmallUrl.orEmpty())
            .into(binding.coverImageView)

        // 설명 입력
        binding.descriptionTextView.text = bookModel?.description.orEmpty()

        Thread {
            // 룸DB에 저장된 내 리뷰 데이터를 불러옴
            val review = db.reviewDao().getOne(bookModel?.id.orEmpty())
            runOnUiThread {
                // 에딧텍스트에 기존에 입력했던값이 있으면 넣어줌 , 리뷰에 값이 없을때 빈 문자열 입력
                binding.reviewEditText.setText(review?.review.orEmpty())
            }
        }.start()

        // 저장 버튼을 눌렀을 때
        binding.saveButton.setOnClickListener {
            Thread {
                db.reviewDao().saveReview(
                    // 리뷰 객체를 만들어서 saveReview에 인자로 전달함
                    Review(
                        bookModel?.id.orEmpty(), // 책 아이디 입력
                        binding.reviewEditText.text.toString() // 에딧텍스트에 입력된값 전달
                    )
                )

            }.start()
        }
    }
}