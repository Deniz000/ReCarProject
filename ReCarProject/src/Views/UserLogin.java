package Views;

import Model.User;
import com.carRental.Helper.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserLogin extends JFrame {
    private JTextField txt_kullaniciAdi;
    private JPasswordField psw_parola;
    private JButton btn_girisYap;
    private JButton btn_uyelikOlustur;
    private JButton btn_return;
    private JPanel wrapper;
    private JPanel wBottom;


    public UserLogin(){
        add(wrapper);
        setSize(600,400);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle(Config.PROJECT_TİTLE);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // ana login ekranına dönüş
        btn_return.addActionListener(e -> {
            setVisible(false);
            Helper.login.setVisible(true);
        });

        // giriş yapma
        btn_girisYap.addActionListener(e -> {
            if(Helper.isFieldEmpty(txt_kullaniciAdi) || Helper.isFieldEmpty(psw_parola)){
                Helper.showMsg("fill");
            }else{
                User u = User.getFetch(txt_kullaniciAdi.getText(),psw_parola.getText());
                if(u ==null){
                    Helper.showMsg("Kullanıcı Bulunamadı !");
                }else{
                    Helper.login.dispose(); //?
                    // giriş doğruysa user giriş ekranı üretme
                    Helper.registerUser = new UserManagement(u);
                    Helper.registerUser.setVisible(true);
                }
            }
        });

        // kullanıcı için yeni üyelik
        btn_uyelikOlustur.addActionListener(e -> {
            if(Helper.isFieldEmpty(txt_kullaniciAdi) || Helper.isFieldEmpty(psw_parola)){
                Helper.showMsg("fill");
            }else{
                User u = User.getFetch(txt_kullaniciAdi.getText(),psw_parola.getText());
                if(u != null){
                    Helper.showMsg("Üyelik Zaten Mevcut !");
                }else{

                    if(User.add(txt_kullaniciAdi.getText(), psw_parola.getText())){
                        Helper.showMsg("Üyelik Başarılı. Giriş yapmak için sekmeyi \n kapatıp GİRİŞ YAP butonuna tıklayınız");

                    }
                }
            }

        });
    }
}
