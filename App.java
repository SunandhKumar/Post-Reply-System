import java.sql.*;
import java.util.*;

public class App{
    static void printreply(int num,int c){
        Connection connection = null;
        
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/demo",
				"root", "7780519892");

			Statement statement;
			statement = connection.createStatement();
            String sqlp="select * from replies where parentid= "+num;
            ResultSet r;
            r = statement.executeQuery(sqlp);
            while (r.next()){
                int t=r.getInt("replyid");
                for(int i=0;i<=c;i++){
                    System.out.print("\t");
                }
                System.out.println("No: "+t+":  "+r.getString("username")+"->>  "+r.getString("reply"));
                printreply(t,c+1);
            }
            return;
        }
        catch(Exception e){
            System.out.println(e);
        }

    }
	public static void main(String arg[])
	{
		Connection connection = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/demo",
				"root", "7780519892");
            
            Scanner sc=new Scanner(System.in);

			Statement statement;
			statement = connection.createStatement();

			ResultSet resultSet;
			resultSet = statement.executeQuery(
				"select * from login");

            String[] s=new String[100];
            int len=0;
			String username,email,password;

			while (resultSet.next()) {
				s[len++]= resultSet.getString("username");
			}

            System.out.println("Enter 1 for login or 2 for signup:  ");
            int login=sc.nextInt();
            if(login!=1){
                System.out.println("Enter username:");
                sc.nextLine();
                while(1>0){
                    username=sc.nextLine();
                    while(username.length()==0){
                        System.out.println("username cannot be Empty please enter username: ");
                        username=sc.nextLine();
                    }
                    int f=0;
                    for(int j=0;j<len;j++){
                        if(s[j].equalsIgnoreCase(username)){
                            f=1;
                            break;
                        }
                    }
                    if(f==1){
                        System.out.println("User Name is already used Choose another:");
                    }
                    else{
                        System.out.println("user name saved..");
                        break;
                    }
                }
                System.out.println("Enter email:");
                email=sc.nextLine();
                while(email.length()==0){
                    System.out.println("Email cannot be Empty please enter Email");
                    email=sc.nextLine();
                }
                System.out.println("Email_ID saved..");
                System.out.println("Enter password:");
                password=sc.nextLine();
                while(password.length()==0){
                    System.out.println("Password cannot be Empty please enter password");
                    password=sc.next();
                }
                System.out.println("Account Created Successfully..");


                String sql="insert into login(username,email,password) values(?,?,?)";
                PreparedStatement ps;
                ps=connection.prepareStatement(sql);
                ps.setString(1,username);
                ps.setString(2,email);
                ps.setString(3,password);
                ps.executeUpdate();
                System.out.println("Details Stored in Database Successfully");

            }
            else{
                System.out.println("Enter username:");
                username=sc.next();
                System.out.println("Enter password:");
                password=sc.next();
                String sql="select * from login where username='" + username +"' and password ='" + password+"'";
                ResultSet result;
			    result = statement.executeQuery(sql);
                if (result.next()){
                    System.out.println("Account Found...");
                    System.out.println("UserName: "+username);
                    System.out.println("Email: "+result.getString("email"));
                    int id=result.getInt("id");
                    System.out.println("ID No: "+id);

                    String message="";
                    System.out.println("Enter \n 1 -> To post Question \n 2 -> To Answer Questions \n 3 -> Exit...");
                    int ques=sc.nextInt();
                    while(ques!=3){
                        message="";
                        if(ques==1){
                            System.out.println("Post your question: ");
                            sc.nextLine();
                            message=sc.nextLine();
                            String sqlq="insert into posts(username,post) values(?,?)";
                            PreparedStatement ps;
                            ps=connection.prepareStatement(sqlq);
                            ps.setString(1,username);
                            ps.setString(2,message);
                            ps.executeUpdate();
                            System.out.println("Question Posted...");
                        }
                        else{
                            ResultSet res;
			                res = statement.executeQuery("select * from posts");
                            System.out.println("List of Questions with their Post_ID:");
                            while(res.next()){
                                System.out.println("Post Id: "+ res.getInt("postid")+" "+res.getString("username")+" ->>   "+res.getString("post"));
                            }
                            System.out.println("Enter Post Id to view that Post or Enter -1 to Exit");
                            int quesid=sc.nextInt();
                            if(quesid==-1){
                                System.out.println("Byee..");
                            }
                            else{
                                //message+="Reply to Question Id "+quesid+" :  ";
                                res=statement.executeQuery("select * from posts where postid="+quesid);
                                if(res.next()){
                                    System.out.println("No: "+quesid+": "+res.getString("post"));
                                }
                                //String sqlp="select * from replies where parentid= "+quesid;
                                //ResultSet r;
			                    //r = statement.executeQuery(sqlp);
                                //while (r.next()){
                                  //  System.out.println("\t No: "+r.getInt("replyid")+":  "+r.getString("username")+"->>  "+r.getString("reply"));
                                //}
                                printreply(quesid,0);
                                System.out.println("Enter the No to Reply :");
                                int num=sc.nextInt();
                                if(num==-1){
                                    return ;
                                }
                                System.out.println("Enter Reply: ");
                                sc.nextLine();
                                message+=sc.nextLine();
                                String sqlq="insert into replies(username,reply,parentid) values(?,?,?)";
                                PreparedStatement ps;
                                ps=connection.prepareStatement(sqlq);
                                ps.setString(1,username);
                                ps.setString(2,message);
                                ps.setInt(3,num);
                                ps.executeUpdate();
                                System.out.println("Reply Posted...");
                            }
                        }
                        System.out.println("Enter \n 1 -> To post Question \n 2 -> To Answer Questions \n 3 -> Exit...");
                        ques=sc.nextInt();
                    }
                    System.out.println("Thanks You - #spidy");
                }
                else{
                    System.out.println("Account Not Found...");

                }

            }

			resultSet.close();
			statement.close();
			connection.close();
            sc.close();
		}

		catch (Exception exception) {
			System.out.println(exception);
		}
	}
}
