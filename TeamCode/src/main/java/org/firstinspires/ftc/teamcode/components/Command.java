package org.firstinspires.ftc.teamcode.components;

/**
 * Represents a command that has been issued to the drive train.
 */
public interface Command {

    /**
     * Starts the command
     */
    void start();

    /**
     * Stops the command
     */
    void stop();

    /**
     * Update the status, and indicate if the command is finished
     *
     * @return true if the command is finished, false if it is still in progress
     */
    boolean update();

}
