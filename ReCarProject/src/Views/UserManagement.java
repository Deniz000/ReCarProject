package Views;

import Model.*;
import com.carRental.Helper.Config;
import com.carRental.Helper.Helper;
import com.carRental.Helper.Item;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.ArrayList;

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
    private JRadioButton radioBtnAvailable;
    private JTextField txtMaxPrice;
    private JButton btnTemizle;
    private JButton çıkışYapButton;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JTextField textField1;
    private JComboBox comboBox3;
    private JComboBox comboBox4;
    private JTextField textField2;
    private JLabel lbl_Firm;
    private DefaultTableModel mdl_userCarlist;
    private DefaultTableModel mdl_rentedCarList;



    Object[] col_userCarList = {"ID", "Şehir", "Araç Tipi", "Müsaitlik Durumu", "Fiyat", "Firma"}; //sortCar() metodunda ulaşabilmek için burada tanımladım
    private Object[] row_userCarList = new Object[col_userCarList.length];

    Object[] col_rentedCarList = {"ID", "Şehir", "Araç Tipi", "Müsaitlik Durumu", "Fiyat", "Firma"};

    private User user;

    public UserManagement(User user) {
        add(wrapper);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle(Config.PROJECT_TİTLE);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        lbl_welcome.setText("Hoşgeldin " + user.getUsername());
        //Model userCarlist
        mdl_userCarlist = new DefaultTableModel();
        mdl_userCarlist.setColumnIdentifiers(col_userCarList);

        tbl_carList.setModel(mdl_userCarlist);
        tbl_carList.getTableHeader().setReorderingAllowed(false); // yeniden sıralanabilirlik
        sortCars();

        mdl_rentedCarList = new DefaultTableModel();
        mdl_rentedCarList.setColumnIdentifiers(col_rentedCarList);

        tbl_rentedCarList.setModel(mdl_rentedCarList);
        tbl_rentedCarList.getTableHeader().setReorderingAllowed(false); // yeniden sıralanabilirlik
        comboSortCity();
        comboSortCarType();

        // Veritabanında kayıtlı araçların araçlar sekmesindeki tabloda listelenmesi




        // şehir ve araç tipine göre tabloda listelenecek araçların filtrelenmesi
        araButton.addActionListener(e -> {
                //String city = cmb_city.getSelectedItem().toString();
                int cityId = cmb_city.getSelectedIndex() + 1;
                int carType = cmb_carType.getSelectedIndex() + 1;
                int maxPrice = Integer.parseInt(txtMaxPrice.getText());
                boolean isSelect = false;
                if(radioBtnAvailable.isSelected()){
                    isSelect = true;
                }
                else{
                    isSelect = false;
                }
                String sql = null;
                if(maxPrice==0){
                 //   sql = Car.searchQuery(cityId, carType,isSelect);

                    sortCars(Car.sortFilter(cityId,carType,isSelect));
                }
                else {
                    sortCars(Car.sortFilterForPrice(cityId, carType,maxPrice,isSelect));
                }
        });


        txt_kullaniciAdi.setText(user.getUsername());
        txt_parola.setText(user.getPassword());

        // kullanıcının kullanıcı adı ve şifre güncellemesi
        değişiklikleriKaydetButton.addActionListener(e -> {
            if (User.update(user, txt_kullaniciAdi.getText(), txt_parola.getText())) {
                lbl_welcome.setText("Hoşgeldin " + txt_kullaniciAdi.getText());
                Helper.showMsg("done");

            } else {
                Helper.showMsg("error");
            }
        });

        // tıklanan aracın rezerve değilse kiralanması
        kiralaButton.addActionListener(e -> {
            DefaultTableModel model = (DefaultTableModel) tbl_carList.getModel();
            int seciliRow = tbl_carList.getSelectedRow(); // tıklanan table satırının atanması
            if (seciliRow != -1) {

                Car obj;
                obj = Car.getList().get(seciliRow);

                // Kullanıcının kiraladığı aracın profil altındaki tabloda listelenmesi
           //     if (obj.isAvailable() != "Rezerve") {
            //        model.setValueAt("Rezerve", seciliRow, 3);  // Rezerve değilse rezerve olarak alanın değiştirilmesi
            //        Object[] row = new Object[col_rentedCarList.length];
            //        row[0] = obj.getId();
            //        row[1] = obj.getCityId();
            //        row[2] = obj.getCarType();
            //        row[3] = "Rezerve";
            //        row[4] = obj.getPrice();
           //         mdl_rentedCarList.addRow(row);
           //         Helper.showMsg("done");
           //     } else {
           //         Helper.showMsg("Araç Rezerve Edilmiş");
          //      }


            }
        });

        // seçilen satırdaki bilgilerin araç bilgileri altında ekrana yazdırılması
        tbl_carList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                DefaultTableModel model = (DefaultTableModel) tbl_carList.getModel();
                int seciliRow = tbl_carList.getSelectedRow();
                lbl_city.setText((String) model.getValueAt(seciliRow, 1));
                lbl_carType.setText((String) model.getValueAt(seciliRow, 2));
                lbl_price.setText(String.valueOf(model.getValueAt(seciliRow, 4)));
                lbl_Firm.setText((String) model.getValueAt(seciliRow,5));
            }
        });

        // filtreyi kaldırır, tüm araçlar listelenir
        btnTemizle.addActionListener(e -> sortCars());
        çıkışYapButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Helper.login.setVisible(true);
            }
        });
    }

    //combobox ın içini doldurmak için
    public  void comboSortCity(){
        cmb_city.removeAllItems();
        for (City city : City.getList()) {
            cmb_city.addItem(new Item(city.getId(), city.getName()).getValue());
        }
    }
    public  void comboSortCarType(){
        cmb_carType.removeAllItems();
        for (CarType carType : CarType.getList()) {
            cmb_carType.addItem(new Item(carType.getId(), carType.getName()).getValue());
        }
    }
    public void sortCars(){
        DefaultTableModel tableModel = (DefaultTableModel) tbl_carList.getModel();
        tableModel.setRowCount(0);
        for (Car obj : Car.getList()) {
            row_userCarList[0] = obj.getId();
            row_userCarList[1] = City.getFetch(obj.getCityId()); // tabloya, id ye göre şehir isimlerini yazar
            row_userCarList[2] = CarType.getFetch(obj.getCarTypeId());
            row_userCarList[3] = obj.isAvailable();
            row_userCarList[4] = obj.getPrice();
            row_userCarList[5] = Company.getFetch(obj.getFirmId());
            mdl_userCarlist.addRow(row_userCarList);
        }
    }

    //ara butonunda filtreye göre tekrar sıralama yapıyor
    public void sortCars(ArrayList<Car> cars){
        System.out.println("Çalışıyor 0");
        DefaultTableModel tableModel1 = (DefaultTableModel) tbl_carList.getModel();
        tableModel1.setRowCount(0);
        System.out.println("Çalışıyor 1");
        for (Car obj : cars) {
            System.out.println("Çalışıyor 2");
            row_userCarList[0] = obj.getId();
            row_userCarList[1] = City.getFetch(obj.getCityId()); // tabloya, id ye göre şehir isimlerini yazar
            row_userCarList[2] = CarType.getFetch(obj.getCarTypeId());
            row_userCarList[3] = obj.isAvailable();
            row_userCarList[4] = obj.getPrice();
            row_userCarList[5] = Company.getFetch(obj.getFirmId());
            mdl_userCarlist.addRow(row_userCarList);
            System.out.println("Çalışıyor 3");
        }
    }


}
