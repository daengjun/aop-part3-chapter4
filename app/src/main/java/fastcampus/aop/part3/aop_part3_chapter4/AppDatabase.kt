package fastcampus.aop.part3.aop_part3_chapter4

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import fastcampus.aop.part3.aop_part3_chapter4.dao.HistoryDao
import fastcampus.aop.part3.aop_part3_chapter4.dao.ReviewDao
import fastcampus.aop.part3.aop_part3_chapter4.model.History
import fastcampus.aop.part3.aop_part3_chapter4.model.Review


// entities 배열 안에 dao 여러 개의 값 추가 , DB가 변경 되면 version 올리기 + Migration
@Database(entities = [History::class, Review::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    //dao 추상 함수로 선언
    abstract fun historyDao(): HistoryDao
    abstract fun reviewDao(): ReviewDao
}


// 마이그레이션후 중복코드 제거 해야해서 따로 함수로 구현
fun getAppDatabase(context: Context): AppDatabase {

    // 버전이 올라 갔을 때 migration 작업 진행
    val migration_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE `REVIEW` (`id` INTEGER, `review` TEXT," + "PRIMARY KEY(`id`))")
        }
    }

    return Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "BookSearchDB"
    )
        .addMigrations(migration_1_2)
        .build()
}