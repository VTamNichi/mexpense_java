package com.hieplh.mexpense.daos;

import com.hieplh.mexpense.dtos.User;

public class UserDAO {

    private User user = new User("Lê Hoàng Hiệp", "hieplh@fpt.edu.vn", "0971245252", 84);

    public User getUser() {
        return user;
    }
}
