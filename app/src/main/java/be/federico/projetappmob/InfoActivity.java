package be.federico.projetappmob;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class InfoActivity extends AppCompatActivity {

    ListView lv;
    ArrayList<String> noms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        lv=findViewById(R.id.listView);

        noms=new ArrayList<String>();
        noms.add("papier");
        noms.add("carton");
        noms.add("PMC");

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.activity_info,noms);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView txt=(TextView)view;
                //System.out
            }
        });

    }
}
