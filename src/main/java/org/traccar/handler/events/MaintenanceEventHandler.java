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
import org.traccar.database.MaintenancesManager;
import org.traccar.model.Event;
import org.traccar.model.Maintenance;
import org.traccar.model.Position;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import java.sql.Timestamp;
import java.util.Calendar;



@ChannelHandler.Sharable
public class MaintenanceEventHandler extends BaseEventHandler {

    private final IdentityManager identityManager;
    private final MaintenancesManager maintenancesManager;
    private static final Logger LOGGER = LoggerFactory.getLogger(MaintenanceEventHandler.class);

    public MaintenanceEventHandler(IdentityManager identityManager, MaintenancesManager maintenancesManager) {
        this.identityManager = identityManager;
        this.maintenancesManager = maintenancesManager;
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
        for (long maintenanceId : maintenancesManager.getAllDeviceItems(position.getDeviceId())) {
            Maintenance maintenance = maintenancesManager.getById(maintenanceId);
            if(maintenance.getType().equals( "Datetime")){

                double timestamp = new Timestamp(System.currentTimeMillis()).getTime();
                   double startTime=maintenance.getStart();
      
                   Calendar cal = Calendar.getInstance();
                   cal.setTimeInMillis((long) timestamp);
   //////////////////// get current date ///////////////////////////                
                   int year = cal.get(Calendar.YEAR);
                   int month = cal.get(Calendar.MONTH);
                   int day = cal.get(Calendar.DATE);
                   int hour= cal.get(Calendar.HOUR);
                   int minute= cal.get(Calendar.MINUTE);   
   ////////////////////////// get the viste date ///////
                   Calendar calV = Calendar.getInstance();
                   calV.setTimeInMillis((long) startTime);
                   int yearV = calV.get(Calendar.YEAR);
                   int monthV = calV.get(Calendar.MONTH);
                   int dayV = calV.get(Calendar.DATE);
                   int hourV=calV.get(Calendar.HOUR);
                   int minuteV=calV.get(Calendar.MINUTE);


                   if(year == yearV && month == monthV && day == dayV && hour== hourV && minuteV==minute && maintenance.getPeriod()==0)
                   {
   
                       Event event = new Event("Visite", position.getDeviceId(), position.getId());
                       event.setMaintenanceId(maintenanceId);
                       event.set(maintenance.getType(), "Datetime");
                       events.put(event, position);
                        return events;

                        }
            }

            if (maintenance.getPeriod() != 0) {
                double oldValue = lastPosition.getDouble(maintenance.getType());
                double newValue = position.getDouble(maintenance.getType());
                if (oldValue != 0.0 && newValue != 0.0
                        && (long) ((oldValue - maintenance.getStart()) / maintenance.getPeriod())
                        < (long) ((newValue - maintenance.getStart()) / maintenance.getPeriod())) {
                    Event event = new Event(Event.TYPE_MAINTENANCE, position.getDeviceId(), position.getId());
                    event.setMaintenanceId(maintenanceId);
                    event.set(maintenance.getType(), newValue);
                    events.put(event, position);
                }
            }
        }

        return events;
    }

}
