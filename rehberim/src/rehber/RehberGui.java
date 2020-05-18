package rehber;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.MenuBar;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutFocusTraversalPolicy;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.omg.CORBA.portable.ApplicationException;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteConnection;

import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class RehberGui extends JFrame implements initRehber {
	Kisi kisiBilgi = new Kisi();
	veritabani vt = new veritabani();
	List<Kisi> listele = vt.TabloDoldur();
	Connection conn = null;
	DefaultTableModel dftModel = new DefaultTableModel();
	Object[] kolon = { "ID", "Isim", "Soyisim", "Telefon" };
	Object satir[] = new Object[4];
	String pathi = null;
	private javax.swing.JTable jTable1;

	private JList table;

	public RehberGui() {
		pencere();
		conn = vt.getConnection();

	}

	@Override
	public void pencere() {
		JPanel jpanel = jpanel();
		JMenuBar menubar = menubar();

		getContentPane().add(jpanel);
		setJMenuBar(menubar);
		setBounds(100, 100, 592, 381);
		setTitle("Rehberim V1");
		setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);

	}

	@Override
	public JPanel jpanel() {
		JPanel panel = new JPanel(new BorderLayout());
		JPanel btnPanel = new JPanel();
		JPanel listePanel = new JPanel();

		btnPanel.setBounds(0, 0, 582, 60);
		getContentPane().add(btnPanel);
		btnPanel.setLayout(null);

		listePanel.setBounds(0, 60, 576, 282);
		getContentPane().add(listePanel);
		listePanel.setLayout(null);

		JLabel lblisim = new JLabel("Isim");
		lblisim.setBounds(10, 32, 46, 14);
		btnPanel.add(lblisim);

		final JTextField txtisim = new JTextField();
		txtisim.setBounds(47, 29, 107, 20);
		btnPanel.add(txtisim);
		txtisim.setColumns(10);

		JLabel lblTelefon = new JLabel("Telefon");
		lblTelefon.setBounds(398, 32, 46, 14);
		btnPanel.add(lblTelefon);

		final JTextField txtTelefon = new JTextField();
		txtTelefon.setBounds(454, 29, 114, 20);
		btnPanel.add(txtTelefon);
		txtTelefon.setColumns(10);

		JLabel lblSoyisim = new JLabel("Soyisim");
		lblSoyisim.setBounds(196, 32, 46, 14);
		btnPanel.add(lblSoyisim);

		final JTextField txtSoyisim = new JTextField();
		txtSoyisim.setBounds(252, 29, 107, 20);
		btnPanel.add(txtSoyisim);
		txtSoyisim.setColumns(10);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 568, 21);
		btnPanel.add(menuBar);

		JMenu menuM = new JMenu("Menu");
		menuBar.add(menuM);
		JMenuItem saveM = new JMenuItem("Kaydet");
		JMenuItem updateM = new JMenuItem("Duzelt");
		updateM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!txtTelefon.getText().isEmpty() && !txtSoyisim.getText().isEmpty()
						&& !txtisim.getText().isEmpty()) {

					int idDeneme = (Integer) dftModel.getValueAt(jTable1.getSelectedRow(), 0);
					kisiBilgi.setIsim(txtisim.getText());
					kisiBilgi.setsoyisim(txtSoyisim.getText());
					kisiBilgi.setTelefon(txtTelefon.getText());

					vt.update(idDeneme, kisiBilgi.getisim(), kisiBilgi.getsoyisim(), kisiBilgi.getTelefon());

				} else {
					JOptionPane.showMessageDialog(null, "Eksik Bilgileri Mevcut!");
					if (txtisim.getText().isEmpty()) {
						txtisim.requestFocus();
					}
					if (txtSoyisim.getText().isEmpty()) {
						txtSoyisim.requestFocus();
					}
					if (txtTelefon.getText().isEmpty()) {
						txtTelefon.requestFocus();
					}
				}
				dftModel.setRowCount(0);
				doldur();

			}
		});
		JMenuItem deleteM = new JMenuItem("Sil");
		deleteM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				kisiBilgi.setId((Integer) dftModel.getValueAt(jTable1.getSelectedRow(), 0));
				vt.delete(kisiBilgi.getId());
				dftModel.setRowCount(0);
				doldur();
			}
		});
		JMenuItem vcfM = new JMenuItem("VCF Aktar");
		vcfM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle("choosertitle");
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);

				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					pathi = chooser.getSelectedFile().toString();
				}

				ResultSet rss = vt.FillTable();
				// scrollpane.setViewportView(jTable1);
				try {
					while (rss.next()) {
						satir[1] = rss.getString("isim");
						satir[2] = rss.getString("soyisim");
						satir[3] = rss.getString("tel");
						vt.vcfAktar(pathi, satir[1], satir[2], satir[3]);
					}
				} catch (Exception e) {

				}

			}
		});
		JMenuItem cikisM = new JMenuItem("Cikis");
		cikisM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		menuM.add(saveM);
		menuM.add(updateM);
		menuM.add(deleteM);
		menuM.addSeparator();
		menuM.add(vcfM);
		menuM.addSeparator();
		menuM.add(cikisM);

		saveM.addActionListener(new ActionListener() {
			// Kayit Ekleme Basüarili
			// Yapilacak varolan kayit varsa ekleme iptali
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!txtTelefon.getText().isEmpty() && !txtSoyisim.getText().isEmpty()
						&& !txtisim.getText().isEmpty()) {
					kisiBilgi.setIsim(txtisim.getText());
					kisiBilgi.setsoyisim(txtSoyisim.getText());
					kisiBilgi.setTelefon(txtTelefon.getText());
					vt.insert(kisiBilgi);
					dftModel.setRowCount(0);
					doldur();
				} else {
					JOptionPane.showMessageDialog(null, "Eksik Bilgileri Mevcut!");
					// Temizleme yerine imlec bosü olana gitsin yapilabilir. Yapalim =)
					// txtisim.setText("");
					// txtisim.setText("");
					// txtisim.setText("");
					if (txtisim.getText().isEmpty()) {
						txtisim.requestFocus();
					}
					if (txtSoyisim.getText().isEmpty()) {
						txtSoyisim.requestFocus();
					}
					if (txtTelefon.getText().isEmpty()) {
						txtTelefon.requestFocus();
					}
				}

			}
		});

		// table = new JList();

		JScrollPane scrollpane = new JScrollPane();
		scrollpane.setBounds(563, 11, 13, 260);
		listePanel.add(scrollpane);

		jTable1 = new JTable();
		jTable1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {

				kisiBilgi.setId((Integer) dftModel.getValueAt(jTable1.getSelectedRow(), 0));
				kisiBilgi.setIsim((String) dftModel.getValueAt(jTable1.getSelectedRow(), 1));
				kisiBilgi.setsoyisim((String) dftModel.getValueAt(jTable1.getSelectedRow(), 2));
				kisiBilgi.setTelefon((String) dftModel.getValueAt(jTable1.getSelectedRow(), 3));
				txtisim.setText((String) dftModel.getValueAt(jTable1.getSelectedRow(), 1));
				txtSoyisim.setText((String) dftModel.getValueAt(jTable1.getSelectedRow(), 2));
				txtTelefon.setText((String) dftModel.getValueAt(jTable1.getSelectedRow(), 3));

			}
		});
		jTable1.setBounds(10, 11, 556, 260);
		dftModel.setColumnIdentifiers(kolon);
		doldur();

		// table.setBounds(10, 11, 556, 260);
		// JScrollPane pane = new JScrollPane();
		// table.setListData(vt.listele().toArray());
		listePanel.add(jTable1);
		// scrollpane.setViewportView(jTable1);
		// listePanel.add(table);

		// jTable1.setModel(model);

		// listePanel.add(tablo);
		// panel.add(listePanel, BorderLayout.SOUTH);
		return panel;
	}

	public void doldur() {
		ResultSet rss = vt.FillTable();
		// scrollpane.setViewportView(jTable1);
		try {
			while (rss.next()) {
				satir[0] = rss.getInt("id");
				satir[1] = rss.getString("isim");
				satir[2] = rss.getString("soyisim");
				satir[3] = rss.getString("tel");
				dftModel.addRow(satir);
			}
		} catch (Exception e) {

		}
		jTable1.setModel(dftModel);
	}

	@Override
	public JMenuBar menubar() {
		// TODO Auto-generated method stub
		return null;
	}
}
