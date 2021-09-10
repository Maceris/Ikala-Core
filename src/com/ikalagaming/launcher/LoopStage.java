package com.ikalagaming.launcher;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import java.util.UUID;
import java.util.function.IntSupplier;

/**
 * A step in the core engine loop that we want to execute.
 *
 * @author Ches Burks
 *
 */
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class LoopStage {
	private IntSupplier method;
	@Getter
	@EqualsAndHashCode.Include
	private UUID id;

	/**
	 * Create a new loop stage given the method to execute.
	 *
	 * @param method The method to run for this stage.
	 */
	public LoopStage(@NonNull IntSupplier method) {
		this.method = method;
		this.id = UUID.randomUUID();
	}

	/**
	 * Executes the stage and returns the result.
	 *
	 * @return The stage to execute.
	 */
	public int execute() {
		return this.method.getAsInt();
	}

}
