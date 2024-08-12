package com.wattbroker.wattbroker.UserHandling;

import com.wattbroker.wattbroker.DB_query;

public class USER_ID {
    private int id;

    public USER_ID(String username) {
        DB_query dbq = new DB_query();
        this.id = dbq.getId(username);

        if(this.id == -1)
            throw new Error("Failed to get user [" + username + "]");
    }

    public int getId() {
        return id;
    }
}
