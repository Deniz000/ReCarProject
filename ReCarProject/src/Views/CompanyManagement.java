package Views;

import Model.*;
import com.carRental.Helper.Config;
import com.carRental.Helper.Helper;
import com.carRental.Helper.Item;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CompanyManagement extends JFrame {
    private JPanel wrapper;
    private Company company;
    private JTabbedPane tabbedPane1;
    private JTextField txtCompanyName1;
    private JComboBox cmbCity1;
    private JTextField txtMail1;
    private JTextField txtPassword1;
    private JButton btUpdate;
    private JTextField txtPrice;
    private JButton ekleButton;
    private JTextField txtDeletedCar;
    private JButton silButton;
    private JTable tbtlCarList;
    private JTabbedPane tabbedPane2;
    private JTextField txtCompanyName;
    private JComboBox cmbCity;
    private JTextField txtMail;
    private JTextField txtPassword;
    private JButton hesabıSilButton;
    private JTable tbl_rentalList;
    private JButton çıkışYapButton;
    private JComboBox cmbCarType;
    private JLabel lblWelcome;

    //sortCar() 'dan ulaşabilmek için
    private DefaultTableModel mdl_carList;
    private Object[] col_carList = {"ID", "Şehir", "Araç Tipi", "Müsaitlik Durumu", "Fiyat"};;
    private Object[] row_carList = new Object[col_carList.length];

    //sortRent() 'dan ulaşabilmek için
    private Object[] col_rentalList = {"ID", "Araç", "Kiralayan", "Kiralama Tarihi", "Dönüş Tarihi"};
    private Object[] row_rentalList = new Object[col_carList.length];
    private DefaultTableModel mdl_rentalList;

    public CompanyManagement(Company company){
        add(wrapper);
        this.company = company;
        setSize(800,600);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle(Config.PROJECT_TİTLE);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        lblWelcome.setText("Hoşgeldiniz " + this.company.getUsername());
        comboSortCity();
        txtCompanyName.setText(this.company.getUsername());
        txtPassword.setText(this.company.getPassword());
        txtMail.setText(this.company.geteMail());
        //Rezervasyon Listesi için
        mdl_rentalList = new DefaultTableModel();
        mdl_rentalList.setColumnIdentifiers(col_rentalList);
        tbl_rentalList.setModel(mdl_rentalList);
        tbl_rentalList.getTableHeader().setReorderingAllowed(false);
        sortRental();

        //Araç tablosu için:
        mdl_carList =new DefaultTableModel();
        mdl_carList.setColumnIdentifiers(col_carList);
        tbtlCarList.setModel(mdl_carList);
        tbtlCarList.getTableHeader().setReorderingAllowed(false);
        tbtlCarList.getSelectionModel().addListSelectionListener(e->{
            try {
                String selectedItemId = tbtlCarList.getValueAt(tbtlCarList.getSelectedRow(),0).toString();
                txtDeletedCar.setText(selectedItemId);
            }
            catch (Exception exception){
                System.out.println(exception.getMessage());
            }
        });
        //yeni üyelik oluşturunca, yeni firmaya ait araç olmadığından sortCar() metoodu hata verdiği için bu şekilde yaptım
        try {
            sortCar();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        // ilişisel işlemler gerçekleştirilecek
        hesabıSilButton.addActionListener(e -> {
            if(Helper.confirm("deleteAnyway")){
                int selectedId = this.company.getId();
                if(Company.delete(selectedId)){
                    Helper.showMsg("done");
                    dispose();
                    Helper.login.setVisible(true);
                }

            }
        });

        // tüm kodlar tam ama çalışmıyor --------------------------------------------------------------------------------!!!!!
        btUpdate.addActionListener(e -> {
            String name = txtCompanyName.getText();
            int cityId = cmbCity.getSelectedIndex() +1;
            String eMail = txtMail.getText();
            String password = txtPassword.getText();

            if(Helper.isFieldEmpty(txtCompanyName) || Helper.isFieldEmpty(txtMail) || Helper.isFieldEmpty(txtPassword)){
                Helper.showMsg("fill");
            }
            else {
                if (Company.update(name, cityId, eMail, password)) {
                    Helper.showMsg("done");

                } else {
                    Helper.showMsg("error");
                }
            }

        });
        çıkışYapButton.addActionListener(e -> {
            dispose();
            Helper.login.setVisible(true);
        });
        comboSortCar();
        ekleButton.addActionListener(e -> {
            if(Helper.isFieldEmpty(txtPrice)){
                Helper.showMsg("fill");
            }else {
                int typeId = cmbCarType.getSelectedIndex() + 1;
                int price = Integer.parseInt(txtPrice.getText());
                if(Car.add(price,this.company.getCityId(),true, this.company.getId(), typeId)){
                    Helper.showMsg("done");
                    sortCar();
                }
                else{
                    Helper.showMsg("error");
                }
            }
        });

        silButton.addActionListener(e -> {
            if(Helper.isFieldEmpty(txtDeletedCar)){
                Helper.showMsg("fill");
            }else{
                if(Helper.confirm("sure")){
                    int carId = Integer.parseInt(txtDeletedCar.getText());
                    if(Car.deleteById(carId)){
                        Helper.showMsg("done");
                        sortCar();
                        txtDeletedCar.setText("");
                        return;
                    } else{
                        System.out.println("Üzgünüz. Bir aksilik oldu!");
                        return;
                    }
                }
            }
        });
    }

    //firmaya ait araçlar listeleniyor
    public void sortCar(){
        DefaultTableModel tableModel = (DefaultTableModel) tbtlCarList.getModel();
        tableModel.setRowCount(0);
        for (Car obj: Company.getList(this.company.getId())){
            row_carList[0] = obj.getId();
            row_carList[1] = City.getFetch(obj.getCityId()); // tabloya, id ye göre şehir isimlerini yazar
            row_carList[2] = obj.getCarTypeId();
            row_carList[3] = obj.isAvailable();
            row_carList[4] = obj.getPrice();
            mdl_carList.addRow(row_carList);        }
    }
    public  void comboSortCity(){
        cmbCity.removeAllItems();
        for (City city : City.getList()) {
            cmbCity.addItem(new Item(city.getId(), city.getName()).getValue());
        }
    }

    public  void comboSortCar(){
        cmbCarType.removeAllItems();
        for (CarType carType : CarType.getList()) {
            cmbCarType.addItem(new Item(carType.getId(), carType.getName()).getValue());
        }
    }
    public void sortRental(){
        DefaultTableModel tableModel = (DefaultTableModel) tbl_rentalList.getModel();
        tableModel.setRowCount(0);
        System.out.println("çalışıyo0");
        System.out.println(company.getId());
        for(Rental rental : Rental.getListForCompany(company.getId())){
            System.out.println("çalışıyo 1");
            row_rentalList[0] = rental.getId();
            row_rentalList[1] = Car.getFetch(rental.getCarId());
            row_rentalList[2] = User.getFetch(rental.getUserId());
            row_rentalList[3] = rental.getRentDate();
            row_rentalList[4] = rental.getReturnDate();
            mdl_rentalList.addRow(row_rentalList);
            System.out.println("calışııyo 2");
        }
    }


}
