package it.mbcraft.regiapn.player.command.updates.diff;


import org.apache.commons.codec.binary.Hex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by marco on 13/05/16.
 */
public class FileInfo {

    private static final Logger logger = LogManager.getLogger(FileInfo.class);

    private final String myRelativePath;
    private final long mySize;
    private final String mySha1Digest;

    public FileInfo(File root, File f) {

        myRelativePath = root.toPath().relativize(f.toPath()).toString();
        mySize = f.length();
        mySha1Digest = calculateSha1Digest(f);
    }

    private String calculateSha1Digest(File f) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            InputStream is = new BufferedInputStream(new FileInputStream(f), 1024 * 1024);
            byte[] buffer = new byte[1024 * 40];
            int readed = 0;
            while ((readed = is.read(buffer)) != -1) {
                digest.update(buffer, 0, readed);
            }
            is.close();

            String result = Hex.encodeHexString(digest.digest());
            logger.debug("Computed digest for file " + myRelativePath + " = " + result);
            return result;
        } catch (IOException e) {
            logger.error("Errore di IO durante il calcolo del digest sha1", e);
        } catch (NoSuchAlgorithmException e) {
            logger.error("Digest sha1 non disponibile", e);
        }

        throw new IllegalStateException("Unable to compute digest for file.");
    }

    public String getRelativePath() {
        return myRelativePath;
    }

    public long getSize() {
        return mySize;
    }

    public String getSha1Digest() {
        return mySha1Digest;
    }
}
