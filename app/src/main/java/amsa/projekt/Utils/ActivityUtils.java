package amsa.projekt.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import amsa.projekt.MainActivity;

/**
 * Created by Łukasz on 2016-02-07.
 */
public class ActivityUtils {

    static public void alertDialog(Context context, Exception e){
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);
        dlgAlert.setMessage(e.getMessage());
        dlgAlert.setTitle("Błąd");
        dlgAlert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //dismiss the dialog
                    }
                });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    static public void confirmDialog(Context context, String title, String message, DialogInterface.OnClickListener action){
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Tak",action)
                .setNegativeButton("Nie",null)
                .create().show();
    }

    static public String milisToDate(long timeInMilis){
        Date date = new Date(timeInMilis);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

}
