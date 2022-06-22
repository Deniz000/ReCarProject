package Model;

import com.carRental.Helper.DbConnector;
import com.carRental.Helper.Helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class Rental {
    private int id;
    private int carId;
    private int userId;
    private int firmId;
    private static Date rentDate;
    private static Date returnDate;

    private Car car;
    private User user;
    private Company company;

    public Rental() {
    }

    public Rental(int id, int carId, int userId, int firmId, Date rentDate, Date returnDate) {
        this.id = id;
        this.carId = carId;
        this.userId = userId;
        this.firmId = firmId;
        this.rentDate = rentDate;
        this.returnDate = returnDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFirmId() {
        return firmId;
    }

    public void setFirmId(int firmId) {
        this.firmId = firmId;
    }

    public Date getRentDate() {
        return this.rentDate;
    }

    public void setRentDate(Date rentDate) {
        this.rentDate = rentDate;
    }

    public Date getReturnDate() {
        return this.returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    // id 'si verilen firmanın rezervasyon işlemlerini listeler
    public static ArrayList<Rental> getListForCompany(int id){
        ArrayList<Rental> rentals = new ArrayList<>();
        String sql = "select * from rentals where firm_id = ?";
        Rental rental = null;
        try {
            PreparedStatement preparedStatement = DbConnector.getInstance().prepareStatement(sql);
            preparedStatement.setInt(1,id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                rental = new Rental();
                rental.setId(rs.getInt("id"));
                rental.setCarId(rs.getInt("car_id"));
                rental.setUserId(rs.getInt("user_id"));
                rental.setRentDate(rs.getDate("rent_date"));
                rental.setReturnDate(rs.getDate("return_date"));
                rentals.add(rental);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rentals;
    }


    public static boolean add(int car_id,int user_id,String rent_date,String return_date ,int firm_id){
        String query="INSERT INTO rentals (car_id,user_id,rent_date,return_date,firm_id) VALUES (?,?,?,?,?)";

        try {
            PreparedStatement pr=DbConnector.getInstance().prepareStatement(query);
            pr.setInt(1,car_id);
            pr.setInt(2,user_id);
            pr.setString(3,rent_date);
            pr.setString(4,return_date);
            pr.setInt(5,firm_id);
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

    // id 'si verilen kullanıcının rezervasyon işlemlerini listeler
    public static ArrayList<Rental> getListForUser(int id){
        ArrayList<Rental> rentals = new ArrayList<>();
        String sql = "select * from rentals where user_id = ?";
        Rental rental = null;
        try {
            PreparedStatement preparedStatement = DbConnector.getInstance().prepareStatement(sql);
            preparedStatement.setInt(1,id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                rental = new Rental();
                rental.setId(rs.getInt("id"));
                rental.setCarId(rs.getInt("car_id"));
                rental.setFirmId(rs.getInt("firm_id"));
                rental.setRentDate(rs.getDate("rent_date"));
                rental.setReturnDate(rs.getDate("return_date"));
                rentals.add(rental);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rentals;
    }

    // Firmalar araç ekleme silme yapabiliyordu, bi aracı sildiği zaman foreign key durumundan dolayı aracın rezervasyon listesi de siliniyor
    public static boolean deleteByCarId(int id){
        String sql = "delete from rentals where car_id = ?";
        try {
            PreparedStatement preparedStatement = DbConnector.getInstance().prepareStatement(sql);
            preparedStatement.setInt(1,id);
            return preparedStatement.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    //Firma hesabını sildiğinde kendisine ait rezervasyon listesi de siliniyor
    public static boolean deleteByFirmId(int id){
        String sql = "delete from rentals where firm_id = ?";
        try {
            PreparedStatement preparedStatement = DbConnector.getInstance().prepareStatement(sql);
            preparedStatement.setInt(1,id);
            return preparedStatement.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}
