package com.ikalagaming.scripting.interpreter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Extra information needed to call a method.
 *
 * @author Ches Burks
 *
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
class FunctionRegistration {

	/**
	 * Convert a method to a registration object.
	 *
	 * @param method The method to register.
	 * @return The registration information.
	 */
	public static FunctionRegistration fromMethod(Method method) {
		List<Class<?>> paramList;

		if (method.getParameterCount() == 0) {
			paramList = List.of();
		}
		else {
			List<Class<?>> tempList = new ArrayList<>();
			for (Parameter param : method.getParameters()) {
				tempList.add(param.getType());
			}
			paramList = Collections.unmodifiableList(tempList);
		}

		return new FunctionRegistration(method.getName(), paramList,
			method.getReturnType());
	}

	/**
	 * The name of the method.
	 *
	 * @return The method name.
	 */
	private final String name;
	/**
	 * The types of each parameter.
	 *
	 * @return The type of each parameter.
	 */
	private final List<Class<?>> parameterTypes;

	/**
	 * The type of the return value, will be Void if there is no return.
	 *
	 * @return The return type.
	 */
	private final Class<?> returnType;

}
