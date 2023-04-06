package com.ikalagaming.scripting.interpreter;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles scripting.
 *
 * @author Ches Burks
 *
 */
public class ScriptManager {

	private static List<Class<?>> registeredClasses =
		Collections.synchronizedList(new ArrayList<>());

	private static Map<String, List<FunctionRegistration>> registeredMethods =
		Collections.synchronizedMap(new HashMap<>());

	/**
	 * Register a class with the script engine, making all of its methods
	 * available for use. If the class is already registered, this will not do
	 * anything. <br>
	 *
	 * Only public static methods, which are not abstract or interfaces, will be
	 * registered.
	 *
	 * @param clazz The class we are registering.
	 */
	public static void registerClass(Class<?> clazz) {
		if (ScriptManager.registeredClasses.contains(clazz)) {
			return;
		}
		ScriptManager.registeredClasses.add(clazz);
		List<FunctionRegistration> funcs = new ArrayList<>();

		for (Method method : clazz.getMethods()) {
			final int modifiers = method.getModifiers();
			if (!(Modifier.isStatic(modifiers)
				|| Modifier.isPublic(modifiers)) || Modifier.isAbstract(modifiers)
				|| Modifier.isInterface(modifiers)) {
				continue;
			}
			funcs.add(FunctionRegistration.fromMethod(method));
		}
		ScriptManager.registeredMethods.put(clazz.getSimpleName(),
			List.copyOf(funcs));
	}

	/**
	 * Unregister a class from the script engine. <br>
	 * If the class is not registered, this will not do anything.
	 *
	 * @param clazz The class we are unregistering.
	 */
	public static void unregisterClass(Class<?> clazz) {
		if (!ScriptManager.registeredClasses.contains(clazz)) {
			return;
		}
		ScriptManager.registeredClasses.remove(clazz);
		ScriptManager.registeredMethods.remove(clazz.getSimpleName());
	}

}
