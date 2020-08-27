package it.polito.ezgas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BootEZGasApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootEZGasApplication.class, args);
	}
	
	@PostConstruct
	public void setupDbWithData() throws SQLException{
		
		/* TABLE PUBLIC.USER
		 	USER_ID INTEGER DEFAULT (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_A60A1CEA_5B49_4737_85C5_788DD51A7D6C) NOT NULL NULL_TO_DEFAULT SEQUENCE PUBLIC.SYSTEM_SEQUENCE_A60A1CEA_5B49_4737_85C5_788DD51A7D6C,
		    ADMIN BOOLEAN,
		    EMAIL VARCHAR(255),
		    PASSWORD VARCHAR(255),
		    REPUTATION INTEGER,
		    USER_NAME VARCHAR(255)
		 */
		
		System.out.println("STARTING SETUB_DB_WITH_DATA");
		Connection conn = DriverManager.getConnection("jdbc:h2:./data/memo", "sa", "password");
		Statement stmt = null;
		String queryFindAll =  "SELECT * "
								+ "FROM USER";
		boolean foundAdmin = false;
		
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(queryFindAll);
			while(rs.next()) {
				int userId = rs.getInt("USER_ID");
		        boolean isAdmin = rs.getBoolean("ADMIN");
		        String password = rs.getString("PASSWORD");
		        int reputation = rs.getInt("REPUTATION");
		        String username = rs.getString("USER_NAME");
		        String email = rs.getString("EMAIL");
				System.out.println(userId + "\t" + username + "\t" + password + "\t\t" + email + "\t" + reputation + "\t" + isAdmin);
				if(!foundAdmin && isAdmin)
					foundAdmin = true;
			}
			
			if(!foundAdmin) {
				String queryAddAdmin = "INSERT INTO USER (USER_NAME, PASSWORD, EMAIL, REPUTATION, ADMIN) "
									+ "VALUES ('admin', 'admin', 'admin@ezgas.com', 5, TRUE)";
				stmt.executeUpdate(queryAddAdmin);
			}
		} catch (SQLException e) {
			System.out.println("Errore accesso database in setupDbWithData");
		} finally {
			stmt.close();
			conn.close();
		}
		
		System.out.println("EXITING SETUB_DB_WITH_DATA");
	
		
		
		/*
		list all the users stored in the database and, if there is no an admin user create it
		 
			User user= new User("admin", "admin", "admin@ezgas.com", 5);
			user.setAdmin(true);
			
		and then save it in the db
	
		*/

	}

}
