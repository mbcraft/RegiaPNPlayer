package it.mbcraft.regiapn.player.media;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This code is property of MBCRAFT di Marco Bagnaresi. All rights reserved.
 * <p>
 * Created by marco on 09/07/16.
 */
public class MediaPrepareService implements Runnable {

    private static MediaPrepareService instance = null;

    private Logger logger = LogManager.getLogger(MediaPrepareService.class);

    public static MediaPrepareService getInstance() {
        if (instance == null) instance = new MediaPrepareService();
        return instance;
    }

    private PlayState myState = null;
    private Thread myThread = null;

    private MediaPrepareService() {
    }

    public void start(PlayState state) {
        myState = state;
        Thread th = new Thread(this);
        th.setName("Music Prepare Track Service Thread");
        th.start();
    }

    public void stop() {
        if (myThread!=null && myThread.isAlive()) {
            myThread.interrupt();
            try {
                myThread.join();
            } catch (InterruptedException e) {
                logger.error("InterruptedException on thread join.");
            } finally {
                myThread = null;
            }
        }
    }

    @Override
    public void run() {
        try {
            while (myState.isPlaying() && myState.hasTrackToPrepare()) {
                myState.prepareNewPlayableTrack();
                myState.deleteOldTracks();
            }
        } catch (InterruptedException e) {
            logger.info("Track prepare thread stopped.");
        } finally {
            myState = null;
        }

    }


}
