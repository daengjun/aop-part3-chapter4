package fastcampus.aop.part3.aop_part3_chapter4.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fastcampus.aop.part3.aop_part3_chapter4.databinding.ItemBookBinding
import fastcampus.aop.part3.aop_part3_chapter4.model.Book


// recyclerview 에 사용할 리스트 어뎁터
class BookAdapter(val clickListener: (Book) -> Unit) : ListAdapter<Book, BookAdapter.ViewHolder>(diffUtil) {

    //clickListener 콜백 람다 함수

    inner class ViewHolder(private val binding: ItemBookBinding) : RecyclerView.ViewHolder(binding.root) {


        // bind 에 book 데이터 전달
        fun bind(bookModel: Book) {
            binding.titleTextView.text = bookModel.title
            binding.descriptionTextView.text = bookModel.description

            // 글라이드 이미지 셋팅
            Glide
                .with(binding.coverImageView.context)
                .load(bookModel.coverSmallUrl)
                .into(binding.coverImageView)

            // 콜백 함수 구현하기 (아이템 클릭했을때 동작)
            binding.root.setOnClickListener {
                clickListener(bookModel)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //currentList 로 현재 데이터 가져올 수 있음
        holder.bind(currentList[position])
    }

    companion object {

        // diffUtil 선언 같은 객체 인지, 값이 같은지 확인 해서 recyclerview 에 반영
        val diffUtil = object : DiffUtil.ItemCallback<Book>() {
            override fun areContentsTheSame(oldItem: Book, newItem: Book) =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: Book, newItem: Book) =
                oldItem.id == newItem.id
        }
    }

}