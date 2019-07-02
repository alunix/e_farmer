package com.example.e_farmer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.e_farmer.models.Machine;
import com.example.e_farmer.models.User;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.objectbox.Box;
import io.objectbox.BoxStore;

public class MyMachinery extends AppCompatActivity {
    private static final String TAG = "MyMachinery";
    public static final int REQUEST_TAKE_PHOTO = 61;
    private String currentPhotoPath;

    private Bitmap mImageBitmap;
    private Toolbar toolbar;
    private EditText machineName, machineType, machineRegYear, machinePurchaceDate, machineOriginalPrice, machineCurrentPrice, machinemilage, machineNotes;
    private Button saveMachine;
    private ImageView machineImage;
    private String name, type, registration_year, purchase_date, original_price, current_price, milage, notes;

    private Machine machine;
    private Box<Machine> machineBox;
    private BoxStore farmerApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_machinery);

        //        objectBox initialization
        farmerApp = FarmerApp.getBoxStore();
        machineBox = farmerApp.boxFor(Machine.class);

        // This convention should follow to have a successful toolbar set with the correct set title.
        toolbar = findViewById(R.id.toolbar_machinery);
        toolbar.setTitle("Add Machine");
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        machineName = findViewById(R.id.machinery_name);
        machineType = findViewById(R.id.machinery_type);
        machineRegYear = findViewById(R.id.registration_year);
        machinePurchaceDate = findViewById(R.id.date_of_purchase);
        machineOriginalPrice = findViewById(R.id.price);
        machineCurrentPrice = findViewById(R.id.current_price);
        machinemilage = findViewById(R.id.miles_per_hour);
        machineNotes = findViewById(R.id.machine_notes);

        saveMachine = findViewById(R.id.save_machinery_btn);
        machineImage = findViewById(R.id.machine_image);

        machineImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        saveMachine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAnimal();
            }
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
//            This magic saves much whe converting a bitmap from a string.
            Toast.makeText(this, "This is image uri : " + currentPhotoPath, Toast.LENGTH_SHORT).show();
            try {
                mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(currentPhotoPath));
                machineImage.setImageBitmap(mImageBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            animalImage.setImageBitmap(imageBitmap);
        }
    }

    private void addAnimal() {
        name = machineName.getText().toString().trim();
        type = machineType.getText().toString().trim();
        registration_year = machineRegYear.getText().toString().trim();
        purchase_date = machinePurchaceDate.getText().toString().trim();
        original_price = machineOriginalPrice.getText().toString().trim();
        current_price = machineCurrentPrice.getText().toString().trim();
        milage = machinemilage.getText().toString().trim();
        notes = machineNotes.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            machineName.setError("Field Required!");
        } else if (TextUtils.isEmpty(type)) {
            machineType.setError("Field Required!");
        } else if (TextUtils.isEmpty(registration_year)) {
            machineRegYear.setError("Field Required!");
        } else if (TextUtils.isEmpty(purchase_date)) {
            machinePurchaceDate.setError("Field Required!");
        } else if (TextUtils.isEmpty(original_price)) {
            machineOriginalPrice.setError("Field Required!");
        } else if (TextUtils.isEmpty(current_price)) {
            machineCurrentPrice.setError("Field Required!");
        } else if (TextUtils.isEmpty(milage)) {
            machinemilage.setError("Field Required!");
        } else if (TextUtils.isEmpty(notes)) {
            machineNotes.setError("Field Required!");
        } else {

            // This where everything is collected and stored in our local database
            User user = new User();

            machine = new Machine();

            machine.setName(name);
            machine.setType(type);
            machine.setRegistration_year(registration_year);
            machine.setPurchase_date(purchase_date);
            machine.setOriginal_price(original_price);
            machine.setCurrent_price(current_price);
            machine.setMilage(milage);
            machine.setNotes(notes);
            machine.setMachineImage(currentPhotoPath);

            machine.user.setTarget(user);
            machineBox.put(machine);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:

                this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}