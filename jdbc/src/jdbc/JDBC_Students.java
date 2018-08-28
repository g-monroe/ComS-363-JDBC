package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBC_Students {
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
			
			String enrollDB = "Enrollment e";
			String studentDB = "Student s";
			// get salaries of all instructors
			rs = statement.executeQuery("select * from " + studentDB + "," + enrollDB + " WHERE s.StudentID = e.StudentID ORDER BY s.StudentID");
													 
			int currStudent = 0;
			double grades = 0.0;
			int courses  = 0;
			while (rs.next()) {
				int newStudent = rs.getInt("StudentID");
				if (newStudent != currStudent) {
					
					//Declare
					int currCrdHrs = rs.getInt("CreditHours");
					int newCrdHrs = (courses * 3) + currCrdHrs;
					String classification = rs.getString("Classification");
					double oldGPA = rs.getInt("GPA");
					double newGPA =  ((oldGPA * currCrdHrs) + grades) / newCrdHrs;
					//round
					newGPA = Math.round((newGPA*100) /100.0);
					//Print and Assign Classification
					if (newCrdHrs <= 29) { //Freshman
						classification = "Freshman";
						System.out.println("Student ID: " + currStudent + " || NEW Grades: " + newGPA + " || New Credits: " + newCrdHrs + " || Current Credits: " + currCrdHrs + " || Class: Freshman");
					}else if (newCrdHrs >= 30 && newCrdHrs <= 59) {//Sophomore
						classification = "Sophomore";
						System.out.println("Student ID: " + currStudent + " || NEW Grades: " + newGPA + " || New Credits: " + newCrdHrs + " || Current Credits: " + currCrdHrs + " || Class: Sophomore");
					}else if (newCrdHrs >= 60 && newCrdHrs <= 89) {//Junior
						classification = "Junior";
						System.out.println("Student ID: " + currStudent + " || NEW Grades: " + newGPA + " || New Credits: " + newCrdHrs + " || Current Credits: " + currCrdHrs + " || Class: Junior");
					}else if (newCrdHrs >= 90) {//Senior
						classification = "Senior";
						System.out.println("Student ID: " + currStudent + " || NEW Grades: " + newGPA + " || New Credits: " + newCrdHrs + " || Current Credits: " + currCrdHrs + " || Class: Senior");
					}
					
					// Send Update
					
					Statement updateStatement = conn1.createStatement();
					int updateResult;
					
					updateResult = updateStatement.executeUpdate("Update " + studentDB + " SET s.Classification = \"" + classification + "\",  s.GPA = " + newGPA + " WHERE s.StudentID = " + currStudent);
					System.out.println("RESULT: " + updateResult);
					
					updateStatement.close();
					//Reset
					currStudent = newStudent;
					grades = 0.0;
					courses = 0;
				}else {
					//Continue
					grades += lttrToDble(rs.getString("Grade")) * 3;
					courses++;
				}
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
	public static double lttrToDble(String grade) {
		grade.trim();
		if (grade.equals("A")) {
			return 4.00;
		}else if (grade.equals("A-")) {
			return 3.66;
		}else if (grade.equals("B+")) {
			return 3.33;
		}else if (grade.equals("B")) {
			return 3.00;
		}else if (grade.equals("B-")) {
			return 2.66;
		}else if (grade.equals("C+")) {
			return 2.33;
		}else if (grade.equals("C")) {
			return 2.00;
		}else if (grade.equals("C-")) {
			return 1.66;
		}else if (grade.equals("D+")) {
			return 1.33;
		}else if (grade.equals("D")) {
			return 1.00;
		}
		return 0.00;
	}
}
