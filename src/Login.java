import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Login extends JFrame implements ActionListener, MouseListener {


    public static String username=null;
    JLabel l = new JLabel();
    private JTextField tf;
    JPanel mainPanel, childPanel;
    JButton login;
    private final JButton cancel;
    private final JLabel companyLabel;
    private final JLabel welcomeLabel;


    public Login(String name) {

        new DraggableJFrame(this);
        childPanel = new JPanel();
        childPanel.setLayout(null);
        childPanel.setSize(400, 350);
        childPanel.setLocation(0, 0);
        childPanel.setBackground(Color.decode("#5acef4"));
        add(childPanel);

        welcomeLabel = new JLabel("<html><p style='color:white;font-size:40px;'>Tic</p></html>");
        welcomeLabel.setBounds(40, -40, 285, 200);
        childPanel.add(welcomeLabel);

        companyLabel = new JLabel("<html><p style='color:gray;font-size:20px;'>TacToe</p></html>");
        companyLabel.setBounds(130, 0, 285, 200);
        childPanel.add(companyLabel);

        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setSize(400, 350);
        mainPanel.setLocation(400, 0);
        mainPanel.setBackground(Color.white);
        add(mainPanel);

        JTextField field = new JTextField();
        mainPanel.add(field);
        
        field.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray));


            tf = new JTextField();   //textFiled
            tf.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray));
            l = new JLabel();        // label

            mainPanel.add(tf);
            mainPanel.add(l);


        tf.setBounds(430, 80, 300, 28);


        cancel = new JButton();
        mainPanel.add(cancel);
        cancel.setBounds(760, 8, 32, 32);
        cancel.setBackground(null);
        cancel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.BLACK));
        Image cancelImage = new ImageIcon(this.getClass().getResource( "/img/exit.png" )).getImage();
        cancel.setIcon(new ImageIcon(cancelImage));

        cancel.addActionListener(this);
        cancel.addMouseListener(this);

        login = new JButton();
        login.setText("Login");
        login.setBounds(427, 270, 90, 25);
        login.setBackground(Color.white);

        login.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 2, Color.decode("#5acef4")));

        Image userImage = new ImageIcon(this.getClass().getResource("/img/user.png")).getImage();
        l.setIcon(new ImageIcon(userImage));

        JLabel userProfileLabel = new JLabel();
        userProfileLabel.setBounds(80, 140, 180, 180);
        userProfileLabel.addMouseListener(this);
        userProfileLabel.setBackground(null);
        childPanel.add(userProfileLabel);
        setImageLabel( "/img/bg.png", userProfileLabel, 180, 180);


        l.setBounds(745, 140, 40, 40);
        tf.setBounds(430, 140, 300, 28);

        mainPanel.add(login);
      
        l.setFont(new Font("Tahoma", 1, 14)); // NOI18N
        tf.setFont(new Font("Tahoma", 1, 14)); // NOI18N

        login.setFont(new Font("Tahoma", 1, 12)); // NOI18N
        login.addActionListener(this);
        login.addMouseListener(this);

        tf.setText(name);
        addMouseListener(this);
    }
    private void setImageLabel(String name, JLabel b, int x, int y) {
        Image ImageName = new ImageIcon(this.getClass().getResource(name)).getImage().getScaledInstance(x, y,
                Image.SCALE_DEFAULT);

        b.setIcon(new ImageIcon(ImageName));
    }


    @Override
    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == cancel) {
            System.exit(0);
        }
        if (ae.getSource() == login) {

            if (tf.getText().length() < 1) {
                JOptionPane.showMessageDialog(null, "please enter your username!");
            } else if (tf.getText().length() < 3) {
                JOptionPane.showMessageDialog(null, "you username can not less than 3 letter!");
            } 
            if (tf.getText().length() >= 3) {
                try {
                    username=tf.getText();  
                } catch (Exception ex) { 
                    Logger.getLogger( Login.class.getName()).log(Level.SEVERE, null, ex);
   
                }
                
            }
            
        }
    }

    @Override
    public void mouseClicked(MouseEvent me) {
    }

    @Override
    public void mousePressed(MouseEvent me) {
    }

    @Override
    public void mouseReleased(MouseEvent me) {
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        if (me.getSource() == login) {
            login.setBounds(429, 272, 90, 25);
            login.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.decode("#5acef4")));
            setCursor(HAND_CURSOR);
        }
        if (me.getSource() == cancel) {
            setCursor(HAND_CURSOR);
        }
    }

    @Override
    public void mouseExited(MouseEvent me) {

        if (me.getSource() == login) {
            login.setBounds(427, 270, 90, 25);
            login.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 2, Color.decode("#5acef4")));
            setCursor(DO_NOTHING_ON_CLOSE);

        }

        if (me.getSource() == cancel) {
            setCursor(DO_NOTHING_ON_CLOSE);
        }
    }
}
