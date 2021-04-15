package signup.signup.dao;

import signup.signup.entity.Device;

import java.util.List;

public interface DeviceDAO {
    void addDevice(Device device);
    List<Device> getAllDevice();
}
