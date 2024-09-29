package pmf.android.soulapp.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Message {

    @ColumnInfo(name = "message_id")
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String message;

    public Message() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
