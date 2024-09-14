package bg.sofia.uni.fmi.mjt.intelligenthome.device;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public abstract class IoTDeviceBase implements IoTDevice {
    private static int uniqueNumberDevice = 0;
    private final String id;
    private final DeviceType type;
    private final String name;
    private final double powerConsumption;
    private final LocalDateTime installationDateTime;
    private LocalDateTime registration;

    public IoTDeviceBase(DeviceType type, String name, double powerConsumption, LocalDateTime installationDateTime) {
        this.type = type;
        this.name = name;
        this.powerConsumption = powerConsumption;
        this.installationDateTime = installationDateTime;

        id = type.getShortName() + '-' + name + '-' + uniqueNumberDevice++;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getPowerConsumption() {
        return powerConsumption;
    }

    @Override
    public LocalDateTime getInstallationDateTime() {
        return installationDateTime;
    }

    @Override
    public DeviceType getType() {
        return type;
    }

    @Override
    public long getRegistration() {
        return Duration.between(registration, LocalDateTime.now()).toHours();
    }

    @Override
    public void setRegistration(LocalDateTime registration) {
        this.registration = registration;
    }

    @Override
    public long getPowerConsumptionKWh() {
        long duration = Duration.between(getInstallationDateTime(), LocalDateTime.now()).toHours();
        return (long) (duration * powerConsumption);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IoTDeviceBase that = (IoTDeviceBase) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static void startIncrementing() {
        uniqueNumberDevice = 0;
    }
}
