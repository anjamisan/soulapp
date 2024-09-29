package pmf.android.soulapp.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface JournalDao {
    @Insert
    void insert(Journal journal);

    @Query("SELECT * FROM Journal ORDER BY date DESC")
    List<Journal> getAllEntries();
}
