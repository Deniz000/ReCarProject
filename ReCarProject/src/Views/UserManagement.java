package Views;

import Model.*;
import com.carRental.Helper.Config;
import com.carRental.Helper.Helper;
import com.carRental.Helper.Item;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Calendar;

import static java.time.LocalDate.of;

public class UserManagement extends JFrame{
    private JPanel wrapper;
    private User user;
    private Calendar calendar = Calendar.getInstance();
    private int thisYear = calendar.get(Calendar.YEAR);
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
    private JLabel lbl_Firm;
    private JComboBox cmbStartDay;
    private JComboBox cmbStartMounth;
    private JButton btnList;
    private JComboBox cmbEndDay;
    private JComboBox cmbEndMounth;
    private JButton btnRent;
    private JLabel lblCarId;
    private JComboBox cmbStartYear;
    private JComboBox cmbEndYear;
    private DefaultTableModel mdl_userCarlist;
    private DefaultTableModel mdl_rentedCarList;


    //sortCar() metodunda ulaşabilmek için burada tanımladım
    Object[] col_userCarList = {"ID", "Şehir", "Araç Tipi", "Müsaitlik Durumu", "Fiyat", "Firma"};
    private Object[] row_userCarList = new Object[col_userCarList.length];

    //sortRent() metodunda ulaşabilmek için burada tanımladım
    Object[] col_rentedCarList ={"ID", "Şehir", "Araç", "Kiralama Tarihi", "Dönüş Tarihi", "Firma"};;
    private Object[] row_rentalCarList = new Object[col_rentedCarList.length];


    public UserManagement(User user) {
        add(wrapper);
        this.user = user;
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle(Config.PROJECT_TİTLE);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        lbl_welcome.setText("Hoşgeldin " + this.user.getUsername());
        comboSortCity();  // şehirleri combobox 'a yazar
        comboSortCarType(); // araç tiplerini yazar


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
        sortRentCar();

        // şehir ve araç tipine göre tabloda listelenecek araçların filtrelenmesi
        araButton.addActionListener(e -> {
            //String city = cmb_city.getSelectedItem().toString();
            int cityId = cmb_city.getSelectedIndex() + 1;
            int carType = cmb_carType.getSelectedIndex() + 1;
            int maxPrice = Integer.parseInt(txtMaxPrice.getText());
            boolean isSelect = false;
            if (radioBtnAvailable.isSelected()) {
                isSelect = true;
            } else {
                isSelect = false;
            }
            String sql = null;
            if (maxPrice == 0) {
                sortCars(Car.sortFilter(cityId, carType, isSelect));
            } else {
                sortCars(Car.sortFilterForPrice(cityId, carType, maxPrice, isSelect));
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

        // seçilen satırdaki bilgilerin araç bilgileri altında ekrana yazdırılması
        tbl_carList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                DefaultTableModel model = (DefaultTableModel) tbl_carList.getModel();
                int seciliRow = tbl_carList.getSelectedRow();


                lblCarId.setText(String.valueOf((int) model.getValueAt(seciliRow,0)));
                lbl_city.setText((String) model.getValueAt(seciliRow, 1));
                lbl_carType.setText((String) model.getValueAt(seciliRow, 2));
                lbl_price.setText(String.valueOf(model.getValueAt(seciliRow, 4)) + " TL");
                lbl_Firm.setText((String) model.getValueAt(seciliRow, 5) + " A.Ş");


            }
        });


        // filtreyi kaldırır, tüm araçlar listelenir
        btnTemizle.addActionListener(e -> sortCars());

        çıkışYapButton.addActionListener(e -> {
            dispose();
            Helper.login.setVisible(true);
        });

        // araç kiralama
        btnRent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel model = (DefaultTableModel) tbl_carList.getModel();
                int seciliRow = tbl_carList.getSelectedRow();

                Car car = Car.getList().get((Integer) model.getValueAt(seciliRow,0)-1);
                System.out.println(car.getId());
                System.out.println(car.isAvailable());
                if(car.isAvailable() == "Müsait"){

                    Rental.add(car.getId(),user.getId(),dateFix()[0],dateFix()[1],car.getFirmId());
                    car.setAvailable(false);
                    model.setValueAt(car.isAvailable(),seciliRow,3);
                    Car.update(car,car.getAvailable(),car.getId());
                    sortRentCar();
                }else{
                    Helper.showMsg("Araç Rezerve Edilmiş");
                }



            }
        });
    }

    public String[] dateFix(){
        String[] array = new String[2];
        array[0]= "2022-"+String.valueOf(cmbStartMounth.getSelectedItem()) +"-"+String.valueOf(cmbStartDay.getSelectedItem());
        array[1]= "2022-"+String.valueOf(cmbEndMounth.getSelectedItem()) +"-"+String.valueOf(cmbEndDay.getSelectedItem());

        return array;
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

    //rezervasyon tablasu
    public void sortRentCar(){
        DefaultTableModel tableModel = (DefaultTableModel) tbl_rentedCarList.getModel();
        tableModel.setRowCount(0);
        for(Rental rental : Rental.getListForUser(this.user.getId())){
            System.out.println("çalışıyo 1");
            row_rentalCarList[0] = rental.getId();
            row_rentalCarList[1] = Car.getFetchCityName(rental.getCarId());
            row_rentalCarList[2] = Car.getFetch(rental.getCarId());
            row_rentalCarList[3] = rental.getRentDate();
            row_rentalCarList[4] = rental.getReturnDate();
            row_rentalCarList[5] = Company.getFetch(rental.getFirmId());
            mdl_rentedCarList.addRow(row_rentalCarList);
            System.out.println("calışııyo 2");
        }
    }


}
