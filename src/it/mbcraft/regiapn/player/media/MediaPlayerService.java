package it.mbcraft.regiapn.player.media;

import it.mbcraft.libraries.command.ICommand;
import it.mbcraft.libraries.process.GetCurrentUserCommand;
import it.mbcraft.libraries.utils.OSChecker;
import it.mbcraft.regiapn.player.command.audio.SetAnalogAudioModeCommand;
import it.mbcraft.regiapn.player.command.audio.SetHDMIAudioModeCommand;
import it.mbcraft.regiapn.player.command.audio.SetVolumeCommand;
import it.mbcraft.regiapn.player.command.media.DynamicPlayMediaElementCommand;
import it.mbcraft.regiapn.player.command.notifications.NotifyMediaPlayerStartedCommand;
import it.mbcraft.regiapn.player.command.notifications.NotifyMediaPlayerStoppedCommand;
import it.mbcraft.regiapn.player.data.dao.PlayerConfigurationDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * This code is property of MBCRAFT di Marco Bagnaresi. All rights reserved.
 * <p>
 * Created by marco on 09/07/16.
 */
public class MediaPlayerService implements Runnable {

    private static MediaPlayerService instance = null;

    private Logger logger = LogManager.getLogger(MediaPlayerService.class);


    public static MediaPlayerService getInstance() {
        if (instance == null) instance = new MediaPlayerService();
        return instance;
    }

    private PlayState myState = null;
    private Thread myThread = null;

    private MediaPlayerService() {
    }

    public void start(PlayState state) {

        OSChecker.ensureNotWindows();

        myState = state;

        try {
            myState.prepareBeforeStart();
        } catch (InterruptedException e) {
            logger.warn("Interrupted in play prepare phase ...",e);
        }
        logger.info("Starting Media Player Service Thread ...");
        myThread = new Thread(this);
        myThread.setName("Media Player Service Thread");
        myThread.setDaemon(true);
        myThread.start();
    }

    public void stop() {

        if (currentProcess!=null) {
            currentProcess.destroy();
        }

        if (myThread!=null) {
            myThread.interrupt();
            try {
                myThread.join();
            } catch (InterruptedException ex) {
                logger.error("Interrupted exception on thread join",ex);
            } finally {
                myThread = null;
                myState = null;
            }

        }
    }

    private void audioSetup() {
        GetCurrentUserCommand currentUser = new GetCurrentUserCommand();
        currentUser.execute();

        if (currentUser.getUser().equals("pi") && PlayerConfigurationDAO.hasAudioOutputMode()) {
            String mode = PlayerConfigurationDAO.getAudioOutputMode();
            ICommand modeCmd = null;
            if (mode.toLowerCase().equals("analog"))
                modeCmd = new SetAnalogAudioModeCommand();
            if (mode.toLowerCase().equals("hdmi"))
                modeCmd = new SetHDMIAudioModeCommand();
            if (modeCmd != null)
                modeCmd.execute();
            else
                logger.warn("Unrecognized audio mode : " + mode);

            SetVolumeCommand vol = new SetVolumeCommand();
            vol.execute();
        }
    }

    @Override
    public void run() {

        audioSetup();

        NotifyMediaPlayerStartedCommand cmd_notify_started = new NotifyMediaPlayerStartedCommand();
        cmd_notify_started.execute();

        try {

            while (myState.isPlaying() && myState.hasMorePlayableTracks()) {
                File track = myState.getNextPlayableTrack();

                try {
                    logger.info("Playing media : "+track.getAbsolutePath());
                    playTrack(track);
                } catch (IOException e) {
                    logger.error("Error playing track  : " + e.getMessage());
                } finally {
                    myState.trackPlayed();
                }
            }
        } catch (InterruptedException ex) {
            logger.warn("Play thread was stopped.");
        } finally {
            myState.stopPlaying();
            MediaPrepareService.getInstance().stop();
            myState.deleteAllTracks();

            //notify media player stopped
            NotifyMediaPlayerStoppedCommand cmd_notify_stopped = new NotifyMediaPlayerStoppedCommand();
            cmd_notify_stopped.execute();
        }
    }

    private Process currentProcess = null;

    private void playTrack(File toPlay) throws IOException, InterruptedException {

        if (toPlay!=null && toPlay.exists() && toPlay.length()>0) {
            DynamicPlayMediaElementCommand dyn_media_play = new DynamicPlayMediaElementCommand(toPlay);
            dyn_media_play.execute();

            ProcessBuilder pb = dyn_media_play.getConfiguredProcessBuilder();

            if (currentProcess != null) throw new IllegalStateException("A process is already active!!");

            currentProcess = pb.start();
            BufferedReader rd = new BufferedReader(new InputStreamReader(currentProcess.getErrorStream()));

            try {
                String line;
                while ((line = rd.readLine()) != null) {
                    logger.debug("Command output : " + line);
                }
            } catch (Exception ex) {
                //
            } finally {
                currentProcess.destroy();
                currentProcess.waitFor();
                currentProcess = null;
            }
        }
        else
            logger.warn("File to play "+toPlay.getPath()+" does not exists or is empty! Skipping ...");

    }
}
