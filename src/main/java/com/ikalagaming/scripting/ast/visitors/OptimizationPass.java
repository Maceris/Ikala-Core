package com.ikalagaming.scripting.ast.visitors;

import com.ikalagaming.scripting.ast.ASTVisitor;
import com.ikalagaming.scripting.ast.CompilationUnit;
import com.ikalagaming.scripting.ast.ConstChar;
import com.ikalagaming.scripting.ast.ConstDouble;
import com.ikalagaming.scripting.ast.ConstInt;
import com.ikalagaming.scripting.ast.ExprArithmetic;
import com.ikalagaming.scripting.ast.ForLoop;
import com.ikalagaming.scripting.ast.Node;
import com.ikalagaming.scripting.ast.Type;
import com.ikalagaming.scripting.ast.Type.Base;
import com.ikalagaming.scripting.ast.TypeNode;
import com.ikalagaming.scripting.ast.VarDeclaration;
import com.ikalagaming.scripting.ast.VarDeclarationList;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Optimize some of the tree.
 *
 * @author Ches Burks
 *
 */
@Slf4j
public class OptimizationPass implements ASTVisitor {

	private void findConstants(Node node) {
		for (Node child : node.getChildren()) {
			this.findConstants(child);
		}

		TypeNode type;
		List<VarDeclaration> variables = new ArrayList<>();

		if (node instanceof VarDeclarationList) {
			VarDeclarationList declList = (VarDeclarationList) node;
			type = (TypeNode) declList.getChildren().get(0);
			for (int i = 1; i < declList.getChildren().size(); ++i) {
				variables.add((VarDeclaration) declList.getChildren().get(i));
			}
		}
		else if (node instanceof ForLoop) {
			ForLoop forLoop = (ForLoop) node;
			if (!forLoop.isInitializer()) {
				return;
			}

			Node initializer = forLoop.getChildren().get(0);
			if (initializer instanceof VarDeclarationList) {
				VarDeclarationList declList = (VarDeclarationList) initializer;
				type = (TypeNode) declList.getChildren().get(0);
				for (int i = 1; i < declList.getChildren().size(); ++i) {
					variables
						.add((VarDeclaration) declList.getChildren().get(i));
				}
			}
			else {
				return;
			}
		}
		else {
			return;
		}

		if (!type.isFinal()) {
		}
		// TODO note down variables and their types
	}

	/**
	 * Optimize the syntax tree.
	 *
	 * @param ast The tree to validate.
	 */
	public void optimize(CompilationUnit ast) {
		this.processTree(ast);
	}

	/**
	 * Process the tree recursively.
	 *
	 * @param node The node we are processing.
	 */
	private void processTree(Node node) {
		for (int i = 0; i < node.getChildren().size(); ++i) {
			Node child = node.getChildren().get(i);
			this.processTree(child);
			if (child instanceof ExprArithmetic) {
				/*
				 * We might need to replace this node with a constant after
				 * processing the children. This will recursively calculate
				 * constant expressions and allows final variable replacements.
				 */
				// replace the node
				node.getChildren().set(i,
					this.simplify((ExprArithmetic) child));
			}
		}
		node.process(this);
	}

	/**
	 * Simplify an arithmetic expression, into a constant if its children are
	 * also constants.
	 *
	 * @param node The node we are simplifying.
	 * @return The resulting node, which might be the same node if it's not a
	 *         constant expression.
	 */
	private Node simplify(ExprArithmetic node) {
		if (node.getChildren().size() == 1) {
			return node;
		}

		final Node firstChild = node.getChildren().get(0);
		final Node secondChild = node.getChildren().get(1);

		if (firstChild.getType().anyOf(Base.UNKNOWN)
			|| secondChild.getType().anyOf(Base.UNKNOWN)
			|| !((firstChild instanceof ConstInt
				|| firstChild instanceof ConstDouble
				|| firstChild instanceof ConstChar)
				&& (secondChild instanceof ConstInt
					|| secondChild instanceof ConstDouble
					|| secondChild instanceof ConstChar))) {
			// Bail if it's not the case that both children are constants
			return node;
		}

		if (node.getType().anyOf(Base.INT)) {
			return this.simplifyInt(node);
		}
		if (node.getType().anyOf(Base.DOUBLE)) {
			return this.simplifyDouble(node);
		}
		if (node.getType().anyOf(Base.CHAR)) {
			return this.simplifyChar(node);
		}

		return node;
	}

