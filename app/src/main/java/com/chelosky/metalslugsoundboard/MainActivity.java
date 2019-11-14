package com.chelosky.metalslugsoundboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.chelosky.metalslugsoundboard.Adapters.ItemRecyclerView;
import com.chelosky.metalslugsoundboard.Models.ItemModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<ItemModel> listaModels;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    SoundPool soundPool;
    ImageView imageLogo;
    LinearLayout progressBar;


    boolean oldVersionFlag;//FLAG USED TO KNOW WHAT SOUNDS HAVE TO SHOW IN THE APP -> OLD SOUNDS OR NEW SOUNDS
    private boolean runnableExecuted = false; // FLAG USED TO KNOW IF THE USER ARE HOLDING A BUTTON TO MAKE APPEAR THE DIALOG

    private final Handler mainHandler = new Handler();

    public static ItemModel itemToSave;
    /**
     * RUNNABLE PARA CREAR UN DIALOG PARA DESCARGAR EL AUDIO
     */
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            runnableExecuted = true;
            DialogItem dialogItem = new DialogItem();
            dialogItem.show(getSupportFragmentManager(),"Save Item Sound");
            //Toast.makeText(MainActivity.this,"MANTENIDO PRESIONADO POR 500MS",Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * COPY-PASTE > just request permission for writ in the external storage of the phone
     * @return
     */
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                ///Log.v(TAG,"Permission is granted");
                return true;
            } else {
                //Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            //Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isStoragePermissionGranted();
        DefineInitialParameters();
        SetOldSounds();
        SetRecyclerView();
        BindAdapterRecycler();
    }

    /**
     * Set the initials variables of the app
     */
    private void DefineInitialParameters(){
        getSupportActionBar().hide();
        oldVersionFlag = true;
        listaModels = new ArrayList<>();
        progressBar = (LinearLayout)findViewById(R.id.progress_circular);
        imageLogo = (ImageView)findViewById(R.id.logoApp);
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC,1);
    }

    /**
     * Set the logic of the recyclerview
     */
    private void SetRecyclerView(){
        recyclerView = (RecyclerView)findViewById(R.id.recycler);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        //Toast.makeText(MainActivity.this,"The RecyclerView is not scrolling",Toast.LENGTH_SHORT).show();
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        runnableExecuted=false;
                        mainHandler.removeCallbacks(runnable);
                        //Toast.makeText(MainActivity.this,"Scrolling now",Toast.LENGTH_SHORT).show();
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        runnableExecuted=false;
                        mainHandler.removeCallbacks(runnable);
                        //Toast.makeText(MainActivity.this,"Scroll Settling",Toast.LENGTH_SHORT).show();
                        break;

                }

            }
        });
        recyclerView.setHasFixedSize(true);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
    }

    /**
     * Change the soundpool between old and new
     * @param view
     */
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_old://for metal slug x
                if (checked)
                    if(!oldVersionFlag){
                        imageLogo.setImageResource(R.drawable.logo1);
                        UpdateActivityApp();
                    }
                    break;
            case R.id.radio_new: //for metal slug x
                if (checked)
                    if(oldVersionFlag){
                        imageLogo.setImageResource(R.drawable.titlogo);
                        UpdateActivityApp();
                    }
                    break;
        }
    }

    /**
     * Update de view app. Change between progressbar and recyclerview
     */
    private void UpdateActivityApp(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                findViewById(R.id.radio_new).setEnabled(false);
                findViewById(R.id.radio_old).setEnabled(false);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                UpdateRecycleView();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        findViewById(R.id.radio_new).setEnabled(true);
                        findViewById(R.id.radio_old).setEnabled(true);
                    }
                });
            }
        }).start();
    }

    /**
     * Update the list model of the recyclerview with the sounds old/new
     */
    private void UpdateRecycleView(){
        listaModels.clear();
        if(oldVersionFlag){
            SetNewSounds();
        }else{
            SetOldSounds();
        }
        oldVersionFlag = !oldVersionFlag;
        BindAdapterRecycler();
    }

    /**
     * Bind the list with the adapter. Then bind with the recyclerview
     */
    private void BindAdapterRecycler(){
        adapter = new ItemRecyclerView(listaModels);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView.setAdapter(adapter);
                /*((ItemRecyclerView) adapter).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ItemModel item = listaModels.get(recyclerView.getChildAdapterPosition(v));
                        //Toast.makeText(MainActivity.this,"ONCLICK",Toast.LENGTH_SHORT).show();
                        soundPool.play(item.getSoundItem(),1,1,1,0,0);
                    }
                });*/
                ((ItemRecyclerView)adapter).setOnTouchListener(new View.OnTouchListener(){
                    @Override
                    public boolean onTouch(View v, MotionEvent event){
                        ItemModel item = listaModels.get(recyclerView.getChildAdapterPosition(v));
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            // start timer
                            runnableExecuted=false;
                            MainActivity.itemToSave = item;
                            mainHandler.postDelayed(runnable,600);
                            //Toast.makeText(MainActivity.this,"TOUCH LISTENER: " + item.getNameItem(),Toast.LENGTH_SHORT).show();
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            // stop timer.
                            if(!runnableExecuted){
                                mainHandler.removeCallbacks(runnable);
                                soundPool.play(item.getSoundItem(),1,1,1,0,0);
                            }
                        }
                        return false;
                    }
                });
            }
        });
    }

    /**
     * SET NEW SOUNDS -> METAL SLUG XX
     */
    private void SetNewSounds(){
        listaModels.add(new ItemModel(R.drawable.g1_hgun,"XX_H_GUN",BindSoundItem(R.raw.xx_hgun),R.raw.xx_hgun));
        listaModels.add(new ItemModel(R.drawable.g2_fgun,"XX_F_GUN",BindSoundItem(R.raw.xx_fgun),R.raw.xx_fgun));
        listaModels.add(new ItemModel(R.drawable.g3_rgun,"XX_R_GUN",BindSoundItem(R.raw.xx_rgun),R.raw.xx_rgun));
        listaModels.add(new ItemModel(R.drawable.g4_sgun,"XX_S_GUN",BindSoundItem(R.raw.xx_sgun),R.raw.xx_sgun));
        listaModels.add(new ItemModel(R.drawable.g5_lgun,"XX_L_GUN",BindSoundItem(R.raw.xx_lgun),R.raw.xx_lgun));
        listaModels.add(new ItemModel(R.drawable.g6_cgun,"XX_C_GUN",BindSoundItem(R.raw.xx_cgun),R.raw.xx_cgun));
        listaModels.add(new ItemModel(R.drawable.g7_dgun,"XX_D_GUN",BindSoundItem(R.raw.xx_dgun),R.raw.xx_dgun));
        listaModels.add(new ItemModel(R.drawable.g8_ggun,"XX_G_GUN",BindSoundItem(R.raw.xx_ggun),R.raw.xx_ggun));
        listaModels.add(new ItemModel(R.drawable.g9_igun,"XX_I_GUN",BindSoundItem(R.raw.xx_igun),R.raw.xx_igun));
        listaModels.add(new ItemModel(R.drawable.g10_2hgun,"XX_2H_GUN",BindSoundItem(R.raw.xx_2hgun),R.raw.xx_2hgun));
        listaModels.add(new ItemModel(R.drawable.g11_ap,"XX_AP",BindSoundItem(R.raw.xx_ap),R.raw.xx_ap));
        listaModels.add(new ItemModel(R.drawable.g12_fire,"XX_FIRE",BindSoundItem(R.raw.xx_firebomb),R.raw.xx_firebomb));
        listaModels.add(new ItemModel(R.drawable.g13_grenade,"XX_BOMB",BindSoundItem(R.raw.xx_bomb),R.raw.xx_bomb));
        //SPECIAL
        listaModels.add(new ItemModel(R.drawable.zgun,"XX_Z_GUN",BindSoundItem(R.raw.xx_zgun),R.raw.xx_zgun));
        listaModels.add(new ItemModel(R.drawable.tgun,"XX_T_GUN",BindSoundItem(R.raw.xx_tgun),R.raw.xx_tgun));
        listaModels.add(new ItemModel(R.drawable.xx_okey,"XX_OKEY",BindSoundItem(R.raw.xx_okey),R.raw.xx_okey));
    }

    /**
     * SET THE OLD SOUNDS -> METAL SLUG 1/X
     */
    private void SetOldSounds(){
        listaModels.add(new ItemModel(R.drawable.g1_hgun,"X_H_GUN",BindSoundItem(R.raw.x_hgun),R.raw.x_hgun));
        listaModels.add(new ItemModel(R.drawable.g2_fgun,"X_F_GUN",BindSoundItem(R.raw.x_fgun),R.raw.x_fgun));
        listaModels.add(new ItemModel(R.drawable.g3_rgun,"X_R_GUN",BindSoundItem(R.raw.x_rgun),R.raw.x_rgun));
        listaModels.add(new ItemModel(R.drawable.g4_sgun,"X_S_GUN",BindSoundItem(R.raw.x_sgun),R.raw.x_sgun));
        listaModels.add(new ItemModel(R.drawable.g5_lgun,"X_L_GUN",BindSoundItem(R.raw.x_lgun),R.raw.x_lgun));
        listaModels.add(new ItemModel(R.drawable.g6_cgun,"X_C_GUN",BindSoundItem(R.raw.x_cgun),R.raw.x_cgun));
        listaModels.add(new ItemModel(R.drawable.g7_dgun,"X_D_GUN",BindSoundItem(R.raw.x_dgun),R.raw.x_dgun));
        listaModels.add(new ItemModel(R.drawable.g8_ggun,"X_G_GUN",BindSoundItem(R.raw.x_ggun),R.raw.x_ggun));
        listaModels.add(new ItemModel(R.drawable.g9_igun,"X_I_GUN",BindSoundItem(R.raw.x_igun),R.raw.x_igun));
        listaModels.add(new ItemModel(R.drawable.g10_2hgun,"X_2H_GUN",BindSoundItem(R.raw.x_2hgunfinal),R.raw.x_2hgun));
        listaModels.add(new ItemModel(R.drawable.g11_ap,"X_AP",BindSoundItem(R.raw.x_ap),R.raw.x_ap));
        listaModels.add(new ItemModel(R.drawable.g12_fire,"X_FIRE",BindSoundItem(R.raw.x_firebomb),R.raw.x_firebomb));
        listaModels.add(new ItemModel(R.drawable.g13_grenade,"X_BOMB",BindSoundItem(R.raw.x_bomb),R.raw.x_bomb));
        //SPECIAL
        listaModels.add(new ItemModel(R.drawable.cloud,"X_CLOUD",BindSoundItem(R.raw.x_cloud),R.raw.x_cloud));
        listaModels.add(new ItemModel(R.drawable.mobs,"X_MOBS",BindSoundItem(R.raw.x_mobile),R.raw.x_mobile));
        listaModels.add(new ItemModel(R.drawable.stone,"X_STONE",BindSoundItem(R.raw.x_stone),R.raw.x_stone));
        listaModels.add(new ItemModel(R.drawable.x_okey,"X_OKEY",BindSoundItem(R.raw.x_okey),R.raw.x_okey));
    }

    /**
     * Bind the raw sound file with a item
     * @param value is the id of the raw sound file
     * @return the audio soundpool of the item
     */
    private int BindSoundItem(int value){
        return soundPool.load(this,value,1);
    }

}
