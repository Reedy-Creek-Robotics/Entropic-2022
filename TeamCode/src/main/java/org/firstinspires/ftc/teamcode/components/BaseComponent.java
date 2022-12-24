package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.RobotDescriptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class BaseComponent implements Component {

    private OpMode opMode;

    protected RobotContext context;
    protected RobotDescriptor robotDescriptor;

    protected HardwareMap hardwareMap;

    protected Telemetry telemetry;

    protected ElapsedTime commandTime;

    private Command currentCommand;
    private List<Command> nextCommands;

    private List<Component> subComponents = new ArrayList<>();

    public BaseComponent(RobotContext context) {
        this.context = context;
        this.opMode = context.opMode;
        this.hardwareMap = opMode.hardwareMap;
        this.telemetry = opMode.telemetry;
        this.robotDescriptor = context.robotDescriptor;
        this.commandTime = new ElapsedTime();
        this.currentCommand = null;
        this.nextCommands = new ArrayList<>();
    }

    /**
     * Executes the given command.  If there's another command in progress, this one will be added to the queue.
     */
    protected void executeCommand(Command command) {

        // Check if the command can be combined with the last command in the queue.
        Command lastCommandInQueue = getLastCommandInQueue();
        if (lastCommandInQueue instanceof CombinableCommand) {
            Command combined = ((CombinableCommand) lastCommandInQueue).combineWith(command);
            if (combined != null) {
                // The command was successfully combined with the last one in the queue, so replace the last one in
                // the queue with this new combination.
                replaceLastCommandInQueue(combined);
                return;
            }
        }

        this.nextCommands.add(command);
    }

    /**
     * Stops the current command and removes any additional commands from the queue.
     */
    public void stopAllCommands() {
        if (currentCommand != null) {
            currentCommand.stop();
        }
        currentCommand = null;
        nextCommands.clear();
    }

    protected Command getCurrentCommand() {
        return currentCommand;
    }

    protected List<Command> getNextCommands() {
        return nextCommands;
    }

    private Command getLastCommandInQueue() {
        return nextCommands.isEmpty() ?
                currentCommand :
                nextCommands.get(nextCommands.size() - 1);
    }

    private void replaceLastCommandInQueue(Command command) {
        if (nextCommands.isEmpty()) {
            // The command queue is empty so the current command is the one being replaced.  Discard the current
            // command and add the new one to the front of the queue.  The next time updateStatus is called, the new
            // command will be started.
            currentCommand = null;
            nextCommands.add(0, command);
        } else {
            // The command is in the queue and hasn't been started yet.  Just replace it with the other command.
            nextCommands.set(nextCommands.size() - 1, command);
        }
    }

    protected void addSubComponents(Component... subComponents) {
        this.subComponents.addAll(Arrays.asList(subComponents));
    }

    @Override
    public void init() {
        for (Component subComponent : subComponents) {
            //telemetry.log().add("Init SubComponent: " + subComponent);
            //telemetry.update();
            subComponent.init();
        }
    }

    @Override
    public void updateStatus() {

        // If there is not a current command, but there are commands in the queue, start the next one.
        if (currentCommand == null && !nextCommands.isEmpty()) {
            currentCommand = nextCommands.remove(0);
            currentCommand.start();
            commandTime.reset();
        }

        // If there is a current command we are trying to execute, delegate to it for update status
        if (currentCommand != null) {
            boolean finished = currentCommand.updateStatus();

            // If the command is finished, remove it
            if (finished) {
                currentCommand.stop();
                currentCommand = null;
            }
        }

        // Also update any sub-components
        for (Component subComponent : subComponents) {
            subComponent.updateStatus();
        }
    }

    @Override
    public boolean isBusy() {
        // We are busy if any child component is busy.
        for (Component subComponent : subComponents) {
            if (subComponent.isBusy()) {
                return true;
            }
        }

        // We are busy if we have a command we are trying to execute and that command is still busy.
        return currentCommand != null || !nextCommands.isEmpty();
    }

    protected boolean isStopRequested() {
        return opMode instanceof LinearOpMode && ((LinearOpMode) opMode).isStopRequested();
    }

    protected void sleep(long millis) {
        if (opMode instanceof LinearOpMode) {
            LinearOpMode opMode = (LinearOpMode) this.opMode;
            opMode.sleep(millis);
            opMode.idle();
        }
    }

}
