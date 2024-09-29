package pmf.android.soulapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;

import pmf.android.soulapp.data.AppDatabase;
import pmf.android.soulapp.data.Journal;

public class JournalListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private JournalAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_list);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onStart() {
        super.onStart();

        // Refreshuj listu kad ides nazad iz JournalActivity
        AppDatabase db = AppDatabase.getInstance(this);
        new Thread(() -> {
            List<Journal> journals = db.journalDao().getAllEntries();
            runOnUiThread(() -> {
                adapter = new JournalAdapter(journals);
                recyclerView.setAdapter(adapter);
            });
        }).start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.journal_page_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.add) { //za svaki slucaj, mzd suvisno?
            Intent intent = new Intent(JournalListActivity.this, JournalActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item); //ako nije hendlovano
    }
}
