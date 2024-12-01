    package be.ehb.tristan.javaadvanced.internquest.basicsremovelater;

    import jakarta.persistence.*;
    import jakarta.validation.constraints.Size;

    @Entity
    @Table
    public class UserOld {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        @Size(min = 3, max = 12,message = "Username needs to be between 3 and 12 characters long!")
        private String username;

        @Column(nullable = false)
        @Size(min = 3, max = 12,message = "password needs to be between 3 and 12 characters long!")
        private String password;

        public @Size(min = 3, max = 12, message = "Username needs to be between 3 and 12 characters long!") String getUsername() {
            return username;
        }

        public void setUsername(@Size(min = 3, max = 12, message = "Username needs to be between 3 and 12 characters long!") String username) {
            this.username = username;
        }

        public @Size(min = 3, max = 12, message = "password needs to be between 3 and 12 characters long!") String getPassword() {
            return password;
        }

        public void setPassword(@Size(min = 3, max = 12, message = "password needs to be between 3 and 12 characters long!") String password) {
            this.password = password;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }
