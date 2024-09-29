package pmf.android.soulapp.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.myapplication.R;

import pmf.android.soulapp.data.AppDatabase;
import pmf.android.soulapp.data.Message;

public class MessageWorker extends Worker{


    public MessageWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    //result moze biti .success(), .failure(), .retry()
    public Result doWork() {

        String dailyMessage = null;
        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        Message message = db.messageDao().getRandomMessage();
        if(message != null){
            dailyMessage = message.getMessage();
            Log.d("MessageWorker", dailyMessage);
        }

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("DailyMessage", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("daily_message", dailyMessage);
        editor.apply(); //ako unese null bice provere u MessageActivity

        return Result.success();
    }
}
