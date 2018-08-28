package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBC_TopStudents {
	public static void main(String[] args) throws Exception {
		// Load and register a JDBC driver
		try {
			// Load the driver (registers itself)
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception E) {
			System.err.println("Unable to load driver.");
			E.printStackTrace();
		}
		try {
			// Connect to the database
			Connection conn1;
			String dbUrl = "jdbc:mysql://csdb.cs.iastate.edu:3306/db363gmonroe";
			String user = "dbu363gmonroe";
			String password = "NhuY5883";
			conn1 = DriverManager.getConnection(dbUrl, user, password);
			System.out.println("*** Connected to the database ***");

			// Create Statement and ResultSet variables to use throughout the project
			Statement statement = conn1.createStatement();
			ResultSet rs;
			
			rs = statement.executeQuery("SELECT distinct s.StudentID, p.Name, s.GPA, p2.Name FROM Student s, Person p, Person p2\r\n" + 
					"WHERE s.Classification = \"Senior\" \r\n" + 
					"AND s.MentorID = p2.ID \r\n" + 
					"GROUP BY s.StudentID, p.Name\r\n" + 
					"HAVING AVG(s.GPA) > 3.9\r\n" + 
					"order by GPA DESC;");
					
			while (rs.next()) {
				System.out.println("Student Name: " + rs.getString("p.Name") + " || Mentor Name: " + rs.getString("p2.Name") + " || GPA: " + rs.getString("s.GPA"));
			
			}
			// Close all statements and connections
			statement.close();
			rs.close();
			conn1.close();

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		}
	}
}
