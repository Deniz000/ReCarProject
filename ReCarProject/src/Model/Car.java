package Model;

import com.carRental.Helper.DbConnector;
import com.carRental.Helper.Helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Car {

    private int id;
    private int cityId;
    private int carTypeId;
    private boolean available;
    private int price;
    private int firmId;

    public Car() {

    }
    public Car(int id, int cityId, int carTypeId, boolean available, int price,int firmId) {
        this.id = id;
        this.cityId = cityId;
        this.carTypeId = carTypeId;
        this.available = available;
        this.price = price;
        this.firmId = firmId;
    }

    public static boolean add(int price, int cityId, boolean b, int id, int typeId) {
        String sql = "insert into car (price, city_id,available, firm_id, car_type_id) values (?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = DbConnector.getInstance().prepareStatement(sql);
            preparedStatement.setInt(1,price);
            preparedStatement.setInt(2,cityId);
            preparedStatement.setBoolean(3,b);
            preparedStatement.setInt(4,id);
            preparedStatement.setInt(5,typeId);
            return preparedStatement.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean deleteById(int carId) {
        String sql = "delete from car where id = ?";
        try {
            PreparedStatement preparedStatement = DbConnector.getInstance().prepareStatement(sql);
            preparedStatement.setInt(1,carId);
            if(Rental.deleteByCarId(carId)){
                Helper.showMsg("Araca ait tüm rezervasyonlar silindi");
            }
            else{
                Helper.showMsg("error");
            }
            return preparedStatement.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean deleteByFirm(int id){
        String sql = "delete from car where firm_id = ?";
        try {
            PreparedStatement preparedStatement = DbConnector.getInstance().prepareStatement(sql);
            preparedStatement.setInt(1,id);
            Helper.showMsg("done");
            return preparedStatement.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getCarTypeId() {
        return carTypeId;
    }

    public void setCarTypeId(int carTypeId) {
        this.carTypeId = carTypeId;
    }

    public String isAvailable() {
        if(this.available){
            return "Müsait";
        }
        else{
            return "Rezerve";
        }
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
    public boolean getAvailable() {
        return this.available;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getFirmId() {
        return firmId;
    }

    public void setFirmId(int firmId) {
        this.firmId = firmId;
    }

    // Kayıtlı araç listesinin verilmesi
    public static ArrayList<Car> getList(){
        ArrayList<Car> carList = new ArrayList<>();
        String query = "SELECT * FROM car";
        Car obj;
        try {
            Statement st = DbConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()){
                obj = new Car();
                obj.setId(rs.getInt("id"));
                obj.setCityId(rs.getInt("city_id"));
                obj.setCarTypeId(rs.getInt("car_type_id"));
                obj.setPrice(rs.getInt("price"));
                obj.setAvailable(rs.getBoolean("available"));
                obj.setFirmId(rs.getInt("firm_id"));
                carList.add(obj);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return carList;
    }

    //firmalara ait araçları listeler
    public static ArrayList<Car> getList(int id){
        String sql = "select * from car where firm_id = ?";
        ArrayList<Car> cars = new ArrayList<>();
        Car car;
        try {
            PreparedStatement preparedStatement = DbConnector.getInstance().prepareStatement(sql);
            preparedStatement.setInt(1,id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                car = new Car(rs.getInt("id"),rs.getInt("city_id"),rs.getInt("car_type_id"),rs.getBoolean("available"), rs.getInt("price"), rs.getInt("firm_id"));
                cars.add(car);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }

    public static boolean update(Car u,boolean available,int id){
        String query="UPDATE car SET available = ? WHERE id = ?";

        try {
            PreparedStatement pr=DbConnector.getInstance().prepareStatement(query);
            pr.setBoolean(1,available);
            pr.setInt(2,u.getId());

            int response= pr.executeUpdate();

            if(response == -1){
                Helper.showMsg("error");
            }
            return response != -1;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return true;
    }

    public static String getFetch(int id){
        String query= "select name from car  \n" +
                "inner join cartype\n" +
                "on car.car_type_id = cartype.id\n" +
                "where car.id = ?";
        String name = null;
        try {
            PreparedStatement pr = DbConnector.getInstance().prepareStatement(query);
            pr.setInt(1,id);
            ResultSet rs=pr.executeQuery();
            if (rs.next()){
                name = rs.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return name;
    }
    public static String getFetchCityName(int id){
        String query= "select name from car \n" +
                "inner join cities\n" +
                "on cities.id = car.city_id\n" +
                "where car.id = ? ";
        String name = null;
        try {
            PreparedStatement pr = DbConnector.getInstance().prepareStatement(query);
            pr.setInt(1,id);
            ResultSet rs=pr.executeQuery();
            if (rs.next()){
                name = rs.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return name;
    }


    //fiyat bilgisi girilmişse çalışacak sorgu
    public static ArrayList<Car> sortFilterForPrice(int cityId, int carType, int maxPrice, boolean isSelect) {
        ArrayList<Car> cars = new ArrayList<>();
        Car car =null;
        String sql = "select * from car where city_id =? and car_type_id = ? and available = ? and  price < ?";
        try {
            PreparedStatement preparedStatement = DbConnector.getInstance().prepareStatement(sql);
            preparedStatement.setInt(1,cityId);
            preparedStatement.setInt(2,carType);
            preparedStatement.setBoolean(3,isSelect);
            preparedStatement.setInt(4,maxPrice);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                car = new Car(rs.getInt("id"),rs.getInt("city_id"),rs.getInt("car_type_id"),rs.getBoolean("available"), rs.getInt("price"), rs.getInt("firm_id"));
                cars.add(car);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cars;
    }
    //Fiyat bilgisi girmemişse çalışacak
    public static ArrayList<Car> sortFilter(int cityId, int carType, boolean isSelect) {
        String sql = "select * from car where city_id = ? and car_type_id = ? and available = ?";
        ArrayList<Car> cars = new ArrayList<>();
        Car car = null;
        try {
            PreparedStatement preparedStatement = DbConnector.getInstance().prepareStatement(sql);
            preparedStatement.setInt(1,cityId);
            preparedStatement.setInt(2,carType);
            preparedStatement.setBoolean(3,isSelect);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                car = new Car(rs.getInt("id"),rs.getInt("city_id"),rs.getInt("car_type_id"),rs.getBoolean("available"), rs.getInt("price"),rs.getInt("firm_id"));
                cars.add(car);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }


}
