package com.wifi.wifiscanner;

import java.util.ArrayList;
import java.util.List;

public class StubReport extends Report {

    public StubReport() {
        this.setDevice(this.initDevice());
        this.setResults(this.initResults());
    }

    private Device initDevice() {
        Device device = new Device();
        device.setIp(0);
        device.setMac("MAC ADDRESS");
        device.setModel("AMAZINGABLE DEVICE");
        device.setSoftVersion("1kk.2kk");
        return device;
    }

    private List<Result> initResults() {
        ArrayList<Result> results = new ArrayList<>();
        results.add(initResult("BAC9I_WIFI", "172.173.23.42", 1, 1));
        results.add(initResult("PIVNUSHKA_FREE_WIFI", "172.11.228.19", 2, 2));
        return results;
    }

    private Result initResult(String name, String ip, int signal, int channel) {
        Result result = new Result();
        result.setSSID(name);
        result.setBSSID(ip);
        result.setRSSI(signal);
        result.setChannel(channel);
        return result;
    }
}
