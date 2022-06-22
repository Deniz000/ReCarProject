package Views;

import Model.Company;
import Model.User;
import com.carRental.Helper.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CompanyLogin extends JFrame {


    private JPanel wrapper;
    private JTextField txt_firmaAdı;
    private JPasswordField psw_firmaParola;
    private JButton btn_girisYap;
    private JButton btn_uyeOl;
    private JPanel wBottom;
    private JButton btn_return;

    public CompanyLogin(){
            add(wrapper);
            setSize(600,400);
            setLocationRelativeTo(null);
            setResizable(false);

            setTitle(Config.PROJECT_TİTLE);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE );

            // Ana login ekranına dönüş
        btn_return.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                Helper.login.setVisible(true);
            }
        });
        //Firma üyeliği mevcutsa giriş
        btn_girisYap.addActionListener(e -> {
            if(Helper.isFieldEmpty(txt_firmaAdı) || Helper.isFieldEmpty(psw_firmaParola)){
                Helper.showMsg("fill");
            }else{
                Company c = Company.getFetch(txt_firmaAdı.getText(),psw_firmaParola.getText());
                if(c ==null){
                    Helper.showMsg("Firma Bulunamadı !");
                }else{
                    dispose();
                    Helper.registerCompany = new CompanyManagement(c);
                    Helper.registerCompany.setVisible(true);// giriş başarılıysa giriş ekranı sonlandır
                }
            }
        });

        //Firmalar için üyelik
        btn_uyeOl.addActionListener(e -> {
            if(Helper.isFieldEmpty(txt_firmaAdı) || Helper.isFieldEmpty(psw_firmaParola)){
                Helper.showMsg("fill");
            }else {
                Company c = Company.getFetch(txt_firmaAdı.getText(),psw_firmaParola.getText());
                if(c != null){
                    Helper.showMsg("Üyelik Zaten Mevcut !");
                }else{

                    if(Company.add(txt_firmaAdı.getText(), psw_firmaParola.getText())){
                        Helper.showMsg("Üyelik Başarılı. Giriş yapmak için sekmeyi \n kapatıp GİRİŞ YAP butonuna tıklayınız");

                    }
                }
            }

        });
    }
}
