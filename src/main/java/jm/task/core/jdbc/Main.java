package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.sql.SQLException;

public class Main {
    private static UserService userService = new UserServiceImpl();

    public static void main(String[] args) throws SQLException {
        userService.createUsersTable();

        userService.saveUser("Петр", "Петров", (byte) 28);
        userService.saveUser("Иван", "Иванов", (byte) 32);
        userService.saveUser("Сергей", "Сергеев", (byte) 48);
        userService.saveUser("Андрей", "Андреев", (byte) 53);

        System.out.println("Список всех пользователей:");
        userService.getAllUsers().forEach(System.out::println);

        userService.cleanUsersTable();

        userService.dropUsersTable();
    }
}
