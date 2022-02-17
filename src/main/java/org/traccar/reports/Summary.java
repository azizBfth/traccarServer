/*
 * Copyright 2016 - 2020 Anton Tananaev (anton@traccar.org)
 * Copyright 2016 Andrey Kunitsyn (andrey@traccar.org)
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
package org.traccar.reports;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.jxls.util.JxlsHelper;
import org.traccar.Context;
import org.traccar.model.Position;
import org.traccar.reports.model.SummaryReport;
import org.traccar.model.Device;


public final class Summary {
    public static final String ATTRIBUTE_REFUEL_THRESHOLD = "refuelThreshold";

    private Summary() {


    }

    private static SummaryReport calculateSummaryResult(long deviceId, Collection<Position> positions) {
       
        SummaryReport result = new SummaryReport();
        
        result.setDeviceId(deviceId);
   
   double    fullFuel=Context.getIdentityManager().lookupAttributeDouble(deviceId, ATTRIBUTE_REFUEL_THRESHOLD, 0, true, false);
       System.out.println(Double.toString(fullFuel));
           if(fullFuel == 0  )
{           
fullFuel=0;
}
        result.setDeviceName(Context.getIdentityManager().getById(deviceId).getName());
        if (positions != null && !positions.isEmpty()) {
            Position firstPosition = null;
            Position previousPosition = null;
            double speedSum = 0;
            double spentfuel=0;
            double fuelRise=0;
            double aux=0;
            double fr=0;
            ArrayList<Integer> fullIndex = new ArrayList<Integer>();

            for (Position position : positions) {

                if (firstPosition == null) {
                    firstPosition = position;
                }
     
     if(previousPosition != null )
                {   
                    if( position.getDouble(Position.KEY_FUEL_LEVEL) != 0 && previousPosition.getDouble(Position.KEY_FUEL_LEVEL) != 0) 

                    {
                        
                if(  (position.getDouble(Position.KEY_FUEL_LEVEL) < previousPosition.getDouble(Position.KEY_FUEL_LEVEL)) )
                {
                   spentfuel+= ReportUtils.calculateFuel(previousPosition, position);
                    fr=0;
                    aux=00;
                } 

                if(  (position.getDouble(Position.KEY_FUEL_LEVEL) > previousPosition.getDouble(Position.KEY_FUEL_LEVEL)) &&  ( position.getDouble(Position.KEY_FUEL_LEVEL) - previousPosition.getDouble(Position.KEY_FUEL_LEVEL ) < 10)   )
                {
                    //fullIndex.add(i);
                   fuelRise+= ReportUtils.calculateFuel(previousPosition, position);
                    fr++;
                    aux=+fuelRise;
                    if(fr>2 && aux >20)
                    {
                        fuelRise=-aux;
                        aux=0;
                        fr =0;
                    }
                } 

            }
            }
                previousPosition = position;
                speedSum += position.getSpeed();
                result.setMaxSpeed(position.getSpeed());

            }
            boolean ignoreOdometer = Context.getDeviceManager()
                    .lookupAttributeBoolean(deviceId, "report.ignoreOdometer", false, false, true);

                
            result.setDistance(ReportUtils.calculateDistance(firstPosition, previousPosition, !ignoreOdometer));

            result.setAverageSpeed(speedSum / positions.size());

        result.setSpentFuel(spentfuel+fuelRise);

            if (firstPosition.getAttributes().containsKey(Position.KEY_HOURS)
                    && previousPosition.getAttributes().containsKey(Position.KEY_HOURS)) {
                result.setEngineHours(
                        previousPosition.getLong(Position.KEY_HOURS) - firstPosition.getLong(Position.KEY_HOURS)
                        );
            }

            if (!ignoreOdometer
                    && firstPosition.getDouble(Position.KEY_ODOMETER) != 0
                    && previousPosition.getDouble(Position.KEY_ODOMETER) != 0) {
                result.setStartOdometer(firstPosition.getDouble(Position.KEY_ODOMETER));
                result.setEndOdometer(previousPosition.getDouble(Position.KEY_ODOMETER));
            } else {
                result.setStartOdometer(firstPosition.getDouble(Position.KEY_TOTAL_DISTANCE));
                result.setEndOdometer(previousPosition.getDouble(Position.KEY_TOTAL_DISTANCE));
            }

            result.setStartTime(firstPosition.getFixTime());
            result.setEndTime(previousPosition.getServerTime());
        }
        return result;
    }

    private static int getDay(long userId, Date date) {
        Calendar calendar = Calendar.getInstance(ReportUtils.getTimezone(userId));
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    private static Collection<SummaryReport> calculateSummaryResults(
            long userId, long deviceId, Date from, Date to, boolean daily) throws SQLException {

        ArrayList<Position> positions = new ArrayList<>(Context.getDataManager().getPositions(deviceId, from, to));

        ArrayList<SummaryReport> results = new ArrayList<>();
        if (daily && !positions.isEmpty()) {
            int startIndex = 0;
            int startDay = getDay(userId, positions.iterator().next().getFixTime());
            for (int i = 0; i < positions.size(); i++) {
                int currentDay = getDay(userId, positions.get(i).getFixTime());
                if (currentDay != startDay) {
                    results.add(calculateSummaryResult(deviceId, positions.subList(startIndex, i)));
                    startIndex = i;
                    startDay = currentDay;
                }
            }
            results.add(calculateSummaryResult(deviceId, positions.subList(startIndex, positions.size())));
        } else {
            results.add(calculateSummaryResult(deviceId, positions));
        }

        return results;
    }

    public static Collection<SummaryReport> getObjects(long userId, Collection<Long> deviceIds,
            Collection<Long> groupIds, Date from, Date to, boolean daily) throws SQLException {
        ReportUtils.checkPeriodLimit(from, to);
        ArrayList<SummaryReport> result = new ArrayList<>();
        for (long deviceId: ReportUtils.getDeviceList(deviceIds, groupIds)) {
            Context.getPermissionsManager().checkDevice(userId, deviceId);
            result.addAll(calculateSummaryResults(userId, deviceId, from, to, daily));
        }
        return result;
    }

    public static void getExcel(OutputStream outputStream,
            long userId, Collection<Long> deviceIds, Collection<Long> groupIds,
            Date from, Date to, boolean daily) throws SQLException, IOException {
        ReportUtils.checkPeriodLimit(from, to);
        Collection<SummaryReport> summaries = getObjects(userId, deviceIds, groupIds, from, to, daily);
        String templatePath = Context.getConfig().getString("report.templatesPath",
                "templates/export/");
        try (InputStream inputStream = new FileInputStream(templatePath + "/summary.xlsx")) {
            org.jxls.common.Context jxlsContext = ReportUtils.initializeContext(userId);
            jxlsContext.putVar("summaries", summaries);
            jxlsContext.putVar("from", from);
            jxlsContext.putVar("to", to);
            JxlsHelper.getInstance().setUseFastFormulaProcessor(false)
                    .processTemplate(inputStream, outputStream, jxlsContext);
        }
    }
}
