/* Anna George
 * 11.20.2019
 * Project 2 Database
 */

import java.sql.*;
import java.util.Scanner;

public class Project_2 {

	//Add a car to Car_T 
	private static void addCar(Scanner sc, Statement stmt, String myQuery) {
		
		String VIN, manufacturerCode, mileage, dealershipName, price = ""; 
		VIN = sc.next(); 
		manufacturerCode = VIN.substring(0,3);
		mileage = sc.next(); 
		dealershipName = sc.next();
		price = sc.next();
		
		myQuery = "INSERT INTO Car_T VALUES('" +VIN+ "', '" +manufacturerCode+ "', '" +mileage+ "', '" +dealershipName+ "', '" +price+ "')"; 
		try {
			stmt.execute(myQuery);
		} catch (SQLException e) {
			System.out.println("Error - cannot add duplicate VIN");
		} 
	}
	
	//Add a manufacturer to Manufacturer_T 
	private static void addManufacturer(Scanner sc, Statement stmt, String myQuery) {
		
		String VIN, manufacturerCode = ""; 
		VIN = sc.next(); 
		manufacturerCode = sc.next(); 
	
		myQuery = "INSERT INTO Manufacturer_T VALUES('" +VIN+ "', '" +manufacturerCode+ "')";
		try {
			stmt.execute(myQuery);
		} catch (SQLException e) {
			System.out.println("Error - cannot add duplicate manufacturer code");
		} 
	}
	
	//Add a Dealer to Dealer_T 
	private static void addDealer(Scanner sc, Statement stmt, String myQuery) {
		
		String dealerName, zipcode, phoneNum, splitNum1, splitNum2, splitNum3 = ""; 
		dealerName = sc.next(); 
		zipcode = sc.next(); 
		phoneNum = sc.next(); 
		splitNum1 = phoneNum.substring(0,3);
		splitNum2 = phoneNum.substring(3,6); 
		splitNum3 = phoneNum.substring(6,10); 
		phoneNum = "(" +splitNum1+ ")" +splitNum2+ "-" +splitNum3; 
	
		myQuery = "INSERT INTO Dealership_T VALUES('" +dealerName+ "', '" +zipcode+ "', '" +phoneNum+ "')";
		try {
			stmt.execute(myQuery);
		} catch (SQLException e) {
			System.out.println("Error - cannot add duplicate dealer name");
		} 
	}
	
