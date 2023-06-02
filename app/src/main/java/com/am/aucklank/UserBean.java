package com.am.aucklank;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "t_user")
public class UserBean {

    @Column(name = "id", isId = true)
    private int id;

    @Column(name = "name")
    public String name;


    @Column(name = "age")
    public String age;

    @Column(name = "username")
    public String username;


    @Column(name = "password")
    public String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserBean() {
    }


    public UserBean(String name, String age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }



    @Override
    public String toString() {
        return "TestEntity{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

}
