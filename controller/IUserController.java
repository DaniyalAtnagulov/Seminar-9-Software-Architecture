package seminarNine.controller;

import seminarNine.exceptions.UserNotFountException;
import seminarNine.model.User;

import java.util.List;

public interface IUserController {
    User addUser(String userName);
    User deleteUser(int userId) throws UserNotFountException;
    List<User> getUsers();
    void addAll(List<User> listUsers);
}
