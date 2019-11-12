package com.chelosky.metalslugsoundboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
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
    boolean oldVersionFlag;
    LinearLayout progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        oldVersionFlag = true;
        progressBar = (LinearLayout)findViewById(R.id.progress_circular);
        imageLogo = (ImageView)findViewById(R.id.logoApp);
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC,1);
        recyclerView = (RecyclerView)findViewById(R.id.recycler);
        listaModels = new ArrayList<>();
        SetOldSounds();
        recyclerView.setHasFixedSize(true);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        BindAdapterRecycler();
    }

    private void SetNewSounds(){
        listaModels.add(new ItemModel(R.drawable.g1_hgun,"H GUN",BindSoundItem(R.raw.xx_hgun)));
        listaModels.add(new ItemModel(R.drawable.g2_fgun,"F GUN",BindSoundItem(R.raw.xx_fgun)));
        listaModels.add(new ItemModel(R.drawable.g3_rgun,"R GUN",BindSoundItem(R.raw.xx_rgun)));
        listaModels.add(new ItemModel(R.drawable.g4_sgun,"S GUN",BindSoundItem(R.raw.xx_sgun)));
        listaModels.add(new ItemModel(R.drawable.g5_lgun,"L GUN",BindSoundItem(R.raw.xx_lgun)));
        listaModels.add(new ItemModel(R.drawable.g6_cgun,"C GUN",BindSoundItem(R.raw.xx_cgun)));
        listaModels.add(new ItemModel(R.drawable.g7_dgun,"D GUN",BindSoundItem(R.raw.xx_dgun)));
        listaModels.add(new ItemModel(R.drawable.g8_ggun,"G GUN",BindSoundItem(R.raw.xx_ggun)));
        listaModels.add(new ItemModel(R.drawable.g9_igun,"I GUN",BindSoundItem(R.raw.xx_igun)));
        listaModels.add(new ItemModel(R.drawable.g10_2hgun,"2H GUN",BindSoundItem(R.raw.xx_2hgun)));
        listaModels.add(new ItemModel(R.drawable.g11_ap,"AP",BindSoundItem(R.raw.xx_ap)));
        listaModels.add(new ItemModel(R.drawable.g12_fire,"FIRE",BindSoundItem(R.raw.xx_firebomb)));
        listaModels.add(new ItemModel(R.drawable.g13_grenade,"BOMB",BindSoundItem(R.raw.xx_bomb)));
        //SPECIAL
        listaModels.add(new ItemModel(R.drawable.zgun,"CLOUD",BindSoundItem(R.raw.xx_zgun)));
        listaModels.add(new ItemModel(R.drawable.tgun,"MOBS",BindSoundItem(R.raw.xx_tgun)));
        listaModels.add(new ItemModel(R.drawable.xx_okey,"OKEY",BindSoundItem(R.raw.xx_okey)));
    }

    private void SetOldSounds(){
        listaModels.add(new ItemModel(R.drawable.g1_hgun,"H GUN",BindSoundItem(R.raw.x_hgun)));
        listaModels.add(new ItemModel(R.drawable.g2_fgun,"F GUN",BindSoundItem(R.raw.x_fgun)));
        listaModels.add(new ItemModel(R.drawable.g3_rgun,"R GUN",BindSoundItem(R.raw.x_rgun)));
        listaModels.add(new ItemModel(R.drawable.g4_sgun,"S GUN",BindSoundItem(R.raw.x_sgun)));
        listaModels.add(new ItemModel(R.drawable.g5_lgun,"L GUN",BindSoundItem(R.raw.x_lgun)));
        listaModels.add(new ItemModel(R.drawable.g6_cgun,"C GUN",BindSoundItem(R.raw.x_cgun)));
        listaModels.add(new ItemModel(R.drawable.g7_dgun,"D GUN",BindSoundItem(R.raw.x_dgun)));
        listaModels.add(new ItemModel(R.drawable.g8_ggun,"G GUN",BindSoundItem(R.raw.x_ggun)));
        listaModels.add(new ItemModel(R.drawable.g9_igun,"I GUN",BindSoundItem(R.raw.x_igun)));
        listaModels.add(new ItemModel(R.drawable.g10_2hgun,"2H GUN",BindSoundItem(R.raw.x_2hgunfinal)));
        listaModels.add(new ItemModel(R.drawable.g11_ap,"AP",BindSoundItem(R.raw.x_ap)));
        listaModels.add(new ItemModel(R.drawable.g12_fire,"FIRE",BindSoundItem(R.raw.x_firebomb)));
        listaModels.add(new ItemModel(R.drawable.g13_grenade,"BOMB",BindSoundItem(R.raw.x_bomb)));
        //SPECIAL
        listaModels.add(new ItemModel(R.drawable.cloud,"CLOUD",BindSoundItem(R.raw.x_cloud)));
        listaModels.add(new ItemModel(R.drawable.mobs,"MOBS",BindSoundItem(R.raw.x_mobile)));
        listaModels.add(new ItemModel(R.drawable.stone,"STONE",BindSoundItem(R.raw.x_stone)));
        listaModels.add(new ItemModel(R.drawable.x_okey,"OKEY",BindSoundItem(R.raw.x_okey)));
    }

    private int BindSoundItem(int value){
        return soundPool.load(this,value,1);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_old://x
                if (checked)
                    if(!oldVersionFlag){
                        imageLogo.setImageResource(R.drawable.logo1);
                        UpdateActivityApp();
                    }
                    break;
            case R.id.radio_new: //xx
                if (checked)
                    if(oldVersionFlag){
                        imageLogo.setImageResource(R.drawable.titlogo);
                        UpdateActivityApp();
                    }
                    break;
        }
    }

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

    private void BindAdapterRecycler(){
        adapter = new ItemRecyclerView(listaModels);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView.setAdapter(adapter);
                ((ItemRecyclerView) adapter).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ItemModel item = listaModels.get(recyclerView.getChildAdapterPosition(v));
                        //Toast.makeText(MainActivity.this,item.getNameItem(),Toast.LENGTH_SHORT).show();
                        soundPool.play(item.getSoundItem(),1,1,1,0,0);
                    }
                });
            }
        });
    }

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
}
