package pmf.android.soulapp.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.myapplication.R;

import java.io.File;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class MessageActivity extends AppCompatActivity {

    private ImageView cookie;
    private TextView messageText;
    private AnimationDrawable animation;
    String msg = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_layout);

        // pokreni worker-a
        scheduleDailyQuote();

        //animacija
        cookie = findViewById(R.id.cookie);
        messageText = findViewById(R.id.messageTextView);
        if(cookie != null){
            cookie.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    msg = getDailyMessage();
                    messageText.setText(msg);
                    messageText.setVisibility(View.VISIBLE);
                    cookie.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("message", msg);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        msg = savedInstanceState.getString("message");
        if(msg != null){
            messageText.setText(msg);
            messageText.setVisibility(View.VISIBLE);
            cookie.setVisibility(View.INVISIBLE);
        }
    }


    public void scheduleDailyQuote() {


        Calendar currentDate = Calendar.getInstance();
        Calendar dueDate = Calendar.getInstance(); //zakazano vreme

        //zakazi za 00:00
        dueDate.set(Calendar.HOUR_OF_DAY, 0);
        dueDate.set(Calendar.MINUTE, 0);
        dueDate.set(Calendar.SECOND, 0);

        if (dueDate.before(currentDate)) {
            // Ako je proslo 00:00 tog dana, zakazi za sledeci dan
            dueDate.add(Calendar.DAY_OF_MONTH, 1); //dodaj jedan dan
        }

        long timeDiff = dueDate.getTimeInMillis() - currentDate.getTimeInMillis();



        PeriodicWorkRequest dailyWorkRequest = //pokrece se jednom pri prvom izvrsavanju
                new PeriodicWorkRequest.Builder(MessageWorker.class, 24, TimeUnit.HOURS) //na svaka 24h
                        .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
                        .build();

        //sprovedi
        WorkManager workManager = WorkManager.getInstance(getApplicationContext());
        //workManager.enqueue(immediateWorkRequest);
        workManager.enqueue(dailyWorkRequest);

    }

    public String getDailyMessage(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("DailyMessage", Context.MODE_PRIVATE);
        String savedMessage = sharedPreferences.getString("daily_message", getApplicationContext().getString(R.string.first_quote)); // Default  vtednost

        return savedMessage;
    }
}
