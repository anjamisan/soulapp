package pmf.android.soulapp.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.myapplication.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import pmf.android.soulapp.data.AppDatabase;
import pmf.android.soulapp.data.Journal;

public class JournalActivity  extends AppCompatActivity {
    private ImageView journalImageView;
    private EditText journalNote;
    private String currentPhotoPath = null;
    private ActivityResultLauncher<Intent> takePictureLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.journal_layout);

        // permisija za koriscenje kamere
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CAMERA},
                    101);
        }

        journalImageView = findViewById(R.id.journalImageView);
        journalNote = findViewById(R.id.journalNote);
        Button saveJournalButton = findViewById(R.id.saveJournalButton);

        //ovo se aktivira nakon intenta iz sendTakePictureIntent-a
        takePictureLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        File imgFile = new File(currentPhotoPath);
                        if (imgFile.exists()) {
                            journalImageView.setImageDrawable(null);
                            journalImageView.setImageURI(Uri.fromFile(imgFile));
                        }
                    }
                }
        );

        // Open camera
        journalImageView.setOnClickListener(v -> sendTakePictureIntent());

        // Save the journal entry
        saveJournalButton.setOnClickListener(v -> saveJournalEntry());
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("currentPhotoPath", currentPhotoPath);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        currentPhotoPath = savedInstanceState.getString("currentPhotoPath");
        if(currentPhotoPath != null) {
            File imgFile = new File(currentPhotoPath);
            if (imgFile.exists()) {
                journalImageView.setImageDrawable(null);//ukloni pozadinu
                journalImageView.setImageURI(Uri.fromFile(imgFile));
            }

        }
    }

    private void sendTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //ako je pronadjena komponenta da podrzava intent:
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            //ako je uspeno alociran prostor za sliku, generisi uri i uslikaj sliku
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "pmf.android.soulapp.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                takePictureLauncher.launch(takePictureIntent);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile( //ovde ce biti smestena slika!
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );

        currentPhotoPath = image.getAbsolutePath(); //pamtimo putanju do slike, kasnije
        return image;
    }


    private void saveJournalEntry() {
        String note = journalNote.getText().toString();
        if(!note.equalsIgnoreCase("") || currentPhotoPath != null) {
            Journal journal = new Journal();
            journal.setImagePath(currentPhotoPath);
            journal.setNote(note);
            journal.date = System.currentTimeMillis();  // Store current date/time

            AppDatabase db = AppDatabase.getInstance(this);
            new Thread(() -> db.journalDao().insert(journal)).start();

            Toast.makeText(this, getString(R.string.saved), Toast.LENGTH_LONG).show();
            finish(); //zavrsi aktiviti
        }else{
            Toast.makeText(this, getString(R.string.no_input), Toast.LENGTH_LONG).show();
        }
    }
}


