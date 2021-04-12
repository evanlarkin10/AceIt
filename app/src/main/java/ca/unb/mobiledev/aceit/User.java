package ca.unb.mobiledev.aceit;

import java.io.Serializable;

public class User implements Serializable {
    private String id;
    private String name;
    public User(){}
    public User(String id, String name){
        this.id=id;
        this.name = name;
    }

    public String getId(){return this.id;}
    public String getName(){return this.name;}
}
