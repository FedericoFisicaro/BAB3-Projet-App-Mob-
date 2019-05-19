package be.federico.projetappmob;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
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

    public ArrayList<String> getNoms(){

        c=db.rawQuery("select id,nom from Dechets", new String[]{});
        ArrayList<String> liste=new ArrayList<String>();


        while(c.moveToNext()){
            StringBuffer buffer= new StringBuffer();
            String nom = c.getString(1);
            buffer.append(c.getInt(0)+") " +nom);
            liste.add(buffer.toString());
        }
        return liste;
    }

    public String getExplication(int ide){

        c=db.rawQuery("select id,informations from Dechets where id ='"+ide+"'", new String[]{});
        c.moveToFirst();
        String liste=c.getString(1);
        return liste;
    }

    public String getNom(int ide){

        c=db.rawQuery("select id,nom from Dechets where id ='"+ide+"'", new String[]{});
        c.moveToFirst();
        String liste=c.getString(1);
        return liste;
    }

    public String getType(int ide){

        c=db.rawQuery("select id,type from Dechets where id ='"+ide+"'", new String[]{});
        c.moveToFirst();
        String liste=c.getString(1);
        return liste;
    }

    public Bitmap getPhoto(int ide){

        c=db.rawQuery("select id,photo from Dechets where id ='"+ide+"'", new String[]{});
        c.moveToFirst();
        byte[] bitmapData = c.getBlob(1);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length);
        return bitmap;
    }

}
