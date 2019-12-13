package my.project.subject.api;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class UserData {

    public UserData(String username, String firstName, String lastName, String email) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String username;
    public String firstName;
    public String lastName;
    public String email;

    public UserData() {
    }
}
