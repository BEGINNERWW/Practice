package day1028;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;
import java.util.TimeZone;

public class Library_Ex {
	static String borrow_date, return_date, name, addr, phone, bookname, state;
	static int menu =0;
	public static void main(String[] args) {
		System.out.println("### 도서관리 프로그램 시작 ###");
		System.out.println();
		
		while(true) {
		menu = librarymenu();
		switch(menu) {
		case 1:
			Book_Ex.bookmenu();
		case 2:
			borrow_book();
			break;
		case 3:
			return_book();
			break;
		case 4:
			printList();
			break;
		case 5:
			System.out.println("프로그램을 종료합니다.");
			return;
		default:
			System.out.print("(오류) 다시 선택하세요 : ");
			break;
		}
		}
	}
	static int librarymenu() {
		Scanner sc = new Scanner(System.in);
		System.out.println("*** 도서대출관리 ***");
		System.out.println(" 1. 도서 관리\n 2. 도서 대출 \n 3. 도서 반납 \n 4. 대출현황 \n 5. 프로그램 종료\n");
		System.out.println("### menu를 선택하세요 ###");
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
	static void borrow_book() {
		Connection con =null;
		Scanner sc = new Scanner(System.in);
		
		GregorianCalendar calendar = new GregorianCalendar();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
		dateFormat.setTimeZone(TimeZone.getTimeZone("KST"));
		borrow_date = dateFormat.format(calendar.getTime());
		calendar.add(Calendar.DAY_OF_MONTH, 3);
		return_date = dateFormat.format(calendar.getTime());
		
		System.out.print("대출일 : " + borrow_date+"\n");
		System.out.print("도서명 : ");
		bookname = sc.next();
		System.out.print("이름 : ");
		name = sc.next();
		System.out.print("주소 : ");
		addr = sc.next();
		System.out.print("전화번호 : ");
		phone = sc.next();
		System.out.println();
		state = "대출";
		
		con= connectDB();
		PreparedStatement pstmt = null;
		String sql = "Insert Into library_db (BORROW_NO,BOOKNAME, NAME, ADDR, PHONE, BORROW_DATE, RETURN_DATE,BORROW_STATE )"
		+"Values(LIBRARY_SEQ.NEXTVAL,?,?,?,?,?,?,?)";
		    
		    try {
		      pstmt = con.prepareStatement(sql);
		      pstmt.setString(1, bookname);
		      pstmt.setString(2, name);
		      pstmt.setString(3, addr);
		      pstmt.setString(4, phone);
		      pstmt.setString(5, borrow_date);
		      pstmt.setString(6, return_date);
		      pstmt.setString(7, state);

		      int res = pstmt.executeUpdate();
		      //int res = pstmt.executeUpdate(); //삽입한 레코드 갯수가 반환
		      if(res ==1)
		    	System.out.println(bookname +"이(가) 대출되었습니다.\n");
		      else
		        System.out.println("\n 이미 대출된 도서입니다!!!");
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
	static void return_book() {
		Connection con =null;
		
		Scanner sc = new Scanner(System.in);
		System.out.println("반납 도서명 입력 : ");
		bookname = sc.next();
		GregorianCalendar calendar = new GregorianCalendar();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
		dateFormat.setTimeZone(TimeZone.getTimeZone("KST"));
		return_date = dateFormat.format(calendar.getTime());
		state = "반납";
		System.out.println(return_date);
		System.out.println(state);
		con= connectDB();
		PreparedStatement pstmt = null;
		String sql = "update library_db set RETURN_DATE=?,BORROW_STATE=? where bookname =? ";
		    
		    try {
		    	pstmt = con.prepareStatement(sql);
		    	pstmt.setString(1, return_date);
			    pstmt.setString(2, state);
			    pstmt.setString(3, bookname);
			    pstmt.executeUpdate();
				System.out.println(bookname + " 이(가) 반납되었습니다.");
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
	static void printList() {
		Connection con =null;
		PreparedStatement stmt =null;
		ResultSet rs = null;
		
		String sql = "Select * from library_DB order by borrow_no";
		try {
		con = connectDB();
		stmt= con.prepareStatement(sql);
		rs = stmt.executeQuery();
		System.out.println("\t\t### 일별대출현황  ###");
		System.out.println("======================================================================================");
		System.out.println("번호    대출일\t\t   회원명\t    주소\t     전화번호\t       도서명\t    반납일(예정일)\t 대출상태");
		System.out.println("======================================================================================");
		while(rs.next()) {
			System.out.print(rs.getString("borrow_no")+"   ");
			System.out.print(rs.getString("borrow_date")+"\t");
			System.out.print(rs.getString("name")+"\t");
			System.out.print(rs.getString("addr")+"\t");
			System.out.print(rs.getString("phone")+"\t");
			System.out.print(rs.getString("bookname")+"\t");
			System.out.print(rs.getString("return_date")+"\t");
			System.out.print(rs.getString("BORROW_state")+"\n");
		}
		System.out.println("======================================================================================");
		System.out.println();
		}
		catch(Exception e) {
			System.out.println("데이터베이스 연결 실패!");
			e.printStackTrace();
		}
		finally {
			try {
				if(rs!=null) rs.close();
				if(stmt !=null) stmt.close();
				if(con != null) con.close();
			}
			catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
}//class
/*
CREATE TABLE LIBRARY_DB(
BORROW_NO VARCHAR2(6) PRIMARY KEY,
BOOKNAME VARCHAR2(20),
NAME VARCHAR2(6),
ADDR VARCHAR2(20),
PHONE NUMBER(14),
BORROW_DATE VARCHAR2(20),
RETURN_DATE VARCHAR2(20),
BORROW_STATE VARCHAR2(5)
);

select * from LIBRARY_DB;

update library_db set return_date = sysdate, borrow_state ='반납' where bookname = '이기자';

CREATE SEQUENCE LIBRARY_SEQ
INCREMENT BY 1
START WITH 1; 

desc library_db;
*/
