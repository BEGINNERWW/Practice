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
		System.out.println("### 도서관리 프로그램 시작 ###\n");
		
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
				System.out.println("프로그램 종료\n");
				return;
			}
			}
		}
	static int bookmenu() {
		Scanner sc = new Scanner(System.in);
		System.out.println("### 도서관리대장 ###");
		System.out.println(" 1. 도서정보 입력\n 2. 도서정보 출력\n 3. 도서정보 조회\n 4. 도서정보 수정\n 5. 도서정보 삭제\n 6. 프로그램 종료");
		System.out.println();
		System.out.print("- 도서관리 메뉴를 입력하세요 : ");
		menu = sc.nextInt();
		System.out.println();
		return menu;
	}
	static Connection connectDB() {  //DB사용할때마다 필요하여 만듬
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
		
		System.out.print("도서명 : ");
		book = sc.next();
		System.out.print("저자 : ");
		writer = sc.next();
		System.out.print("도서위치 : ");
		site = sc.next();
		System.out.print("출판사 : ");
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
		      //int res = pstmt.executeUpdate(); //삽입한 레코드 갯수가 반환
		      if(res ==1)
		    	System.out.println("\n신규도서가 등록되었습니다.\n");
		      else
		        System.out.println("\n 이미 등록된 도서입니다!!!");
		    }
		    catch(Exception e) {
		      System.out.println("\n데이터베이스 연결 실패+"+e.getMessage());
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
		    con = connectDB();//연결객체를 할당받는 작업
		    pstmt= con.prepareStatement(sql);
		    rs= pstmt.executeQuery();
		    
	        System.out.println();
	    	System.out.println("### 등록도서목록 ###");
			System.out.println();
			System.out.println("======================================");
			System.out.println("도서번호\t 위치\t 도서명\t 저자\t 출판사\t");
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
		      System.out.println("데이터 베이스 연결 실패!+ "+e.getMessage());
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
	}//아웃
	static void booksearch() {
		Connection con =null;
		PreparedStatement pstmt= null;
		ResultSet rs = null;
		
		String sql = "Select * from book_db where book = ?";
		try {
			Scanner sc = new Scanner(System.in);
			System.out.print("조회할 도서명을 입력하세요 : ");
			book = sc.next();
			System.out.println();
		    con = connectDB();//연결객체를 할당받는 작업
		    pstmt= con.prepareStatement(sql);
		    pstmt.setString(1, book);
			rs= pstmt.executeQuery();
			
			System.out.println("======================================");
			System.out.println("도서번호\t 위치\t 도서명\t 저자\t 출판사\t");
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
			System.out.println("도서 조회가 완료되었습니다.\n");
		}
		  catch(Exception e) {
		      System.out.println("데이터 베이스 연결 실패!+ "+e.getMessage());
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
	}//서치
	static void bookrevise() {
		Connection con =null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql ="update book_db set writer = ?, site =?, puble =? where book =?";
		
		try {
			 con = connectDB();//연결객체를 할당받는 작업
		
			 Scanner sc = new Scanner(System.in);
			 System.out.print("수정할 도서명을 입력하세요 : ");
			 book = sc.next();
			 System.out.print("저자 : ");
			 writer = sc.next();
			 System.out.print("도서위치 : ");
			 site = sc.next();
			 System.out.print("출판사 : ");
			 puble = sc.next();
			 
			 pstmt= con.prepareStatement(sql);
			 pstmt.setString(1, writer);
			 pstmt.setString(2, site);
			 pstmt.setString(3, puble);
			 pstmt.setString(4, book);
			 pstmt.executeUpdate();
			 System.out.println("도서정보가 수정되었습니다.\n");
			} 
		catch(Exception e) {
			System.out.println("데이터베이스 연결 실패 !"+ e.getMessage());
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
	}//리바이스
	
	static void bookdelete() {
		Connection con =null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql ="delete from book_db where book =?";
		
		try {
			 con = connectDB();//연결객체를 할당받는 작업
		
			 Scanner sc = new Scanner(System.in);
			 System.out.print("삭제할 도서명을 입력하세요 : ");
			 book = sc.next();
			 pstmt = con.prepareStatement(sql);
			 pstmt.setString(1, book);
			 pstmt.executeUpdate();
			 System.out.println("등록된 도서가 삭제되었습니다.\n");
		}
		catch(Exception e) {
			System.out.println("데이터베이스 연결 실패 !"+ e.getMessage());
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
