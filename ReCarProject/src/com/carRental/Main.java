package com.carRental;

import Views.Login;
import Views.Splash;
import com.carRental.Helper.*;

public class Main {
    public static void main(String[] args) {
        DbConnector.getInstance();
        Helper.changeUINimbus();
        Login login = Helper.login;
        Splash splash = new Splash();
        splash.dispose();
        login.setVisible(true);
    }
}
