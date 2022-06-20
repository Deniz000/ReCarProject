package Model;

import com.carRental.Helper.DbConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CarType {
    private int id;
    private String name;

    public CarType() {
    }

    public CarType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String getFetch(int id){
        String sql = "select name from cartype where id = ?";
        String name = null;
        try {
            PreparedStatement preparedStatement = DbConnector.getInstance().prepareStatement(sql);
            preparedStatement.setInt(1,id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                name = rs.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return name;
    }
    // araç tiplerini listeler
    public static ArrayList<CarType> getList(){
        ArrayList<CarType> carTypes = new ArrayList<>();
        CarType carType;
        String sql = "select * from cartype";

        try {
            Statement statement = DbConnector.getInstance().createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()){
                carType = new CarType();
                carType.setId(rs.getInt("id"));
                carType.setName(rs.getString("name"));
                carTypes.add(carType);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return carTypes;
    }
}
