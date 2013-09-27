package com.jiangqq.media.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;

/**
 * 播放网络音频
 * @author jiangqingqing
 * @time 2013/09/27 11:45
 */
public class MainActivity extends Activity {
	 private Button btnPause, btnPlayUrl, btnStop;  
	 private SeekBar skbProgress;  
	 private Player player;  
	 private SurfaceView surfaceView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);  
		surfaceView=(SurfaceView)this.findViewById(R.id.surfaceView1);
		btnPlayUrl=(Button)this.findViewById(R.id.btnUrl);
		btnPause=(Button)this.findViewById(R.id.btnPuase);
		btnStop=(Button)this.findViewById(R.id.btnStop);
		btnPlayUrl.setOnClickListener(new ClickEvent());
		btnPause.setOnClickListener(new ClickEvent());
		btnStop.setOnClickListener(new ClickEvent());
		skbProgress=(SeekBar)this.findViewById(R.id.skbProgress);
		skbProgress.setOnSeekBarChangeListener(new SeekBarChangeEvent());
		player = new Player(skbProgress,surfaceView);  
		
		
	}
  
	 class ClickEvent implements OnClickListener{

		@Override
		public void onClick(View v) {
            switch (v.getId()) {
			case R.id.btnUrl:
				String strUrl="http://10.1.35.27:8080/UpdateAppServer/test.3gp";
				player.playUrl(strUrl);
				break;
			case R.id.btnPuase:
				player.pause();
				break;
			case R.id.btnStop:
                player.stop();				
				break;
			}			
		}
		 
	 }
	 
	 class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener{
		int progress;  
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			 // 原本是(progress/seekBar.getMax())*player.mediaPlayer.getDuration()  
            this.progress = progress * player.mediaPlayer.getDuration()  
                    / seekBar.getMax();  
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			 player.mediaPlayer.seekTo(progress);  
		}
		 
	 }

}
