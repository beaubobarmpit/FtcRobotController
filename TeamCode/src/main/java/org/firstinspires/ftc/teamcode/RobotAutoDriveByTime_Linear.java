/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;

import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

/*
 * This OpMode illustrates the concept of driving a path based on time.
 * The code is structured as a LinearOpMode
 *
 * The code assumes that you do NOT have encoders on the wheels,
 *   otherwise you would use: RobotAutoDriveByEncoder;
 *
 *   The desired path in this example is:
 *   - Drive forward for 3 seconds
 *   - Spin right for 1.3 seconds
 *   - Drive Backward for 1 Second
 *
 *  The code is written in a simple form with no optimizations.
 *  However, there are several ways that this type of sequence could be streamlined,
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list
 */

@Autonomous(name="Robot: Shorter Drive", group="Robot")
public class RobotAutoDriveByTime_Linear extends LinearOpMode {

    /* Declare OpMode members. */
    private DcMotor leftFrontDrive = null;
    private DcMotor leftBackDrive = null;
    private DcMotor rightFrontDrive = null;
    private DcMotor rightBackDrive = null;
    public Servo leftClaw = null;
    private DcMotor hanger = null;
    double hangerPower = .3;
    private ElapsedTime     runtime = new ElapsedTime();
    private DcMotor mainArm = null;
    TouchSensor touchSensor;
    private DcMotor otherArm = null;
    public Servo airplane = null;
    private boolean beauscreamFound;
    @Override
    public void runOpMode() {
        int beauscreamSoundID   = hardwareMap.appContext.getResources().getIdentifier("beauscream",   "raw", hardwareMap.appContext.getPackageName());
        int narrator_1_1_17ID   = hardwareMap.appContext.getResources().getIdentifier("narrator1_1_17",   "raw", hardwareMap.appContext.getPackageName());
        int narrator_1_2_17ID   = hardwareMap.appContext.getResources().getIdentifier("narrator1_2_17",   "raw", hardwareMap.appContext.getPackageName());
        int narrator_1_3_17ID   = hardwareMap.appContext.getResources().getIdentifier("narrator1_3_17",   "raw", hardwareMap.appContext.getPackageName());
        int narrator_1_4_17ID   = hardwareMap.appContext.getResources().getIdentifier("narrator1_4_17",   "raw", hardwareMap.appContext.getPackageName());
        int narrator_2_1_8ID   = hardwareMap.appContext.getResources().getIdentifier("narrator2_1_8",   "raw", hardwareMap.appContext.getPackageName());
        int narrator_2_2_8ID   = hardwareMap.appContext.getResources().getIdentifier("narrator2_2_8",   "raw", hardwareMap.appContext.getPackageName());
        int narrator_3_1_3980ID   = hardwareMap.appContext.getResources().getIdentifier("narrator3_1_3980",   "raw", hardwareMap.appContext.getPackageName());
        int narrator_3_2_5270ID   = hardwareMap.appContext.getResources().getIdentifier("narrator3_2_5270",   "raw", hardwareMap.appContext.getPackageName());
        int narrator_4_1_3110ID   = hardwareMap.appContext.getResources().getIdentifier("narrator4_1_3110",   "raw", hardwareMap.appContext.getPackageName());
        int narrator_4_2_5190ID   = hardwareMap.appContext.getResources().getIdentifier("narrator4_2_5190",   "raw", hardwareMap.appContext.getPackageName());
        int narrator_4_3_4480ID   = hardwareMap.appContext.getResources().getIdentifier("narrator4_3_4480",   "raw", hardwareMap.appContext.getPackageName());
        int narrator_5_1_4380ID   = hardwareMap.appContext.getResources().getIdentifier("narrator5_1_4380",   "raw", hardwareMap.appContext.getPackageName());
        int narrator_5_2_3290ID   = hardwareMap.appContext.getResources().getIdentifier("narrator5_2_3290",   "raw", hardwareMap.appContext.getPackageName());
        int narrator_5_3_3390ID   = hardwareMap.appContext.getResources().getIdentifier("narrator5_3_3390",   "raw", hardwareMap.appContext.getPackageName());
        int narrator_6_1_4210ID   = hardwareMap.appContext.getResources().getIdentifier("narrator6_1_4210",   "raw", hardwareMap.appContext.getPackageName());
        int narrator_6_2_5380ID   = hardwareMap.appContext.getResources().getIdentifier("narrator6_2_5380",   "raw", hardwareMap.appContext.getPackageName());
        int narrator_6_3_5320ID   = hardwareMap.appContext.getResources().getIdentifier("narrator6_3_5320",   "raw", hardwareMap.appContext.getPackageName());
        int narrator_7_1_4310ID   = hardwareMap.appContext.getResources().getIdentifier("narrator7_1_4310",   "raw", hardwareMap.appContext.getPackageName());
        int narrator_7_2_5290ID   = hardwareMap.appContext.getResources().getIdentifier("narrator7_2_5290",   "raw", hardwareMap.appContext.getPackageName());
        int sound1_6ID   = hardwareMap.appContext.getResources().getIdentifier("sound1_6",   "raw", hardwareMap.appContext.getPackageName());
        int sound2_4ID   = hardwareMap.appContext.getResources().getIdentifier("sound2_4",   "raw", hardwareMap.appContext.getPackageName());
        int sound3_5ID   = hardwareMap.appContext.getResources().getIdentifier("sound3_5",   "raw", hardwareMap.appContext.getPackageName());
        int sound4_8ID   = hardwareMap.appContext.getResources().getIdentifier("sound4_8",   "raw", hardwareMap.appContext.getPackageName());

        telemetry.addData("beauscream resource",   beauscreamFound ?   "Found" : "NOT found\n Add beauscream.wav to /src/main/res/raw" );

        telemetry.addData("Playing", "Resource BeauScream");
        telemetry.update();

        // Initialize the drive system variables.
        leftFrontDrive  = hardwareMap.get(DcMotor.class, "left_front_drive"); // port 3
        leftBackDrive  = hardwareMap.get(DcMotor.class, "left_back_drive"); // port 2
        rightFrontDrive = hardwareMap.get(DcMotor.class, "right_front_drive"); // port 1
        rightBackDrive = hardwareMap.get(DcMotor.class, "right_back_drive"); // port 0
        leftClaw  = hardwareMap.get(Servo.class, "right_claw"); // port 5 and its actually right claw
        touchSensor = hardwareMap.get(TouchSensor.class, "sensor_touch"); // port 1 digital
        hanger = hardwareMap.get(DcMotor.class, "hanger"); // port 2
        mainArm = hardwareMap.get(DcMotor.class, "main_arm"); // port 1
        otherArm = hardwareMap.get(DcMotor.class, "other_arm"); // port 0
        airplane  = hardwareMap.get(Servo.class, "airplane"); // port 5
        // To drive forward, most robots need the motor on one side to be reversed, because the axles point in opposite directions.
        // When run, this OpMode should start both motors driving forward. So adjust these two lines based on your first test drive.
        // Note: The settings here assume direct drive on left and right wheels.  Gear Reduction or 90 Deg drives may require direction flips
        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Ready to run");    //
        telemetry.update();

        int[] sounds = new int[] {
                sound1_6ID,
                narrator_1_1_17ID,
                narrator_1_2_17ID,
                narrator_1_3_17ID,
                narrator_1_4_17ID,
                narrator_2_1_8ID,
                narrator_2_2_8ID,
                sound2_4ID,
                narrator_3_1_3980ID,
                narrator_3_2_5270ID,
                sound3_5ID,
                narrator_4_1_3110ID,
                narrator_4_2_5190ID,
                narrator_4_3_4480ID,
                narrator_5_1_4380ID,
                narrator_5_2_3290ID,
                narrator_5_3_3390ID,
                narrator_6_1_4210ID,
                narrator_6_2_5380ID,
                narrator_6_3_5320ID,
                narrator_7_1_4310ID,
                narrator_7_2_5290ID,
                sound4_8ID
        };
        boolean[] soundPlayed = new boolean[sounds.length];
        int[] soundDurations = new int[] {
                6000,
          3800,
          4000,
          4000,
          4000,
                4000,
                4000,
                4000,
                3980,
                5270,
                5000,
                3110,
                5190,
                4480,
                4380,
                3290,
                3390,
                4210,
                5380,
                5320,
                4310,
                5290,
                8000
        };

        //SoundSequence soundSequence = new SoundSequence(hardwareMap, sounds, sounds);
        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        //soundSequence.playSounds(hardwareMap);
        telemetry.update();
        //SoundPlayer.getInstance().startPlaying(hardwareMap.appContext, narrator_1_17ID);
        // wait for touch sensor to be pushed
        while (opModeIsActive()) {

            // send the info back to driver station using telemetry function.
            if (touchSensor.isPressed()) {
                //telemetry.addData("Touch Sensor", "Is Pressed");

                break;
            } else {
                //telemetry.addData("Touch Sensor", "Is Not Pressed hello");
                hanger.setPower(0);
            }

            //telemetry.update();
        }
        runtime.reset();

        // start playing audio
        SoundPlayer.getInstance().startPlaying(hardwareMap.appContext, sound1_6ID);

        while (opModeIsActive() && (runtime.seconds() < 45)) {
            if(runtime.milliseconds() > getPreviousDurations(1, soundDurations) && !soundPlayed[1]) {
                soundPlayed[1] = true;
                SoundPlayer.getInstance().startPlaying(hardwareMap.appContext, narrator_1_1_17ID);
            }
            if(runtime.milliseconds() > getPreviousDurations(2, soundDurations) && !soundPlayed[2]) {
                soundPlayed[2] = true;
                SoundPlayer.getInstance().startPlaying(hardwareMap.appContext, narrator_1_2_17ID);
            }
            if(runtime.milliseconds() > getPreviousDurations(3, soundDurations) && !soundPlayed[3]) {
                soundPlayed[3] = true;
                SoundPlayer.getInstance().startPlaying(hardwareMap.appContext, narrator_1_3_17ID);
            }
            if(runtime.milliseconds() > getPreviousDurations(4, soundDurations) && !soundPlayed[4]) {
                soundPlayed[4] = true;
                SoundPlayer.getInstance().startPlaying(hardwareMap.appContext, narrator_1_4_17ID);
            }
            if(runtime.milliseconds() > getPreviousDurations(5, soundDurations) && !soundPlayed[5]) {
                soundPlayed[5] = true;
                SoundPlayer.getInstance().startPlaying(hardwareMap.appContext, narrator_2_1_8ID);
            }
            if(runtime.milliseconds() > getPreviousDurations(6, soundDurations) && !soundPlayed[6]) {
                soundPlayed[6] = true;
                SoundPlayer.getInstance().startPlaying(hardwareMap.appContext, narrator_2_2_8ID);
            }
            if(runtime.milliseconds() > getPreviousDurations(7, soundDurations) && !soundPlayed[7]) {
                soundPlayed[7] = true;
                SoundPlayer.getInstance().startPlaying(hardwareMap.appContext, sound2_4ID);
            }
            if(runtime.milliseconds() > getPreviousDurations(8, soundDurations) && !soundPlayed[8]) {
                soundPlayed[8] = true;
                SoundPlayer.getInstance().startPlaying(hardwareMap.appContext, narrator_3_1_3980ID);
            }
            if(runtime.milliseconds() > getPreviousDurations(9, soundDurations) && !soundPlayed[9]) {
                soundPlayed[9] = true;
                SoundPlayer.getInstance().startPlaying(hardwareMap.appContext, narrator_3_2_5270ID);
            }
            if(runtime.milliseconds() > getPreviousDurations(10, soundDurations) && !soundPlayed[10]) {
                soundPlayed[10] = true;
                SoundPlayer.getInstance().startPlaying(hardwareMap.appContext, sound3_5ID);
            }
            //play through sound 3 or index 10
        }


        // start moving the rocket actuator up at a slow speed
        while (opModeIsActive() && (runtime.seconds() < 75)) {


            // play narrator 4 and 5
            if(runtime.milliseconds() > getPreviousDurations(11, soundDurations) && !soundPlayed[11]) {
                soundPlayed[11] = true;
                SoundPlayer.getInstance().startPlaying(hardwareMap.appContext, narrator_4_1_3110ID);
                hanger.setPower(.3);
            }
            if(runtime.milliseconds() > getPreviousDurations(12, soundDurations) && !soundPlayed[12]) {
                soundPlayed[12] = true;
                SoundPlayer.getInstance().startPlaying(hardwareMap.appContext, narrator_4_2_5190ID);
            }
            if(runtime.milliseconds() > getPreviousDurations(13, soundDurations) && !soundPlayed[13]) {
                soundPlayed[13] = true;
                SoundPlayer.getInstance().startPlaying(hardwareMap.appContext, narrator_4_3_4480ID);
            }
            if(runtime.milliseconds() > getPreviousDurations(14, soundDurations) && !soundPlayed[14]) {
                soundPlayed[14] = true;
                SoundPlayer.getInstance().startPlaying(hardwareMap.appContext, narrator_5_1_4380ID);
            }
            if(runtime.milliseconds() > getPreviousDurations(15, soundDurations) && !soundPlayed[15]) {
                soundPlayed[15] = true;
                SoundPlayer.getInstance().startPlaying(hardwareMap.appContext, narrator_5_2_3290ID);
            }
            if(runtime.milliseconds() > getPreviousDurations(16, soundDurations) && !soundPlayed[16]) {
                soundPlayed[16] = true;
                SoundPlayer.getInstance().startPlaying(hardwareMap.appContext, narrator_5_3_3390ID);
            }
            if(runtime.seconds() > 60){
                airplane.setPosition(1);
            }

        }
        //SoundPlayer.getInstance().startPlaying(hardwareMap.appContext, beauscreamSoundID);
        hanger.setPower(0);
        while (opModeIsActive() && (runtime.seconds() < 78)) {
            mainArm.setPower(-.45);
        }
        //SoundPlayer.getInstance().startPlaying(hardwareMap.appContext, beauscreamSoundID);

        while (opModeIsActive() && (runtime.seconds() < 95)) {
            mainArm.setPower(-.05);
            otherArm.setPower(.15);

            if(runtime.milliseconds() > getPreviousDurations(17, soundDurations) && !soundPlayed[17]) {
                soundPlayed[17] = true;
                SoundPlayer.getInstance().startPlaying(hardwareMap.appContext, narrator_6_1_4210ID);
            }
            if(runtime.milliseconds() > getPreviousDurations(18, soundDurations) && !soundPlayed[18]) {
                soundPlayed[18] = true;
                SoundPlayer.getInstance().startPlaying(hardwareMap.appContext, narrator_6_2_5380ID);
            }
            if(runtime.milliseconds() > getPreviousDurations(19, soundDurations) && !soundPlayed[19]) {
                soundPlayed[19] = true;
                SoundPlayer.getInstance().startPlaying(hardwareMap.appContext, narrator_6_3_5320ID);
            }
        }
        otherArm.setPower(0);


        while (opModeIsActive() && (runtime.seconds() < 98)) {
            mainArm.setPower(.1);
        }
        mainArm.setPower(0);

        while (opModeIsActive() && (runtime.seconds() < 118)) {
            hanger.setPower(-.3);

            if(runtime.milliseconds() > getPreviousDurations(20, soundDurations) && !soundPlayed[20]) {
                soundPlayed[20] = true;
                SoundPlayer.getInstance().startPlaying(hardwareMap.appContext, narrator_7_1_4310ID);
            }
            if(runtime.milliseconds() > getPreviousDurations(21, soundDurations) && !soundPlayed[21]) {
                soundPlayed[21] = true;
                SoundPlayer.getInstance().startPlaying(hardwareMap.appContext, narrator_7_2_5290ID);
            }
            if(runtime.milliseconds() > getPreviousDurations(22, soundDurations) && !soundPlayed[22]) {
                soundPlayed[22] = true;
                SoundPlayer.getInstance().startPlaying(hardwareMap.appContext, sound4_8ID);
            }
        }
        hanger.setPower(0);
        // activate servo to move syringe for booster drop off

        // move arm up to display planet

        // rotate arm on arm to show the moon orbiting the planet


        // Define the flippin wheel power to what the flippin wheel power should be

        /*
        // Step 2:  Spin right for 1.3 seconds
        leftDrive.setPower(TURN_SPEED);
        rightDrive.setPower(-TURN_SPEED);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 1.3)) {
            telemetry.addData("Path", "Leg 2: %4.1f S Elapsed", runtime.seconds());
            telemetry.update();
        }

        // Step 3:  Drive Backward for 1 Second
        leftDrive.setPower(-FORWARD_SPEED);
        rightDrive.setPower(-FORWARD_SPEED);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 1.0)) {
            telemetry.addData("Path", "Leg 3: %4.1f S Elapsed", runtime.seconds());
            telemetry.update();
        }

        // Step 4:  Stop
        leftDrive.setPower(0);
        rightDrive.setPower(0);
        */

    }

    private double getPreviousDurations(int soundToBePlayedIndex, int[] durations) {
        double duration = 0;
        for(int i = 0;i<soundToBePlayedIndex;i++) {
            duration+= durations[i];
        }
        return duration;
    }
}
