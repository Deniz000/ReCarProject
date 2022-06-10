package Model;

import com.carRental.Helper.DbConnector;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Car {

    private int id;
    private String city;
    private String carType;
    private String available;
    private int price;

    public Car() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String isAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    // Kayıtlı araç listesinin verilmesi
    public static ArrayList<Car> getCarList(){
        ArrayList<Car> carList = new ArrayList<>();
        String query = "SELECT * FROM car";
        Car obj;
        try {
            Statement st = DbConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()){
                obj = new Car();
                obj.setId(rs.getInt("id"));
                obj.setCity(rs.getString("city"));
                obj.setCarType(rs.getString("carType"));
                obj.setPrice(rs.getInt("price"));
                if(rs.getString("rentDate") != null && rs.getString("returnDate") != null){
                    obj.setAvailable("Rezerve");
                }else {
                    obj.setAvailable("Müsait");
                }



                carList.add(obj);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return carList;
    }
}
