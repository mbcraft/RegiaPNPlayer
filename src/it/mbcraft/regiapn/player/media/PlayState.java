package it.mbcraft.regiapn.player.media;

import it.mbcraft.libraries.encryption.sca0.SCA0Decrypter;
import it.mbcraft.regiapn.player.data.models.Playlist;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.concurrent.Semaphore;

/**
 * This code is property of MBCRAFT di Marco Bagnaresi. All rights reserved.
 * <p>
 * Created by marco on 09/07/16.
 */
public class PlayState {

    private final Logger logger = LogManager.getLogger(PlayState.class);

    public static final int WRITE_AHEAD = 2;

    private final Playlist myPlaylist;
    private final int maxElements;
    private int playIndex;
    private int windowStartIndex;
    private int windowEndIndex;
    private final Semaphore prepareBuffer;
    private boolean playing;

    private static PlayState currentPlayState = null;

    public static PlayState getCurrentPlayState() {
        return currentPlayState;
    }

    public PlayState(Playlist p) {
        myPlaylist = p;
        maxElements = p.getList().size();
        playIndex = 0;
        windowStartIndex = 0;
        windowEndIndex = 0;
        prepareBuffer = new Semaphore(WRITE_AHEAD);
        playing = true;
        currentPlayState = this;
    }

    private void trackDeleted() {
        //log something?
        windowEndIndex++;
    }

    private void trackPrepared() {
        //log something?
        windowStartIndex++;
    }

    /**
     * Returns the next track that can be played
     *
     * @return a File of the next track to be played
     */
    public File getNextPlayableTrack() {
        return getDecodedTrackFile(playIndex);
    }

    /**
     * Checks if there are more tracks that can be played
     *
     * @return true if there are more tracks that can be played, false otherwise
     */
    public boolean hasMorePlayableTracks() {
        return playIndex<maxElements;
    }

    /**
     * Called when a play of a track is completed
     */
    public void trackPlayed() {
        prepareBuffer.release();
        //log something?
        playIndex++;
    }

    private final File baseEncodedTrackFolder = new File("data/http_root/");

    private File getEncodedTrackFile(int index) {
        if (myPlaylist==null) return null;
        return new File(baseEncodedTrackFolder,myPlaylist.getList().get(index).getPath());
    }

    private final File baseDecodedTrackFolder = new File("tmp/state/");

    private File getDecodedTrackFile(int index) {
        if (myPlaylist==null) return null;
        File enc = getEncodedTrackFile(index);

        String cleanName = enc.getName().substring(0,enc.getName().lastIndexOf('.'));

        return new File(baseDecodedTrackFolder,cleanName);
    }

    /**
     * Called before the threads are initialized with PlayState, in order to
     * skip problems with initial synchronization.
     *
     * @throws InterruptedException
     */
    public void prepareBeforeStart() throws InterruptedException {
        while (prepareBuffer.availablePermits()>0 && hasTrackToPrepare()) prepareNewPlayableTrack();
    }

    public boolean hasTrackToPrepare() {
        return windowStartIndex<maxElements;
    }

    /**
     * Called by the
     *
     * @throws InterruptedException
     */
    public void prepareNewPlayableTrack() throws InterruptedException {
        prepareBuffer.acquire();
        File in = getEncodedTrackFile(windowStartIndex);
        File out = getDecodedTrackFile(windowStartIndex);
        try {
            SCA0Decrypter dec = new SCA0Decrypter(new FileInputStream(in),new FileOutputStream(out));
            dec.decrypt();
        } catch (FileNotFoundException e) {
            logger.error("File not found during decoding : "+e.getMessage());
        } finally {
            trackPrepared();
        }
    }

    /**
     * Called to check if there are track available for deletion.
     *
     * @return true if some track can be deleted, false otherwise
     */
    public boolean hasTrackToDelete() {
        return windowEndIndex<playIndex;
    }

    /**
     * Deletes the next window end track.
     */
    public void deleteOldAlreadyPlayedTrack() {
        File toDel = getDecodedTrackFile(windowEndIndex);
        if (toDel.exists()) toDel.delete();
        trackDeleted();
    }

    /**
     * Deletes all the old already played tracks
     */
    public void deleteOldTracks() {
        while (hasTrackToDelete()) deleteOldAlreadyPlayedTrack();
    }

    /**
     * Stop playing.
     */
    public void stopPlaying() {
        playing = false;
        currentPlayState = null;
    }

    /**
     * Checks if the play state is active.
     *
     * @return true if the play state is active, false otherwise.
     */
    public boolean isPlaying() {
        return playing;
    }

    /**
     * Deletes all tracks found in the state folder.
     */
    public void deleteAllTracks() {
        File[] decodedTracks = baseDecodedTrackFolder.listFiles();
        for (File trk : decodedTracks) {
            trk.delete();
        }
    }
}
