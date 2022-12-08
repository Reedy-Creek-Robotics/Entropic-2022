package org.firstinspires.ftc.teamcode.components;

public interface CombinableCommand extends Command {

    /**
     * Gives this command the opportunity to combine itself with the given command.
     *
     * If combination is possible, this method should return the newly combined command that will replace both this
     * one and the other.
     *
     * If combination is not possible, this method should return null.
     */
    Command combineWith(Command other);

}
