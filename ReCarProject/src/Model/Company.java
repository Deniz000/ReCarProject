package Model;

import com.carRental.Helper.DbConnector;
import com.carRental.Helper.Helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Company {

    private int id;
    private String username;
    private String password;
    private String eMail;
    private int cityId;

    public Company() {

    }
    public Company(int id,String username, String password, String eMail, int cityId) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.cityId = cityId;
        this.eMail = eMail;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public int getCityId() {
        return cityId + 1;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    // id'si verilen firmanın adını döndürür
    public static String getFetch(int id){
        String name = null;
        String query="SELECT companyname FROM company WHERE id =?";
        try {
            PreparedStatement pr = DbConnector.getInstance().prepareStatement(query);
            pr.setInt(1,id);
            ResultSet rs=pr.executeQuery();
            if (rs.next()){
                name = rs.getString("companyname");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return name;
    }


    // adı verilen firmanın id'sini döndürür
    public static int getFetch(String username){
        int id = 0;
        String query="SELECT id FROM company WHERE companyname = ?";
        try {
            PreparedStatement pr = DbConnector.getInstance().prepareStatement(query);
            pr.setString(1,username);
            ResultSet rs=pr.executeQuery();
            if (rs.next()){
                id = rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public static Company getFetch(String username,String password){
        Company obj=null;
        String query="SELECT * FROM company WHERE companyname = ? AND password=?";

        try {
            PreparedStatement pr = DbConnector.getInstance().prepareStatement(query);
            pr.setString(1,username);
            pr.setString(2,password);
            ResultSet rs=pr.executeQuery();
            if (rs.next()){
                obj=new Company();
                obj.setId(rs.getInt("id"));
                obj.setUsername(rs.getString("companyname"));
                obj.setPassword(rs.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return obj;
    }


    // Firma üyeliği eklenmesi
    public static boolean add(String username,String password){
        String query="INSERT INTO company (companyname,password) VALUES (?,?)";

        try {
            PreparedStatement pr=DbConnector.getInstance().prepareStatement(query);
            pr.setString(1,username);
            pr.setString(2,password);
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

    public static boolean update(String name, int cityId, String eMail, String password) {
        String sql = "update company set companyname = ?, password = ?, email = ?, city_id = ? where id = ?";
        int id = getFetch(name);
        try {
            PreparedStatement preparedStatement = DbConnector.getInstance().prepareStatement(sql);
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,password);
            preparedStatement.setString(3,eMail);
            preparedStatement.setInt(4,cityId);
            preparedStatement.setInt(5,id);
            return preparedStatement.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //id 'ye göre firmayı siler
    public static boolean delete(int selectedId) {
        String sql = "delete from company where id = ?";

        try {
            PreparedStatement preparedStatement = DbConnector.getInstance().prepareStatement(sql);
            preparedStatement.setInt(1,selectedId);
            Car.deleteByFirm(selectedId);
            Rental.deleteByFirmId(selectedId);
            return preparedStatement.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

}
