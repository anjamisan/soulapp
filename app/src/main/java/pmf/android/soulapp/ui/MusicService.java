package pmf.android.soulapp.ui;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.myapplication.R;

import java.io.IOException;

public class MusicService extends Service {

    private static final String CHANNEL_ID = "music_channel";
    private MediaPlayer mediaPlayer;
    private OnTrackCompleteListener listener;
    private String lastSubdir = null;
    private boolean playing = false;


    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        createNotificationChannel();

        mediaPlayer.setOnCompletionListener(mp -> {
            if(listener != null){
             listener.onTrackComplete();
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notification notification = createNotification(getString(R.string.music_service), getString(R.string.running));
        startForeground(1, notification);
        Log.d("MusicService", "Foreground Service Started with ID=1");
            return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        super.onDestroy();
    }


    //nudimo binder da se activity moze konektovati na servis
    private final IBinder binder = new LocalBinder();

    public class LocalBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Music Service Channel",
                    NotificationManager.IMPORTANCE_LOW //ne pravi zvuk
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
                Log.d("MusicService", "Notification Channel Created: " + CHANNEL_ID);
            }
        }
    }

    public String getLastSubdir() {
        return lastSubdir;
    }

    public boolean isPlaying() {
        return playing;
    }

    private Notification createNotification(String title, String text) {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.soulapp_launcher)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();
    }

    public void setListener(OnTrackCompleteListener listener){
        this.listener = listener;
    }

    public void playSong(String songPath, String subdir) {
        try {
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();  // stopiraj ako se pusta nesto
                }
                mediaPlayer.reset(); // svakako moze reset

                // assets
                AssetFileDescriptor afd = getAssets().openFd(subdir + "/" + songPath);
                mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());

                mediaPlayer.prepare();
                mediaPlayer.start();

                // FOREGROUND
                Notification notification = createNotification(subdir, getString(R.string.playing) + songPath);
                Log.d("MusicService", "Notification Created");
                startForeground(1, notification); //id ce za sve pesme biti isti,
                // sto znaci da ce nova notifikacija zamniti staru umesto da se redjaju jedna pored druge
                Log.d("MusicService", "Foreground Service Started with ID=1");

                //restorovanje
                lastSubdir = subdir;
                playing = true;

            } else {
                throw new IllegalStateException("MediaPlayer is not initialized");
            }
        } catch (IOException | IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void pauseSong(){
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            playing = false;
        }
    }

}
