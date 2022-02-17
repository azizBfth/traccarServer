
package org.traccar.reports;

import org.traccar.reports.model.DdsReport;
import org.traccar.model.Position;
import java.util.Collection;
import java.util.Date;
import java.sql.SQLException;
import org.traccar.Context;
import java.util.ArrayList;

public final class Dds {

    private Dds() {

    }

    private static Collection<DdsReport> PositionModified(long deviceId, Date from, Date to) throws SQLException {

        ArrayList<Position> positions = new ArrayList<>(Context.getDataManager().getPositions(deviceId, from, to));

        Collection<DdsReport> result = new ArrayList<>();

        if (positions != null && !positions.isEmpty()) {
            double aux = 0.0;
            double onePercent = 24;
            for (int i = 0; i < positions.size(); i++) {
                DdsReport dds = new DdsReport();
                if (positions.get(i).getDouble(Position.Dds) != aux && Math.abs(positions.get(i).getDouble(Position.Dds) - aux) > (onePercent * 2)) {
                    dds.setAddress(positions.get(i).getAddress());
                    dds.setLatitude(positions.get(i).getLatitude());
                    dds.setLongitude(positions.get(i).getLongitude());
                    dds.setdeviceId(positions.get(i).getDeviceId());
                    dds.setserverTime(positions.get(i).getServerTime());
                    dds.setSpeed(positions.get(i).getSpeed());
                    dds.setPositionId(positions.get(i).getId());
                    dds.setMasse(positions.get(i).getDouble(Position.Dds));

                    result.add(dds);
                    aux = positions.get(i).getDouble(Position.Dds);
                }

            }

        }
        return result;

    }

    public static Collection<DdsReport> getObjects(long userId, Collection<Long> deviceIds, Collection<Long> groupIds,
            Date from, Date to) throws SQLException {
        ReportUtils.checkPeriodLimit(from, to);
        ArrayList<DdsReport> result = new ArrayList<>();
        for (long deviceId : ReportUtils.getDeviceList(deviceIds, groupIds)) {
            Context.getPermissionsManager().checkDevice(userId, deviceId);
            result.addAll(PositionModified(deviceId, from, to));
        }
        return result;
    }

}