package Model;

import com.carRental.Helper.DbConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class Rental {
    private int id;
    private int carId;
    private int userId;
    private int firmId;
    private Date rentDate;
    private Date returnDate;

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
        return rentDate;
    }

    public void setRentDate(Date rentDate) {
        this.rentDate = rentDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

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


    public static ArrayList<Date> startDateControl(int id){
        String sql = "select * from rentals where id = ?";
        ArrayList<Date> dates = new ArrayList<>();
        Rental rental = null;
        try {
            PreparedStatement preparedStatement = DbConnector.getInstance().prepareStatement(sql);
            preparedStatement.setInt(1,id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                rental = new Rental();
                rental.setRentDate(rs.getDate("rent_date"));
                dates.add(rental.rentDate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dates;
    }

    public static ArrayList<Date> endDateControl(int id){
        String sql = "select * from rentals where id = ?";
        ArrayList<Date> dates = new ArrayList<>();
        Rental rental = null;
        try {
            PreparedStatement preparedStatement = DbConnector.getInstance().prepareStatement(sql);
            preparedStatement.setInt(1,id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                rental = new Rental();
                rental.setReturnDate(rs.getDate("return_date"));
                dates.add(rental.returnDate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dates;
    }

}
