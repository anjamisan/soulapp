package pmf.android.soulapp.ui;
import android.content.Context;
import android.content.res.AssetManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {

    private List<String> musicTracks; //ime podfoldera? kao relaxationmusic
    Context context;
    private OnTrackClickListener listener;
    private int selectedPosition = -1; //nista selektovano

    public MusicAdapter(Context context, OnTrackClickListener listener, String subdir) {
        this.context = context;
        this.listener = listener;
        this.musicTracks = loadMusicTracks(subdir); // Load tracks from assets
    }

    private List<String> loadMusicTracks(String subdir) {
        List<String> tracks = new ArrayList<>();
        AssetManager assetManager = context.getAssets();
        try {
            // muzika iz potfoldera
            String[] files = assetManager.list(subdir);
            if (files != null) {
                tracks.addAll(Arrays.asList(files));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tracks;
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(com.example.myapplication.R.layout.item_music, parent, false);
        return new MusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
        String track = musicTracks.get(position);
        holder.songTitle.setText(track);

        //postavi za selektovano ako su ove dve varijable identicne
        holder.itemView.setSelected(position == selectedPosition);

        // u slucaju klika na pesmu
        holder.itemView.setOnClickListener(v -> { //lambda koja menja funkcijski interfejs tj implementira OnClick
            if (listener != null) {
                listener.onTrackClick(track, position);
            }
            setSelectedPosition(position); // Update the selected position
        });
    }

    @Override
    public int getItemCount() {
        return musicTracks.size();
    }

    public String getTrack(int pos){
        return musicTracks.get(pos);
    }

    // Method to update selected position and refresh the adapter
    public void setSelectedPosition(int position) {
        selectedPosition = position;
        notifyDataSetChanged(); //refresh
    }

    static class MusicViewHolder extends RecyclerView.ViewHolder {
        TextView songTitle;

        MusicViewHolder(View itemView) {
            super(itemView);
            songTitle = itemView.findViewById(R.id.songTitle);
        }
    }
}
