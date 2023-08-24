package cc.cybereflex.resources.repository;

import cc.cybereflex.resources.model.data.Device;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeviceRepository {
    List<Device> queryAllAvailable();

    void insert(Device device);

    Device queryByUUID(String uuid);

    Device queryByIpAndUsername(@Param("ip") String ip, @Param("username") String username);
}
