/* Copyright (c) 2014, 2015 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */
package org.firstinspires.ftc.teamcode.robotlibrary.testing.navxtesting;

import android.util.Log;

import com.kauailabs.navx.ftc.AHRS;
import com.kauailabs.navx.ftc.navXPIDController;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.DriveOnHeading;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.GyroTurn;

import java.text.DecimalFormat;

/*
 * An example loop op mode where the robot will drive in
 * a straight line (where the driving direction is guided by
 * the Yaw angle from a navX-Model device).
 *
 * This example uses a simple PID controller configuration
 * with a P coefficient, and will likely need tuning in order
 * to achieve optimal performance.
 *
 * Note that for the best accuracy, a reasonably high update rate
 * for the navX-Model sensor should be used.  This example uses
 * the default update rate (50Hz), which may be lowered in order
 * to reduce the frequency of the updates to the drive system.
 */

@TeleOp(name = "Concept: navX Drive Straight PID - Loop", group = "Concept")
// @Disabled Comment this in to remove this from the Driver Station OpMode List
public class ConceptNavXDriveStraightPIDLoopOp extends OpMode {

    DriveTrain driveTrain;
    AHRS navx_device;

    int stage = 0;

    DriveOnHeading driveOnHeading;

    DecimalFormat df = new DecimalFormat("#.##");

    @Override
    public void init() {
        driveTrain = new DriveTrain(hardwareMap);

        navx_device = AHRS.getInstance(hardwareMap.deviceInterfaceModule.get("Device Interface Module"), 0, AHRS.DeviceDataType.kProcessedData);
    }

    @Override
    public void start() {
        navx_device.zeroYaw();
    }

    @Override
    public void loop() {
        if (stage == 0) {
            if (!navx_device.isCalibrating()) {
                stage++;
                navx_device.zeroYaw();
            }
        }
        if (stage == 1) {
            if (driveOnHeading == null) {
                driveOnHeading = new DriveOnHeading(navx_device, driveTrain, 25, 0.75);
            }
            driveOnHeading.run();
            if (driveOnHeading.isCompleted()) {
                driveOnHeading.completed();
                stage++;
            }
        }

        telemetry.addData("Stage", String.valueOf(stage));
        telemetry.addData("L", driveTrain.LeftFrontMotor.getPower());
        telemetry.addData("R", driveTrain.RightFrontMotor.getPower());
        telemetry.addData("Heading", df.format(navx_device.getYaw()));
    }

    @Override
    public void stop() {
        navx_device.close();
    }
}
