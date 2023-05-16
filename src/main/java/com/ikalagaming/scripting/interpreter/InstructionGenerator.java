package com.ikalagaming.scripting.interpreter;

import com.ikalagaming.scripting.ScriptManager;
import com.ikalagaming.scripting.ast.ASTVisitor;
import com.ikalagaming.scripting.ast.ArgumentList;
import com.ikalagaming.scripting.ast.ArrayAccess;
import com.ikalagaming.scripting.ast.Block;
import com.ikalagaming.scripting.ast.Break;
import com.ikalagaming.scripting.ast.Call;
import com.ikalagaming.scripting.ast.Cast;
import com.ikalagaming.scripting.ast.CompilationUnit;
import com.ikalagaming.scripting.ast.ConstBool;
import com.ikalagaming.scripting.ast.ConstChar;
import com.ikalagaming.scripting.ast.ConstDouble;
import com.ikalagaming.scripting.ast.ConstInt;
import com.ikalagaming.scripting.ast.ConstNull;
import com.ikalagaming.scripting.ast.ConstString;
import com.ikalagaming.scripting.ast.Continue;
import com.ikalagaming.scripting.ast.DoWhile;
import com.ikalagaming.scripting.ast.Exit;
import com.ikalagaming.scripting.ast.ExprArithmetic;
import com.ikalagaming.scripting.ast.ExprArithmetic.Operator;
import com.ikalagaming.scripting.ast.ExprAssign;
import com.ikalagaming.scripting.ast.ExprEquality;
import com.ikalagaming.scripting.ast.ExprLogic;
import com.ikalagaming.scripting.ast.ExprRelation;
import com.ikalagaming.scripting.ast.ExprTernary;
import com.ikalagaming.scripting.ast.FieldAccess;
import com.ikalagaming.scripting.ast.ForLoop;
import com.ikalagaming.scripting.ast.Goto;
import com.ikalagaming.scripting.ast.Identifier;
import com.ikalagaming.scripting.ast.If;
import com.ikalagaming.scripting.ast.Label;
import com.ikalagaming.scripting.ast.LabeledStatement;
import com.ikalagaming.scripting.ast.Node;
import com.ikalagaming.scripting.ast.Parameter;
import com.ikalagaming.scripting.ast.ParameterList;
import com.ikalagaming.scripting.ast.StatementList;
import com.ikalagaming.scripting.ast.SwitchBlockGroup;
import com.ikalagaming.scripting.ast.SwitchLabel;
import com.ikalagaming.scripting.ast.SwitchStatement;
import com.ikalagaming.scripting.ast.Type.Base;
import com.ikalagaming.scripting.ast.TypeNode;
import com.ikalagaming.scripting.ast.VarDeclaration;
import com.ikalagaming.scripting.ast.VarDeclarationList;
import com.ikalagaming.scripting.ast.While;
import com.ikalagaming.util.SafeResourceLoader;

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
	 * Used for generating unique label names.
	 */
	private int nextLabel = 0;

	/**
	 * Where we need to jump to in order to break out of the current, most
	 * proximal, loop.
	 */
	private String breakLabel;

	/**
	 * Where we need to jump to in order to continue with the next iteration of
	 * the current, most proximal, loop.
	 */
	private String continueLabel;

	/**
	 * A temporary list of instructions. We include a bunch of labels, including
	 * temporary ones used for loops and conditionals.
	 */
	private List<Instruction> tempInstructions;

	/**
	 * Calculates the type of instruction to use for assignment, since we can
	 * have shortcuts like "+=", we can use different instructions for
	 * efficiency.
	 *
	 * @param operator The Operator we want to use.
	 * @param numericType The numerical type, assuming this is not a standard
	 *            assignment. Might be null for normal assigns.
	 * @return The instruction type to use.
	 */
	private InstructionType assignmentExpressionInstruction(
		@NonNull ExprAssign.Operator operator, Class<?> numericType) {
		// Yikes
		switch (operator) {
			case ADD_ASSIGN:
				if (numericType == Integer.class) {
					return InstructionType.ADD_INT;
				}
				if (numericType == Double.class) {
					return InstructionType.ADD_DOUBLE;
				}
				if (numericType == Character.class) {
					return InstructionType.ADD_CHAR;
				}
				break;
			case DIV_ASSIGN:
				if (numericType == Integer.class) {
					return InstructionType.DIV_INT;
				}
				if (numericType == Double.class) {
					return InstructionType.DIV_DOUBLE;
				}
				if (numericType == Character.class) {
					return InstructionType.DIV_CHAR;
				}
				break;
			case MOD_ASSIGN:
				if (numericType == Integer.class) {
					return InstructionType.MOD_INT;
				}
				if (numericType == Double.class) {
					return InstructionType.MOD_DOUBLE;
				}
				if (numericType == Character.class) {
					return InstructionType.MOD_CHAR;
				}
				break;
			case MUL_ASSIGN:
				if (numericType == Integer.class) {
					return InstructionType.MUL_INT;
				}
				if (numericType == Double.class) {
					return InstructionType.MUL_DOUBLE;
				}
				if (numericType == Character.class) {
					return InstructionType.MUL_CHAR;
				}
				break;
			case SUB_ASSIGN:
				if (numericType == Integer.class) {
					return InstructionType.SUB_INT;
				}
				if (numericType == Double.class) {
					return InstructionType.SUB_DOUBLE;
				}
				if (numericType == Character.class) {
					return InstructionType.SUB_CHAR;
				}
				break;
			case ASSIGN:
				return InstructionType.MOV;
			default:
				break;
		}
		InstructionGenerator.log
			.warn(SafeResourceLoader.getString("UNKNONW_ASSIGN_OPERATOR",
				ScriptManager.getResourceBundle()), operator.toString());
		return InstructionType.NOP;
	}

	/**
	 * Calculate and emit a jump based on the expression provided. This does not
	 * emit the expression itself, only calculates which jump expression is
	 * appropriate based on what we predict the expression to emit.
	 *
	 * We do this because comparison and jumps are separate instructions,
	 * emitted by different visitors, but we need them to cooperate.
	 *
	 * @param expression Any kind of expression according to the grammar, but
	 *            must evaluate to a boolean.
	 * @param targetLabel Where we are jumping to.
	 */
	private void calculateJump(@NonNull Node expression,
		@NonNull String targetLabel) {
		/*
		 * These are arranged in descending order of how likely I think they are
		 * to show up in practice, because I have to pick an order so I may as
		 * well try to minimize the expected number of checks.
		 */
		if (expression instanceof ExprRelation relation) {
			InstructionType type;
			switch (relation.getOperator()) {
				case GT:
					type = InstructionType.JGT;
					break;
				case GTE:
					type = InstructionType.JGE;
					break;
				case LT:
					type = InstructionType.JLT;
					break;
				case LTE:
					type = InstructionType.JLE;
					break;
				default:
					type = InstructionType.JMP;
					break;
			}
			this.emitJump(type, targetLabel);
			return;
		}
		if (expression instanceof ExprEquality equality) {
			InstructionType type;
			switch (equality.getOperator()) {
				case EQUAL:
					type = InstructionType.JEQ;
					break;
				case NOT_EQUAL:
					type = InstructionType.JNE;
					break;
				default:
					type = InstructionType.JMP;
					break;
			}
			this.emitJump(type, targetLabel);
			return;
		}
		if (expression instanceof ExprLogic || expression instanceof ExprAssign
			|| expression instanceof ExprTernary) {
			// Boolean values

			// Convert the resulting boolean value to a flag, clean the stack
			this.tempInstructions.add(new Instruction(InstructionType.CMP,
				new MemLocation(MemArea.STACK, Boolean.class),
				new MemLocation(MemArea.IMMEDIATE, Boolean.class, true), null));

			this.emitJump(InstructionType.JEQ, targetLabel);
			return;
		}
		InstructionGenerator.log
			.warn(SafeResourceLoader.getString("UNEXPECTED_EXPRESSION",
				ScriptManager.getResourceBundle()), expression.toString());
	}

	/**
	 * Calculate where the value will be found for the given node, as part of an
	 * expression.
	 *
	 * @param node The node we are interested in.
	 * @param clazz The class of the memory location.
	 * @return The location where the contents would be found.
	 */
	private MemLocation calculateLocation(Node node, Class<?> clazz) {
		if (node instanceof ExprArithmetic) {
			return new MemLocation(MemArea.STACK, clazz);
		}
		if (node instanceof ConstBool boolNode) {
			return new MemLocation(MemArea.IMMEDIATE, clazz,
				boolNode.isValue());
		}
		if (node instanceof ConstChar charNode) {
			return new MemLocation(MemArea.IMMEDIATE, clazz,
				charNode.getValue());
		}
		if (node instanceof ConstDouble doubleNode) {
			return new MemLocation(MemArea.IMMEDIATE, clazz,
				doubleNode.getValue());
		}
		if (node instanceof ConstInt intNode) {
			return new MemLocation(MemArea.IMMEDIATE, clazz,
				intNode.getValue());
		}
		if (node instanceof ConstString stringNode) {
			return new MemLocation(MemArea.IMMEDIATE, clazz,
				stringNode.getValue());
		}
		if (node instanceof Identifier identifier) {
			return new MemLocation(MemArea.VARIABLE, clazz,
				identifier.getName());
		}
		InstructionGenerator.log
			.warn(SafeResourceLoader.getString("UNHANDLED_EXPRESSION_MEMBER",
				ScriptManager.getResourceBundle()), node.toString());
		return new MemLocation(MemArea.STACK, clazz);
	}

	/**
	 * Check if a loop contains a break without a label name. We will need a
	 * label to break to after the loop in that case.
	 *
	 * @param loop The loop we are interested in.
	 * @return Whether there is a generic break in that loop.
	 */
	private boolean containsBreak(Node loop) {
		for (Node child : loop.getChildren()) {
			if (child instanceof Break br) {
				return br.getChildren().isEmpty();
			}
			// recurse
			if (this.containsBreak(child)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Emit a temporary jump instruction. The type should be one of the jump
	 * types.
	 *
	 * @param type The instruction type, one of the jumps.
	 * @param target The name of the target label.
	 */
	private void emitJump(@NonNull final InstructionType type,
		@NonNull final String target) {
		this.tempInstructions.add(new Instruction(type,
			new MemLocation(MemArea.IMMEDIATE, String.class, target), null,
			null));
	}

	/**
	 * Emit a label, in a standardized form.
	 *
	 * @param label The name of the label.
	 * @see #getNextLabelName()
	 */
	private void emitLabel(@NonNull final String label) {
		this.tempInstructions.add(new Instruction(InstructionType.NOP,
			new MemLocation(MemArea.IMMEDIATE, String.class, label), null,
			null));
	}

	/**
	 * Return the next label name, and update the value for the next call.
	 *
	 * @return The name for the next valid label.
	 */
	private String getNextLabelName() {
		return String.format(".L%d", this.nextLabel++);
	}

	/**
	 * Calculate which numeric class best represents the math that will be done
	 * involving a node, based on the base of the type of that node.
	 *
	 * @param node The nod ewe are interested in.
	 * @return The class that best represents that node's base type.
	 */
	private Class<?> getNumericBase(Node node) {
		switch (node.getType().getBase()) {
			case CHAR, DOUBLE, INT, STRING:
				return node.getType().getBase().getCorrespondingClass();
			case UNKNOWN:
				// We assume it's an integer, as that's most common
				return Integer.class;
			case BOOLEAN, IDENTIFIER, LABEL, VOID:
			default:
				// Fallback, but the tree verification should(tm) prevent this
				InstructionGenerator.log.warn(
					SafeResourceLoader.getString("INVALID_ARITHMETIC_TYPE",
						ScriptManager.getResourceBundle()),
					node.getType().toString());
				return Integer.class;
		}
	}

	/**
	 * Calculate the instruction type based on the operator.
	 *
	 * @param node The node we are interested in.
	 * @return The type of instruction we are generating.
	 */
	private InstructionType instructionType(ExprArithmetic node) {
		switch (node.getOperator()) {
			case ADD:
				if (node.getType().anyOf(Base.INT)) {
					return InstructionType.ADD_INT;
				}
				else if (node.getType().anyOf(Base.DOUBLE)) {
					return InstructionType.ADD_DOUBLE;
				}
				else if (node.getType().anyOf(Base.CHAR)) {
					return InstructionType.ADD_CHAR;
				}
				else {
					return InstructionType.CONCAT_STRING;
				}
			case DEC_PREFIX, DEC_SUFFIX:
				if (node.getType().anyOf(Base.INT)) {
					return InstructionType.SUB_INT;
				}
				else if (node.getType().anyOf(Base.DOUBLE)) {
					return InstructionType.SUB_DOUBLE;
				}
				else {
					return InstructionType.SUB_CHAR;
				}
			case DIV:
				if (node.getType().anyOf(Base.INT)) {
					return InstructionType.DIV_INT;
				}
				else if (node.getType().anyOf(Base.DOUBLE)) {
					return InstructionType.DIV_DOUBLE;
				}
				else {
					return InstructionType.DIV_CHAR;
				}
			case INC_PREFIX, INC_SUFFIX:
				if (node.getType().anyOf(Base.INT)) {
					return InstructionType.ADD_INT;
				}
				else if (node.getType().anyOf(Base.DOUBLE)) {
					return InstructionType.ADD_DOUBLE;
				}
				else {
					return InstructionType.ADD_CHAR;
				}
			case MOD:
				if (node.getType().anyOf(Base.INT)) {
					return InstructionType.MOD_INT;
				}
				else if (node.getType().anyOf(Base.DOUBLE)) {
					return InstructionType.MOD_DOUBLE;
				}
				else {
					return InstructionType.MOD_CHAR;
				}
			case MUL:
				if (node.getType().anyOf(Base.INT)) {
					return InstructionType.MUL_INT;
				}
				else if (node.getType().anyOf(Base.DOUBLE)) {
					return InstructionType.MUL_DOUBLE;
				}
				else {
					return InstructionType.MUL_CHAR;
				}
			case SUB:
				if (node.getType().anyOf(Base.INT)) {
					return InstructionType.SUB_INT;
				}
				else if (node.getType().anyOf(Base.DOUBLE)) {
					return InstructionType.SUB_DOUBLE;
				}
				else {
					return InstructionType.SUB_CHAR;
				}
			default:
				return InstructionType.NOP;
		}
	}

	/**
	 * Translate an abstract syntax tree to instructions.
	 *
	 * @param ast The tree to process.
	 * @return The list of instructions corresponding to the tree.
	 */
	public List<Instruction> process(@NonNull CompilationUnit ast) {
		this.tempInstructions = new LinkedList<>();

		this.processTree(ast);

		// generate temporary instructions

		return this.processJumps();
	}

	private List<Instruction> processJumps() {
		List<Instruction> result = new ArrayList<>();

		// calculate jump locations for labels, conditionals, etc

		/**
		 * The number of the instruction each label actually refers to.
		 */
		Map<String, Integer> labelLocations = new HashMap<>();
		/**
		 * Stores labels we have passed, but not yet assigned a jump location
		 * to. We might have multiple labels in a row between instructions.
		 */
		List<String> currentLabels = new ArrayList<>();
		/**
		 * The actual instruction number we are on, ignoring labels.
		 */
		int instructionCount = 0;
		for (Instruction i : this.tempInstructions) {
			if (i.type() == InstructionType.NOP) {
				// Label
				currentLabels.add((String) i.firstLocation().value());
				// Strip out labels by just not copying them over
				continue;
			}
			if (!currentLabels.isEmpty()) {
				for (String label : currentLabels) {
					labelLocations.put(label, instructionCount);
				}
				currentLabels.clear();
			}
			result.add(i);
			++instructionCount;
		}

		// Handle labels at the end of the program
		if (!currentLabels.isEmpty()) {
			for (String label : currentLabels) {
				labelLocations.put(label, instructionCount);
			}
			currentLabels.clear();
		}

		// Replace jumps label field with relative locations
		for (int i = 0; i < result.size(); ++i) {
			Instruction current = result.get(i);
			final InstructionType type = current.type();

			if (type == InstructionType.JEQ || type == InstructionType.JNE
				|| type == InstructionType.JGE || type == InstructionType.JGT
				|| type == InstructionType.JLE || type == InstructionType.JLT
				|| type == InstructionType.JMP) {
				// Look up the jump target
				Instruction replacement = new Instruction(current.type(),
					new MemLocation(MemArea.IMMEDIATE, Integer.class,
						labelLocations.get(current.firstLocation().value())),
					null, null);
				result.set(i, replacement);
			}
		}
		return result;
	}

	private void processTree(Node node) {
		if (node instanceof ForLoop || node instanceof VarDeclaration
			|| node instanceof Goto || node instanceof ExprAssign
			|| node instanceof While || node instanceof DoWhile) {
			/*
			 * Skip processing children because the visitor handles processing
			 * them, or they should be skipped.
			 */
		}
		else if (node instanceof ExprArithmetic || node instanceof ExprEquality
			|| node instanceof ExprRelation) {
			// Reverse order
			for (int i = node.getChildren().size() - 1; i >= 0; --i) {
				this.processTree(node.getChildren().get(i));
			}
		}
		else {
			// Forward order
			for (Node child : node.getChildren()) {
				if (node instanceof CompilationUnit
					&& (child instanceof ExprArithmetic expr)) {
					switch (expr.getOperator()) {
						case DEC_PREFIX, DEC_SUFFIX, INC_PREFIX, INC_SUFFIX:
							expr.setIgnoreResult(true);
							break;
						case ADD, DIV, MOD, MUL, SUB:
						default:
							break;
					}
				}

				this.processTree(child);
			}
		}
		node.process(this);
	}

	@Override
	public void visit(ArgumentList node) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(ArrayAccess node) {
		// TODO Auto-generated method stub
		// Push the array access to the stack to be read or written to
	}

	@Override
	public void visit(Block node) {
		// Not required
	}

	@Override
	public void visit(Break node) {
		emitJump(InstructionType.JMP, breakLabel);
	}

	@Override
	public void visit(Call node) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(Cast node) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(CompilationUnit node) {
		// Not required
	}

	@Override
	public void visit(ConstBool node) {
		this.tempInstructions.add(new Instruction(InstructionType.MOV,
			new MemLocation(MemArea.IMMEDIATE, Boolean.class, node.isValue()),
			null, new MemLocation(MemArea.STACK, Boolean.class)));
	}

	@Override
	public void visit(ConstChar node) {
		this.tempInstructions.add(new Instruction(InstructionType.MOV,
			new MemLocation(MemArea.IMMEDIATE, Character.class,
				node.getValue()),
			null, new MemLocation(MemArea.STACK, Character.class)));
	}

	@Override
	public void visit(ConstDouble node) {
		this.tempInstructions.add(new Instruction(InstructionType.MOV,
			new MemLocation(MemArea.IMMEDIATE, Double.class, node.getValue()),
			null, new MemLocation(MemArea.STACK, Double.class)));
	}

	@Override
	public void visit(ConstInt node) {
		this.tempInstructions.add(new Instruction(InstructionType.MOV,
			new MemLocation(MemArea.IMMEDIATE, Integer.class, node.getValue()),
			null, new MemLocation(MemArea.STACK, Integer.class)));
	}

	@Override
	public void visit(ConstNull node) {
		this.tempInstructions.add(new Instruction(InstructionType.MOV,
			new MemLocation(MemArea.IMMEDIATE, Object.class, null), null,
			new MemLocation(MemArea.STACK, Object.class)));
	}

	@Override
	public void visit(ConstString node) {
		this.tempInstructions.add(new Instruction(InstructionType.MOV,
			new MemLocation(MemArea.IMMEDIATE, String.class, node.getValue()),
			null, new MemLocation(MemArea.STACK, String.class)));
	}

	@Override
	public void visit(Continue node) {
		emitJump(InstructionType.JMP, continueLabel);
	}

	@Override
	public void visit(DoWhile node) {
		Node body = node.getChildren().get(0);
		Node conditional = node.getChildren().get(1);

		final String topOfLoopLabel = this.getNextLabelName();
		final String conditionLabel = this.getNextLabelName();
		this.continueLabel = conditionLabel;

		final boolean containsBreak = this.containsBreak(body);
		if (containsBreak) {
			this.breakLabel = this.getNextLabelName();
		}

		this.emitLabel(topOfLoopLabel);

		this.processTree(body);

		this.processTree(conditional);
		this.calculateJump(conditional, topOfLoopLabel);

		if (containsBreak) {
			this.emitLabel(this.breakLabel);
		}
	}

	@Override
	public void visit(Exit node) {
		this.tempInstructions
			.add(new Instruction(InstructionType.HALT, null, null, null));
	}

	@Override
	public void visit(ExprArithmetic node) {
		Class<?> clazz = this.getNumericBase(node);

		InstructionType type = this.instructionType(node);

		MemLocation first =
			this.calculateLocation(node.getChildren().get(0), clazz);
		MemLocation second = null;
		switch (node.getOperator()) {
			case ADD, DIV, MOD, MUL, SUB:
				second =
					this.calculateLocation(node.getChildren().get(1), clazz);
				break;
			case DEC_PREFIX, DEC_SUFFIX, INC_PREFIX, INC_SUFFIX:
				second = new MemLocation(MemArea.IMMEDIATE, clazz,
					node.getUnaryCount());
				break;
			default:
				type = InstructionType.NOP;
				break;
		}

		if (node.getOperator().equals(Operator.DEC_PREFIX)
			|| node.getOperator().equals(Operator.INC_PREFIX)) {
			this.tempInstructions
				.add(new Instruction(type, first, second, first));
			if (!node.isIgnoreResult()) {
				// Copy the post-increment value onto the stack
				this.tempInstructions.add(new Instruction(InstructionType.MOV,
					first, null, new MemLocation(MemArea.STACK, clazz)));
			}
		}
		else if (node.getOperator().equals(Operator.DEC_SUFFIX)
			|| node.getOperator().equals(Operator.INC_SUFFIX)) {
			if (!node.isIgnoreResult()) {
				// Push the pre-increment value onto the stack
				this.tempInstructions.add(new Instruction(InstructionType.MOV,
					first, null, new MemLocation(MemArea.STACK, clazz)));
			}
			// Update the value by however much, in place
			this.tempInstructions
				.add(new Instruction(type, first, second, first));
		}
		else {
			// Normal binary operator
			this.tempInstructions.add(new Instruction(type, first, second,
				new MemLocation(MemArea.STACK, clazz)));
		}
	}

	@Override
	public void visit(ExprAssign node) {
		final Node leftSide = node.getChildren().get(0);
		final Node rightSide = node.getChildren().get(1);
		this.processTree(rightSide);

		Class<?> numericType = null;
		if (node.getOperator() != ExprAssign.Operator.ASSIGN) {
			numericType = this.getNumericBase(leftSide);
		}

		InstructionType instruction = this
			.assignmentExpressionInstruction(node.getOperator(), numericType);

		MemLocation first = new MemLocation(MemArea.STACK,
			rightSide.getType().getBase().getCorrespondingClass());

		Class<?> clazz = node.getType().getBase().getCorrespondingClass();

		MemLocation target = null;
		// Determine if the left side is a variable, field access, array access
		if (leftSide instanceof Identifier identifier) {
			target =
				new MemLocation(MemArea.VARIABLE, clazz, identifier.getName());
		}
		else if (leftSide instanceof FieldAccess field) {
			Identifier id = (Identifier) field.getChildren().get(1);
			target = new MemLocation(MemArea.VARIABLE, clazz, id.getName());
			this.processTree(leftSide);
		}
		else if (leftSide instanceof ArrayAccess) {
			target = new MemLocation(MemArea.ARRAY, clazz);
			this.processTree(leftSide);
		}
		else {
			// Impossible due to grammar.
			InstructionGenerator.log
				.warn(
					SafeResourceLoader.getString("UNKNOWN_ASSIGN_LEFT_SIDE",
						ScriptManager.getResourceBundle()),
					leftSide.toString());
			target = new MemLocation(MemArea.IMMEDIATE, Void.class);
		}

		MemLocation second = null;
		if (node.getOperator() != ExprAssign.Operator.ASSIGN) {
			// Handle somewhat in-place modification
			// We want to do target _= right -> target for whatever operator _
			second =
				new MemLocation(target.area(), target.type(), target.value());
		}

		this.tempInstructions
			.add(new Instruction(instruction, first, second, target));
	}

	@Override
	public void visit(ExprEquality node) {
		// TODO use jumps or sets instead of pushing to the stack
		// This happens on ExprLogic, for loop, and ifs
		/*
		 * We reverse the order of child parsing so the order here makes sense.
		 * This is largely irrelevant, as it's commutative, but should be noted.
		 */
		Node left = node.getChildren().get(0);
		Node right = node.getChildren().get(1);

		/*
		 * We reverse the order of child parsing so the order here makes sense.
		 */
		MemLocation first;
		if (left instanceof Identifier leftID) {
			first = new MemLocation(MemArea.VARIABLE,
				left.getType().getBase().getCorrespondingClass(),
				leftID.getName());
		}
		else {
			first = new MemLocation(MemArea.STACK,
				left.getType().getBase().getCorrespondingClass());
		}
		MemLocation second;

		if (right instanceof Identifier rightID) {
			second = new MemLocation(MemArea.VARIABLE,
				left.getType().getBase().getCorrespondingClass(),
				rightID.getName());
		}
		else {
			second = new MemLocation(MemArea.STACK,
				left.getType().getBase().getCorrespondingClass());
		}

		this.tempInstructions
			.add(new Instruction(InstructionType.CMP, first, second, null));
	}

	@Override
	public void visit(ExprLogic node) {
		// TODO Auto-generated method stub
		// TODO handle
	}

	@Override
	public void visit(ExprRelation node) {
		// TODO Auto-generated method stub
		// TODO use jumps or sets instead of pushing to the stack
		// This happens on ExprLogic, for loop, and ifs

		Node left = node.getChildren().get(0);
		Node right = node.getChildren().get(1);

		/*
		 * We reverse the order of child parsing so the order here makes sense.
		 */
		MemLocation first;
		if (left instanceof Identifier leftID) {
			first = new MemLocation(MemArea.VARIABLE,
				left.getType().getBase().getCorrespondingClass(),
				leftID.getName());
		}
		else {
			first = new MemLocation(MemArea.STACK,
				left.getType().getBase().getCorrespondingClass());
		}
		MemLocation second;

		if (right instanceof Identifier rightID) {
			second = new MemLocation(MemArea.VARIABLE,
				left.getType().getBase().getCorrespondingClass(),
				rightID.getName());
		}
		else {
			second = new MemLocation(MemArea.STACK,
				left.getType().getBase().getCorrespondingClass());
		}

		this.tempInstructions
			.add(new Instruction(InstructionType.CMP, first, second, null));
	}

	@Override
	public void visit(ExprTernary node) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(FieldAccess node) {
		// TODO Auto-generated method stub
		// Push the field access to the stack to be read or written to
	}

	@Override
	public void visit(ForLoop node) {
		int position = 0;

		Node init = null;
		Node condition = null;
		Node update = null;
		if (node.isInitializer()) {
			init = node.getChildren().get(position);
			++position;
		}
		if (node.isCondition()) {
			condition = node.getChildren().get(position);
			++position;
		}
		if (node.isUpdate()) {
			update = node.getChildren().get(position);
			++position;
			if (update instanceof ExprArithmetic expr) {
				// Handle post/pre-fix expressions being thrown away
				expr.setIgnoreResult(true);
			}
		}
		Node body = node.getChildren().get(position);

		final String topOfLoopLabel = this.getNextLabelName();
		final String conditionLabel = this.getNextLabelName();

		final boolean containsBreak = this.containsBreak(body);
		if (containsBreak) {
			this.breakLabel = this.getNextLabelName();
		}

		this.continueLabel = conditionLabel;

		if (init != null) {
			this.processTree(init);
		}
		this.emitJump(InstructionType.JMP, conditionLabel);
		this.emitLabel(topOfLoopLabel);
		this.processTree(body);
		if (update != null) {
			this.processTree(update);
		}
		this.emitLabel(conditionLabel);
		if (condition != null) {
			this.processTree(condition);
			this.calculateJump(condition, topOfLoopLabel);
		}
		else {
			this.emitJump(InstructionType.JMP, topOfLoopLabel);
		}
		if (containsBreak) {
			this.emitLabel(this.breakLabel);
		}
	}

	@Override
	public void visit(Goto node) {
		Identifier target = (Identifier) node.getChildren().get(0);
		this.emitJump(InstructionType.JMP, target.getName());
	}

	@Override
	public void visit(Identifier node) {
		this.tempInstructions.add(new Instruction(InstructionType.NOP,
			new MemLocation(MemArea.VARIABLE, String.class, node.getName()),
			null, new MemLocation(MemArea.STACK,
				node.getType().getBase().getCorrespondingClass())));
	}

	@Override
	public void visit(If node) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(Label node) {
		this.tempInstructions.add(new Instruction(InstructionType.NOP,
			new MemLocation(MemArea.IMMEDIATE, String.class, node.getName()),
			null, null));
	}

	@Override
	public void visit(LabeledStatement node) {
		// Not required
	}

	@Override
	public void visit(Parameter node) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(ParameterList node) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(StatementList node) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(SwitchBlockGroup node) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(SwitchLabel node) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(SwitchStatement node) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(TypeNode node) {
		// Not required
	}

	@Override
	public void visit(VarDeclaration node) {
		final int dims = node.getDimensions();

		Identifier id = (Identifier) node.getChildren().get(0);
		final String varName = id.getName();

		Class<?> clazz;
		switch (node.getType().getBase()) {
			case BOOLEAN:
				clazz = Boolean.class;
				break;
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
			case UNKNOWN, IDENTIFIER:
				clazz = Object.class;
				break;
			case LABEL, VOID:
			default:
				InstructionGenerator.log.warn(
					SafeResourceLoader.getString("INVALID_MEMORY_TYPE",
						ScriptManager.getResourceBundle()),
					node.getType().getBase());
				clazz = Object.class;
		}

		MemLocation target = new MemLocation(MemArea.VARIABLE, clazz, varName);
		MemLocation defaultValue;

		if (node.getChildren().size() > 1) {
			this.processTree(node.getChildren().get(1));
			defaultValue = new MemLocation(MemArea.STACK, clazz);
		}
		else if (dims == 0) {
			// Check for primitives, store defaults

			if (node.getType().anyOf(Base.CHAR, Base.INT, Base.DOUBLE)) {
				defaultValue = new MemLocation(MemArea.IMMEDIATE, clazz, 0);
			}
			else if (node.getType().anyOf(Base.BOOLEAN)) {
				defaultValue = new MemLocation(MemArea.IMMEDIATE, clazz, false);
			}
			else if (node.getType().anyOf(Base.STRING)) {
				defaultValue = new MemLocation(MemArea.IMMEDIATE, clazz, "");
			}
			else {
				defaultValue = new MemLocation(MemArea.IMMEDIATE, clazz, null);
			}
		}
		else {
			defaultValue = new MemLocation(MemArea.IMMEDIATE, clazz, null);
		}
		this.tempInstructions.add(
			new Instruction(InstructionType.MOV, defaultValue, null, target));
	}

	@Override
	public void visit(VarDeclarationList node) {
		// Not required
	}

	@Override
	public void visit(While node) {
		Node conditional = node.getChildren().get(0);
		Node body = node.getChildren().get(1);

		final String topOfLoopLabel = this.getNextLabelName();
		final String conditionLabel = this.getNextLabelName();
		this.continueLabel = conditionLabel;

		final boolean containsBreak = this.containsBreak(body);
		if (containsBreak) {
			this.breakLabel = this.getNextLabelName();
		}

		this.emitJump(InstructionType.JMP, conditionLabel);
		this.emitLabel(topOfLoopLabel);

		this.processTree(body);

		this.emitLabel(conditionLabel);
		this.processTree(conditional);
		this.calculateJump(conditional, topOfLoopLabel);

		if (containsBreak) {
			this.emitLabel(this.breakLabel);
		}
	}

}
