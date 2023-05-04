package fastcampus.aop.part3.aop_part3_chapter4.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import fastcampus.aop.part3.aop_part3_chapter4.model.History


// 룸 Dao 선언
@Dao
interface HistoryDao {

    // DB 전체값 가져 오기
    @Query("SELECT * FROM history")
    fun getAll(): List<History>

    // 인자값 으로 데이터 받아서 DB에 INSERT
    @Insert
    fun insertHistory(history: History)

    // DB 에서 인자로 받은 키워드 값을 찾아서 삭제
    @Query("DELETE FROM history WHERE keyword = :keyword")
    fun delete(keyword: String)

}