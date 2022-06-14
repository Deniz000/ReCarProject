package Views;

import Model.Car;
import Model.City;
import Model.Company;
import com.carRental.Helper.Config;
import com.carRental.Helper.Helper;
import com.carRental.Helper.Item;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CompanyManagement extends JFrame {
    private JPanel wrapper;
    private Company company;
    private JTabbedPane tabbedPane1;
    private JTextField txtCompanyName1;
    private JComboBox cmbCity1;
    private JTextField txtMail1;
    private JTextField txtPassword1;
    private JButton btUpdate;
    private JTextField txtCarType;
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
    private JTable table1;
    private Object[] col_carList = {"ID", "Şehir", "Araç Tipi", "Müsaitlik Durumu", "Fiyat"};;
    private Object[] row_carList = new Object[col_carList.length];
    private DefaultTableModel mdl_carList;

    public CompanyManagement(Company company){
        add(wrapper);
        this.company = company;
        setSize(800,600);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle(Config.PROJECT_TİTLE);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        comboSortCity();
        txtCompanyName.setText(company.getUsername());
        txtPassword.setText(company.getPassword());
        txtMail.setText(company.geteMail());
        //Araç tablosu için:
        mdl_carList =new DefaultTableModel();
        mdl_carList.setColumnIdentifiers(col_carList);

        tbtlCarList.setModel(mdl_carList);
        tbtlCarList.getTableHeader().setReorderingAllowed(false);

        //yeni üyelik oluşturunca, yeni firmaya ait araç olmadığından sortCar() metoodu hata verdiği için bu şekilde yaptım
        try {
            sortCar();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }


        //Bilgileri güncelleme ksımı
        btUpdate.addActionListener(e -> {

        });

        // ilişisel işlemler gerçekleştirilecek
        hesabıSilButton.addActionListener(e -> {
            if(Helper.confirm("sure")){
                int selectedId = this.company.getId();
                if(Company.delete(selectedId)){
                    Helper.showMsg("succed");
                    dispose();
                    Helper.login.setVisible(true);
                }

            }
        });
    }

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


}
