package com.hieplh.mexpense.daos;

import com.hieplh.mexpense.dtos.User;

public class UserDAO {

    private User user = new User("Lê Hoàng Hiệp", "hieplhgcs190464@fpt.edu.vn", "0397455229", 84);

    public User getUser() {
        return user;
    }
}
