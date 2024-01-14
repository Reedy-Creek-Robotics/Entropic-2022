package org.firstinspires.ftc.teamcode.components;

public interface Component {

    /**
     * This method will be called as the robot is starting to set any initial state.
     */
    void init();

    /**
     * This method will be called continuously in every iteration of the loop.
     *
     * Components should use this as a chance to complete whatever commands they've been given.
     */
    void update();

    /**
     * Indicates if the component is busy trying to execute a command, or it is idle.
     */
    boolean isBusy();

}
