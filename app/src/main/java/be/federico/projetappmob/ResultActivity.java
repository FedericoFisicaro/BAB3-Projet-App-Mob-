package be.federico.projetappmob;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ResultActivity extends AppCompatActivity {

    Button back;
    TextView ScanResult, TrashResult, Info;
    String Résultat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        final DatabaseAccess databaseAccess=DatabaseAccess.getInstance(getApplicationContext());
        databaseAccess.open();

        back=findViewById(R.id.BtnBack);
        TrashResult=findViewById(R.id.TvTrashResult);
        Info=findViewById(R.id.TvInfos);



        Intent intent=getIntent();
        Résultat=intent.getStringExtra("Detected");

        //tout ce qui suit pourrait être résumé en quelques lignes si les noms de la bdd correspondait à celle de l'algo .tflite

        if (Résultat.contains("apple_core"))
        {   TrashResult.setText("Trognon de pomme");
            Info.setText(databaseAccess.getExplication("Autres"));
        }
        if (Résultat.contains("cardboard_sheet"))
        {   TrashResult.setText("Carton");
            Info.setText(databaseAccess.getExplication("Papier-Carton"));
        }
        if (Résultat.contains("carton_milk"))
        {   TrashResult.setText("Boite de lait");
            Info.setText(databaseAccess.getExplication("PMC"));
        }
        if (Résultat.contains("drink_can"))
        {   TrashResult.setText("Canette");
            Info.setText(databaseAccess.getExplication("PMC"));
        }
        if (Résultat.contains("food_can"))
        {   TrashResult.setText("Boite de conserve");
            Info.setText(databaseAccess.getExplication("PMC"));
        }
        if (Résultat.contains("glass_bottle"))
        {   TrashResult.setText("Bouteille en verre");
            Info.setText("Pas d'info dans la base de données");
        }
        if (Résultat.contains("paper_sheet"))
        {   TrashResult.setText("Papier");
            Info.setText(databaseAccess.getExplication("Papier-Carton"));
        }
        if (Résultat.contains("plastic_ bottle"))
        {   TrashResult.setText("Bouteille en plastique");
            Info.setText(databaseAccess.getExplication("Autres"));
        }




        /*Si les noms de la bdd étaient les même que ceux de l'algo, on pourrait le résumer en ca :


        ArrayList<String> noms;=databaseAccess.getTypes();
        Iterator<String> it = noms.iterator();

        while (it.hasNext()) {
            String s = it.next();
                if(Résultat.contains(s)){
                    TrashResult.setText(s);
                    Info.setText(databaseAccess.getExplication(s))
                }
        }
       */





        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseAccess.close();
                ResultActivity.this.finish();
            }
        });






    }
}
