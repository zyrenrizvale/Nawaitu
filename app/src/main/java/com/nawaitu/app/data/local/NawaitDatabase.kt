package com.nawaitu.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.nawaitu.app.data.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [User::class, TodoItem::class, AlarmItem::class, CommunityPost::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class NawaitDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun todoDao(): TodoDao
    abstract fun alarmDao(): AlarmDao
    abstract fun communityPostDao(): CommunityPostDao

    companion object {
        @Volatile
        private var INSTANCE: NawaitDatabase? = null

        fun getDatabase(context: Context): NawaitDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NawaitDatabase::class.java,
                    "nawaitu_database"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                INSTANCE?.seedDefaultData()
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private suspend fun seedDefaultData() {
        val now = System.currentTimeMillis()

        // Default admin user
        userDao().insert(
            User(
                name = "Admin Nawaitu",
                email = "admin@nawaitu.app",
                password = "Nawaitu123"
            )
        )

        // Seed community posts
        communityPostDao().insert(
            CommunityPost(
                authorId = 1,
                authorName = "Admin Nawaitu",
                content = "Bismillah, selamat datang di Nawaitu. Semoga aplikasi ini bermanfaat untuk menjaga waktu sholat dan produktivitas kita.",
                likes = 24,
                createdAt = now - 3_600_000
            )
        )
        communityPostDao().insert(
            CommunityPost(
                authorId = 2,
                authorName = "Ustadz Fadhli",
                content = "\"Sesungguhnya sholat adalah tiang agama, barangsiapa mendirikan sholat maka ia telah menegakkan agama.\" - HR. Al-Baihaqi. Jangan sampai waktu sholat berlalu tanpa kita sadari.",
                likes = 87,
                createdAt = now - 7_200_000
            )
        )
        communityPostDao().insert(
            CommunityPost(
                authorId = 3,
                authorName = "Rizki Pratama",
                content = "Alhamdulillah, sudah 30 hari berhasil sholat tepat waktu! Semangat teman-teman, konsistensi adalah kunci.",
                likes = 41,
                createdAt = now - 86_400_000
            )
        )
        communityPostDao().insert(
            CommunityPost(
                authorId = 4,
                authorName = "Siti Aisyah",
                content = "Tips produktif: tulis niat harianmu sebelum tidur. Dengan Nawaitu, to-do list pagi bisa langsung disiapkan malam sebelumnya.",
                likes = 33,
                createdAt = now - 172_800_000
            )
        )
    }
}
