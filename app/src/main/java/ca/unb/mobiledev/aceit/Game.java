package ca.unb.mobiledev.aceit;

import java.util.ArrayList;

public interface Game {

    ArrayList<User> getUsers();
    void addUser(User user);
    boolean isStarted();
    void start();
    GameType getGameType();
    GameStatus getStatus();
    
}
