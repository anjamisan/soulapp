package pmf.android.soulapp.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
@Dao
public interface MessageDao {

    @Insert
    void insert(Message message);


    @Query("SELECT * FROM Message ORDER BY RANDOM() LIMIT 1")
    Message getRandomMessage(); //da vraca random poruku

    @Query("SELECT COUNT(*) FROM Message;")
    int getMessageCount(); //provera da li je paza populated
}
