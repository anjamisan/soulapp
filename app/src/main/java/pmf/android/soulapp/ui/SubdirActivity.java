package pmf.android.soulapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.myapplication.R;

public class SubdirActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subdir_layout);

        // Find the buttons by their IDs
        ImageButton buttonNature = findViewById(R.id.nature);
        ImageButton buttonFocus = findViewById(R.id.focus);
        ImageButton buttonMeditation = findViewById(R.id.meditation);
        ImageButton buttonSleep = findViewById(R.id.sleep);

        if(buttonNature != null) {
            buttonNature.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(SubdirActivity.this, MusicActivity.class);
                    intent.putExtra("subdir", "nature");
                    startActivity(intent);
                }
            });
        }

        if(buttonFocus != null) {
            buttonFocus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(SubdirActivity.this, MusicActivity.class);
                    intent.putExtra("subdir", "focus");
                    startActivity(intent);
                }
            });
        }

        if(buttonMeditation != null) {
            buttonMeditation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(SubdirActivity.this, MusicActivity.class);
                    intent.putExtra("subdir", "meditation");
                    startActivity(intent);
                }
            });
        }

        if(buttonSleep != null) {
            buttonSleep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(SubdirActivity.this, MusicActivity.class);
                    intent.putExtra("subdir", "sleep");
                    startActivity(intent);
                }
            });
        }
    }
}
