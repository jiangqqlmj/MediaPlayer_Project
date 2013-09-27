package com.musicplayer;


import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.SeekBar;

public class Player implements OnBufferingUpdateListener,
		OnCompletionListener, MediaPlayer.OnPreparedListener{
	public MediaPlayer mediaPlayer;
	private SeekBar skbProgress;
	private Timer mTimer=new Timer();
	public Player(SeekBar skbProgress)
	{
		this.skbProgress=skbProgress;
		
		try {
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnBufferingUpdateListener(this);
			mediaPlayer.setOnPreparedListener(this);
		} catch (Exception e) {
			Log.e("mediaPlayer", "error", e);
		}
		
		mTimer.schedule(mTimerTask, 0, 1000);
		
	    // 进行代理初始化设置
		HttpGetProxy proxy = new HttpGetProxy(9090);

		try {
			proxy.startProxy("conteadiwagner.com");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*******************************************************
	 * 通过定时器和Handler来更新进度条 
	 ******************************************************/
	TimerTask mTimerTask = new TimerTask() {
		@Override
		public void run() {
			if(mediaPlayer==null)
				return;
			if (mediaPlayer.isPlaying() && skbProgress.isPressed() == false) {
				handleProgress.sendEmptyMessage(0);
			}
		}
	};
	
	Handler handleProgress = new Handler() {
		public void handleMessage(Message msg) {

			int position = mediaPlayer.getCurrentPosition();
			int duration = mediaPlayer.getDuration();
			
			if (duration > 0) {
				long pos = skbProgress.getMax() * position / duration;
				skbProgress.setProgress((int) pos);
			}
		};
	};
	//*****************************************************
	
	public void play()
	{
		mediaPlayer.start();
	}
	
	public void playUrl(String videoUrl)
	{
		//http://conteadiwagner.com/audio/sf.mp3
		try {
			mediaPlayer.reset();
			//mediaPlayer.setDataSource("http://qzone-music.qq.com/fcg-bin/fcg_set_musiccookie.fcg?fromtag=9");
			mediaPlayer.setDataSource("http://127.0.0.1:9090/audio/sf.mp3");
			//mediaPlayer.setDataSource(videoUrl);
			mediaPlayer.prepare();
			//mediaPlayer.start();
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

	
	public void pause()
	{
		mediaPlayer.pause();
	}
	
	public void stop()
	{
		if (mediaPlayer != null) { 
			mediaPlayer.stop();
            mediaPlayer.release(); 
            mediaPlayer = null; 
        } 
	}

	@Override
	/**
	 * 通过onPrepared播放 
	 */
	public void onPrepared(MediaPlayer arg0) {
		Log.e("mediaPlayer", "onPrepared");
		arg0.start();
		
	}

	@Override
	public void onCompletion(MediaPlayer arg0) {
		Log.e("mediaPlayer", "onCompletion");
	}

	@Override
	public void onBufferingUpdate(MediaPlayer arg0, int bufferingProgress) {
		skbProgress.setSecondaryProgress(bufferingProgress);
		int currentProgress=skbProgress.getMax()*mediaPlayer.getCurrentPosition()/mediaPlayer.getDuration();
		Log.e(currentProgress+"% play", bufferingProgress + "% buffer");
	}

}