	/**
	 * Simplify a char expression.
	 *
	 * @param node The node we are simplifying.
	 * @return The resulting value.
	 */
	private Node simplifyChar(ExprArithmetic node) {
		final Node firstChild = node.getChildren().get(0);
		final Node secondChild = node.getChildren().get(1);

		char firstValue;
		char secondValue;

		if (firstChild instanceof ConstInt) {
			int value = ((ConstInt) firstChild).getValue();
			// Good luck
			firstValue = (char) value;
		}
		else if (firstChild instanceof ConstDouble) {
			double value = ((ConstDouble) firstChild).getValue();
			// Good luck
			firstValue = (char) value;
		}
		else if (firstChild instanceof ConstChar) {
			char value = ((ConstChar) firstChild).getValue();
			// Already a char
			firstValue = value;
		}
		else {
			OptimizationPass.log.warn("Invalid constant {}",
				firstChild.toString());
			return node;
		}

		if (secondChild instanceof ConstInt) {
			int value = ((ConstInt) secondChild).getValue();
			// Good luck
			secondValue = (char) value;
		}
		else if (secondChild instanceof ConstDouble) {
			double value = ((ConstDouble) secondChild).getValue();
			// Good luck
			secondValue = (char) value;
		}
		else if (secondChild instanceof ConstChar) {
			char value = ((ConstChar) secondChild).getValue();
			// Already a char
			secondValue = value;
		}
		else {
			OptimizationPass.log.warn("Invalid constant {}",
				secondChild.toString());
			return node;
		}

		ConstChar result = new ConstChar();
		result.setType(Type.primitive(Base.CHAR));

		switch (node.getOperator()) {
			case DEC_PREFIX:
			case DEC_SUFFIX:
			case INC_PREFIX:
			case INC_SUFFIX:
			default:
				// Can't happen, but let's cover it anyway
				OptimizationPass.log.warn("Can't use operator {} on a {}",
					node.getOperator().toString(),
					firstChild.getClass().getSimpleName());
				return node;
			case ADD:
				result.setValue((char) (firstValue + secondValue));
				break;
			case DIV:
				result.setValue((char) (firstValue / secondValue));
				break;
			case MOD:
				result.setValue((char) (firstValue % secondValue));
				break;
			case MUL:
				result.setValue((char) (firstValue * secondValue));
				break;
			case SUB:
				result.setValue((char) (firstValue - secondValue));
				break;
		}

		return result;
	}

