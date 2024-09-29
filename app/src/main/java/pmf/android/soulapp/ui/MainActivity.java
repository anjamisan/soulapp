package pmf.android.soulapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.R;

import pmf.android.soulapp.data.AppDatabase;
import pmf.android.soulapp.data.DatabaseInitializer;


public class MainActivity extends AppCompatActivity {

    Button musicButton;
    Button journalButton;
    Button messageButton;
    Button queryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // Inicijalizacija
        AppDatabase db = AppDatabase.getInstance(getApplicationContext());

        //populate the database
        DatabaseInitializer.populateDatabase(db, this);

        new Thread(() -> {
            int count = db.messageDao().getMessageCount();
            Log.d("MAIN", "number of messages: " + count);
        }).start();

        musicButton = findViewById(R.id.music);
        journalButton = findViewById(R.id.journal);
        messageButton = findViewById(R.id.message);
        queryButton = findViewById(R.id.query);

        //OnClickListener za dugmad
        musicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SubdirActivity.class);
                // Start
                startActivity(intent);
            }
        });
        journalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, JournalListActivity.class);
                // Start
                startActivity(intent);
            }
        });
        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MessageActivity.class);
                // Start
                startActivity(intent);
            }
        });
        queryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FoodQueryActivity.class);
                // Start
                startActivity(intent);
            }
        });
    }
}