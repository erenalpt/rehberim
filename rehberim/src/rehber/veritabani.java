package rehber;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.print.attribute.standard.DateTimeAtCompleted;
import javax.sql.PooledConnection;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.sqlite.SQLiteDataSource;
import org.sqlite.SQLiteException;
import org.sqlite.core.DB;
import org.sqlite.javax.SQLiteConnectionPoolDataSource;


import net.proteanit.sql.DbUtils;

public class veritabani {

	Connection con = null;
	DefaultTableModel dft = new DefaultTableModel();
	private DefaultTableModel dftModel;
	LocalDateTime dateT = LocalDateTime.now();

	static {
		try {

			Class.forName("org.sqlite.JDBC");

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
			JOptionPane.showMessageDialog(null, "Kapatiliyor");
			System.exit(0);

		}
	}

	public Connection getConnection() {

		try {
			String dir = System.getProperty("user.dir");
            String maindir = dir + File.separator + File.separator ;
			con = DriverManager.getConnection("jdbc:sqlite:"+maindir+"rehberim.sqlite");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e);
			JOptionPane.showMessageDialog(null, "Kapatiliyor");
			System.exit(0);
		}
		return con;

	}

	public void insert(Kisi kisi) {
		String sql = "INSERT INTO rehber(isim, soyisim, tel) VALUES(?,?,?)";
		Connection conn = getConnection();

		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, kisi.getisim());
			pstmt.setString(2, kisi.getsoyisim());
			pstmt.setString(3, kisi.getTelefon());
			pstmt.executeUpdate();
			conn.close();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e);
		}
	}

	public void delete(int id) {
		String sql = "DELETE FROM rehber WHERE id = ?";

		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, id);
			pstmt.executeUpdate();
			conn.close();
		} catch (SQLException e) {
			System.out.println("Kisi Silme Basarisiz!");
		}
	}

	public void update(int id, String isim, String soyisim, String tel) {
		String sql = "UPDATE rehber SET isim = ? , soyisim = ?,tel = ? WHERE id=?";

		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			// set the corresponding param
			pstmt.setString(1, isim);
			pstmt.setString(2, soyisim);
			pstmt.setString(3, tel);
			pstmt.setInt(4, id);
			// update
			pstmt.executeUpdate();
			conn.close();
		} catch (SQLException e) {
			System.out.println(e);
		}

	}

	public List<Kisi> TabloDoldur() {
		String sql = "SELECT * FROM 'rehber'";
		List<Kisi> list = new ArrayList<Kisi>();
		Connection conn = getConnection();

		try {
			Statement pstmt = conn.createStatement();
			ResultSet rs = pstmt.executeQuery(sql);
			while (rs.next()) {
				Kisi kisiList = new Kisi();

				kisiList.setId(rs.getInt("id"));
				kisiList.setIsim(rs.getString("isim"));
				kisiList.setIsim(rs.getString("soyisim"));
				kisiList.setIsim(rs.getString("tel"));

				list.add(kisiList);
			}
			// rs.close();
			return list;

		} catch (Exception e) {
			JOptionPane.showConfirmDialog(null, "Baglanti Basarisiz", "SQLite Baglantisi", JOptionPane.PLAIN_MESSAGE);
			JOptionPane.showConfirmDialog(null, e);
			return null;
		}
	}

	public DefaultTableModel listele(JTable tablo) {
		String sql = "SELECT * FROM 'rehber'";
		DefaultTableModel dftModel = (DefaultTableModel) tablo.getModel();
		dftModel.setRowCount(0);
		try {
			Connection conn = getConnection();
			Statement stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(sql);
			if (rs.isBeforeFirst()) {
				while (rs.next()) {
					Object dataObject[] = { rs.getInt("id"), rs.getString("isim"), rs.getString("soyisim"),
							rs.getString("telefon") };
					dftModel.addRow(dataObject);
				}
			}
			rs.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Listeleme Hatasi Olusütu!");
		}

		return dftModel;

	}

	public void loadData() {
		String sql = "SELECT * FROM rehber";
		Connection con = getConnection();
		if (con != null) {
			try {
				PreparedStatement pst = con.prepareStatement(sql);
				ResultSet rst = pst.executeQuery();
				Object[] columnData = new Object[4];
				while (rst.next()) {
					columnData[0] = rst.getInt("id");
					columnData[1] = rst.getString("isim");
					columnData[2] = rst.getString("soyisim");
					columnData[3] = rst.getString("telefon");
					dft.addRow(columnData);
				}
				rst.close();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Listeleme Hatasi!");
			}
		}

	}

	public List<Kisi> listele() {

		List<Kisi> liste = new ArrayList<Kisi>();

		try {
			Connection con = getConnection();
			Statement stmt = con.createStatement();

			ResultSet rs = stmt.executeQuery("SELECT * FROM rehber");

			while (rs.next()) {

				Kisi kisiN = new Kisi();

				kisiN.setId(rs.getInt("id"));
				kisiN.setIsim(rs.getString("isim"));
				kisiN.setsoyisim(rs.getString("soyisim"));
				kisiN.setTelefon(rs.getString("tel"));

				liste.add(kisiN);
			}

		} catch (SQLException e) {

			System.out.println(e);
		}
		return liste;

	}

	public ResultSet FillTable() {
		String sql = "SELECT * FROM rehber";
		ResultSet rs = null;

		try {
			getConnection();
			Statement stat = con.createStatement();
			rs = stat.executeQuery(sql);

			return rs;
		} catch (SQLException e) {

		}
		return rs;
	}

	public void vcfAktar(String pathi, Object satir, Object satir2, Object satir3) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(pathi+"/output.vcf",true));
			writer.write("BEGIN:VCARD\nVERSION:3.0\nN:" + satir + " " + satir2 + "\nTEL;type=CELL;type=VOICE;type=pref:"
					+ satir3 + "\nREV:" + dateT + "\nEND:VCARD\n");
			writer.close();
		} catch (IOException e) {
			System.err.println(e);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					System.err.println(e);
				}
			}
		}

	}

}
