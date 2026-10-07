package com.ikalagaming.scripting;

import lombok.NonNull;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Extra information needed to call a method.
 *
 * @param owner The class that was registered, which may not be the class that declared the method
 *     if it was inherited. Methods with the same signature from different classes are separate.
 * @param name The name of the method.
 * @param parameterTypes The types of each parameter. Will be empty if there are no parameters.
 * @param returnType The type of the return value, will be Void if there is no return.
 * @author Ches Burks
 */
record FunctionRegistration(
        @NonNull Class<?> owner,
        @NonNull String name,
        @NonNull List<Class<?>> parameterTypes,
        @NonNull Class<?> returnType) {

    /**
     * Convert a method to a registration object.
     *
     * @param owner The class that is being registered.
     * @param method The method to register.
     * @return The registration information.
     */
    public static FunctionRegistration fromMethod(@NonNull Class<?> owner, Method method) {
        List<Class<?>> paramList;

        if (method.getParameterCount() == 0) {
            paramList = List.of();
        } else {
            List<Class<?>> tempList = new ArrayList<>();
            for (Parameter param : method.getParameters()) {
                tempList.add(param.getType());
            }
            paramList = Collections.unmodifiableList(tempList);
        }

        return new FunctionRegistration(owner, method.getName(), paramList, method.getReturnType());
    }
}