	//List all cars in Car_T 
	private static void listCars(Statement stmt, String myQuery) throws SQLException {
		
		ResultSet rs = null;
		myQuery = "SELECT * FROM Car_T ORDER BY VIN";
		try {
			rs = stmt.executeQuery(myQuery);
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
		while (rs.next()) {
			//Print needed column rows 
		    System.out.print(rs.getString(1) + " "); 
		    System.out.print(rs.getString(3) + " ");
		    System.out.print(rs.getString(4) + " $");
		    System.out.println(rs.getString(5));
		}
	}
	
	//List all dealers in Dealership_T
	private static void listDealers(Statement stmt, String myQuery) throws SQLException {
		
		ResultSet rs = null;
		myQuery = "SELECT * FROM Dealership_T ORDER BY Zipcode, DealershipName";
		try {
			
			rs = stmt.executeQuery(myQuery);
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
		while (rs.next()) {
			//Print needed column rows 
		    System.out.print(rs.getString(1) + " "); 
		    System.out.print(rs.getString(2) + " ");
		    System.out.println(rs.getString(3));
		}
	}
	
	//Delete car with specified VIN 
	private static void deleteCar(Scanner sc, Statement stmt, String myQuery) {
		
		String VIN = sc.next(); 
		myQuery = "DELETE FROM Car_T WHERE VIN = '" +VIN+ "'"; 
		try {
			stmt.execute(myQuery);
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
	}
	
	//Delete dealer with specified dealer name
	private static void deleteDealer(Scanner sc, Statement stmt, String myQuery) {
		
		String dealerName = sc.next(); 
		myQuery = "DELETE FROM Dealership_T WHERE DealershipName = '" +dealerName+ "'";
		try {
			stmt.execute(myQuery);
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
		myQuery = "DELETE FROM Car_T WHERE DealershipName = '" +dealerName+ "'";
		try {
			stmt.execute(myQuery);
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	}
	
	//Print out line for each manufacturer with average price of the cars made by that manufacturer
	private static void summarize(Statement stmt, String myQuery) throws SQLException {
		
		ResultSet rs = null;
		myQuery = "SELECT Car_T.ManufacturerCode, AVG(Price) " + 
			"FROM Car_T " +
			"GROUP BY Car_T.ManufacturerCode"; 
		try {
			rs = stmt.executeQuery(myQuery);
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
		while (rs.next()) {
			//Print needed column rows 
		    System.out.print(rs.getString(1) + " ");
		    System.out.println(rs.getString(2));
		}
	}
	
	//Print out information associated with a zipcode 
	private static void findZipcode(Scanner sc, Statement stmt, String myQuery) throws SQLException {
		
		ResultSet rs = null;
		String zipcode = sc.next(); 
		myQuery = "SELECT Car_T.VIN, Car_T.Mileage, Car_T.Price, Car_T.DealershipName, Dealership_T.PhoneNum " +
			"FROM Dealership_T, Car_T WHERE Dealership_T.DealershipName = Car_T.DealershipName " +
			"AND Dealership_T.Zipcode = '" +zipcode+ "' " +
			"ORDER BY Dealership_T.DealershipName ASC, Car_T.Price DESC, Car_T.DealershipName";
		try {
			rs = stmt.executeQuery(myQuery);
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
		while (rs.next()) {
			//Print needed column rows 
			System.out.print(rs.getString(1) + " "); 
			System.out.print(rs.getString(2) + " $");
		    System.out.print(rs.getString(3) + " ");
		    System.out.print(rs.getString(4) + " ");
		    System.out.println(rs.getString(5));
		}
	}
	
	//Print out information associated with a manufacturer
	private static void findManufacturer(Scanner sc, Statement stmt, String myQuery) throws SQLException {
		
		ResultSet rs = null;
		String manufacturerName = sc.next(); 
		myQuery = "SELECT * FROM Car_T, Manufacturer_T, Dealership_T " +
		"WHERE ManufacturerName = '" +manufacturerName+ "' " +
		"AND Car_T.ManufacturerCode = Manufacturer_T.ManufacturerCode" +
		"ORDER BY Car_T.Price DESC, Car_T.Mileage ASC, Car_T.DealershipName";
		try {
			rs = stmt.executeQuery(myQuery);
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
		while (rs.next()) {
			//Print needed column rows 
			System.out.print(rs.getString(1) + " "); 
			System.out.print(rs.getString(2) + " $");
		    System.out.print(rs.getString(3) + " ");
		    System.out.print(rs.getString(4) + " ");
		    System.out.println(rs.getString(5));
		}
   }
	
   //JDBC driver name and database URL
   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
   static final String DB_URL = "jdbc:mysql://lindenwoodcshome.ddns.net/sblythe";

   //Database credentials
   static final String USER = "username";
   static final String PASS = "password";
   
   public static void main(String[] args) {
   
	    Scanner kbd = new Scanner(System.in);
	   
	    Connection conn; //The actual mysql server connection
	    Statement stmt;
	   
   		//Strings for mysql hostname, userid, password
		String db_host;
		String db_user;
		String db_password;
	   
		//Get user credentials and mysql server info
		System.out.print("Enter MySQL database hostname (or IP adress):");
		db_host = kbd.nextLine();

		System.out.print("Enter MySQL database username:");
		db_user = kbd.nextLine();

		System.out.print("Enter MySQL database password:");
		db_password=kbd.nextLine(); 
		
		db_host = "jdbc:mysql://" + db_host+ "/" + db_user;
		
	   //Most mysql calls can cause exceptions, so we'll try to catch them. 
	   try {
		   
		   //Set up the client (this machine) to use mysql
		   System.out.print("Initializing client DB subsystem ...");
		   Class.forName("com.mysql.jdbc.Driver");
		   System.out.println("Done.");
		   			   
		   //Go out and connect to the mysql server
		   System.out.print("Connecting to remote DB ...");
		   conn = DriverManager.getConnection(db_host,db_user, db_password);
		   System.out.println("DB connection established.");
		   
		   //Send mysql our query
		   stmt = conn.createStatement();
		   
		   //Create each table 
     	   String myQuery = "CREATE TABLE IF NOT EXISTS Car_T (VIN varchar(30), " +
      			  "ManufacturerCode varchar(30), Mileage varchar(30), " +
     			  "DealershipName varchar(30), Price varchar(30), PRIMARY KEY(VIN) )";
     	   stmt.execute(myQuery);     
     	   
     	   myQuery = "CREATE TABLE IF NOT EXISTS Manufacturer_T " + "(ManufacturerCode varchar(30), " +
     			  "ManufacturerName varchar(30), PRIMARY KEY (ManufacturerCode) )";
     	   stmt.execute(myQuery);
     	 
     	   myQuery = "CREATE TABLE IF NOT EXISTS Dealership_T " + "(DealershipName varchar(30), " +
     			  "Zipcode varchar(30), PhoneNum varchar(30), PRIMARY KEY (DealershipName) )";
     	   stmt.execute(myQuery);
     	   
     	   String firstLetter, secondLetter = ""; 
     	   do {
     		   System.out.print(">>"); //tells user to enter letters and/or data
			   firstLetter = kbd.next(); 
			   if (!firstLetter.equals("q")) { //check that program is not quitting
				   if(firstLetter.equals("s")) {
					   summarize(stmt, myQuery); //print out line for each manufacturer
				   } else {
					   secondLetter = kbd.next(); //read in second letter of input
				   }
			   }
			   if (firstLetter.equals("a")) {
				   if (secondLetter.equals("c")) {
					   addCar(kbd, stmt, myQuery); //add car to Car_T 
				   }
				   else if (secondLetter.equals("m")) {
					   addManufacturer(kbd, stmt, myQuery); //add manufacturer to Manufacturer_T
				   }
				   else if (secondLetter.equals("d")) {
					   addDealer(kbd, stmt, myQuery); //add dealer to Dealer_T
				   }
				   else {
					   break;
				   }
			   }
			   else if (firstLetter.equals("l")) {
				   if (secondLetter.equals("c")) {
					   listCars(stmt, myQuery); //list all cars
				   }
				   else if (secondLetter.equals("d")) {
					   listDealers(stmt, myQuery); //list all dealers
				   }
				   else {
					   break;
				   }
			   }
			   else if (firstLetter.equals("f")) {
				   if (secondLetter.equals("m")) {
					   findManufacturer(kbd, stmt, myQuery); //find specified manufacturer with associated info
				   }
				   else if (secondLetter.equals("z")) {
					   findZipcode(kbd, stmt, myQuery); //find specified zipcode with associated info
				   }
				   else {
					   break;
				   }
			   }
			   else if (firstLetter.equals("d")) {
				   if (secondLetter.equals("c")) {
					   deleteCar(kbd, stmt, myQuery); //delete specified car 
				   }
				   else if (secondLetter.equals("d")) {
					   deleteDealer(kbd, stmt, myQuery); //delete specified dealer 
				   }
				   else {
					   break;
				   }
			   }
  	   } while (!firstLetter.equalsIgnoreCase("q")); //Check if quitting program
     	   conn.close(); 
	   }
	   catch(SQLException se) {
		   //Handle errors for JDBC
		   se.printStackTrace();
	   }
	   catch(Exception e) {
		   //Handle any other exceptions
		   System.out.println("Exception"+e);
	   }
   }
}