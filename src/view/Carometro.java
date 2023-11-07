package view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import model.connection;
import utils.validator;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Carometro extends JFrame {

	// instanciar objetos JDBC
	connection conn = new connection();
	private Connection con;
	private PreparedStatement pst;
	private ResultSet rs;

	// instanciar objeto para o fluxo de bytes
	private FileInputStream fis;

	// variavel global para armazenar o tamanho da imagem(bytes)
	private int tamanho;

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblStatus;
	private JLabel lblData;
	private JLabel lblNewLabel;
	private JTextField textRA;
	private JLabel iblNewLabel;
	private JLabel txtName;
	private JTextField textField;
	private JTextField TXTname;
	private JLabel lblphoto;
	private JButton btnReset;
	private JButton btnBuscar;
	private JScrollPane scrollPaneLista;
	private JList listNomes;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Carometro frame = new Carometro();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Carometro() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				status();
				setarData();
			}
		});
		setTitle("Carometro");
		setResizable(false);
		setIconImage(
				Toolkit.getDefaultToolkit().getImage(Carometro.class.getResource("/img/instagram.jessicaalencar.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 640, 364);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		scrollPaneLista = new JScrollPane();
		scrollPaneLista.setBorder(null);
		scrollPaneLista.setVisible(false);
		scrollPaneLista.setBounds(92, 93, 231, 57);
		contentPane.add(scrollPaneLista);

		listNomes = new JList();
		listNomes.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				buscarNome();
			}
		});
		listNomes.setBorder(null);
		scrollPaneLista.setViewportView(listNomes);

		JPanel panel = new JPanel();
		panel.setBackground(SystemColor.textHighlight);
		panel.setBounds(-10, 276, 636, 51);
		contentPane.add(panel);
		panel.setLayout(null);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(312, 5, 1, 1);
		panel_1.setBackground(new Color(0, 128, 192));
		panel.add(panel_1);
		panel_1.setLayout(null);

		lblStatus = new JLabel("");
		lblStatus.setIcon(new ImageIcon(Carometro.class.getResource("/img/dboff.png")));
		lblStatus.setBounds(584, 10, 32, 32);
		panel.add(lblStatus);

		lblData = new JLabel("");
		lblData.setForeground(SystemColor.text);
		lblData.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblData.setBounds(42, 21, 271, 20);
		panel.add(lblData);

		lblNewLabel = new JLabel("RA");
		lblNewLabel.setBounds(23, 34, 45, 13);
		contentPane.add(lblNewLabel);

		textRA = new JTextField();
		textRA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buscarRA();
			}
		});
		textRA.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				String caracteres = "0123456789";
				if (!caracteres.contains(e.getKeyChar() + "")) {
					e.consume();
				}
			}
		});
		textRA.setBounds(57, 31, 96, 19);
		contentPane.add(textRA);
		textRA.setColumns(10);
		// uso do PlainDocument para limitar campos
		textRA.setDocument(new validator(6));

		iblNewLabel = new JLabel("Nome");
		iblNewLabel.setBounds(37, 80, 45, 13);
		contentPane.add(iblNewLabel);

		TXTname = new JTextField();
		TXTname.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		TXTname.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				listarNomes();
			}
		});
		TXTname.setBounds(92, 77, 231, 19);
		contentPane.add(TXTname);
		TXTname.setColumns(10);
		TXTname.setDocument(new validator(30));

		lblphoto = new JLabel("");
		lblphoto.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		lblphoto.setIcon(new ImageIcon(Carometro.class.getResource("/img/camera.png")));
		lblphoto.setBounds(380, 10, 256, 256);
		contentPane.add(lblphoto);

		JButton btnCarregar = new JButton("Carregar foto");
		btnCarregar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				carregarFoto();

			}

		});
		btnCarregar.setForeground(SystemColor.textHighlight);
		btnCarregar.setBounds(210, 158, 131, 21);
		contentPane.add(btnCarregar);

		JButton btnadicionar = new JButton("");
		btnadicionar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				adicionar();
			}
		});
		btnadicionar.setToolTipText("Adicionar");
		btnadicionar.setIcon(new ImageIcon(Carometro.class.getResource("/img/create.png")));
		btnadicionar.setBounds(23, 189, 64, 64);
		contentPane.add(btnadicionar);

		btnReset = new JButton("");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reset();
			}
		});
		btnReset.setIcon(new ImageIcon(Carometro.class.getResource("/img/eraser.png")));
		btnReset.setToolTipText("Limpar campos");
		btnReset.setBounds(289, 189, 64, 64);
		contentPane.add(btnReset);

		btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buscarRA();
			}
		});
		btnBuscar.setForeground(SystemColor.textHighlight);
		btnBuscar.setBounds(182, 30, 131, 21);
		contentPane.add(btnBuscar);
		
		JButton btnEditar = new JButton("");
		btnEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editar();
			}
		});
		btnEditar.setIcon(new ImageIcon(Carometro.class.getResource("/img/update.png")));
		btnEditar.setToolTipText("Editar");
		btnEditar.setBounds(81, 189, 64, 64);
		contentPane.add(btnEditar);

	}// fim construtor

	private void status() {
		try {
			con = conn.conectar();
			if (con == null) {
				// System.out.println("Connection error");
				lblStatus.setIcon(new ImageIcon(Carometro.class.getResource("/img/dboff.png")));

			} else {
				// System.out.println("Connected database ");
				lblStatus.setIcon(new ImageIcon(Carometro.class.getResource("/img/dbon.png")));
			}
			con.close();

		} catch (Exception e) {
			System.out.println(e);

		}

	}

	private void setarData() {
		Date data = new Date();
		DateFormat formatador = DateFormat.getDateInstance(DateFormat.FULL);
		lblData.setText(formatador.format(data));
	}

	private void carregarFoto() {
		JFileChooser jfc = new JFileChooser();
		jfc.setDialogTitle("Selecionar arquivo");
		jfc.setFileFilter(new FileNameExtensionFilter("Arquivo de imagens(*.PNG,* .JPG)", "png", "jpg", "jpeg"));
		int result = jfc.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			try {
				fis = new FileInputStream(jfc.getSelectedFile());
				tamanho = (int) jfc.getSelectedFile().length();
				Image photo = ImageIO.read(jfc.getSelectedFile()).getScaledInstance(lblphoto.getWidth(),
						lblphoto.getHeight(), Image.SCALE_SMOOTH);
				lblphoto.setIcon(new ImageIcon(photo));
				lblphoto.updateUI();
			} catch (Exception e) {
				System.out.println(e);
			}
		}

	}

	private void adicionar() {

		if (TXTname.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Preenha o nome");
			TXTname.requestFocus();
		} else {
			String insert = "insert into students(name,photo)values(?,?)";
			try {
				con = conn.conectar();
				pst = con.prepareStatement(insert);
				pst.setString(1, TXTname.getText());
				pst.setBlob(2, fis, tamanho);
				int confirma = pst.executeUpdate();
				if (confirma == 1) {
					JOptionPane.showMessageDialog(null, "Aluno cadastrado com sucesso.");
					reset();
				} else {
					JOptionPane.showMessageDialog(null, " ERRO! Aluno não cadastrado.");
					con.close();
				}

			} catch (Exception e) {
				System.out.println(e);
			}

		}

	}

	private void buscarRA() {
		if (textRA.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Digite o RA");
			textRA.requestFocus();
		} else {
			String readRA = " select * from  students where ra = ?";
			try {
				con = conn.conectar();
				pst = con.prepareStatement(readRA);
				pst.setString(1, textRA.getText());
				rs = pst.executeQuery();
				if (rs.next()) {
					TXTname.setText(rs.getString(2));
					Blob blob = (Blob) rs.getBlob(3);
					byte[] img = blob.getBytes(1, (int) blob.length());
					BufferedImage image = null;
					try {
						image = ImageIO.read(new ByteArrayInputStream(img));
					} catch (Exception e) {
						System.out.println(e);

					}
					ImageIcon icone = new ImageIcon(image);
					Icon photo = new ImageIcon(icone.getImage().getScaledInstance(lblphoto.getWidth(),
							lblphoto.getHeight(), image.SCALE_SMOOTH));
					lblphoto.setIcon(photo);

				} else {
					JOptionPane.showMessageDialog(null, "Aluno não cadastrado ");

				}
				con.close();

			} catch (Exception e) {
				System.out.println(e);
			}
		}

	}

	private void listarNomes() {
		DefaultListModel<String> modelo = new DefaultListModel<>();
		listNomes.setModel(modelo);
		String readLista = "select * from students where name like '" + TXTname.getText() + "%'" + "order by name";
		try {
			con = conn.conectar();
			pst = con.prepareStatement(readLista);
			rs = pst.executeQuery();
			while (rs.next()) {
				scrollPaneLista.setVisible(true);
				modelo.addElement(rs.getString(2));
				if (TXTname.getText().isEmpty()) {
					scrollPaneLista.setVisible(false);

				}

			}
			con.close();

		} catch (Exception e) {
			System.out.println(e);
		}

	}

	private void buscarNome() {
		int linha = listNomes.getSelectedIndex();
		if (linha >= 0) {
			String readNome = "Select * from students where name like '" + TXTname.getText() + "%'"
					+ "order by name limit " + (linha) + ", 1";
			try {
				con = conn.conectar();
				pst = con.prepareStatement(readNome);
				rs = pst.executeQuery();
				while (rs.next()) {
					scrollPaneLista.setVisible(false);
					textRA.setText(rs.getString(1));
					TXTname.setText(rs.getString(2));
					Blob blob = (Blob) rs.getBlob(3);
					byte[] img = blob.getBytes(1, (int) blob.length());
					BufferedImage image = null;
					try {
						image = ImageIO.read(new ByteArrayInputStream(img));
					} catch (Exception e) {
						System.out.println(e);

					}
					ImageIcon icone = new ImageIcon(image);
					Icon photo = new ImageIcon(icone.getImage().getScaledInstance(lblphoto.getWidth(),
							lblphoto.getHeight(), image.SCALE_SMOOTH));
					lblphoto.setIcon(photo);
				}
			} catch (Exception e) {
				System.out.println(e);
			}

		} else {
			scrollPaneLista.setVisible(false);
			

		}

	}
	
	private void editar() {
		if (TXTname.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Preenha o nome");
			TXTname.requestFocus();
		} else {
			String update = "update students set name =? , photo=? where ra=?";
			try {
				con = conn.conectar();
				pst = con.prepareStatement(update);
				pst.setString(1, TXTname.getText());
				pst.setBlob(2, fis, tamanho);
				pst.setString(3, textRA.getText());
				int confirma = pst.executeUpdate();
				if (confirma == 1) {
					JOptionPane.showMessageDialog(null, "Dados do aluno alterado.");
					reset();
				} else {
					JOptionPane.showMessageDialog(null, " ERRO! Dados do aluno não alterdos.");
					con.close();
				}

			} catch (Exception e) {
				System.out.println(e);
			}

		}
	}

	private void reset() {
		scrollPaneLista.setVisible(false);
		textRA.setText(null);
		TXTname.setText(null);
		lblphoto.setIcon(new ImageIcon(Carometro.class.getResource("/img/camera.png")));
		TXTname.requestFocus();

	}
}
