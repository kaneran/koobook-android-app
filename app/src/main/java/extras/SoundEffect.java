package extras;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.koobookandroidapp.R.*;

public class SoundEffect {


    //This method works by using the Media Player to play the barcode sound effect
    public void play(Context context){
        final MediaPlayer player = MediaPlayer.create(context, raw.barcode_sound_effect);
        player.start();
    }

}
