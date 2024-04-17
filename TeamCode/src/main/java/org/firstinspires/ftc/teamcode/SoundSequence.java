package org.firstinspires.ftc.teamcode;

import android.os.Handler;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.ftccommon.SoundPlayer;

public class SoundSequence {
    private SoundPlayer soundPlayer;
    private int[] soundQueue;
    private int[] soundDurations; // durations in milliseconds
    private int currentSoundIndex = 0;
    private Handler handler = new Handler();

    public SoundSequence(HardwareMap hardwareMap, int[] sounds, int[] durations) {
        soundPlayer = SoundPlayer.getInstance();
        soundQueue = sounds;
        soundDurations = durations;
    }

    public void playSounds(final HardwareMap hardwareMap) {
        if (currentSoundIndex < soundQueue.length) {
            int soundID = soundQueue[currentSoundIndex];
            soundPlayer.startPlaying(hardwareMap.appContext, soundID);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        currentSoundIndex++;
                        playSounds(hardwareMap);
                    }
                }, soundDurations[currentSoundIndex]);

        }
    }
}
