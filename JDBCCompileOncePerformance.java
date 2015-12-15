import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class JDBCCompileOncePerformance{

  public static void main(String[] args) throws Exception {

    System.out.println("It is a benchmark test");
    
    Connection connect = null;
    Statement selectStat = null;
    Statement insertStat = null;
    PreparedStatement preparedStatement = null;
    ResultSet selectRs = null;

    try {
      // This will load the MySQL driver, each DB has its own driver
      Class.forName("com.mysql.jdbc.Driver");
      // Setup the connection with the DB
      connect = DriverManager
          .getConnection("jdbc:mysql://localhost/ormbenchmark?"
              + "user=root&password=yac336");

      String selectStr = "select * from users";
     
      selectStat = connect.createStatement();

      selectRs = selectStat.executeQuery(selectStr);

          
      int i=0;
      //check and print out database data 
      while(selectRs.next()){
        System.out.println("id is:"+selectRs.getString("id"));
        System.out.println("name is:"+selectRs.getString("name"));
        System.out.println("age is:"+selectRs.getInt("age")); 
        i++;
      }     
     

      if(i==0){
        //no record, insert one line of data
        System.out.println("no data in the database, inserting one line of data");
        System.out.println("inserting id:1,name:Jeffrey,age:35");  
        String insertStr = "insert into users value ('1','Jeffrey', 35)"; 
        insertStat = connect.createStatement();
        insertStat.execute(insertStr);
      }
     
 
      //Now our test begins; Run tests compilation once, and query mulitple times 
      long start = System.nanoTime(); 
      preparedStatement = connect.prepareStatement("select * from users"); 
      for(int j=0;j<10000;j++){
        executePrepareStatement(preparedStatement);
      } 
      double duration = (double) (System.nanoTime()-start)/1000/1000/1000;       
      System.out.println("Compile once, and run multiple times duration is:"+duration); 
      preparedStatement.close();
  

    }catch(Exception e){
      throw e;
    }finally{
      selectRs.close();
      selectStat.close();
      if(insertStat!=null) 
        insertStat.close();
      if(preparedStatement!=null)
        preparedStatement.close();
    }

  }
  
  private static void executePrepareStatement(PreparedStatement pStatment) throws SQLException {
    ResultSet resultSet = pStatment.executeQuery();
    while(resultSet.next()){
      String id=resultSet.getString("id");
      String name=resultSet.getString("name");
      int age = resultSet.getInt("age");
    }  
    resultSet.close(); 
  } 

}


