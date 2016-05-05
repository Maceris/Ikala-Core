package com.ikalagaming.packages;

/**
 * A command that has been registered with the system. This contains a string
 * representing the command and a reference to the owner of the command.
 *
 * @author Ches Burks
 *
 */
public class PackageCommand implements Comparable<PackageCommand> {
	private final String command;
	private final Package owner;

	/**
	 * Constructs a new registered command with the given command and package
	 *
	 * @param cmd the command registered
	 * @param theOwner what package registered the command
	 */
	public PackageCommand(final String cmd, final Package theOwner) {
		this.command = cmd;
		this.owner = theOwner;
	}

	@Override
	public int compareTo(PackageCommand o) {
		return this.getCommand().toLowerCase()
				.compareTo(o.getCommand().toLowerCase());
	}

	/**
	 * Returns the command
	 *
	 * @return the command
	 */
	public String getCommand() {
		return this.command;
	}

	/**
	 * Returns the package that registered this command
	 *
	 * @return the owner
	 */
	public Package getOwner() {
		return this.owner;
	}

}
