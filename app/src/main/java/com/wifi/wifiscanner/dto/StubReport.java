package com.wifi.wifiscanner.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StubReport extends Report {

    public StubReport() {
        this.setDevice(this.initDevice());
        this.setAccessPoints(this.initResults());
    }

    private Device initDevice() {
        Device device = new Device();
        device.setIp(0);
        device.setMac("MAC ADDRESS");
        device.setModel("AMAZINGABLE DEVICE");
        device.setSoftVersion("1kk.2kk");
        return device;
    }

    private List<AccessPoint> initResults() {
        ArrayList<AccessPoint> accessPoints = new ArrayList<>();
        accessPoints.add(initResult(UUID.randomUUID().toString(), "172.173.23.42", 1, 1));
        accessPoints.add(initResult(UUID.randomUUID().toString(), "172.11.228.19", 1, 1));
        accessPoints.add(initResult(UUID.randomUUID().toString(), "172.11.228.19", 2, 2));
        accessPoints.add(initResult(UUID.randomUUID().toString(), "172.11.228.19", 3, 3));
        accessPoints.add(initResult(UUID.randomUUID().toString(), "172.11.228.19", 4, 4));
        accessPoints.add(initResult(UUID.randomUUID().toString(), "172.11.228.19", 5, 5));
        accessPoints.add(initResult(UUID.randomUUID().toString(), "172.11.228.19", 6, 6));
        accessPoints.add(initResult(UUID.randomUUID().toString(), "172.173.23.42", 2, 2));
        accessPoints.add(initResult(UUID.randomUUID().toString(), "172.173.23.42", 3, 3));
        accessPoints.add(initResult(UUID.randomUUID().toString(), "172.173.23.42", 4, 4));
        accessPoints.add(initResult(UUID.randomUUID().toString(), "172.173.23.42", 5, 5));
        accessPoints.add(initResult(UUID.randomUUID().toString(), "00:0e:8f:d3:5b:e4", 6, 6));
        return accessPoints;
    }

    private AccessPoint initResult(String name, String ip, int signal, int channel) {
        AccessPoint accessPoint = new AccessPoint();
        accessPoint.setSSID(name);
        accessPoint.setBSSID(ip);
        accessPoint.setRSSI(signal);
        accessPoint.setChannel(channel);
        return accessPoint;
    }
}
