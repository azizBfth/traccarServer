package org.traccar.reports;
import org.traccar.reports.model.ChartReport;
import org.traccar.model.Position;
import java.util.Collection;
import java.util.Date;
import java.sql.SQLException;
import org.traccar.Context;
import java.util.ArrayList;



public final class   Chart {

    private Chart(){

    }

    private static Collection<ChartReport> PositionModified(  long deviceId, Date from, Date to) throws SQLException {
    
       ArrayList<Position> positions = new ArrayList<>(Context.getDataManager().getPositions(deviceId, from, to));

       Collection<ChartReport> result = new ArrayList<>();

        if (positions != null && !positions.isEmpty()) {
      
            for (int i = 0; i < positions.size(); i++) {
                ChartReport  chart = new ChartReport();
                chart.setAddress(positions.get(i).getAddress());
                chart.setFuel(positions.get(i).getDouble(Position.KEY_FUEL_LEVEL));
                chart.setLatitude(positions.get(i).getLatitude());
                chart.setLongitude(positions.get(i).getLongitude());
                chart.setdeviceId(positions.get(i).getDeviceId());
                chart.setserverTime(positions.get(i).getServerTime());
                chart.setTemperature(positions.get(i).getDouble(Position.KEY_COOLANT_TEMP));
                chart.setSpeed(positions.get(i).getSpeed());
                chart.setPositionId(positions.get(i).getId());
                chart.setBluecointemp1(positions.get(i).getDouble(Position.BlueCoinT1));
                chart.setBluecointemp2(positions.get(i).getDouble(Position.BlueCoinT2));

result.add(chart);
            }

           
}
return result;

    }



    public static Collection<ChartReport> getObjects(
            long userId, Collection<Long> deviceIds, Collection<Long> groupIds,
            Date from, Date to) throws SQLException {
        ReportUtils.checkPeriodLimit(from, to);
        ArrayList<ChartReport> result = new ArrayList<>();
        for (long deviceId: ReportUtils.getDeviceList(deviceIds, groupIds)) {
            Context.getPermissionsManager().checkDevice(userId, deviceId);
            result.addAll(PositionModified(deviceId, from, to));
        }
        return result;
    }


}