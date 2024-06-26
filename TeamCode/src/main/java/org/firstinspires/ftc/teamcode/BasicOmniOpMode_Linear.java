/* Copyright (c) 2021 FIRST. All rights reserved.
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

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.checkerframework.checker.formatter.qual.FormatBottom;

/*
 * This file contains an example of a Linear "OpMode".
 * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
 * The names of OpModes appear on the menu of the FTC Driver Station.
 * When a selection is made from the menu, the corresponding OpMode is executed.
 *
 * This particular OpMode illustrates driving a 4-motor Omni-Directional (or Holonomic) robot.
 * This code will work with either a Mecanum-Drive or an X-Drive train.
 * Both of these drives are illustrated at https://gm0.org/en/latest/docs/robot-design/drivetrains/holonomic.html
 * Note that a Mecanum drive must display an X roller-pattern when viewed from above.
 *
 * Also note that it is critical to set the correct rotation direction for each motor.  See details below.
 *
 * Holonomic drives provide the ability for the robot to move in three axes (directions) simultaneously.
 * Each motion axis is controlled by one Joystick axis.
 *
 * 1) Axial:    Driving forward and backward               Left-joystick Forward/Backward
 * 2) Lateral:  Strafing right and left                     Left-joystick Right and Left
 * 3) Yaw:      Rotating Clockwise and counter clockwise    Right-joystick Right and Left
 *
 * This code is written assuming that the right-side motors need to be reversed for the robot to drive forward.
 * When you first test your robot, if it moves backward when you push the left stick forward, then you must flip
 * the direction of all 4 motors (see code below).
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list
 */

@TeleOp(name="Basic: Omni Linear OpMode", group="Linear OpMode")
public class BasicOmniOpMode_Linear extends LinearOpMode {

    // Declare OpMode members for each of the 4 motors.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftFrontDrive = null;
    private DcMotor leftBackDrive = null;
    private DcMotor rightFrontDrive = null;
    private DcMotor rightBackDrive = null;
    private DcMotor mainArm = null;
    private DcMotor otherArm = null;
    private DcMotor hanger = null;
    public Servo leftClaw = null;
    public Servo rightClaw = null;
    public Servo bandLauncher = null;
    public Servo airplane = null;

    double leftClawOffset = 0;
    double rightClawOffset = 0;

    public static final double MAIN_ARM_UP_POWER    =  0.45 ;
    public static final double MAIN_ARM_DOWN_POWER  = -0.35 ;
    public static final double OTHER_ARM_UP_POWER    =  0.15 ;
    public static final double OTHER_ARM_DOWN_POWER  = -0.45 ;
    public static final double HANGER_POWER =  0;
    public static final double CLAW_SPEED  = 0.002 ;
    public static final double FB_DPAD_SPEED_SCALE  = 0.2;
    public static final double LR_DPAD_SPEED_SCALE  = 0.8;
    public static final double STICK_SPEED_SCALE  = 0.5;
    public static final double BACK_MOTOR_OFFSET = .3;

