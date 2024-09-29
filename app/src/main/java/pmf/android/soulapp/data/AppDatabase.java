package pmf.android.soulapp.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Journal.class, Message.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract JournalDao journalDao();
    public abstract MessageDao messageDao();

    // Singleton metoda
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "soul_app_database")
                    .fallbackToDestructiveMigration() // korisno ako dodje do migracija (apdejtova, promena)
                    .build();
        }
        return instance;
    }
}
