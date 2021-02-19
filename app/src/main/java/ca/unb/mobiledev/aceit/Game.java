package ca.unb.mobiledev.aceit;

import java.util.ArrayList;

public interface Game {
    public ArrayList<User> getUsers();
    public void addUser(User user);
    public boolean isStarted();
    public void start();

}
