/*
 * Copyright 2016 - 2018 Anton Tananaev (anton@traccar.org)
 * Copyright 2016 - 2018 Andrey Kunitsyn (andrey@traccar.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.traccar.handler.events;

import java.util.HashMap;
import java.util.Map;

import io.netty.channel.ChannelHandler;
import org.traccar.database.IdentityManager;
import org.traccar.database.MissionsManager;
import org.traccar.model.Event;
import org.traccar.model.Mission;

import org.traccar.model.Position;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import java.sql.Timestamp;
import java.util.Calendar;



@ChannelHandler.Sharable
public class MissionEventHandler extends BaseEventHandler {

    private final IdentityManager identityManager;
    private final MissionsManager missionsManager;
    private static final Logger LOGGER = LoggerFactory.getLogger(MissionEventHandler.class);

    public MissionEventHandler(IdentityManager identityManager, MissionsManager missionsManager) {
        this.identityManager = identityManager;
        this.missionsManager = missionsManager;
    }

    @Override
    protected Map<Event, Position> analyzePosition(Position position) {


        
        if (identityManager.getById(position.getDeviceId()) == null
                || !identityManager.isLatestPosition(position)) {
            return null;
        }

        Position lastPosition = identityManager.getLastPosition(position.getDeviceId());
        if (lastPosition == null) {
            return null;
        }

        Map<Event, Position> events = new HashMap<>();
      

        return events;
    }

}
