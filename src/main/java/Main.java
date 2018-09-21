import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {

  public static void main(String args[]) throws Exception {
    Class.forName("com.mysql.jdbc.Driver");
    String URL = "jdbc:mysql://localhost:3306/kiosk?autoReconnect=true&amp;createDatabaseIfNotExist=true&amp;";
    Connection dbConn = DriverManager.getConnection(URL, "root", "root");
    Employee employee = new Employee(42, "AA", 9);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    oos.writeObject(employee);
    byte[] employeeAsBytes = baos.toByteArray();
    PreparedStatement pstmt = dbConn
        .prepareStatement("INSERT INTO orders (orders_details_blob) VALUES(?)");
    ByteArrayInputStream bais = new ByteArrayInputStream(employeeAsBytes);
    pstmt.setBinaryStream(1, bais, employeeAsBytes.length);
    pstmt.executeUpdate();
    pstmt.close();
    Statement stmt = dbConn.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT emp FROM Employee");
    while (rs.next()) {
      byte[] st = (byte[]) rs.getObject(1);
      ByteArrayInputStream baip = new ByteArrayInputStream(st);
      ObjectInputStream ois = new ObjectInputStream(baip);
      Employee emp = (Employee) ois.readObject();
    }
    stmt.close();
    rs.close();
    dbConn.close();
  }
}

class Employee implements Serializable {
  int ID;
  String name;
  double salary;

  public Employee(int ID, String name, double salary) {
    this.ID = ID;
    this.name = name;
    this.salary = salary;
  }
}