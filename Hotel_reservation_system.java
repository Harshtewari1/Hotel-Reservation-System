import  java.sql.ResultSet;
import java.sql.SQLException;
import  java.sql.Statement;
import java.sql.Connection;
import  java.sql.DriverManager;
import java.util.Scanner;

public class Hotel_reservation_system {
    private static final String url = "jdbc:mysql://localhost:3306/hotel_reservation_db";
    private static final String username = "root";
    private static final String password = "rOOT@1234";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            while (true) {
                System.out.println();
                System.out.println("HOTEL MANAGEMENT SYSTEM ");
                Scanner scanner = new Scanner(System.in);
                System.out.println("1 : Reserve a room ;");
                System.out.println("2 : View reservation room :");
                System.out.println(" 3 : Get room no . :");
                System.out.println("4 :  Update reservation : ");
                System.out.println(" 5 : Delete reservation : ");
                System.out.println(" 0 : Exit : ");
                System.out.println(" Choose an option : ");
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        reserveRoom(connection,scanner,statement);
                        break;
                    case 2:
                        viewReservation(connection,statement);
                        break;
                    case 3:
                        getRoomNumber(connection,scanner,statement);
                        break;
                    case 4:
                        updateReservation(connection , scanner , statement);
                        break;
                    case 5:
                        deleteReservation(connection , scanner , statement );
                        break;
                    case 0 :
                        exit();
                        scanner.close();
                        return;
                    default:
                        System.out.println(" invalid Entry ");


                }
            }
        }catch (SQLException e ){
            System.out.println(e.getMessage());
        }catch (InterruptedException e){
            throw new RuntimeException();
        }


    }

    private static void reserveRoom(Connection connection, Scanner scanner, Statement statement) {
        try {
            System.out.println("Enter Guest Name : ");
            String guestName = scanner.next();
            scanner.nextLine();
            System.out.println("Enter Room No . :");
            int roomNumber = scanner.nextInt();
            System.out.println("Enter Contact No.");
            String contactNumber = scanner.next();


            String sql = " INSERT INTO reservations(guest_name , room_no , contact_no)  VALUES (' " + guestName + " ' , " + roomNumber + " , ' " + contactNumber + " ' )";

            try {
                int affectedRow = statement.executeUpdate(sql);

                if (affectedRow > 0) {
                    System.out.println("Reservation Succesfully !");
                } else {
                    System.out.println(" Reservation failed !");
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private static void viewReservation(Connection connection, Statement statement) throws SQLException {
        String sql = " SELECT reservation_id , guest_name , room_no , contact_no , reservation_date FROM reservations";
        try (ResultSet resultSet = statement.executeQuery(sql)) {
            System.out.println();
            while (resultSet.next()) {
                int reservationId = resultSet.getInt("reservation_id");
                String guestName = resultSet.getString("guest_name");
                int roomNumber = resultSet.getInt("room_no");
                String contactNumber = resultSet.getString("contact_no");
                String reservationDate = resultSet.getString("reservation_date").toString();

                System.out.println("===========************************===========================");
                System.out.println(" Reservartion Id : " + reservationId);
                System.out.println("Guest Name  : " + guestName);
                System.out.println("Room Number : " + roomNumber);
                System.out.println(" Contact Number : " + contactNumber);
                System.out.println(" Regestration Date :" + reservationDate);
                System.out.println("===========************************===========================");
            }
        }
    }

    private static void getRoomNumber(Connection connection, Scanner scanner, Statement statement) {
        try {
            System.out.println("Enter Reservation Id : ");
            int reservationId = scanner.nextInt();
            System.out.println("Guest Name : ");
            String guestName = scanner.next();

            String sql = "SELECT room_no FROM reservations " +
                    "WHERE reservation_id = " + reservationId +
                    " AND guest_name = '" + guestName + "'";

//            String sql = "SELECT room_no FROM reservations " +
//                    "WHERE reservation_id = " + reservationId +
//                    "AND guest_name = " + guestName + " ' ";

            try (ResultSet resultSet = statement.executeQuery(sql)) {
                if (resultSet.next()) {
                    int roomNumber = resultSet.getInt("room_no.");
                    System.out.println(" Room Number for Reservation id " + reservationId + " and Guest " + guestName + " roomNumber : " + roomNumber);
                } else {
                    System.out.println("Reservation is not found for the given name or id ");
                }

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
    }

    private static void updateReservation(Connection connection, Scanner scanner, Statement statement) {
        System.out.println("Enter Reservation Id to update :");
        int reservationId = scanner.nextInt();
        scanner.nextLine();

        if (!reservationExits(connection, statement, reservationId)) {
            System.out.println("Reservation not fount for the given Id.");
            return;
        }
        System.out.println("Enter new Guest Name :");
        String newGuestName = scanner.nextLine();
        System.out.println("Enter new room number :");
        int newRoomNumber = scanner.nextInt();
        System.out.println("Enter New Contact Number :");
        String newContactNumber = scanner.nextLine();

//        String sql = "UPDATE  reservations SET guest_name = ' " + newGuestName + " ', " +  " room_no. = " + newRoomNumber + " ," + "contact_no. =  ' " + newContactNumber + " '  " +
//                "WHERE  reservation_id = " + reservationId;
        String sql = "UPDATE reservations SET guest_name = '" + newGuestName + "', room_no = " + newRoomNumber + ", contact_no = '" + newContactNumber +
                "' WHERE reservation_id = " + reservationId;

        try {
            int affectedRows = statement.executeUpdate(sql);

            if (affectedRows > 0) {
                System.out.println("Reservation Update Succesfully !");
            } else {
                System.out.println("Reservation Failed ! ");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteReservation(Connection connection, Scanner scanner, Statement statement) {
        try {
            System.out.println("Enter Reservation Id to Delete : ");
            int reservationId = scanner.nextInt();

            if (!reservationExits(connection, statement, reservationId)) {
                System.out.println("Reservation not Found for the given Id .");
                return;
            }
            String sql = "DELETE FROM reservations WHERE  reservation_id = " + reservationId;
            try {
                int affectedRow = statement.executeUpdate(sql);

                if (affectedRow > 0) {
                    System.out.println("Reservation Deleted Successfully ! ");
                } else {
                    System.out.println("Reservation Deleted Failed :");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

    }

    private static boolean reservationExits(Connection connection , Statement statement , int reservationId)  {
        try{
            String sql = "SELECT reservation_id FROM reservations WHERE reservation_id = " + reservationId ;
            try(ResultSet resultSet = statement.executeQuery(sql)){
                return resultSet.next();

            }catch (SQLException e){
                e.printStackTrace();
                return false ;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void exit() throws InterruptedException {
        System.out.println("Exiting System ") ;
        int i = 5 ;
        while (i != 0){
            System.out.print(" . ");
            Thread.sleep(450);
            i--;
        }
        System.out.println();
        System.out.println("Thank You For Using Hotel Reservation System ");
    }


}
