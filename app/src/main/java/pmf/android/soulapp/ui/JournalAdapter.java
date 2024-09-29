package pmf.android.soulapp.ui;

import static java.util.Locale.*;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import pmf.android.soulapp.data.Journal;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.ViewHolder> {

    private List<Journal> journalList;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MMM.yyyy HH:mm", ENGLISH);

    public JournalAdapter(List<Journal> journalList) {
        this.journalList = journalList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.journal_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Journal journal = journalList.get(position);
        holder.noteTextView.setText(journal.getNote());
        holder.dateTextView.setText(dateFormat.format(new Date(journal.date)));

        if(journal.getImagePath() != null) { //inace bez slike
            File imgFile = new File(journal.getImagePath());
            if (imgFile.exists()) {

                Uri imageUri = Uri.fromFile(imgFile);
                holder.imageView.setImageURI(imageUri);
                //proveravanje orijentacije
//                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//
//                if (bitmap != null) {
//                    int width = bitmap.getWidth();
//                    int height = bitmap.getHeight();
//
//                    if (width > height) {
//                        // Landscape mode: tekst ispod slike
//                        holder.container.setOrientation(LinearLayout.VERTICAL);
//                    } else {
//                        // Portrait mode: tekst desno od slike
//                        holder.container.setOrientation(LinearLayout.HORIZONTAL);
//                    }
//
//                    holder.imageView.setImageBitmap(bitmap);
//                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return journalList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView noteTextView, dateTextView;
        public LinearLayout container;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            noteTextView = itemView.findViewById(R.id.note);
            dateTextView = itemView.findViewById(R.id.date);
            container = itemView.findViewById(R.id.container);
        }
    }
}