    @Override
    public void runOpMode() {

        // Initialize the hardware variables. Note that the strings used here must correspond
        // to the names assigned during the robot configuration step on the DS or RC devices.
        leftFrontDrive  = hardwareMap.get(DcMotor.class, "left_front_drive"); // port 3
        leftBackDrive  = hardwareMap.get(DcMotor.class, "left_back_drive"); // port 2
        rightFrontDrive = hardwareMap.get(DcMotor.class, "right_front_drive"); // port 1
        rightBackDrive = hardwareMap.get(DcMotor.class, "right_back_drive"); // port 0
        mainArm = hardwareMap.get(DcMotor.class, "main_arm"); // port 1
        otherArm = hardwareMap.get(DcMotor.class, "other_arm"); // port 0
        leftClaw  = hardwareMap.get(Servo.class, "left_claw"); // port 4
        rightClaw = hardwareMap.get(Servo.class, "right_claw"); // port 5
        bandLauncher = hardwareMap.get(Servo.class, "band_launcher"); // port 3
        hanger = hardwareMap.get(DcMotor.class, "hanger"); // port 2
        airplane  = hardwareMap.get(Servo.class, "airplane"); // port 5


        // change direction of claw
        leftClaw.setDirection(Servo.Direction.REVERSE);

        // ########################################################################################
        // !!!            IMPORTANT Drive Information. Test your motor directions.            !!!!!
        // ########################################################################################
        // Most robots need the motors on one side to be reversed to drive forward.
        // The motor reversals shown here are for a "direct drive" robot (the wheels turn the same direction as the motor shaft)
        // If your robot has additional gear reductions or uses a right-angled drive, it's important to ensure
        // that your motors are turning in the correct direction.  So, start out with the reversals here, BUT
        // when you first test your robot, push the left joystick forward and observe the direction the wheels turn.
        // Reverse the direction (flip FORWARD <-> REVERSE ) of any wheel that runs backward
        // Keep testing until ALL the wheels move the robot forward when you push the left joystick forward.
        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);
        // Set the brakes for all da motors
        mainArm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        otherArm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        // Wait for the game to start (driver presses PLAY)
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            double max;

            // POV Mode uses left joystick to go forward & strafe, and right joystick to rotate.
            double axial   = -gamepad1.left_stick_y;  // Note: pushing stick forward gives negative value
            double lateral =  gamepad1.left_stick_x;
            double yaw     =  gamepad1.right_stick_x;

            // Combine the joystick requests for each axis-motion to determine each wheel's power.
            // Set up a variable for each drive wheel to save the power level for telemetry.
            double leftFrontPower  = axial + lateral + yaw;
            double rightFrontPower = axial - lateral - yaw;
            double leftBackPower   = axial - lateral + yaw ;
            double rightBackPower  = axial + lateral - yaw;
            double hangerPower = 0;


            // Normalize the values so no wheel power exceeds 100%
            // This ensures that the robot maintains the desired motion.
            max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
            max = Math.max(max, Math.abs(leftBackPower));
            max = Math.max(max, Math.abs(rightBackPower));

            if (max > 1.0) {
                leftFrontPower  /= max;
                rightFrontPower /= max;
                leftBackPower   /= max;
                rightBackPower  /= max;
            }

            // reduce speed because our robot is too dang fast
            leftFrontPower *= STICK_SPEED_SCALE;
            rightFrontPower *= STICK_SPEED_SCALE;
            leftBackPower *= STICK_SPEED_SCALE;
            rightBackPower *= STICK_SPEED_SCALE;

            // the robot is heavier in the back so we need to offset the power to make up for it
            if(leftFrontPower < (0 - BACK_MOTOR_OFFSET)) {
                leftFrontPower += BACK_MOTOR_OFFSET;
                rightFrontPower -= BACK_MOTOR_OFFSET;
            } else if(leftFrontPower > (0 + BACK_MOTOR_OFFSET)) {
                leftFrontPower -= BACK_MOTOR_OFFSET;
                rightFrontPower += BACK_MOTOR_OFFSET;
            }

            // This is test code:
            //
            // Uncomment the following code to test your motor directions.
            // Each button should make the corresponding motor run FORWARD.
            //   1) First get all the motors to take to correct positions on the robot
            //      by adjusting your Robot Configuration if necessary.
            //   2) Then make sure they run in the correct direction by modifying the
            //      the setDirection() calls above.
            // Once the correct motors move in the correct direction re-comment this code.

            /*
            leftFrontPower  = gamepad1.x ? 1.0 : 0.0;  // X gamepad
            leftBackPower   = gamepad1.a ? 1.0 : 0.0;  // A gamepad
            rightFrontPower = gamepad1.y ? 1.0 : 0.0;  // Y gamepad
            rightBackPower  = gamepad1.b ? 1.0 : 0.0;  // B gamepad
            */

