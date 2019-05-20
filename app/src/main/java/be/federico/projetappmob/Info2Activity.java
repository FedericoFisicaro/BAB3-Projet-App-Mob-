package be.federico.projetappmob;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Info2Activity extends AppCompatActivity {

    TextView tvExplications,tvTypeDechets;
    Button back;
    ImageView photo;
    int id;
    Bitmap b;
    byte[]c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info2);

        tvExplications=findViewById(R.id.tvExplications);
        tvTypeDechets=findViewById(R.id.tvTypeDechet);
        back=findViewById(R.id.bRetour);
        photo=findViewById(R.id.iv4);

        final DatabaseAccess databaseAccess=DatabaseAccess.getInstance(getApplicationContext());
        databaseAccess.open();

        Intent intent=getIntent();
        id=Integer.valueOf(intent.getStringExtra("ID"));
        tvExplications.setText(databaseAccess.getExplication(id));
        tvTypeDechets.setText(databaseAccess.getType(id));


        c=databaseAccess.getPhoto(id);
        Bitmap bitmap = BitmapFactory.decodeByteArray(c, 0, c.length);


        photo.setImageBitmap(bitmap);
        
        //else  Toast.makeText(getApplicationContext(),"Erreur",Toast.LENGTH_LONG).show();

        databaseAccess.close();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Info2Activity.this.finish();
            }
        });
    }
}
