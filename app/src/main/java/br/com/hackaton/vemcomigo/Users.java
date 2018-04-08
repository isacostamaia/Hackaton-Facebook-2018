package br.com.hackaton.vemcomigo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class Users {
    private String userId;

    public String getUserId() {
        return userId;
    }

    public String getCurso() {
        return curso;
    }

    public int getCommonFriends() {
        return commonFriends;
    }

    private String curso;
    private int commonFriends;

    public Users(String userId, String curso, int commonFriends) {
        this.userId=userId;
        this.curso=curso;
        this.commonFriends=commonFriends;
    }


    public static List<Users> getUsersList() {
        List<Users> usersList = new ArrayList<>();
        usersList.add(new Users("Cynthia", "Engenharia Elétrica - Unicamp", 25));
        usersList.add(new Users("Gabriela", "Ciência da Computação - Unicamp", 81));
        usersList.add(new Users("Junior", "Ciência e Tecnologia - UFABC", 10));
        usersList.add(new Users("Isabella", "Engenharia Mecatrônica - USP", 8));
        return usersList;
    }
}
