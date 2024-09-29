package pmf.android.soulapp.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

public class MusicActivity extends AppCompatActivity implements OnTrackClickListener, OnTrackCompleteListener, CompoundButton.OnCheckedChangeListener {

    private MusicService musicService;
    private boolean isBound = false;  //jel konektovan servis
    private RecyclerView recyclerView;
    private MusicAdapter musicAdapter;
    private TextView title;
    ToggleButton btnPlay;


    //trenutan subdir
    String subdir = null;

    //trenutno selektovana pesma
    private String currentTrack = null;
    private int currentTrackPos = -1;

    // ServiceConnection to manage binding to MusicService
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {

            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            musicService = binder.getService();
            isBound = true;

            Log.d("MusicActivity", "Service binding started");
            if (isBound) {
                Log.d("MusicActivity", "Service successfully bound");
            } else {
                Log.d("MusicActivity", "Service binding failed");
            }

            //listener
            musicService.setListener(MusicActivity.this);
            //restore state
            if (musicService.getLastSubdir() != null && musicService.getLastSubdir().equalsIgnoreCase(subdir)) {
                SharedPreferences prefs = getSharedPreferences("MusicPrefs", MODE_PRIVATE);
                currentTrack = prefs.getString("currentTrack", null);
                currentTrackPos = prefs.getInt("currentTrackPos", -1);

                // Ako je isti subdir, podesi UI na staro stanje
                if (currentTrack != null) {
                    runOnUiThread(() -> {
                        title.setText(currentTrack);
                        musicAdapter.setSelectedPosition(currentTrackPos);
                        recyclerView.scrollToPosition(currentTrackPos);
                    });
                    if (musicService.isPlaying()) {
                        runOnUiThread(() -> { //DA NE BI KRENULO ISPOCETKA
                            btnPlay.setOnCheckedChangeListener(null);  // ukloni listener
                            btnPlay.setChecked(true);  // podesi dugme
                            btnPlay.setOnCheckedChangeListener(MusicActivity.this);  //vrati listener
                        });
                    }
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            musicService = null;
            isBound = false;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); //nema night mode

        // Request notification permission if needed
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                        100);
            }
        }


        //gledamo koji potfolder treba da otvorimo
        Intent intent = getIntent();
        subdir = intent.getStringExtra("subdir");

        recyclerView = findViewById(R.id.recyclerViewTracks);
        // vertikalno scrollovanje
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicijalizujem adapter
        musicAdapter = new MusicAdapter(this, this, subdir);

        // Set the adapter to the RecyclerView
        recyclerView.setAdapter(musicAdapter);


        title = findViewById(R.id.textView3);
        TextView textView = findViewById(R.id.textView2);

        // naslov
        if (textView != null) {
            textView.setText(subdir);
        }


        if (title != null) {
            title.setText(getString(R.string.no_item_selected));
        }

        // dugmad
        ImageButton btnPrev = findViewById(R.id.button_previous);
        btnPlay = findViewById(R.id.button_play);
        ImageButton btnNext = findViewById(R.id.button_next);

        if(btnPlay != null) {
            btnPlay.setOnCheckedChangeListener(this);
        }
        if(btnPrev != null) {
            btnPrev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentTrack != null && currentTrackPos >= 0) { //ako je selektovano nesto
                        if (isBound) {
                            //pomeranje unazad
                            currentTrackPos = currentTrackPos > 0 ? currentTrackPos - 1 : musicAdapter.getItemCount() - 1;
                            currentTrack = musicAdapter.getTrack(currentTrackPos);
                            //selektuj
                            runOnUiThread(() -> title.setText(currentTrack));
                            musicAdapter.setSelectedPosition(currentTrackPos);
                            recyclerView.scrollToPosition(currentTrackPos);
                            //pusti
                            runOnUiThread(() -> {btnPlay.setChecked(false); btnPlay.setChecked(true);}); //mora false pa true da bi reagovao listener
                        }
                    } else {
                        showNothingSelectedToast();
                    }
                }
            });
        }
        if(btnNext != null) {
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentTrack != null && currentTrackPos >= 0) { //ako je selektovano nesto
                        if (isBound) {
                            playNextTrack();
                        }
                    } else {
                        showNothingSelectedToast();
                    }
                }
            });
        }
    }


    @Override
    protected void onStart() {  //povezivanje i pokretanje servisa i pamcenje prethodnog stanja
        super.onStart();
        // Bind
        Intent intent = new Intent(this, MusicService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);  // Ako je api level iznad 26
        } else {
            startService(intent);  // starije verzije
        }
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

        Log.d("MusicActivity", "Service binding started");
        if (isBound) {
            Log.d("MusicActivity", "Service successfully bound");
        } else {
            Log.d("MusicActivity", "Service binding failed");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service when the activity stops
        if (isBound) {
            unbindService(connection);
            isBound = false;
        }

        // cuvamo stanje
        if(currentTrackPos != -1) { //samo ako se nesto bilo pustilo, onda se prekida pustanje u prethodnom subdiru
            SharedPreferences prefs = getSharedPreferences("MusicPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("currentTrack", currentTrack);
            editor.putInt("currentTrackPos", currentTrackPos);
            editor.apply();
        }
    }

    @Override
    public void onTrackClick(String trackName, int position) {
        currentTrack = trackName;
        currentTrackPos = position;

        if (isBound) {
            //apdejtujemo UI elemente
            runOnUiThread(() -> {btnPlay.setChecked(false); btnPlay.setChecked(true);});
        }
    }

    @Override
    public void onTrackComplete() {
        playNextTrack();
    }

    private void playNextTrack(){
        //pomeranje unapred
        currentTrackPos =  currentTrackPos = (currentTrackPos + 1) % musicAdapter.getItemCount();
        currentTrack = musicAdapter.getTrack(currentTrackPos);
        //selektuj
        runOnUiThread(() -> title.setText(currentTrack));
        musicAdapter.setSelectedPosition(currentTrackPos);
        recyclerView.scrollToPosition(currentTrackPos);
        //pusti
        runOnUiThread(() -> {btnPlay.setChecked(false); btnPlay.setChecked(true);}); //mora false pa true da bi reagovao listener
    }

    private void showNothingSelectedToast() {
        // nije selektovana pesma nijedna
        Toast.makeText(this, getString(R.string.no_item_selected), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (currentTrack != null && currentTrackPos >= 0) { //ako je selektovano nesto
            if (btnPlay.isChecked()) {
                runOnUiThread(() -> title.setText(currentTrack));
                if (isBound && musicService != null) {
                    musicService.playSong(currentTrack, subdir);
                }
            } else { //not checked
                musicService.pauseSong();
            }
        } else { //ako nista nije selektovano
            showNothingSelectedToast();
        }
    }
}