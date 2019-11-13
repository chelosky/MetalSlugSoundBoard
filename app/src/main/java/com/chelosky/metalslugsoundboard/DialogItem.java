package com.chelosky.metalslugsoundboard;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.app.ActivityCompat;

import com.chelosky.metalslugsoundboard.Models.ItemModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class DialogItem extends AppCompatDialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Save Sound")
                .setMessage("Do you want to save this sound?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            //String globalPath = Environment.getExternalStorageDirectory().toString();
                            String globalPath = "/mnt/sdcard/";
                            String finalGlobalPath = "/mnt/sdcard/MetalSlugSoundBoard/";
                            File file = new File(globalPath, "MetalSlugSoundBoard");
                            //Toast.makeText(getContext(),file.getPath().toString(),Toast.LENGTH_SHORT).show();
                            if(!file.exists()){
                                file.mkdirs();
                            }
                            Resources res = getResources();
                            InputStream ins = res.openRawResource(MainActivity.itemToSave.getSoundItemRAW());
                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                            int size = 0;
                            // Read the entire resource into a local byte buffer.
                            byte[] buffer = new byte[1024];
                            while((size=ins.read(buffer,0,1024))>=0){
                                outputStream.write(buffer,0,size);
                            }
                            ins.close();
                            buffer=outputStream.toByteArray();
                            FileOutputStream fos = new FileOutputStream(finalGlobalPath+MainActivity.itemToSave.getNameItem()+".wav");
                            fos.write(buffer);
                            fos.close();
                            Toast.makeText(getContext(),"Sound Saved With Success!",Toast.LENGTH_SHORT).show();
                            //if(dir.mkdirs() || dir.isDirectory()){ }
                        }catch (Exception e){
                            Toast.makeText(getContext(),"An Error Occurred!",Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //NOTHING
                    }
                });
        return builder.create();
    }
}
