package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class BoardDAO {

	Connection con;
	PreparedStatement pstmt;
	ResultSet rs;
	
	// �����ͺ��̽��� Ŀ�ؼ�Ǯ�� ����ϵ��� �����ϴ� �޼ҵ�
	public void getCon() {
		try {
			Context initctx=new InitialContext();
			Context envctx=(Context) initctx.lookup("java:comp/env");
			DataSource ds=(DataSource) envctx.lookup("jdbc/pool");
			// datasource
			con=ds.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// �ϳ��� ���ο� �Խñ��� �Ѿ�ͼ� ����Ǵ� �޼ҵ�
	public void insertBoard(BoardBean bean) {
		
		getCon();
		// ��Ŭ������ �Ѿ���� �ʾҴ� �����͵��� �ʱ�ȭ ���־����
		int ref=0; // �� �׷��� �ǹ� = ������ ������Ѽ� ���� ū ref���� ������ �� +1�� �������
		int re_step=1; // �����̱⿡ = �θ���̱⿡
		int re_level=1;
		
		try {
			// ���� ū ref���� �о���� ���� �غ�
			String refsql = "select max(ref) from board";
			// ���� ���� ��ü
			pstmt=con.prepareStatement(refsql);
			// ���������� ����� ����
			rs=pstmt.executeQuery();
			if(rs.next()) {// ������� �ִٸ�
				ref=rs.getInt(1)+1; // �ִ밪�� +1�� ���༭ �۱׷� ����
			}
			// ������ �Խñ� ��ü���� ���̺� ����
			String sql="insert into board values(board_seq.NEXTVAL,?,?,?,?,sysdate,?,?,?,0,?)";
			pstmt = con.prepareStatement(sql);
			// ?�� ���� ����
			pstmt.setString(1, bean.getWriter());
			pstmt.setString(2, bean.getEmail());
			pstmt.setString(3, bean.getSubject());
			pstmt.setString(4, bean.getPassword());
			pstmt.setInt(5, ref);
			pstmt.setInt(6, re_step);
			pstmt.setInt(7, re_level);
			pstmt.setString(8, bean.getContent());
			// ������ �����Ͻÿ�
			pstmt.executeUpdate();
			// �ڿ� �ݳ�
			con.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// ��� �Խñ��� ����
	public Vector<BoardBean> getAllBoard(int start, int end){
		
		// ������ ��ü ����
		Vector<BoardBean> v = new Vector<>();
		
		try {
			getCon();
			// ���� �غ� (Rownum�� ����Ŭ������ �ִ� ����)
			// �ؼ� : 1. board�� ��� �����͸� ���Ľ�Ų �� �� ���ĵȰ��� A��� ����
			//		2. A�� ��� �����Ϳ� Rownum�� ���� select�� �� 
			// 		3. Rnum�� �������� where���ǽ� ���� 
			String sql="select * from (select A.* ,Rownum Rnum from (select * from board order by ref desc, re_step  asc)A)"
					+"where Rnum>=? and Rnum <=?";
			// ���� ������ ��ü ����
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, start);
			pstmt.setInt(2, end);
			// ���� ���� �� ��� ����
			rs=pstmt.executeQuery();
			// ������ ������ ����� �𸣱⿡ �ݺ����� �̿��Ͽ� ������ ����
			while(rs.next()) {
				// �����͸� ��Ű¡(����=BoardbeanŬ������ �̿�)
				BoardBean bean=new BoardBean();
				bean.setNum(rs.getInt(1));
				bean.setWriter(rs.getString(2));
				bean.setEmail(rs.getString(3));
				bean.setSubject(rs.getString(4));
				bean.setPassword(rs.getString(5));
				bean.setReg_date(rs.getDate(6).toString());
				bean.setRef(rs.getInt(7));
				bean.setRe_step(rs.getInt(8));
				bean.setRe_level(rs.getInt(9));
				bean.setReadcount(rs.getInt(10));
				bean.setContent(rs.getString(11));
				// ��Ű¡ �� �����͸� ���Ϳ� ����
				v.add(bean);
			}
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return v;
	}
	
	// BoardInfo�� �ϳ��� �Խñ��� ����
	public BoardBean getOneBoard(int num) {
		// ����Ÿ�� ����
		BoardBean bean = new BoardBean();
		getCon();
		
		try {
			// ��ȸ�� ���� ����
			String readsql = "update board set readcount=readcount+1 where num=?";
			pstmt=con.prepareStatement(readsql);
			pstmt.setInt(1, num);
			pstmt.executeUpdate();
			
			// ���� �غ�
			String sql="select * from board where num=?";
			// ���� ���� ��ü �غ�
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, num);
			// ���� ���� �� ����� ����
			rs=pstmt.executeQuery();
			if(rs.next()) {
				bean.setNum(rs.getInt(1));
				bean.setWriter(rs.getString(2));
				bean.setEmail(rs.getString(3));
				bean.setSubject(rs.getString(4));
				bean.setPassword(rs.getString(5));
				bean.setReg_date(rs.getDate(6).toString());
				bean.setRef(rs.getInt(7));
				bean.setRe_step(rs.getInt(8));
				bean.setRe_level(rs.getInt(9));
				bean.setReadcount(rs.getInt(10));
				bean.setContent(rs.getString(11));
			}
			con.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return bean;
	}
	
	// �亯���� ����Ǿ��ִ� �޼ҵ�
	public void reWriteBoard(BoardBean bean) {
		// �θ�۱׷�, �۷���, �۽����� �о�帲
		int ref=bean.getRef();
		int re_step=bean.getRe_step();
		int re_level=bean.getRe_level();
		
		getCon();
		
		try {
			////////////////// �ٽ� �ڵ� ////////////////
			// �θ�ۺ��� ū re_level�� ���� ���� 1�� ����������
			String levelsql="update board set re_level=re_level+1 where ref=? and re_level>?";
			// �������ఴü ����
			pstmt=con.prepareStatement(levelsql);
			pstmt.setInt(1, ref);
			pstmt.setInt(2, re_level);
			// ���� ����
			pstmt.executeUpdate();
			
			// �亯�� �����͸� ����
			String sql="insert into board values(board_seq.NEXTVAL,?,?,?,?,sysdate,?,?,?,0,?)";
			pstmt=con.prepareStatement(sql);
			// ?�� ���� ����
			pstmt.setString(1, bean.getWriter());
			pstmt.setString(2, bean.getEmail());
			pstmt.setString(3, bean.getSubject());
			pstmt.setString(4, bean.getPassword());
			pstmt.setInt(5, ref); // �θ��� ref���� �־���
			pstmt.setInt(6, re_step+1); // ����̱⿡ �θ� �� re_step�� 1�� ������
			pstmt.setInt(7, re_level+1); 
			pstmt.setString(8, bean.getContent());
			// ������ �����Ͻÿ�
			pstmt.executeUpdate();
			// �ڿ� �ݳ�
			con.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	// BoardUpdate, BoardDelete �� �ϳ��� �Խñ��� ����
	public BoardBean getOneUpdateBoard(int num) {
		// ����Ÿ�� ����
		BoardBean bean = new BoardBean();
		getCon();
		
		try {
			// ���� �غ�
			String sql="select * from board where num=?";
			// ���� ���� ��ü �غ�
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, num);
			// ���� ���� �� ����� ����
			rs=pstmt.executeQuery();
			if(rs.next()) {
				bean.setNum(rs.getInt(1));
				bean.setWriter(rs.getString(2));
				bean.setEmail(rs.getString(3));
				bean.setSubject(rs.getString(4));
				bean.setPassword(rs.getString(5));
				bean.setReg_date(rs.getDate(6).toString());
				bean.setRef(rs.getInt(7));
				bean.setRe_step(rs.getInt(8));
				bean.setRe_level(rs.getInt(9));
				bean.setReadcount(rs.getInt(10));
				bean.setContent(rs.getString(11));
			}
			con.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return bean;
	}
	
	// update�� delete �� �ʿ��� �н����尪�� �������ִ� �޼ҵ�
	public String getPass(int num) {
		// ������ ���� ��ü ����
		String pass="";
		getCon();
		
		try {
			// �����غ�
			String sql="select password from board where num=?";
			// ���� ���� ��ü �غ�
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, num);
			// ���� ����
			rs=pstmt.executeQuery();
			// �н����尪�� ����
			if(rs.next()) {
				pass=rs.getString(1);
			}
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return pass;
	}
	
	// �ϳ��� �Խñ��� �����ϴ� �޼ҵ�
	public void updateBoard(BoardBean bean) {
		getCon();
		
		try {
			// ���� �غ�
			String sql="update board set subject=? , content=? where num=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, bean.getSubject());
			pstmt.setString(2, bean.getContent());
			pstmt.setInt(3, bean.getNum());
			
			pstmt.executeUpdate();
			
			con.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// �ϳ��� �Խñ��� �����ϴ� �޼ҵ�
	public void deleteBoard(int num) {
		getCon();
		
		try {
			// ���� �غ�
			String sql = "delete from board where num=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, num);
			pstmt.executeUpdate();
			
			con.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// ��ü ���� ������ �����ϴ� �޼ҵ�
	public int getAllcount() {
		
		getCon();
		
		// �Խñ� ��ü���� �����ϴ� ����
		int count=0;
		
		try {
			String sql="select count(*) from board";
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				count=rs.getInt(1); // ��ü�Խñ� ��
			}
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return count;
	}
	
}
