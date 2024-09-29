package pmf.android.soulapp.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.util.Log;

import androidx.preference.PreferenceManager;
import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class DatabaseInitializer {

    private static final String PREF_KEY_DATABASE_POPULATED = "db_populated"; //pravi se final key (final da bi bio konstantan naziv)
    private static final String FILE_NAME = "messages.txt";

    public static void populateDatabase(final AppDatabase db, final Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isPopulated = prefs.getBoolean(PREF_KEY_DATABASE_POPULATED, false); //proveri vrednost tog kljuca, ako nema kljuca stavi false

        if (!isPopulated) {
            new Thread(() -> { //runnable funkcionalni interfejs
                try {
                    // Obtain the DAO
                    MessageDao dao = db.messageDao();

                    //tekst sa porukama smesten u src/main/assets
                    AssetManager assetManager = context.getAssets();
                    try (InputStream inputStream = assetManager.open(FILE_NAME);
                         BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

                        String line;
                        while ((line = reader.readLine()) != null) {
                            // ubacujem u bazu
                            Message message = new Message();
                            message.setMessage(line); //id automatski generisan
                            dao.insert(message);
                            Log.d("Database", "inserted message");
                        }
                    }

                    // Mark as populated
                    SharedPreferences.Editor editor = prefs.edit(); //menjamo na true
                    editor.putBoolean(PREF_KEY_DATABASE_POPULATED, true);
                    editor.apply();

                } catch (IOException e) {
                    e.printStackTrace(); // Handle the exception appropriately
                }
            }).start(); // Start the thread
        }
        else{
            Log.d("Database", "already populated");
        }
    }
}