package Views;

import com.carRental.Helper.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame {


    private JPanel wrapper;
    private JButton btn_firmaGiris;
    private JButton btn_kullaniciGiris;

    private CompanyLogin companyLogin = new CompanyLogin();

    private UserLogin userLogin = new UserLogin();

    public UserLogin getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(UserLogin userLogin) {
        this.userLogin = userLogin;
    }

    public CompanyLogin getCompanyLogin() {
        return companyLogin;
    }

    public void setCompanyLogin(CompanyLogin companyLogin) {
        this.companyLogin = companyLogin;
    }

    public Login(){

        add(wrapper);
        setSize(800,600);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle(Config.PROJECT_TİTLE);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE );

        // firma giriş ekranına yönlendirme
        btn_firmaGiris.addActionListener(e -> {
            setVisible(false);
            getCompanyLogin().setVisible(true);
        });
        // kullanıcı giriş ekranına yönlendirme
        btn_kullaniciGiris.addActionListener(e -> {
            setVisible(false);
            getUserLogin().setVisible(true);
        });
    }
}

