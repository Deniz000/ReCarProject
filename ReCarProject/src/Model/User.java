package Model;

import com.carRental.Helper.DbConnector;
import com.carRental.Helper.Helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    private int id;
    private String username;
    private String password;

    private Car car;

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public User() {

    }
    public User(int id,String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
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



    //id 'si verilen kullanıcın adını verir
    public static String getFetch(int id){
        String query="SELECT * FROM users WHERE id = ?";
        String name = null;
        try {
            PreparedStatement pr = DbConnector.getInstance().prepareStatement(query);
            pr.setInt(1,id);
            ResultSet rs=pr.executeQuery();
            if (rs.next()){
                name = rs.getString("username");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return name;
    }


    // kullanıcı üyelikleri listelenmesi
    public static User getFetch(String username,String password){
        User obj=null;
        String query="SELECT * FROM users WHERE username =? AND password=?";

        try {
            PreparedStatement pr = DbConnector.getInstance().prepareStatement(query);
            pr.setString(1,username);
            pr.setString(2,password);
            ResultSet rs=pr.executeQuery();
            if (rs.next()){
                obj=new User();
                obj.setId(rs.getInt("id"));
                obj.setUsername(rs.getString("username"));
                obj.setPassword(rs.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return obj;
    }

    // kullanıcı üyeliği ekleme
    public static boolean add(String username,String password){
        String query="INSERT INTO users (username,password) VALUES (?,?)";

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

    // kullanıcı üyelik bilgileri güncelleme
    public static boolean update(User u,String username,String password){
        String query="UPDATE users SET username = ?, password = ? WHERE username = ? AND password = ?";

        try {
            PreparedStatement pr=DbConnector.getInstance().prepareStatement(query);
            pr.setString(1,username);
            pr.setString(2,password);
            pr.setString(3,u.getUsername());
            pr.setString(4,u.getPassword());

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

}