	/**
	 * Simplify a double expression.
	 *
	 * @param node The node we are simplifying.
	 * @return The resulting value.
	 */
	private Node simplifyDouble(ExprArithmetic node) {
		final Node firstChild = node.getChildren().get(0);
		final Node secondChild = node.getChildren().get(1);

		double firstValue;
		double secondValue;

		if (firstChild instanceof ConstInt) {
			int value = ((ConstInt) firstChild).getValue();
			// Fits fine
			firstValue = value;
		}
		else if (firstChild instanceof ConstDouble) {
			double value = ((ConstDouble) firstChild).getValue();
			// Already a double
			firstValue = value;
		}
		else if (firstChild instanceof ConstChar) {
			char value = ((ConstChar) firstChild).getValue();
			// Fits fine
			firstValue = value;
		}
		else {
			OptimizationPass.log.warn("Invalid constant {}",
				firstChild.toString());
			return node;
		}

		if (secondChild instanceof ConstInt) {
			int value = ((ConstInt) secondChild).getValue();
			// Fits fine
			secondValue = value;
		}
		else if (secondChild instanceof ConstDouble) {
			double value = ((ConstDouble) secondChild).getValue();
			// Already a double
			secondValue = value;
		}
		else if (secondChild instanceof ConstChar) {
			char value = ((ConstChar) secondChild).getValue();
			// Fits fine
			secondValue = value;
		}
		else {
			OptimizationPass.log.warn("Invalid constant {}",
				secondChild.toString());
			return node;
		}

		ConstDouble result = new ConstDouble();
		result.setType(Type.primitive(Base.DOUBLE));

		switch (node.getOperator()) {
			case DEC_PREFIX:
			case DEC_SUFFIX:
			case INC_PREFIX:
			case INC_SUFFIX:
			default:
				// Can't happen, but let's cover it anyway
				OptimizationPass.log.warn("Can't use operator {} on a {}",
					node.getOperator().toString(),
					firstChild.getClass().getSimpleName());
				return node;
			case ADD:
				result.setValue(firstValue + secondValue);
				break;
			case DIV:
				result.setValue(firstValue / secondValue);
				break;
			case MOD:
				result.setValue(firstValue % secondValue);
				break;
			case MUL:
				result.setValue(firstValue * secondValue);
				break;
			case SUB:
				result.setValue(firstValue - secondValue);
				break;
		}

		return result;
	}

	/**
	 * Simplify an integer expression.
	 *
	 * @param node The node we are simplifying.
	 * @return The resulting value.
	 */
	private Node simplifyInt(ExprArithmetic node) {
		final Node firstChild = node.getChildren().get(0);
		final Node secondChild = node.getChildren().get(1);

		int firstValue;
		int secondValue;

		if (firstChild instanceof ConstInt) {
			int value = ((ConstInt) firstChild).getValue();
			// Already an int
			firstValue = value;
		}
		else if (firstChild instanceof ConstDouble) {
			double value = ((ConstDouble) firstChild).getValue();
			// Truncate
			firstValue = (int) value;
		}
		else if (firstChild instanceof ConstChar) {
			char value = ((ConstChar) firstChild).getValue();
			// Fits fine
			firstValue = value;
		}
		else {
			OptimizationPass.log.warn("Invalid constant {}",
				firstChild.toString());
			return node;
		}

		if (secondChild instanceof ConstInt) {
			int value = ((ConstInt) secondChild).getValue();
			// Already an int
			secondValue = value;
		}
		else if (secondChild instanceof ConstDouble) {
			double value = ((ConstDouble) secondChild).getValue();
			// Truncate
			secondValue = (int) value;
		}
		else if (secondChild instanceof ConstChar) {
			char value = ((ConstChar) secondChild).getValue();
			// Fits fine
			secondValue = value;
		}
		else {
			OptimizationPass.log.warn("Invalid constant {}",
				secondChild.toString());
			return node;
		}

		ConstInt result = new ConstInt();
		result.setType(Type.primitive(Base.INT));

		switch (node.getOperator()) {
			case DEC_PREFIX:
			case DEC_SUFFIX:
			case INC_PREFIX:
			case INC_SUFFIX:
			default:
				// Can't happen, but let's cover it anyway
				OptimizationPass.log.warn("Can't use operator {} on a {}",
					node.getOperator().toString(),
					firstChild.getClass().getSimpleName());
				return node;
			case ADD:
				result.setValue(firstValue + secondValue);
				break;
			case DIV:
				result.setValue(firstValue / secondValue);
				break;
			case MOD:
				result.setValue(firstValue % secondValue);
				break;
			case MUL:
				result.setValue(firstValue * secondValue);
				break;
			case SUB:
				result.setValue(firstValue - secondValue);
				break;
		}

		return result;
	}
}
