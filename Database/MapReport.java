// CTS2: Simeunovic, Savo (3138210)

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;

public class MapReport {

	private static Connection c = null;
	private static PreparedStatement[] mapinfo = new PreparedStatement[10];
	private static ResultSet mapresult = null;
	private static ResultSet mapresult1 = null;
	private static final String driverClass = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private static final String connectionDescriptor =
			"jdbc:sqlserver://i-mssql-01.informatik.hs-ulm.de;databasename=kratzer_db";
	
	public static void main(String[] args) {
		try {
			Class.forName(driverClass);
			c =  DriverManager.getConnection(connectionDescriptor, "reader_kratzer_db", "thu123456!");
			makePreparedStatements();
			mapresult1 = mapinfo[0].executeQuery();
			while (mapresult1.next()) {
				int id = mapresult1.getInt(1);
				String mapName = getMapName(id);			// returns Name of the map for given ID
				int numOfCities = getNumOfCities(id);		// returns number of cities for given mapID
				int numOfRoads = numOfRoads(id);			// returns number of Roads for given mapID
				int avarageRoadLength = getAvarage(id);		// returns avarage road length for given mapID
				String data[] = getBiggestRoadAndCities(id);	// 0-distance; 1-1st city; 2-2nd city (indexes)
				int biggestDistance = Integer.parseInt(data[0]);
				String city1 = getCityName(id, data[1]);
				String city2 = getCityName(id, data[2]);
				System.out.print(
					"---------------------------------------\n" +
					"Map " + mapName + " (" + Integer.toString(id) + "):\n" +
					"Cities: " + Integer.toString(numOfCities) + "\n" +
					"Roads: " + Integer.toString(numOfRoads) + "\n" +
					"Average Road Length: " + Integer.toString(avarageRoadLength) + " km\n" +
					"The longest road runs from " + city1 + " to " + city2 + " - length: " + Integer.toString(biggestDistance)  + " km\n" +
					"---------------------------------------\n"
				);
			}
			mapresult.close();
		} catch (ClassNotFoundException e) {
			System.out.println("Unable to open driver class ...");
		} catch (SQLException e) {
			System.out.println("Database error: " + e.getMessage());
		} finally {
			try {
				if (c != null) c.close();
			} catch (SQLException e) {}
		}
	}
	private static void makePreparedStatements() {
		try {
			mapinfo[0] = c.prepareStatement(
					"SELECT	M.ID " +
					"FROM	[HS-ULM\\KRATZER].MAP M " +
					"ORDER BY M.ID ASC "					
			);
			
			mapinfo[1] = c.prepareStatement(
					"SELECT	M.Name " +
					"FROM	[HS-ULM\\KRATZER].MAP M " +
					"WHERE M.ID = ? "					
			);
			
			mapinfo[2] = c.prepareStatement(
					"SELECT	COUNT(*) " +
					"FROM	[HS-ULM\\KRATZER].CITY C " +
					"WHERE C.MapID = ? "					
			);
			
			mapinfo[3] = c.prepareStatement(
					"SELECT	COUNT(*) " +
					"FROM	[HS-ULM\\KRATZER].ROAD R " +
					"WHERE R.MapID = ? AND R.IDfrom < R.IDto "					
			);
			
			mapinfo[4] = c.prepareStatement(
					"SELECT	AVG(R.Distance) " +
					"FROM	[HS-ULM\\KRATZER].ROAD R " +
					"WHERE R.MapID = ? AND R.IDfrom < R.IDto "					
			);
			
			mapinfo[5] = c.prepareStatement(
					"SELECT	R.Distance, R.IDfrom, R.IDto, R.MapID " +
					"FROM	[HS-ULM\\KRATZER].ROAD R " +
					"WHERE R.MapID = ? AND R.IDfrom < R.IDto AND R.Distance = ( " +
					"SELECT MAX(R1.Distance) FROM [HS-ULM\\KRATZER].ROAD R1 " +
					"WHERE R1.MapID = R.MapID) "
			);
			
			mapinfo[6] = c.prepareStatement(
					"SELECT	C.Name " +
					"FROM	[HS-ULM\\KRATZER].CITY C " +
					"WHERE C.MapID = ? AND C.ID = ? "					
			);
		}catch (SQLException e) {
			System.out.println("Database error: " + e.getMessage());
		} finally {
		}
	}
	private static String getCityName(int n, String cityID) {
		try {
			mapinfo[6].setInt(1, n);
			mapinfo[6].setString(2, cityID);
			mapresult = mapinfo[6].executeQuery();
			String name = "-1";
			while (mapresult.next()) {
				name = mapresult.getString(1);
			}
			return name;
		}catch (SQLException e) {
			System.out.println("Database error: " + e.getMessage());
			return "-1";
		} finally {
		}
	}
	private static String[] getBiggestRoadAndCities(int n) {
		try {
			mapinfo[5].setInt(1, n);
			mapresult = mapinfo[5].executeQuery();
			String data[] = new String[3];
			while (mapresult.next()) {
				data[0] = Integer.toString(mapresult.getInt(1));
				data[1] = mapresult.getString(2);
				data[2] = mapresult.getString(3);
			}
			return data;
		}catch (SQLException e) {
			System.out.println("Database error: " + e.getMessage());
			String[] temp = {"-1", "-", "-"};
			return temp;
		} finally {
		}
	}
	private static int getAvarage(int n) {
		try {
			mapinfo[4].setInt(1, n);
			mapresult = mapinfo[4].executeQuery();
			int num = 0;
			while (mapresult.next()) {
				num = mapresult.getInt(1);
			}
			return num;
		}catch (SQLException e) {
			System.out.println("Database error: " + e.getMessage());
			return -1;
		} finally {
		}
	}
	
	
	private static int numOfRoads(int n) {
		try {
			mapinfo[3].setInt(1, n);
			mapresult = mapinfo[3].executeQuery();
			int num = 0;
			while (mapresult.next()) {
				num = mapresult.getInt(1);
			}
			return num;
		}catch (SQLException e) {
			System.out.println("Database error: " + e.getMessage());
			return -1;
		} finally {
		}
	}
	
	
	private static int getNumOfCities(int n) {
		try {
			mapinfo[2].setInt(1, n);
			mapresult = mapinfo[2].executeQuery();
			int num = 0;
			while (mapresult.next()) {
				num = mapresult.getInt(1);
			}
			return num;
		}catch (SQLException e) {
			System.out.println("Database error: " + e.getMessage());
			return -1;
		} finally {
		}
	}
	
	
	private static String getMapName(int n) {
		try {
			mapinfo[1].setInt(1, n);
			mapresult = mapinfo[1].executeQuery();
			String name = "";
			while (mapresult.next()) {
				name = mapresult.getString(1);
			}
			return name;
		}catch (SQLException e) {
			System.out.println("Database error: " + e.getMessage());
			return "-1";
		} finally {
		}
	}
}
