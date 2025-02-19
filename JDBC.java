//STEP 1. Import required packages
import java.sql.*;
import java.util.*;

public class JDBC {
   //  CONSTANTS ===============================================================
   //  JDBC driver name and database URL
   private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
   private static final String DB_URL = "jdbc:mysql://localhost/cpsc408_2288893_2290022?serverTimezone=UTC";
   private static final String INVALID_TABLE_ERROR = "The inputted table does not exist. Please enter a valid table.";
   private static final String INVALID_ATTRIBUTE_ERROR = "One or more of the attributes is invalid.";

   //  Database credentials
   static final String USER = "jdbcuser";
   static final String PASS = "jdbc";

   //  VARIABLES ===============================================================
   private List<String> list;
   private Connection conn;
   private Statement stmt;

   //  METHODS =================================================================
   //  Constructor - Set up connection stuff
   public JDBC()
   {
     conn = null;
     stmt = null;
   }

   // Establish environment for connecting to database -------------------------
   public void startConn() throws Exception
   {
      //  Register JDBC driver
      Class.forName(JDBC_DRIVER);

      //  Open a connection
      System.out.println("Connecting to database...");
      conn = DriverManager.getConnection(DB_URL,USER,PASS);

      //  Execute SQL Statments
      System.out.println("Creating statement...");
      stmt = conn.createStatement();
   }

   // Close environment variables ----------------------------------------------
   public void endConn()
   {
     // close statement
     try {
        if(stmt!=null)
           stmt.close();
     } catch(SQLException se2){ }  // nothing we can do

     // close connection
     try {
        if(conn!=null)
           conn.close();
     } catch(SQLException se){ se.printStackTrace(); }
   }

   //  INSERT METHODS ==========================================================
   //  Grab Table Attributes and return them as a list (String)
   public List<String> getTableAttributes(String tableName)
   {
     list = new ArrayList<String>();
     try
     {
        startConn(); // establish connection to database

        //  Grab attributes from table
        String sql;
        sql = String.format("SHOW COLUMNS FROM %s", tableName);
        ResultSet rs = stmt.executeQuery(sql);
        //  Grab metadata to count number of columns
        ResultSetMetaData rsmd  = rs.getMetaData();
        Integer columnsNumber = rsmd .getColumnCount();

        //  Extract data from result set
        while(rs.next())
        {
          String columnValue = rs.getString(1);
          list.add(columnValue);
        }
        //  Clean-up environment
        rs.close();
     }
     catch(SQLException se){  //Handle errors for JDBC
       System.out.println(INVALID_TABLE_ERROR);
       list.add("-1");

     } catch(Exception e){ e.printStackTrace(); } //Handle errors for Class.forName
     // close connection to database
     finally{ endConn(); }
     return list;
  }

  // Executes sqlstatement. Returns 1 if success.
  public int insertIntoDatabase(String statement)
  {
    try
    {
      startConn();
      int rows = stmt.executeUpdate(statement);
      System.out.println("Rows inserted into Table: " + rows);
    }

    catch(SQLException se){  //Handle errors for JDBC
      System.out.println(INVALID_ATTRIBUTE_ERROR);
      return 0;

    } catch(Exception e){ e.printStackTrace(); } //Handle errors for Class.forName
    // close connection to database
    finally{ endConn(); }
    return 1;
  }


  // Executes sqlstatement. Returns 1 if success.
  public int updateDatabase(String statement)
  {
    try
    {
      startConn();
      int rows = stmt.executeUpdate(statement);
      System.out.println("Rows updated in Table: " + rows);
    }

    catch(SQLException se){  //Handle errors for JDBC
      System.out.println(INVALID_ATTRIBUTE_ERROR);
      return 0;

    } catch(Exception e){ e.printStackTrace(); } //Handle errors for Class.forName
    // close connection to database
    finally{ endConn(); }
    return 1;
  }


  public List<String> getPrimaryKey(String tableName)
  {
    list = new ArrayList<String>();
    try
    {
       startConn(); // establish connection to database

       //  Grab attributes from table
       String sql;
       sql = String.format("SHOW COLUMNS FROM %s", tableName);
       ResultSet rs = stmt.executeQuery(sql);
       //  Grab metadata to count number of columns
       ResultSetMetaData rsmd  = rs.getMetaData();
       Integer columnsNumber = rsmd .getColumnCount();

       String res = "";
       String temp;
       //  Extract data from result set
       while(rs.next())
       {
         for (int i=1; i<=columnsNumber-2; i++)
         {
           if(rs.getString(i) != null)
            res = res.concat(rs.getString(i));
           res = res.concat(" ");
         }
         res = res.concat(",");
       }

       String[] splitStr = res.trim().split(",");
       for(String s : splitStr)
       {
         String[] tempSplit = s.trim().split("\\s+");
         for(String t : tempSplit)
         {
           if(t.contains("PRI"))
            list.add(tempSplit[0]);
         }
       }
       //  Clean-up environment
       rs.close();
    }
    catch(SQLException se){  //Handle errors for JDBC
      System.out.println(INVALID_TABLE_ERROR);
      list.add("-1");

    } catch(Exception e){ e.printStackTrace(); } //Handle errors for Class.forName
    // close connection to database
    finally{ endConn(); }
    return list;
  }


  // Executes sqlstatement. Returns 1 if success.
  public int deleteFromDatabase(String statement)
  {
    try
    {
      startConn();
      int rows = stmt.executeUpdate(statement);
      System.out.println("Rows deleted from Table: " + rows);
    }

    catch(SQLException se){  //Handle errors for JDBC
      System.out.println(INVALID_ATTRIBUTE_ERROR);
      return 0;

    } catch(Exception e){ e.printStackTrace(); } //Handle errors for Class.forName
    // close connection to database
    finally{ endConn(); }
    return 1;
  }


    // Executes sqlstatement. Returns 1 if success.
    public List<String> query(String statement, int columns)
    {
      list = new ArrayList<String>();
      try
      {
        String temp = "";
        startConn();
        ResultSet rs = stmt.executeQuery(statement);
        //  Extract data from result set
        while(rs.next())
        {
          for (int i=1; i<=columns; i++)
          {
            if(rs.getString(i) != null)
             temp = temp.concat(rs.getString(i));
            temp = temp.concat(" ");
          }
          list.add(temp);
          temp = "";
        }
        System.out.println("finished!");
      }

      catch(SQLException se){  //Handle errors for JDBC
        System.out.println(INVALID_ATTRIBUTE_ERROR);
        list.add("0");
        return list;

      } catch(Exception e){ e.printStackTrace(); } //Handle errors for Class.forName
      // close connection to database
      finally{ endConn(); }
      return list;
    }
}
