package com.messenger.server;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

class CommandRegistry {

    @FunctionalInterface
    interface TriFunction<A, B, C, R> {

        R apply(A a, B b, C c);

        default <V> TriFunction<A, B, C, V> andThen(Function<? super R, ? extends V> after) {
            Objects.requireNonNull(after);
            return (A a, B b, C c) -> after.apply(apply(a, b, c));
        }
    }

    private String pluginsPath;

    private Map<String, Function<String[], String>> commands;
    private Map<String, TriFunction<Server, ClientConnection, Command, String>> systemCommands;

    CommandRegistry(String pluginsPath) {
        this.pluginsPath = pluginsPath;

        commands = new HashMap<>();
        systemCommands = new HashMap<>();

        loadSystemCommands();
        loadPluginsCommands();
    }

    String execute(Server server, Command command) {
        String name = command.getCommand();
        if (systemCommands.containsKey(name)) {
            String result = systemCommands.get(name).apply(server, command.getConnection(), command);

            return result == null ? "" : result;
        }
        if (commands.containsKey(name)) {
            String result = commands.get(name).apply(command.getParams());

            return result == null ? "" : result;
        }

        return null;
    }

    private void loadSystemCommands() {
        Class commandsClass = SystemCommands.class;
        for (Method method : commandsClass.getDeclaredMethods()) {
            String name = method.getName();
            systemCommands.put(name, (Server server, ClientConnection connection, Command command) -> {
                try {
                    return (String) method.invoke(null, server, connection, command);
                } catch (ReflectiveOperationException e) {
                    e.printStackTrace();
                    systemCommands.remove(name);

                    return "";
                }
            });
        }
    }

    private void loadPluginsCommands() {
        // todo
    }

}
