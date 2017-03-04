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

import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.GyroTurn;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.PID;

import java.text.DecimalFormat;

import static org.firstinspires.ftc.teamcode.robotlibrary.AutonomousUtils.df;

/*
 * An example loop op mode where the robot will rotate
 * to a specified angle an then stop.
 *
 * This example uses a simple PID controller configuration
 * with a P coefficient, and will likely need tuning in order
 * to achieve optimal performance.
 *
 * Note that for the best accuracy, a reasonably high update rate
 * for the navX-Model sensor should be used.
 */
@Autonomous(name = "Concept: navX Rotate to Angle PID", group = "Concept")
public class ConceptNavXRotateToAnglePIDLoopOp extends OpMode {

    DriveTrain driveTrain;
    AHRS navx_device;

    int stage = 0;

    GyroTurn gyroTurn;

    ElapsedTime time = new ElapsedTime();
    double completedTime;

    @Override
    public void init() {

        driveTrain = new DriveTrain(hardwareMap);
        navx_device = AHRS.getInstance(hardwareMap);

    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {
        if (stage == 0) {
            if (!navx_device.isCalibrating()) {
                navx_device.zeroYaw();
                stage++;
                time.reset();
            }
        }
        if (stage == 1) {
            if (gyroTurn == null) {
                // TWEAK THESE VARIABLES FOR TESTING
                PID pid = new PID(
                        0.00625,
                        0.0,
                        0.0);
                gyroTurn = new GyroTurn(navx_device, driveTrain, 90, pid); // You don't have to provide a pid argument
            }
            gyroTurn.run(); // You must place this outside of the null statement, this is different from encoder routines
            if (gyroTurn.isCompleted()) { // isCompleted will automatically stop robot
                stage++;
            }
        }

        if (stage == 2) {
            completedTime = time.time();
            stage++;
        }

        if (stage == 3) {
            telemetry.addData("Completed", df.format(completedTime));
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
