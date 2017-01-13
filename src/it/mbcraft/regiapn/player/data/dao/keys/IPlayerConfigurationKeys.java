package it.mbcraft.regiapn.player.data.dao.keys;

import it.mbcraft.libraries.command.CodeClassification;
import it.mbcraft.libraries.command.CodeType;

/**
 * This code is property of MBCRAFT di Marco Bagnaresi. All rights reserved.
 * <p>
 * Created by marco on 25/07/16.
 */
@CodeClassification(CodeType.SPECIFIC)
public interface IPlayerConfigurationKeys {
    String TIME_ZONE_KEY = "time_zone";
    String PLAY_START_TIME_KEY = "play_start_time"; //format is HH:mm:ss
    String PLAY_END_TIME_KEY = "play_end_time"; //format is HH:mm:ss
    String PLAY_END_TIME_DAY_OFFSET_KEY = "play_end_time_day_offset"; //integer offset, 0 or 1
    String CHECK_PLAY_MINUTES_INTERVAL_KEY = "check_play_minutes_interval";
    String FILES_UPDATE_TIME_KEY = "files_update_time";
    String LOOP_PLAYLIST_KEY = "loop_playlist";
    String START_FROM_BEGINNING_KEY = "start_from_beginning";
    String AUDIO_MODE_KEY = "audio_mode";
}
