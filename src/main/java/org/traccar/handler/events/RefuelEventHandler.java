/*
 * Copyright 2017 - 2019 Anton Tananaev (anton@traccar.org)
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

import io.netty.channel.ChannelHandler;
import org.traccar.database.IdentityManager;
import org.traccar.model.Device;
import org.traccar.model.Event;
import org.traccar.model.Position;

import java.util.Collections;
import java.util.Map;

@ChannelHandler.Sharable
public class RefuelEventHandler extends BaseEventHandler {

    public static final String ATTRIBUTE_REFUEL_THRESHOLD = "refuelThreshold";

    private final IdentityManager identityManager;

    public RefuelEventHandler(IdentityManager identityManager) {
        this.identityManager = identityManager;
    }

    @Override
    protected Map<Event, Position> analyzePosition(Position position) {

        Device device = identityManager.getById(position.getDeviceId());
        if (device == null) {
            return null;
        }
        if (!identityManager.isLatestPosition(position)) {
            return null;
        }

        double refuelThreshold = identityManager
                .lookupAttributeDouble(device.getId(), ATTRIBUTE_REFUEL_THRESHOLD, 0, true, false);

        if (refuelThreshold > 0) {
            Position lastPosition = identityManager.getLastPosition(position.getDeviceId());
            if (position.getAttributes().containsKey(Position.KEY_FUEL_LEVEL)
                    && lastPosition != null && lastPosition.getAttributes().containsKey(Position.KEY_FUEL_LEVEL)) {

                double drop = lastPosition.getDouble(Position.KEY_FUEL_LEVEL)
                        - position.getDouble(Position.KEY_FUEL_LEVEL);
                if ( (drop * -1)  <= refuelThreshold) {
                    Event event = new Event(Event.TYPE_DEVICE_REFUEL, position.getDeviceId(), position.getId());
                    event.set(ATTRIBUTE_REFUEL_THRESHOLD, refuelThreshold);
                    return Collections.singletonMap(event, position);
                }
            }
        }

        return null;
    }

}
