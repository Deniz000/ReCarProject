package Views;

import Model.Car;
import Model.User;
import com.carRental.Helper.Config;
import com.carRental.Helper.Helper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;

public class UserManagement extends JFrame{
    private JPanel wrapper;
    private JButton araButton;
    private JComboBox cmb_city;
    private JComboBox cmb_carType;
    private JTextField txt_dateStart;
    private JTextField txt_dateEnd;
    private JTabbedPane tabbedPane1;
    private JButton kiralaButton;
    private JTable tbl_carList;
    private JLabel lbl_welcome;
    private JTextField txt_kullaniciAdi;
    private JTextField txt_parola;
    private JButton değişiklikleriKaydetButton;
    private JTable tbl_rentedCarList;
    private JLabel lbl_city;
    private JLabel lbl_carType;
    private JLabel lbl_price;
    private DefaultTableModel mdl_userCarlist;
    private DefaultTableModel mdl_rentedCarList;
    private Object[] row_userCarList;
    private Object[] row_rentedCarList;

    private User user;

    public UserManagement(User user){
        add(wrapper);
        setSize(800,500);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle(Config.PROJECT_TİTLE);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        lbl_welcome.setText("Hoşgeldin "+ user.getUsername());
        //Model userCarlist
        mdl_userCarlist = new DefaultTableModel();
        Object[] col_userCarList = {"ID","Şehir","Araç Tipi","Müsaitlik Durumu","Fiyat"};
        mdl_userCarlist.setColumnIdentifiers(col_userCarList);

        tbl_carList.setModel(mdl_userCarlist);
        tbl_carList.getTableHeader().setReorderingAllowed(false); // yeniden sıralanabilirlik
        row_userCarList = new Object[col_userCarList.length];


        mdl_rentedCarList = new DefaultTableModel();
        Object[] col_rentedCarList = {"ID","Şehir","Araç Tipi","Müsaitlik Durumu","Fiyat"};
        mdl_rentedCarList.setColumnIdentifiers(col_rentedCarList);

        tbl_rentedCarList.setModel(mdl_rentedCarList);
        tbl_rentedCarList.getTableHeader().setReorderingAllowed(false); // yeniden sıralanabilirlik
        row_rentedCarList = new Object[col_userCarList.length];


        // Veritabanında kayıtlı araçların araçlar sekmesindeki tabloda listelenmesi
        for(Car obj : Car.getCarList()){
            Object[] row = new Object[col_userCarList.length];
            row[0] = obj.getId();
            row[1] = obj.getCity();
            row[2] = obj.getCarType();
            row[3] = obj.isAvailable();
            row[4] = obj.getPrice();
            mdl_userCarlist.addRow(row);


        }



        txt_dateStart.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                txt_dateStart.setText("");
            }
        });
        txt_dateEnd.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                txt_dateEnd.setText("");
            }
        });

        // şehir ve araç tipine göre tabloda listelenecek araçların filtrelenmesi
        araButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel clearModel = (DefaultTableModel) tbl_carList.getModel();
                clearModel.setRowCount(0);
                for(Car obj : Car.getCarList()){
                    int i =0;
                    row_userCarList[i++] = obj.getId();
                    row_userCarList[i++] = obj.getCity();
                    row_userCarList[i++] = obj.getCarType();
                    row_userCarList[i++] = obj.isAvailable();
                    row_userCarList[i++] = obj.getPrice();
                    if(cmb_city.getSelectedItem().equals(row_userCarList[1]) && cmb_carType.getSelectedItem().equals(row_userCarList[2])){
                        mdl_userCarlist.addRow(row_userCarList);
                    }

                }
            }
        });

        txt_kullaniciAdi.setText(user.getUsername());
        txt_parola.setText(user.getPassword());

        // kullanıcının kullanıcı adı ve şifre güncellemesi
        değişiklikleriKaydetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(User.update(user,txt_kullaniciAdi.getText(),txt_parola.getText())){
                    lbl_welcome.setText("Hoşgeldin "+ txt_kullaniciAdi.getText());
                    Helper.showMsg("done");

                }else{
                    Helper.showMsg("error");
                }
            }
        });

        // tıklanan aracın rezerve değilse kiralanması
        kiralaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel model = (DefaultTableModel) tbl_carList.getModel();
                int seciliRow = tbl_carList.getSelectedRow(); // tıklanan table satırının atanması
                if(seciliRow != -1){

                    Car obj;
                    obj = Car.getCarList().get(seciliRow);

                    // Kullanıcının kiraladığı aracın profil altındaki tabloda listelenmesi
                    if(obj.isAvailable() != "Rezerve"){
                        model.setValueAt("Rezerve",seciliRow,3);  // Rezerve değilse rezerve olarak alanın değiştirilmesi
                        Object[] row = new Object[col_rentedCarList.length];
                        row[0] = obj.getId();
                        row[1] = obj.getCity();
                        row[2] = obj.getCarType();
                        row[3] = "Rezerve";
                        row[4] = obj.getPrice();
                        mdl_rentedCarList.addRow(row);
                        Helper.showMsg("done");
                    }else{
                        Helper.showMsg("Araç Rezerve Edilmiş");
                    }




                }
            }
        });

        // seçilen satırdaki bilgilerin araç bilgileri altında ekrana yazdırılması
        tbl_carList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                DefaultTableModel model = (DefaultTableModel) tbl_carList.getModel();
                int seciliRow = tbl_carList.getSelectedRow();
                lbl_city.setText((String) model.getValueAt(seciliRow,1));
                lbl_carType.setText((String) model.getValueAt(seciliRow,2));
                lbl_price.setText(String.valueOf(model.getValueAt(seciliRow,4)));
            }
        });
    }




}
