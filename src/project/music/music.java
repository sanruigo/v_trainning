package project.music;


import java.io.IOException;


import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

public class music{
	MediaPlayer player;
	AssetFileDescriptor afd;
	public void mediaPlayer(Context myContext,String nameMusic,Boolean loop){
		
		try {
		// Read the music file from the asset folder
			
		afd = myContext.getAssets().openFd(nameMusic);
		// Creation of new media player;
		player = new MediaPlayer();
		// Set the player music source.
		player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),afd.getLength());
		// Set the looping and play the music.
		player.setLooping(loop);
		player.prepare();
		player.start();
		} catch (IOException e) {
		e.printStackTrace();
		}
		//## Always use try catch block two avoid error exception.
		//f an activity is pause, say out of user visibility you have call this in order to avoid collision of other sound of other activity
	
		
	}
	public void onPause() {
		//super.onPause();
		player.pause();
	}

	//To resume this player we have to call following method.
	public void onResume() {
		//super.onResume();
		player.start();
	}

	//When activity is going to stop we have to call this to release the media player
	protected void onStop() {
		//super.onStop();
		player.stop();
		player = null;
	}

}
