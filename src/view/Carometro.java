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
import java.io.FileInputStream;
import java.sql.Connection;
import java.text.DateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import model.connection;
import utils.validator;

public class Carometro extends JFrame {
	
	// instanciar objetos JDBC
	connection conn = new connection();
	private Connection con;
	
	//instanciar objeto para o fluxo de bytes
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
		setIconImage(Toolkit.getDefaultToolkit().getImage(Carometro.class.getResource("/img/instagram.jessicaalencar.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 640, 364);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
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
		textRA.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				String caracteres = "0123456789";
				if (!caracteres.contains(e.getKeyChar()+ "")) {
					e.consume();
				}
			}
		});
		textRA.setBounds(57, 31, 96, 19);
		contentPane.add(textRA);
		textRA.setColumns(10);
		// uso do PlainDocument para limitar campos
		textRA.setDocument(new validator(6) );
		
		
		
		iblNewLabel = new JLabel("Nome");
		iblNewLabel.setBounds(37, 80, 45, 13);
		contentPane.add(iblNewLabel);
		
		TXTname = new JTextField();
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
		btnCarregar.setBounds(209, 129, 131, 21);
		contentPane.add(btnCarregar);
		
		
		
				
		
		
		
	}// fim construtor
	
	private void status() {
		try {
			con = conn.conectar();
			if (con == null) {
				//System.out.println("Connection error");
				lblStatus.setIcon(new ImageIcon(Carometro.class.getResource("/img/dboff.png")));
				
			} else {
				//System.out.println("Connected database ");
				lblStatus.setIcon(new ImageIcon(Carometro.class.getResource("/img/dbon.png")));
			}
			con.close();
			
		} catch (Exception e) {
			System.out.println(e);
			 
		}
		
	}
	private void setarData() {
		Date data = new Date();
		DateFormat formatador =
				DateFormat.getDateInstance(DateFormat.FULL);
	lblData.setText(formatador.format(data));
	}
	
	private void carregarFoto( ) {
		JFileChooser jfc = new JFileChooser();
		jfc.setDialogTitle("Selecionar arquivo");
		jfc.setFileFilter(new FileNameExtensionFilter("Arquivo de imagens(*.PNG,* .JPG)","png","jpg","jpeg"));
		int result = jfc.showOpenDialog(this);
		if(result ==JFileChooser.APPROVE_OPTION) {
			try {
				fis = new FileInputStream(jfc.getSelectedFile());
				tamanho = (int) jfc.getSelectedFile().length();		
				Image photo = ImageIO.read(jfc.getSelectedFile()).getScaledInstance
						(lblphoto.getWidth(),lblphoto.getHeight() , Image.SCALE_SMOOTH);
				lblphoto.setIcon(new ImageIcon(photo));
				lblphoto.updateUI();
			}catch (Exception e) {
				System.out.println(e);
			}
		}
		
		
	}
	
	
			
		
		
	}

