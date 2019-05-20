package be.federico.projetappmob;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.sql.Blob;
import java.util.ArrayList;

public class DatabaseAccess {

    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DatabaseAccess instance;
    Cursor c= null,d=null;

    private DatabaseAccess(Context context){
        this.openHelper=new DataBaseOpenHelper(context);
    }

    public static DatabaseAccess getInstance(Context context){
        if(instance==null){
            instance= new DatabaseAccess(context);
        }
        return instance;
    }

    public void open(){
        this.db=openHelper.getWritableDatabase();
    }

    public void close(){
        if(db!=null){
            this.db.close();
        }
    }

    public String getNoms(){

        c=db.rawQuery("select id,nom from Dechets", new String[]{});
        StringBuffer buffer= new StringBuffer();
        buffer.append("Exemples : ");
        while(c.moveToNext()){

            String nom = c.getString(1);
            buffer.append(nom+", ");
        }
        return buffer.toString();
    }

    public ArrayList<String> getTypes(){

        c=db.rawQuery("select type from Poubelle", new String[]{});
        ArrayList<String> liste=new ArrayList<String>();
        while(c.moveToNext()){
            String nom = c.getString(0);
            liste.add(nom);
        }
        return liste;
    }




    public String getExplication(String Type){

        c=db.rawQuery("select type,information from Poubelle where type ='"+Type+"'", new String[]{});
        c.moveToFirst();
        return c.getString(1);
    }

    public String getNom(String id){

        c=db.rawQuery("select nom from Dechets where type ='"+id+"'", new String[]{});
        StringBuffer buffer= new StringBuffer();
        buffer.append("Exemples : ");
        while(c.moveToNext()){

            String nom = c.getString(0);
            buffer.append(nom+", ");
        }
        buffer.append("...");
        return buffer.toString();
    }

    public String getType(String Type){

        c=db.rawQuery("select type from Poubelle where type ='"+Type+"'", new String[]{});
        c.moveToFirst();
        String liste=c.getString(1);
        return liste;
    }

    public byte[] getPhoto(String Type){

        c=db.rawQuery("select type,photo from Poubelle where type ='"+Type+"'", new String[]{});
        c.moveToFirst();
        return c.getBlob(1);
    }

    public void Ajout(String Nom, String Type){

        db.execSQL("INSERT INTO Dechets (nom, type) "+ "VALUES ('"+Nom+"','"+Type+"')", new String[]{});

    }

    public void Suppression(String Nom){

        db.execSQL("delete from Dechets where nom ='"+Nom+"'", new String[]{} );
    }

    public void Modif(String set, String element, String Nom){
        db.execSQL("update Dechets set "+set+" = '"+element+"' where nom = '"+Nom+"' ",new  String []{});;
    }

    public boolean Verification(String nom){
        if(db.rawQuery("select nom from Dechets where nom ='"+nom+"'", new String[]{})!=null)return true;
        else return false;
    }

}
