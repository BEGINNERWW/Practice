package day1028;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Book_Ex {
	static String book, bookno;
	static String writer;
	static String site;
	static String puble;
	static int menu=0, i=0,cnt=0;

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("### �������� ���α׷� ���� ###\n");
		
		while(true) {
			menu = bookmenu();
			switch(menu) {
			case 1:
				bookin();
				break;
			case 2:
				bookout();
				break;
			case 3:
				booksearch();
				break;
			case 4:
				bookrevise();
				break;
			case 5:
				bookdelete();
				break;
			default:
				System.out.println("���α׷� ����\n");
				return;
			}
			}
		}
	static int bookmenu() {
		Scanner sc = new Scanner(System.in);
		System.out.println("### ������������ ###");
		System.out.println(" 1. �������� �Է�\n 2. �������� ���\n 3. �������� ��ȸ\n 4. �������� ����\n 5. �������� ����\n 6. ���α׷� ����");
		System.out.println();
		System.out.print("- �������� �޴��� �Է��ϼ��� : ");
		menu = sc.nextInt();
		System.out.println();
		return menu;
	}
	static Connection connectDB() {  //DB����Ҷ����� �ʿ��Ͽ� ����
	    Connection con =null;
	    try {
	      String driver = "oracle.jdbc.driver.OracleDriver";
	      String url ="jdbc:oracle:thin:@127.0.0.1:1521:orcl";
	      Class.forName(driver);
	      con =DriverManager.getConnection(url, "scott","123456");
	    }
	    catch(Exception e) {
	      e.printStackTrace();
	    }
	    return con;
	  } // connection DB
	static void bookin() {
		Connection con =null;
		Scanner sc = new Scanner(System.in);
		
		System.out.print("������ : ");
		book = sc.next();
		System.out.print("���� : ");
		writer = sc.next();
		System.out.print("������ġ : ");
		site = sc.next();
		System.out.print("���ǻ� : ");
		puble = sc.next();
		
		con= connectDB();
		PreparedStatement pstmt = null;
		String sql = "Insert Into book_db (BOOKNO, BOOK, WRITER, SITE, PUBLE) " +"Values(BOOKNO_SEQ.NEXTVAL,?,?,?,?)";
		    
		    try {
		      pstmt = con.prepareStatement(sql);
		      pstmt.setString(1, book);
		      pstmt.setString(2, writer);
		      pstmt.setString(3, site);
		      pstmt.setString(4, puble);
		     
		      int res = pstmt.executeUpdate();
		      //int res = pstmt.executeUpdate(); //������ ���ڵ� ������ ��ȯ
		      if(res ==1)
		    	System.out.println("\n�űԵ����� ��ϵǾ����ϴ�.\n");
		      else
		        System.out.println("\n �̹� ��ϵ� �����Դϴ�!!!");
		    }
		    catch(Exception e) {
		      System.out.println("\n�����ͺ��̽� ���� ����+"+e.getMessage());
		    }
		    finally {
		      try {
		        if(pstmt != null) pstmt.close();
		      }
		      catch(Exception e) {
		        System.out.println(e.getMessage());
		      }
		      try {
		        if(con != null)con.close();
		      }
		      catch(Exception e) {
		        System.out.println(e.getMessage());
		      }
		    }
	}
	static void bookout() {
		Connection con =null;
		PreparedStatement pstmt= null;
		ResultSet rs = null;
		
		String sql = "Select * from book_db order by bookno";
		try {
		    con = connectDB();//���ᰴü�� �Ҵ�޴� �۾�
		    pstmt= con.prepareStatement(sql);
		    rs= pstmt.executeQuery();
		    
	        System.out.println();
	    	System.out.println("### ��ϵ������ ###");
			System.out.println();
			System.out.println("======================================");
			System.out.println("������ȣ\t ��ġ\t ������\t ����\t ���ǻ�\t");
			System.out.println("======================================");

		    while(rs.next()) {
		    	System.out.print(rs.getString("bookno")+"\t");
		    	System.out.print(rs.getString("site")+"\t");
		    	System.out.print(rs.getString("book")+"\t");
		    	System.out.print(rs.getString("writer")+"\t");
		    	System.out.print(rs.getString("puble")+"\n");
		        }
			System.out.println("======================================");
		    }//try
		    catch(Exception e) {
		      System.out.println("������ ���̽� ���� ����!+ "+e.getMessage());
		    }//catch
		    finally {
		      try {
		        if(rs != null)rs.close();
		        if(pstmt != null)pstmt.close();
		        if(con != null)con.close();
		      }
		      catch(Exception e) {
		        System.out.println(e.getMessage());
		      }
		    }		
	}//�ƿ�
	static void booksearch() {
		Connection con =null;
		PreparedStatement pstmt= null;
		ResultSet rs = null;
		
		String sql = "Select * from book_db where book = ?";
		try {
			Scanner sc = new Scanner(System.in);
			System.out.print("��ȸ�� �������� �Է��ϼ��� : ");
			book = sc.next();
			System.out.println();
		    con = connectDB();//���ᰴü�� �Ҵ�޴� �۾�
		    pstmt= con.prepareStatement(sql);
		    pstmt.setString(1, book);
			rs= pstmt.executeQuery();
			
			System.out.println("======================================");
			System.out.println("������ȣ\t ��ġ\t ������\t ����\t ���ǻ�\t");
			System.out.println("======================================");
			while(rs.next()) {
			System.out.print(rs.getString("bookno")+"\t");
	    	System.out.print(rs.getString("site")+"\t");
	    	System.out.print(rs.getString("book")+"\t");
	    	System.out.print(rs.getString("writer")+"\t");
	    	System.out.print(rs.getString("puble")+"\n");
			}
			System.out.println("======================================");
			System.out.println();
			System.out.println("���� ��ȸ�� �Ϸ�Ǿ����ϴ�.\n");
		}
		  catch(Exception e) {
		      System.out.println("������ ���̽� ���� ����!+ "+e.getMessage());
		    }//catch
		    finally {
		      try {
		        if(rs != null)rs.close();
		        if(pstmt != null)pstmt.close();
		        if(con != null)con.close();
		      }
		      catch(Exception e) {
		        System.out.println(e.getMessage());
		      }
		    }		
	}//��ġ
	static void bookrevise() {
		Connection con =null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql ="update book_db set writer = ?, site =?, puble =? where book =?";
		
		try {
			 con = connectDB();//���ᰴü�� �Ҵ�޴� �۾�
		
			 Scanner sc = new Scanner(System.in);
			 System.out.print("������ �������� �Է��ϼ��� : ");
			 book = sc.next();
			 System.out.print("���� : ");
			 writer = sc.next();
			 System.out.print("������ġ : ");
			 site = sc.next();
			 System.out.print("���ǻ� : ");
			 puble = sc.next();
			 
			 pstmt= con.prepareStatement(sql);
			 pstmt.setString(1, writer);
			 pstmt.setString(2, site);
			 pstmt.setString(3, puble);
			 pstmt.setString(4, book);
			 pstmt.executeUpdate();
			 System.out.println("���������� �����Ǿ����ϴ�.\n");
			} 
		catch(Exception e) {
			System.out.println("�����ͺ��̽� ���� ���� !"+ e.getMessage());
		}
		finally {
			try {
				if(pstmt !=null) pstmt.close();
				if(con != null) con.close();
			}
			catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}//�����̽�
	
	static void bookdelete() {
		Connection con =null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql ="delete from book_db where book =?";
		
		try {
			 con = connectDB();//���ᰴü�� �Ҵ�޴� �۾�
		
			 Scanner sc = new Scanner(System.in);
			 System.out.print("������ �������� �Է��ϼ��� : ");
			 book = sc.next();
			 pstmt = con.prepareStatement(sql);
			 pstmt.setString(1, book);
			 pstmt.executeUpdate();
			 System.out.println("��ϵ� ������ �����Ǿ����ϴ�.\n");
		}
		catch(Exception e) {
			System.out.println("�����ͺ��̽� ���� ���� !"+ e.getMessage());
		}
		finally {
			try {
				if(pstmt !=null) pstmt.close();
				if(con != null) con.close();
			}
			catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
}
