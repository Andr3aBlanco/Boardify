package com.boardify.boardify.javat;

import com.boardify.boardify.entities.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.SQLException;
import java.util.List;

public class Catdao {

    JdbcTemplate template;


    public Catdao(JdbcTemplate template) {
        this.template = template;
    }
    public void setTemplate(JdbcTemplate template) {
    }


    public List<User> display() throws ClassNotFoundException, SQLException {
        //create an array list that will contain the data recovered

        return template.query("select * from category", (RowMapper) (rs, row) -> {
            User user = new User();
            user.setUsername(rs.getString(1));
            user.setFirstName(rs.getString(2));
            user.setLastName(rs.getString(3));
            user.setEmail(rs.getString(4));
            user.setAccountStatus(rs.getString(5));

            return user;

        });

    }

    public int deleteData (String email) {
        return template.update("delete from users where email= ?", email);
    }
}
