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
    String globalPath = "/mnt/sdcard/";//Environment.getExternalStorageDirectory()
    String nameFolder = "MetalSlugSoundBoard/";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Save Sound :" + MainActivity.itemToSave.getNameItem())
                .setMessage("Do you want to save this sound?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            CreateDir();
                            byte[] buffer = ReadRawFile(MainActivity.itemToSave.getSoundItemRAW());
                            CopyRawFile(buffer,globalPath+nameFolder+MainActivity.itemToSave.getNameItem()+".wav");
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

    /**
     * Read a specific raw file and saved in a byte array
     * @param value Id of the raw file
     * @return a byte array with the information of the specified raw file
     * @throws IOException
     */
    private byte[] ReadRawFile(int value)throws IOException{
        Resources res = getResources();
        InputStream ins = res.openRawResource(value);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int size = 0;
        // Read the entire resource into a local byte buffer.
        byte[] buffer = new byte[1024];
        while((size=ins.read(buffer,0,1024))>=0){
            outputStream.write(buffer,0,size);
        }
        ins.close();
        buffer=outputStream.toByteArray();
        return buffer;
    }

    /**
     * Copy the byte information of a raw file and write it in the internal storage device
     * @param buffer buffer array contains the raw file byte are saved
     * @param filename name of the new file
     * @throws IOException
     */
    private void CopyRawFile(byte[] buffer, String filename) throws IOException{
        FileOutputStream fos = new FileOutputStream(filename);
        fos.write(buffer);
        fos.close();
        Toast.makeText(getContext(),"Sound Saved With Success!",Toast.LENGTH_SHORT).show();
    }

    /**
     * Create a directory where are gonna copy the raw files
     */
    private void CreateDir(){
        //String globalPath = Environment.getExternalStorageDirectory().toString();
        File file = new File(globalPath, nameFolder);
        //Toast.makeText(getContext(),file.getPath().toString(),Toast.LENGTH_SHORT).show();
        //if(dir.mkdirs() || dir.isDirectory()){ }
        if(!file.exists()){
            file.mkdirs();
        }
    }
}
