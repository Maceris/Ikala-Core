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
import com.ikalagaming.scripting.ast.EnhancedForLoop;
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

import java.util.LinkedList;
import java.util.List;

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
	 * A temporary list of instructions. We include a bunch of labels, including
	 * temporary ones used for loops and conditionals.
	 */
	private List<Instruction> tempInstructions;

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
			this.tempInstructions.add(new Instruction(InstructionType.TEST,
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

		return this.processJumps(this.tempInstructions);
	}

	private List<Instruction> processJumps(List<Instruction> tempResult) {
		// TODO
		// calculate jump locations for labels, conditionals, etc
		// Strip out labels
		// Replace jumps label field with relative locations
		return tempResult;
	}

	private void processTree(Node node) {
		if (node instanceof ForLoop) {
			/*
			 * Skip processing children because the visitor handles processing
			 * them.
			 */
		}
		else if (node instanceof ExprArithmetic) {
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
	}

	@Override
	public void visit(Block node) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(Break node) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(ConstBool node) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(ConstChar node) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(ConstDouble node) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(ConstInt node) {
		this.tempInstructions.add(new Instruction(InstructionType.MOV,
			new MemLocation(MemArea.IMMEDIATE, Integer.class, node.getValue()),
			null, new MemLocation(MemArea.STACK, Integer.class)));
	}

	@Override
	public void visit(ConstNull node) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(ConstString node) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(Continue node) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(DoWhile node) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(EnhancedForLoop node) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(Exit node) {
		this.tempInstructions
			.add(new Instruction(InstructionType.HALT, null, null, null));
	}

	@Override
	public void visit(ExprArithmetic node) {
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
			case BOOLEAN, IDENTIFIER, LABEL, VOID:
			default:
				// Fallback, but the tree verification should(tm) prevent this
				clazz = Integer.class;
				InstructionGenerator.log.warn(
					SafeResourceLoader.getString("INVALID_ARITHMETIC_TYPE",
						ScriptManager.getResourceBundle()),
					node.getType().toString());
				break;
		}

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
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(ExprEquality node) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(ExprLogic node) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(ExprRelation node) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(ExprTernary node) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(FieldAccess node) {
		// TODO Auto-generated method stub
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

		if (init != null) {
			this.processTree(init);
		}
		this.emitJump(InstructionType.JMP, conditionLabel);
		this.emitLabel(topOfLoopLabel);
		if (body != null) {
			this.processTree(body);
		}
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
	}

	@Override
	public void visit(Goto node) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(Identifier node) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(VarDeclaration node) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(VarDeclarationList node) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(While node) {
		// TODO Auto-generated method stub
	}

}
