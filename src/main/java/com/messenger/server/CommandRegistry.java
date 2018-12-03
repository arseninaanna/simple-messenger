package com.messenger.server;

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
    private Map<String, TriFunction<Server, ClientConnection, String[], String>> systemCommands;

    CommandRegistry(String pluginsPath) {
        this.pluginsPath = pluginsPath;

        loadSystemCommands();
        loadPluginsCommands();
    }

    String execute(Server server, Command command) {
        String name = command.getCommand();
        if (systemCommands.containsKey(name)) {
            String result = systemCommands.get(name).apply(server, command.getConnection(), command.getParams());

            return result == null ? "" : result;
        }
        if (commands.containsKey(name)) {
            String result = commands.get(name).apply(command.getParams());

            return result == null ? "" : result;
        }

        return null;
    }

    private void loadSystemCommands() {
        // todo
    }

    private void loadPluginsCommands() {
        // todo
    }

}
