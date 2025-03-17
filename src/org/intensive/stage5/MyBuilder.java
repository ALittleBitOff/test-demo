package org.intensive.stage5;

public class MyBuilder {
    public static void main(String[] args) {
        User user1 = new User.UserBuilder("John").lastName(":dsasdd").build();
        System.out.println(user1);
        User user2 = new User.UserBuilder("Joh1221dsqdfdsfdn").build();
        System.out.println(user2);
    }
}

class User {
    private final String firstName;
    private final String lastName;

    private User(UserBuilder builder) {
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    public static class UserBuilder {
        private final String firstName;
        private String lastName = "default";

        public UserBuilder(String firstName) {
            this.firstName = firstName;
        }

        public UserBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}

