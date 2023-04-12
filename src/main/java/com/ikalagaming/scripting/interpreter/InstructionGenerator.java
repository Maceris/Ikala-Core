package com.ikalagaming.scripting.interpreter;

import com.ikalagaming.scripting.ast.ASTVisitor;
import com.ikalagaming.scripting.ast.CompilationUnit;
import com.ikalagaming.scripting.ast.ConstInt;
import com.ikalagaming.scripting.ast.ExprArithmetic;
import com.ikalagaming.scripting.ast.ExprArithmetic.Operator;
import com.ikalagaming.scripting.ast.Label;
import com.ikalagaming.scripting.ast.Node;
import com.ikalagaming.scripting.ast.Type.Base;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Utility class for transforming an abstract syntax tree into instructions.
 * 
 * @author Ches Burks
 *
 */
@Slf4j
public class InstructionGenerator implements ASTVisitor {

	/**
	 * Instructions that might be decorated with a label.
	 * 
	 * @param instruction The instruction.
	 * @param label The label for that instruction, or null if one does not
	 *            exist.
	 */
	private record LabeledInstruction(@NonNull Instruction instruction,
		String label) {}

	/**
	 * Used for generating unique jump target names.
	 */
	private int jumpTargetCount = 0;

	/**
	 * 
	 * Maps nodes to the corresponding instructions in the
	 */
	Map<Node, List<Instruction>> instructionMap = new HashMap<>();

	/**
	 * A map of names to the actual label node that corresponds to that name.
	 */
	Map<String, Node> labelMap = new HashMap<>();

	List<Instruction> tempInstructions = new ArrayList<>();

	public List<Instruction> process(@NonNull CompilationUnit ast) {
		/*
		 * Temporary instructions are instructions, but jumps store Nodes as the
		 * object. Then in a second pass, we look up the instruction index where
		 * those nodes should actually jump to.
		 */
		List<Instruction> tempInstructions2 = new LinkedList<>();

		processLabels(ast);

		this.processTree(ast);

		tempInstructions.forEach(System.out::println);

		// generate temporary instructions

		return processJumps(tempInstructions2);
	}

	private void processTree(Node node) {
		if (node instanceof ExprArithmetic) {
			// Reverse order
			for (int i = node.getChildren().size() - 1; i >= 0; --i) {
				this.processTree(node.getChildren().get(i));
			}
		}
		else {
			// Forward order
			for (Node child : node.getChildren()) {
				this.processTree(child);
			}
		}

		node.process(this);
	}

	@Override
	public void visit(ExprArithmetic node) {
		InstructionType type;
		MemLocation second = null;
		Class<?> clazz;
		switch (node.getType().getBase()) {
			case CHAR:
				clazz = Character.class;
				break;
			case DOUBLE:
				clazz = Double.class;
				break;
			case INT:
				clazz = Integer.class;
				break;
			case STRING:
				clazz = String.class;
				break;
			case UNKNOWN:
				// We assume it's an integer, as that's most common
				clazz = Integer.class;
				break;
			case BOOLEAN:
			case IDENTIFIER:
			case LABEL:
			case VOID:
			default:
				// Fallback, but the tree verification should(tm) prevent this
				clazz = Integer.class;
				log.warn("Trying to add nodes to get a {}",
					node.getType().toString());
				break;
		}
		switch (node.getOperator()) {
			case ADD:
				if (node.getType().anyOf(Base.INT)) {
					type = InstructionType.ADD_INT;
				}
				else if (node.getType().anyOf(Base.DOUBLE)) {
					type = InstructionType.ADD_DOUBLE;
				}
				else if (node.getType().anyOf(Base.CHAR)) {
					type = InstructionType.ADD_CHAR;
				}
				else {
					type = InstructionType.CONCAT_STRING;
				}
				second = new MemLocation(MemArea.STACK, clazz);
				break;
			case DEC_PREFIX:
			case DEC_SUFFIX:
				if (node.getType().anyOf(Base.INT)) {
					type = InstructionType.SUB_INT;
				}
				else if (node.getType().anyOf(Base.DOUBLE)) {
					type = InstructionType.SUB_DOUBLE;
				}
				else {
					type = InstructionType.SUB_CHAR;
				}
				second = new MemLocation(MemArea.IMMEDIATE, clazz,
					node.getUnaryCount());
				break;
			case DIV:
				if (node.getType().anyOf(Base.INT)) {
					type = InstructionType.DIV_INT;
				}
				else if (node.getType().anyOf(Base.DOUBLE)) {
					type = InstructionType.DIV_DOUBLE;
				}
				else {
					type = InstructionType.DIV_CHAR;
				}
				second = new MemLocation(MemArea.STACK, clazz);
				break;
			case INC_PREFIX:
			case INC_SUFFIX:
				if (node.getType().anyOf(Base.INT)) {
					type = InstructionType.ADD_INT;
				}
				else if (node.getType().anyOf(Base.DOUBLE)) {
					type = InstructionType.ADD_DOUBLE;
				}
				else {
					type = InstructionType.ADD_CHAR;
				}
				second = new MemLocation(MemArea.IMMEDIATE, clazz,
					node.getUnaryCount());
				break;
			case MOD:
				if (node.getType().anyOf(Base.INT)) {
					type = InstructionType.MOD_INT;
				}
				else if (node.getType().anyOf(Base.DOUBLE)) {
					type = InstructionType.MOD_DOUBLE;
				}
				else {
					type = InstructionType.MOD_CHAR;
				}
				second = new MemLocation(MemArea.STACK, clazz);
				break;
			case MUL:
				if (node.getType().anyOf(Base.INT)) {
					type = InstructionType.MUL_INT;
				}
				else if (node.getType().anyOf(Base.DOUBLE)) {
					type = InstructionType.MUL_DOUBLE;
				}
				else {
					type = InstructionType.MUL_CHAR;
				}
				second = new MemLocation(MemArea.STACK, clazz);
				break;
			case SUB:
				if (node.getType().anyOf(Base.INT)) {
					type = InstructionType.SUB_INT;
				}
				else if (node.getType().anyOf(Base.DOUBLE)) {
					type = InstructionType.SUB_DOUBLE;
				}
				else {
					type = InstructionType.SUB_CHAR;
				}
				second = new MemLocation(MemArea.STACK, clazz);
				break;
			default:
				type = InstructionType.NOP;
				break;
		}
		if (node.getOperator().equals(Operator.DEC_SUFFIX)
			|| node.getOperator().equals(Operator.INC_SUFFIX)) {
			// TODO don't push, instead push the previous value

		}
		else {
			// TODO include immediate values if constants
			tempInstructions.add(
				new Instruction(type, new MemLocation(MemArea.STACK, clazz),
					second, new MemLocation(MemArea.STACK, clazz)));
		}
	}

	@Override
	public void visit(ConstInt node) {
		tempInstructions.add(new Instruction(InstructionType.MOV,
			new MemLocation(MemArea.IMMEDIATE, Integer.class, node.getValue()),
			null, new MemLocation(MemArea.STACK, Integer.class)));
	}

	private List<Instruction> processJumps(List<Instruction> tempResult) {
		// TODO
		// calculate jump locations for labels, conditionals, etc
		// Generate actual instructions
		return tempResult;
	}

	/**
	 * Find all the labels, put them in a map for quick reference.
	 * 
	 * @param root The root of the tree to look through.
	 */
	private void processLabels(Node root) {
		if (root.getChildren().size() > 0) {
			for (Node child : root.getChildren()) {
				processLabels(child);
			}
		}
		if (root instanceof Label) {
			labelMap.put(((Label) root).getName(), root);
		}
	}
}
