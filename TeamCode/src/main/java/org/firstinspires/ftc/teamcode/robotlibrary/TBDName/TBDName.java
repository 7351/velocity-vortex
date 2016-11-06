package org.firstinspires.ftc.teamcode.robotlibrary.TBDName;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class TBDName {

    public VuforiaSystem vuforiaSystem = new VuforiaSystem();
    // We declare all of the objects that the opmode brings into the programs lifecycle
    HardwareMap hardwareMap;
    public DriveTrain driveTrain = new DriveTrain(hardwareMap);
    Telemetry telemetry;
    public GyroUtils gyroUtils = new GyroUtils(hardwareMap, driveTrain, telemetry);
    public TBDName(HardwareMap hardwareMap, Telemetry telemetry) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
    }

}
