package com.carRental.Helper;

import Model.Company;
import Model.User;
import Views.CompanyManagement;
import Views.Login;
import Views.UserManagement;

import javax.jws.Oneway;
import javax.swing.*;

public class Helper {
    public static Login login = new Login();
    public static CompanyManagement registerCompany = null;
    public static UserManagement registerUser = null;
    public static void changeUINimbus(){
        for(UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()){
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (UnsupportedLookAndFeelException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static boolean isFieldEmpty(JTextField field){
        return field.getText().trim().isEmpty();
    }

    // Uyarı mesajları
    public static void showMsg(String str){
        optionPageTR();
        String msg;
        String title;
        switch (str){
            case "fill":
                msg="Lütfen tüm alanları doldurunuz";
                title="Hata!";
                break;
            case "done":
                msg="İşlem Başarılı";
                title="Sonuç";
                break;
            case "error":
                msg="Bir hata oluştu";
                title="HATA";
            default:
                msg=str;
                title="Mesaj";

        }

        JOptionPane.showMessageDialog(null,msg,title,JOptionPane.INFORMATION_MESSAGE);
    }

    // Option pane onay butonu türkçeleştirme
    public static void optionPageTR(){
        UIManager.put("OptionPane.okButtonText" , "Tamam");
        UIManager.put("OptionPane.yesButtonText" , "Evet");
        UIManager.put("OptionPane.noButtonText" , "Hayır");
    }


    public static boolean confirm(String str) {
        String msg;
        switch (str) {
            case "sure":
                msg = "Bu işlemi gerçekleştirmek istediğine emin misiniz?";
                break;
            case "deleteAnyway":
                msg = "Bu işlemi gerçekleştirmek istediğine emin misiniz?\n" + " Firmaya ait tüm araçlar silinecek!" ;
                break;
            default:
                msg = str;
                break;
        }
        return JOptionPane.showConfirmDialog(null, msg, "!", JOptionPane.YES_NO_OPTION) == 0;
    }
}
