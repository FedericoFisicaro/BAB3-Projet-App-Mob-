package be.federico.projetappmob;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class InfoActivity extends AppCompatActivity {


    String nomBdd = "Bdd_projet_applimob.db",nomtable="Dechets";
    SQLiteDatabase db;
    ListView lv;
    ArrayList<String> noms;
    ArrayList<String> test=new ArrayList<String>();
    String ok;
    StringBuilder sb = new StringBuilder();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        lv=findViewById(R.id.lvPapier);

        final DatabaseAccess databaseAccess=DatabaseAccess.getInstance(getApplicationContext());
        databaseAccess.open();

        noms=databaseAccess.getTypes();





        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,noms);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView txt=(TextView) view;
                Intent intent=new Intent(InfoActivity.this,Info2Activity.class);
                intent.putExtra("ID",String.valueOf(position+1));
                startActivity(intent);
            }
        });



        databaseAccess.close();
    }
}
