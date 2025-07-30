package com.server.ai.tool;

//设备枚举
public enum DeviceEnum {
    PUSH_ROD(39L, "电动推杆", 1),
    FAN(37L, "排气扇", 0),
    ALARM_LIGHT(38L, "警报灯", 0);


    private final Long id;
    private final String name;
    private final int index;

    DeviceEnum(Long id, String name, int index) {
        this.id = id;
        this.name = name;
        this.index = index;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public int getIndex() { return index; }

    public DeviceControlRequest buildRequest(String action) {
        DeviceControlRequest req = new DeviceControlRequest();
        req.setDeviceId(this.id);
        req.setAction(action);
        req.setIndex(this.index);
        return req;
    }
}
