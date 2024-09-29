package pmf.android.soulapp.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Journal {

    @ColumnInfo(name = "journal_id")
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String imagePath;  // putanja do slike

    private String note;  // poruka uz sliku

    public long date; //timestamp za sortiranje

    public Journal() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}