package sound;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.koobookandroidapp.R.*;

public class SoundEffect {


    public void play(Context context){
        final MediaPlayer player = MediaPlayer.create(context, raw.barcode_sound_effect);
        player.start();
    }

}
