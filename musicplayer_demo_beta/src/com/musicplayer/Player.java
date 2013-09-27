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
	private HttpGetProxy proxy;
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
		
		proxy = new HttpGetProxy(9090);

		try {
			proxy.startProxy();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
		
	public void play()
	{
		mediaPlayer.start();
	}
	
	public void playUrl(String mediaUrl)
	{
		try {
			String url=proxy.getLocalURL(mediaUrl); //获取转换成功的本地地址
			mediaPlayer.reset();
			mediaPlayer.setDataSource(url); //设置请求数据源地址
			mediaPlayer.prepare();          //这边进行去网络上面请求数据，准备播放器
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
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
	}

}