            // use dpad for constant reliable movements
            if(gamepad1.dpad_up) {
                leftFrontPower = 1.0 * FB_DPAD_SPEED_SCALE;
                rightFrontPower = 1.0 * FB_DPAD_SPEED_SCALE;
                leftBackPower = 1.0 * FB_DPAD_SPEED_SCALE;
                rightBackPower = 1.0 * FB_DPAD_SPEED_SCALE;
            }
            else if(gamepad1.dpad_down) {
                leftFrontPower = -1.0 * FB_DPAD_SPEED_SCALE;
                rightFrontPower = -1.0 * FB_DPAD_SPEED_SCALE;
                leftBackPower = -1.0 * FB_DPAD_SPEED_SCALE;
                rightBackPower = -1.0 * FB_DPAD_SPEED_SCALE;
            }
            else if(gamepad1.dpad_right) {
                leftFrontPower = -1.0 * LR_DPAD_SPEED_SCALE;
                rightFrontPower = 1.0 * LR_DPAD_SPEED_SCALE;
                leftBackPower = -.3 * LR_DPAD_SPEED_SCALE;
                rightBackPower = .3 * LR_DPAD_SPEED_SCALE;
            }
            else if(gamepad1.dpad_left) {
                leftFrontPower = 1.0 * LR_DPAD_SPEED_SCALE;
                rightFrontPower = -1.0 * LR_DPAD_SPEED_SCALE;
                leftBackPower = .3 * LR_DPAD_SPEED_SCALE;
                rightBackPower = -.3 * LR_DPAD_SPEED_SCALE;
            }
            // Send calculated power to wheels
            leftFrontDrive.setPower(leftFrontPower);
            rightFrontDrive.setPower(rightFrontPower);
            leftBackDrive.setPower(leftBackPower);
            rightBackDrive.setPower(rightBackPower);

            // Use gamepad1 bumper and trigger to move the hanger
            if(gamepad1.right_bumper){
                hangerPower = 1.0;
            } else if (gamepad1.right_trigger > 0){
                hangerPower = -gamepad1.right_trigger;
            }
            // Control for the right claw
            if (gamepad2.right_bumper) {
                rightClawOffset += CLAW_SPEED;
            } else if (gamepad2.right_trigger > 0) {
                rightClawOffset -= CLAW_SPEED;
            }

            // Ensure the claw positions remain within their physical limits
            leftClawOffset = Range.clip(leftClawOffset, 0, 0.78);
            rightClawOffset = Range.clip(rightClawOffset, 0, 0.78);
            hanger.setPower(hangerPower);
            // Update servo positions
            leftClaw.setPosition(leftClawOffset);
            rightClaw.setPosition(rightClawOffset);
            // Use gamepad2 b to launch drone and move arm
            if (gamepad2.a)
                mainArm.setPower(MAIN_ARM_UP_POWER);
            else if (gamepad2.y)
                mainArm.setPower(MAIN_ARM_DOWN_POWER);
            else
                mainArm.setPower(0);

            if (gamepad2.x)
                otherArm.setPower(OTHER_ARM_UP_POWER);
            else if (gamepad2.b)
                otherArm.setPower(OTHER_ARM_DOWN_POWER);
            else
                otherArm.setPower(-0.05);

            if(gamepad2.dpad_up){
                airplane.setPosition(1);
            } else if(gamepad2.dpad_down) {
                airplane.setPosition(-1);
            }

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Front left/Right", "%4.2f, %4.2f", leftFrontPower, rightFrontPower);
            telemetry.addData("Back  left/Right", "%4.2f, %4.2f", leftBackPower, rightBackPower);
            telemetry.addData("Servo position left/Right", "%2.2f, %2.2f", leftClaw.getPosition(), rightClaw.getPosition());

            telemetry.update();
        }
    }}
