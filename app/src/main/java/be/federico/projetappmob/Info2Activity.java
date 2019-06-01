package be.federico.projetappmob;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Info2Activity extends AppCompatActivity {

    TextView tvExplications,tvTypeDechets,tvExemple;
    Button back,add,delete;
    ImageView photo;
    String id;
    byte[]c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info2);

        tvExplications=findViewById(R.id.tvExplications);
        tvTypeDechets=findViewById(R.id.tvTypeDechet);
        tvExemple=findViewById(R.id.tvExemple);
        back=findViewById(R.id.bRetour);
        add=findViewById(R.id.bAdd);
        delete=findViewById(R.id.bDelete);
        photo=findViewById(R.id.iv4);




        Intent intent=getIntent();
        id=intent.getStringExtra("ID");
        final DatabaseAccess databaseAccess=DatabaseAccess.getInstance(getApplicationContext());
        databaseAccess.open();

        tvExemple.setText(databaseAccess.getNom(id));
        tvExplications.setText(databaseAccess.getExplication(id));
        tvTypeDechets.setText(id);
        c=databaseAccess.getPhoto(id);
        Bitmap bitmap = BitmapFactory.decodeByteArray(c, 0, c.length);
        photo.setImageBitmap(bitmap);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Info2Activity.this);
                mBuilder.setTitle("Entrez un élément à ajouter");
                final EditText input = new EditText(Info2Activity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setHint("nom");
                mBuilder.setView(input);
                mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String txt=input.getText().toString();

                        if(txt.length()!=0){
                            databaseAccess.Ajout(txt,id);
                            tvExemple.setText(databaseAccess.getNom(id));
                        }
                        else Toast.makeText(getApplicationContext(),"Entrez un nom valide d'élément à ajouter",Toast.LENGTH_LONG).show();
                    }
                });

                mBuilder.setNeutralButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Info2Activity.this);
                mBuilder.setTitle("Entrez le nom de l'élément à supprimer");
                final EditText input = new EditText(Info2Activity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setHint("nom");
                mBuilder.setView(input);
                mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String txt=input.getText().toString();

                        if(databaseAccess.Verification(txt)){
                            databaseAccess.Suppression(txt);
                            tvExemple.setText(databaseAccess.getNom(id));
                        }
                        else Toast.makeText(getApplicationContext(),"Entrez un nom valide d'élément à supprimer",Toast.LENGTH_LONG).show();
                    }
                });

                mBuilder.setNeutralButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });






        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseAccess.close();
                Info2Activity.this.finish();
            }
        });
    }
}
