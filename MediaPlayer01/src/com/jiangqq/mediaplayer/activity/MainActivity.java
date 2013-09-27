package com.jiangqq.mediaplayer.activity;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

public class MainActivity extends Activity {

	private SeekBar skb_audio;
	private Button btn_start_audio;
	private Button btn_stop_audio;
	private SeekBar skb_video;
	private SurfaceView surfaceView;
	private Button btn_start_video;
	private Button btn_stop_video;

	private SurfaceHolder surfaceHolder;
	private MediaPlayer m = null;
	private Timer mTimer;
	private TimerTask mTimerTask;
	private boolean isChanging = false; // 互斥变量，防止定时器与SeekBar拖动时进度冲突

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// 初始化界面元素
		initView();
		initListener();
		m = new MediaPlayer();
		// 播放器播放完成事件的监听
		m.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				Toast.makeText(MainActivity.this, "结束", 1000).show();
				m.release();
			}
		});
		
		surfaceHolder=surfaceView.getHolder();
		surfaceHolder.setFixedSize(100, 100);  //设置分辨率
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);//设置surface类型
		surfaceHolder.addCallback(new Callback() {
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				
			}
			
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				m.reset();
		      
			    //skb_video.setProgress(m.getDuration());
			    m.setAudioStreamType(AudioManager.STREAM_MUSIC);
			    m.setDisplay(surfaceHolder);
                try {
					m.setDataSource("sdcard/test.3gp");
					m.prepare();
                } catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
              
			}
			
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width,
					int height) {
				
			}
		});
	}

    /**
     * 初始化界面元素	
     */
	private void initView() {
		skb_audio=(SeekBar)this.findViewById(R.id.seekbar01);
		btn_start_audio=(Button)this.findViewById(R.id.button01);
		btn_stop_audio=(Button)this.findViewById(R.id.button02);
		skb_video=(SeekBar)this.findViewById(R.id.seekbar02);
		surfaceView=(SurfaceView)this.findViewById(R.id.surfaceview01);
		btn_start_video=(Button)this.findViewById(R.id.button03);
		btn_stop_video=(Button)this.findViewById(R.id.button04);
				
	}
	
	
	/**
	 * 初始化监听器
	 */
	private void initListener()
	{
		btn_start_audio.setOnClickListener(new ClickEvent());
		btn_stop_audio.setOnClickListener(new ClickEvent());
		btn_start_video.setOnClickListener(new ClickEvent());
		btn_stop_video.setOnClickListener(new ClickEvent());	
		
		skb_audio.setOnSeekBarChangeListener(new SeekBarChangeEvent());
		skb_video.setOnSeekBarChangeListener(new SeekBarChangeEvent());
	}
	
	/*
	 * 点击时间监听
	 */
	class ClickEvent implements View.OnClickListener
	{

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.button01:
				m.reset();
				//m=MediaPlayer.create(MainActivity.this, R.raw.chenglong);
				 // ----------定时器记录播放进度---------//
				mTimer = new Timer();
				mTimerTask = new TimerTask() {
					@Override
					public void run() {
						if (isChanging) {
							return;
						}

						if (m.getVideoHeight() == 0) {
							skb_audio.setProgress(m.getCurrentPosition());
						} else {
							skb_video.setProgress(m.getCurrentPosition());
						}
					}
				};
				mTimer.schedule(mTimerTask, 0, 10);
				skb_audio.setMax(m.getDuration());
				m.start();
				break;

			case R.id.button02:
				m.stop();
				break;
			
			case R.id.button03:
			    
			   // ----------定时器记录播放进度---------//
				mTimer = new Timer();
				mTimerTask = new TimerTask() {
					@Override
					public void run() {
						if (isChanging) {
							return;
						}

						if (m.getVideoHeight() == 0) {
							skb_audio.setProgress(m.getCurrentPosition());
						} else {
							skb_video.setProgress(m.getCurrentPosition());
						}
					}
				};
				mTimer.schedule(mTimerTask, 0, 10);
			    m.start();
				break;
			
			case R.id.button04:
				m.stop();
				break;
			    
			}
		}
		
	}
	
	/*
	 * 进度条的监听
	 */
	class SeekBarChangeEvent implements OnSeekBarChangeListener
	{

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			isChanging=true;  
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			m.seekTo(seekBar.getProgress());
			isChanging=false;
		}		
	}
}
