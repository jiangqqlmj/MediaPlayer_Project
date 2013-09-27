package com.jiangqq.media.activity;

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
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.SeekBar;


public class Player implements OnBufferingUpdateListener ,OnCompletionListener,MediaPlayer.OnPreparedListener,SurfaceHolder.Callback{
	public MediaPlayer mediaPlayer;  
	private SeekBar mSeekBar;
	private int videoWidth;  
	private int videoHeight; 
	private SurfaceHolder surfaceHolder;
	private Timer mTimer=new Timer();  
	public Player(SeekBar pSeekBar,SurfaceView pSurfaceView )
	{
		this.mSeekBar=pSeekBar;
		this.surfaceHolder=pSurfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);  
        //  启动定时器定时器进行更新进度
			mTimer.schedule(MyTimeTask, 0, 1000);
		
	}
	
	TimerTask MyTimeTask =new TimerTask() {
		
		@Override
		public void run() {
			if(mediaPlayer==null)
				return;
			if(mediaPlayer.isPlaying()&&mSeekBar.isPressed()==false)
			{
				mHandlerProgress.sendEmptyMessage(0);
			}
		}
	};
	
	
	Handler mHandlerProgress=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			 int position = mediaPlayer.getCurrentPosition();  //获取视频当前播放的进度
	         int duration = mediaPlayer.getDuration();  //获取视频总共的进度
			 if(position>0){
				 int pos=mSeekBar.getMax()*position/duration;
				 mSeekBar.setProgress((int)pos);
			 }
		}
		
	};
	
	public void play()
	{
		 mediaPlayer.start();  
	}
	
	public void pause()  
    {  
        mediaPlayer.pause();  
    }  
	
	public void stop()
	{
		if(mediaPlayer!=null)
		{
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer=null;
		}
	}
	
	public void playUrl(String videoUrl)  {
		mediaPlayer.reset();
		try {
			mediaPlayer.setDataSource(videoUrl);
			mediaPlayer.prepare();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	/*
	 * (non-Javadoc)  缓冲更新
	 * @see android.media.MediaPlayer.OnBufferingUpdateListener#onBufferingUpdate(android.media.MediaPlayer, int)
	 */
	@Override 
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		mSeekBar.setSecondaryProgress(percent);
		int currentProgress=mSeekBar.getMax()*mediaPlayer.getCurrentPosition()/mediaPlayer.getDuration();  
        Log.d(currentProgress+"% play", percent + "% buffer");  
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		 Log.d("mediaPlayer", "onCompletion"); 
		 if(mp!=null)
		 {
			 mp.stop();
			 mp.release();
		 }
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		videoWidth = mediaPlayer.getVideoWidth();  
        videoHeight = mediaPlayer.getVideoHeight();  
        if (videoHeight != 0 && videoWidth != 0) {  
        	mp.start();  
        }  
        Log.e("mediaPlayer", "onPrepared");  
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		 try {  
	            mediaPlayer = new MediaPlayer();  
	            mediaPlayer.setDisplay(surfaceHolder);  
	            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);  
	            mediaPlayer.setOnBufferingUpdateListener(this);  
	            mediaPlayer.setOnPreparedListener(this);  
	        } catch (Exception e) {  
	            Log.e("mediaPlayer", "error", e);  
	        }  
	        Log.e("mediaPlayer", "surface created");  
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

}
