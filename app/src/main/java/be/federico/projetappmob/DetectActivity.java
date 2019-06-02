package be.federico.projetappmob;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DetectActivity extends AppCompatActivity {

    Button prendrePhoto,detect;
    ImageView photo;
    Uri pictureUri;
    private String Résultat;
    private static final int PERMISSION_CODE = 1001;

    private Classifier classifier;


    private static final String MODEL_PATH = "model.tflite";
    private static final boolean QUANT = false;
    private static final String LABEL_PATH = "classe.txt";
    private static final int INPUT_SIZE = 224;

    private Executor executor = Executors.newSingleThreadExecutor();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect);
        prendrePhoto=findViewById(R.id.BtnToggleCamera);
        detect=findViewById(R.id.BtnDetect);
        photo=findViewById(R.id.ImageView);

        initTensorFlowAndLoadModel();



        prendrePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());

                File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES); //prend la reference du fichier ou on stock les ficher comme musique images, videos etc
                String pictureName =  getPictureName();
                File imageFile = new File(pictureDirectory, pictureName);
                pictureUri = Uri.fromFile(imageFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);  // 1er argument = je veux l'enregistrer 2eme= où l'enregistrer (soit public pour que qutres appli peuvent l'utiliser )
                startActivityForResult(intent,0);
            }
        });

        detect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(DetectActivity.this,ResultActivity.class);
                intent.putExtra("Detected",Résultat);
                startActivity(intent);
            }
        });
    }


    private String getPictureName(){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = sdf.format(new Date());
        return "SmartBin" + timestamp + ".jpg";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {

            //permission not granted => request it
            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
            //show popup for runtime permission
            requestPermissions(permissions, PERMISSION_CODE);

        } else{
            //permission already granted

            affImage();
        }

    }


    //handle result of runtime permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE: {
                if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //permission was granted

                    affImage();

                } else {
                    //permission was denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void affImage(){

        InputStream inputStream;

        try {
            inputStream = getContentResolver().openInputStream(pictureUri);
            Bitmap image = BitmapFactory.decodeStream(inputStream);

            Bitmap imagerotate = imageOreintationValidator(image,pictureUri.getPath());

            photo.setImageBitmap(imagerotate);

            image = Bitmap.createScaledBitmap(imagerotate, INPUT_SIZE, INPUT_SIZE, false);

            final List<Classifier.Recognition> results = classifier.recognizeImage(image);

            Résultat=results.toString();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "Impossible ouvrir l'image", Toast.LENGTH_LONG).show();
        }
        //Bitmap bitmap=(Bitmap)data.getExtras().get("data");
        //photo.setImageBitmap(bitmap);

    }


    private Bitmap imageOreintationValidator(Bitmap bitmap, String path) {

        ExifInterface ei;
        try {
            ei = new ExifInterface(path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    bitmap = rotateImage(bitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    bitmap = rotateImage(bitmap, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    bitmap = rotateImage(bitmap, 270);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private Bitmap rotateImage(Bitmap source, float angle) {

        Bitmap bitmap = null;
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                    matrix, true);
        } catch (OutOfMemoryError err) {
            err.printStackTrace();
        }
        return bitmap;
    }

    private void initTensorFlowAndLoadModel() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    classifier = TensorFlowImageClassifier.create(getAssets(), MODEL_PATH, LABEL_PATH, INPUT_SIZE, QUANT);
                } catch (final Exception e) {
                    throw new RuntimeException("Error initializing TensorFlow!", e);
                }
            }
        });
    }
}