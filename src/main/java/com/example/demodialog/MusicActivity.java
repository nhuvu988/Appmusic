package com.example.demodialog;

import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MusicActivity extends AppCompatActivity {

    TextView txtTitle, txtTimeSong, txtTimeTotal;
    SeekBar skSong;
    ImageView imgHinh;
    ImageButton btnPrev, btnPlay, btnStop, btnNext;
    ArrayList<Song> arraySong;
    int position = 0;
    MediaPlayer mediaPlayer;
    Animation animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_main);
        AnhXa();
        AddSong();
        khoiTaoMediaPlayer();
        animation= AnimationUtils.loadAnimation(this,R.anim.disc_rotate);//xoay đĩa

        //--------------------------------------------------
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                btnPlay.setImageResource(R.drawable.play);
                khoiTaoMediaPlayer();
            }
        });
//--------------------------------------------------
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()){
                    //nếu đang phát -> pause -> đổi hình play
                    mediaPlayer.pause();
                    btnPlay.setImageResource(R.drawable.play);
                }
                else {
                    //dang ngừng -> play-> dổi hình
                    mediaPlayer.start();
                    btnPlay.setImageResource(R.drawable.pause);
                }
                SetTimeTotal();
                UpdatetimeSong();
                imgHinh.startAnimation(animation);
            }

        });
//        ----------------------------------------------
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position++;
                if(position > arraySong.size()-1){
                    position=0;
                }
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                khoiTaoMediaPlayer();
                mediaPlayer.start();
                btnPlay.setImageResource(R.drawable.pause);
                SetTimeTotal();
                UpdatetimeSong();
            }
        });
//       -----------------------------------------------
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position--;
                if(position < 0){
                    position=arraySong.size()-1;
                }
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                khoiTaoMediaPlayer();
                mediaPlayer.start();
                btnPlay.setImageResource(R.drawable.pause);
                SetTimeTotal();
                UpdatetimeSong();
            }
//            -------------------------------------------
        });
        skSong.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(skSong.getProgress());
            }
        });
    }
    private void SetTimeTotal(){
        SimpleDateFormat dinhDangGio = new SimpleDateFormat("mm:ss");
        txtTimeTotal.setText(dinhDangGio.format(mediaPlayer.getDuration()));
        //gán max của skSOng = mediaPlayer.getDuration
        skSong.setMax(mediaPlayer.getDuration());
    }
    private void UpdatetimeSong(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat dinhDangGio = new SimpleDateFormat("mm:ss");
                txtTimeSong.setText(dinhDangGio.format(mediaPlayer.getCurrentPosition()));//vị trí hiện tại đc phát curren
//                update progress skSong
                skSong.setProgress(mediaPlayer.getCurrentPosition());
//                kiểm tra time bài hát nêu két thúc thì  next
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        position++;
                        if(position > arraySong.size()-1){
                            position=0;
                        }
                        if(mediaPlayer.isPlaying()){
                            mediaPlayer.stop();
                        }
                        khoiTaoMediaPlayer();
                        mediaPlayer.start();
                        btnPlay.setImageResource(R.drawable.pause);
                        SetTimeTotal();
                        UpdatetimeSong();
                    }
                });
                handler.postDelayed(this,500);
            }
        }, 100);
    }
    private void khoiTaoMediaPlayer(){
        mediaPlayer= MediaPlayer.create(MusicActivity.this,arraySong.get(position).getFile());
        txtTitle.setText(arraySong.get(position).getTitle());
    }
    private void AnhXa(){
        txtTimeSong   = (TextView) findViewById(R.id.textViewTimeSong);
        txtTimeTotal  = (TextView) findViewById(R.id.textViewTimeTotal);
        txtTitle      = (TextView) findViewById(R.id.textviewTitle);
        btnPrev       = (ImageButton) findViewById(R.id.imageButtonPrev);
        btnPlay       = (ImageButton) findViewById(R.id.imageButtonPlay);
        btnStop       = (ImageButton) findViewById(R.id.imageButtonStop);
        btnNext       = (ImageButton) findViewById(R.id.imageButtonNext);
        skSong        = (SeekBar) findViewById(R.id.seekBarSong);
        imgHinh       = (ImageView) findViewById(R.id.imageViewDisc);
    }
    private void AddSong(){
        arraySong = new ArrayList<>();
        arraySong.add(new Song("Tomine Harket",R.raw.tomine_harket));
        arraySong.add(new Song("Energettic",R.raw.energetic));
        arraySong.add(new Song("Girl Like You",R.raw.girls_like_you));
        arraySong.add(new Song("Sakura",R.raw.sakura));
        arraySong.add(new Song("Đáp Án",R.raw.dap_an));
        arraySong.add(new Song("Anne Marie",R.raw.anne_marie));
    }
}
