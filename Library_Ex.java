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
		System.out.println("### �������� ���α׷� ���� ###");
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
			System.out.println("���α׷��� �����մϴ�.");
			return;
		default:
			System.out.print("(����) �ٽ� �����ϼ��� : ");
			break;
		}
		}
	}
	static int librarymenu() {
		Scanner sc = new Scanner(System.in);
		System.out.println("*** ����������� ***");
		System.out.println(" 1. ���� ����\n 2. ���� ���� \n 3. ���� �ݳ� \n 4. ������Ȳ \n 5. ���α׷� ����\n");
		System.out.println("### menu�� �����ϼ��� ###");
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
	static void borrow_book() {
		Connection con =null;
		Scanner sc = new Scanner(System.in);
		
		GregorianCalendar calendar = new GregorianCalendar();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy�� MM�� dd��");
		dateFormat.setTimeZone(TimeZone.getTimeZone("KST"));
		borrow_date = dateFormat.format(calendar.getTime());
		calendar.add(Calendar.DAY_OF_MONTH, 3);
		return_date = dateFormat.format(calendar.getTime());
		
		System.out.print("������ : " + borrow_date+"\n");
		System.out.print("������ : ");
		bookname = sc.next();
		System.out.print("�̸� : ");
		name = sc.next();
		System.out.print("�ּ� : ");
		addr = sc.next();
		System.out.print("��ȭ��ȣ : ");
		phone = sc.next();
		System.out.println();
		state = "����";
		
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
		      //int res = pstmt.executeUpdate(); //������ ���ڵ� ������ ��ȯ
		      if(res ==1)
		    	System.out.println(bookname +"��(��) ����Ǿ����ϴ�.\n");
		      else
		        System.out.println("\n �̹� ����� �����Դϴ�!!!");
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
	static void return_book() {
		Connection con =null;
		
		Scanner sc = new Scanner(System.in);
		System.out.println("�ݳ� ������ �Է� : ");
		bookname = sc.next();
		GregorianCalendar calendar = new GregorianCalendar();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy�� MM�� dd��");
		dateFormat.setTimeZone(TimeZone.getTimeZone("KST"));
		return_date = dateFormat.format(calendar.getTime());
		state = "�ݳ�";
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
				System.out.println(bookname + " ��(��) �ݳ��Ǿ����ϴ�.");
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
	static void printList() {
		Connection con =null;
		PreparedStatement stmt =null;
		ResultSet rs = null;
		
		String sql = "Select * from library_DB order by borrow_no";
		try {
		con = connectDB();
		stmt= con.prepareStatement(sql);
		rs = stmt.executeQuery();
		System.out.println("\t\t### �Ϻ�������Ȳ  ###");
		System.out.println("======================================================================================");
		System.out.println("��ȣ    ������\t\t   ȸ����\t    �ּ�\t     ��ȭ��ȣ\t       ������\t    �ݳ���(������)\t �������");
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
			System.out.println("�����ͺ��̽� ���� ����!");
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

update library_db set return_date = sysdate, borrow_state ='�ݳ�' where bookname = '�̱���';

CREATE SEQUENCE LIBRARY_SEQ
INCREMENT BY 1
START WITH 1; 

desc library_db;
*/
