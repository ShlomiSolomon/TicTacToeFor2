import generic.Stack;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientTicTacToe extends JFrame implements MouseListener, KeyListener {



	private static boolean Next_Previews = false;
	private static String username;
	private Stack LiFo;
    private Stack FiLo;
    private final JLabel userProfileLabel;
    private final JLabel usernameLabel;
    private final JLabel winLabel;
    private final JLabel lossLabel;
    private final JLabel turnLabel;
	private final JButton BackButton;
	private final JButton BackForwardButton;
	private final JButton Exit;
	private final JButton makeConnectionButton;
	private final JButton RematchButton;
	private static int win = 0;
	private static int loss = 0;
	private int pos;

	boolean yourTurn = true;
	private static Socket socket = null;
	private static DataInputStream input;
	private static DataOutputStream output;

	private ArrayList<Integer> arrayList;
	private boolean finish = false;
	public String id2;

	JButton b[] = new JButton[9];
	String ch[] = new String[9];
	JPanel p;
	int i = 0;

	public ClientTicTacToe() throws IOException {

	    setTitle("Client");
		arrayList = new ArrayList<Integer>();
		FiLo = new Stack();
		LiFo = new Stack();
		p = new JPanel();
		p.setLayout(null);

		new DraggableJFrame(this);
		usernameLabel = new JLabel(username);
		usernameLabel.setBounds(20, 10, 200, 20);
		p.add(usernameLabel);
		usernameLabel.setForeground(Color.WHITE);
		usernameLabel.setFont(new Font("Arial", Font.PLAIN, 15));

		userProfileLabel = new JLabel();
		userProfileLabel.setBounds(160, 25, 42, 42);
		userProfileLabel.addMouseListener(this);
		userProfileLabel.setBackground(null);
		userProfileLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.gray));
		p.add(userProfileLabel);
		setImageLabel( "/img/X.png", userProfileLabel, 40, 40);

		winLabel = new JLabel("Wins: " + win);
		winLabel.setBounds(20, 40, 100, 20);
		p.add(winLabel);
		winLabel.setForeground(Color.WHITE);
		winLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 15));

		lossLabel = new JLabel("Loss: " + loss);
		lossLabel.setBounds(20, 60, 100, 20);
		p.add(lossLabel);
		lossLabel.setForeground(Color.WHITE);
		lossLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 15));

        turnLabel = new JLabel("First click to start");
        turnLabel.setBounds(110, 70, 250, 20);
		p.add(turnLabel);
		turnLabel.setForeground(Color.WHITE);
		turnLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 15));

		Exit = new JButton();
		p.add( Exit );
		Exit.setBounds(300, 10, 32, 32);
		Exit.setBackground(null);
		Exit.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.BLACK));
		Exit.addMouseListener(this);
		setImage( "/img/exit.png", Exit );


		RematchButton = new JButton();
		RematchButton.setBounds(140, 440, 64, 64);
		RematchButton.addMouseListener(this);
		RematchButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		RematchButton.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.gray));
		RematchButton.setVisible( false );
		p.add(RematchButton);
		RematchButton.setBackground(null);
		p.setBackground(Color.gray);
		setImage("/img/reload.png", RematchButton);


		BackButton = new JButton();
		BackButton.setBounds(30, 440, 64, 64);
		BackButton.addMouseListener(this);
		BackButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		p.add(BackButton);
		BackButton.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.gray));
		BackButton.setBackground(null);
		setImage("/img/left.png", BackButton);
		BackButton.setVisible( false );


		BackForwardButton = new JButton();
		BackForwardButton.setBounds(264, 440, 64, 64);
		BackForwardButton.addMouseListener(this);
		BackForwardButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		p.add(BackForwardButton);
		BackForwardButton.setBackground(null);
		BackForwardButton.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.gray));
		setImage("/img/right.png", BackForwardButton);
		BackForwardButton.setVisible( false );



		makeConnectionButton = new JButton("Reconnection");
		makeConnectionButton.setBounds(110, 515, 124, 30);
		makeConnectionButton.addMouseListener(this);
		makeConnectionButton.setBackground(null);
		makeConnectionButton.setForeground(Color.white);
		p.add(makeConnectionButton);

		add(p);
		i = 0;
		while (i < 9) {
			ch[i] = "";
			b[i] = new JButton();
			p.add(b[i]);
			b[i].setBackground(Color.white);
			b[i].addMouseListener(this);
			if (i < 3) {
				b[i].setBounds(20 + i * 105, 120, 100, 100);
			} else if (i < 6) {
				b[i].setBounds(20 + (i - 3) * 105, 225, 100, 100);
			} else if (i < 9) {
				b[i].setBounds(20 + (i - 6) * 105, 330, 100, 100);
			}
			b[i].setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
			i++;
		}

		setFocusable(true);
		addKeyListener(this);
	}

	@Override
	public void setFocusable(boolean b) {
		super.setFocusable(b);
	}

	public static void main(String[] args) throws IOException {

		String name = "Player 2";
		Login l = new Login(name);

		l.setBounds(280, 210, 810, 350);
		l.setVisible(true);

		while (l.username == null) {
			try {
				Thread.sleep(500);
			} catch (Exception e) {
			}
		}
		username = l.username;
		ClientTicTacToe s = new ClientTicTacToe();
		s.setVisible(true);
		l.setVisible(false);
		s.setBounds(800, 160, 345, 560);

		if (socket == null) {
			s.makeSocket();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Thread o = new Thread(() -> {
			if (socket == null) {
				if (e.getSource() == makeConnectionButton) {
					makeSocket();
				}
			}

			if (e.getSource() == Exit) {
				try {
					if (socket != null) {
						output.writeUTF("" + 20);
					}
					System.exit(0);
				} catch (IOException ex) {
					Logger.getLogger( ServerTicTacToe.class.getName()).log(Level.SEVERE, null, ex);
				}
			}

			if (e.getSource() == RematchButton) {
				if (finish) {
					try {
						output.writeUTF("10");
					} catch (IOException ex) {
						Logger.getLogger( ClientTicTacToe.class.getName()).log(Level.SEVERE, null, ex);
					}
				}
			}
			if (finish) {

				if (e.getSource() == BackButton) {
					int back = Integer.parseInt("" + FiLo.pop());
					LiFo.push(back);
					FiLo.display(FiLo);
					LiFo.display(LiFo);
					Next_Previews = !Next_Previews;
					b[back].setIcon(null);
				}

				if (e.getSource() == BackForwardButton) {
					int forward = Integer.parseInt("" + LiFo.pop());
					FiLo.push(forward);
					if (Next_Previews) {
						setImage( "/img/O.png", b[forward]);
						Next_Previews = !Next_Previews;
					} else {
						setImage( "/img/X.png", b[forward]);
						Next_Previews = !Next_Previews;
					}
				}

			}

			i = 0;
			label: if (socket != null) {
				while (i < 9) {
					if (b[i] == e.getSource()) {
						if (!arrayList.contains(i) && yourTurn) {
							turnLabel.setText("Your turn ! ");
							doMove();
							playSound();
						} else {
							break label;
						}
					}
					i++;
				}
			} else {
				JOptionPane.showMessageDialog(null, "there is a problem please call your friend to make connection!");
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ex) {
				Logger.getLogger( ServerTicTacToe.class.getName()).log(Level.SEVERE, null, ex);
			}

		});
		o.start();
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if (e.getSource() == Exit || e.getSource() == RematchButton || e.getSource() == BackButton
				|| e.getSource() == BackForwardButton || e.getSource() == makeConnectionButton) {
			setCursor(HAND_CURSOR);
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {

		if (e.getSource() == Exit || e.getSource() == RematchButton || e.getSource() == BackButton
				|| e.getSource() == BackForwardButton || e.getSource() == makeConnectionButton) {
			setCursor(DO_NOTHING_ON_CLOSE);
		}
	}

	private boolean isWon() {

		switch (i) {
		case 0:
			if ((ch[1].equals("*") && ch[2].equals("*")) || (ch[3].equals("*") && ch[6].equals("*"))
					|| (ch[4].equals("*") && ch[8].equals("*"))) {
				finish = true;
				return true;
			}
			break;
		case 1:
			if ((ch[0].equals("*") && ch[2].equals("*")) || (ch[4].equals("*") && ch[7].equals("*"))) {
				finish = true;
				return true;
			}
			break;
		case 2:
			if ((ch[0].equals("*") && ch[1].equals("*")) || (ch[5].equals("*") && ch[8].equals("*"))
					|| (ch[4].equals("*") && ch[6].equals("*"))) {
				finish = true;
				return true;
			}
			break;

		case 3:
			if ((ch[0].equals("*") && ch[6].equals("*")) || (ch[4].equals("*") && ch[5].equals("*"))) {
				finish = true;
				return true;
			}
			break;

		case 4:
			if ((ch[0].equals("*") && ch[8].equals("*")) || (ch[2].equals("*") && ch[6].equals("*"))
					|| (ch[3].equals("*") && ch[5].equals("*")) || (ch[1].equals("*") && ch[7].equals("*"))) {
				finish = true;
				return true;
			}
			break;
		case 5:
			if ((ch[3].equals("*") && ch[4].equals("*")) || (ch[2].equals("*") && ch[8].equals("*"))) {
				finish = true;
				return true;
			}
			break;

		case 6:
			if ((ch[0].equals("*") && ch[3].equals("*")) || (ch[7].equals("*") && ch[8].equals("*"))
					|| (ch[4].equals("*") && ch[2].equals("*"))) {
				finish = true;
				return true;
			}
			break;
		case 7:
			if ((ch[6].equals("*") && ch[8].equals("*")) || (ch[1].equals("*") && ch[4].equals("*"))) {
				finish = true;
				return true;
			}
			break;
		case 8:
			if ((ch[0].equals("*") && ch[4].equals("*")) || (ch[6].equals("*") && ch[7].equals("*"))
					|| (ch[2].equals("*") && ch[5].equals("*"))) {
				finish = true;
				return true;
			}
			break;
		}
		return false;
	}

	private boolean isDraw() {
		for (int j = 0; j < 9; j++) {
			if (ch[j].equals("")) {
				return false;
			}
		}
		finish = true;

		try {
			output.writeUTF("15");
		} catch (IOException ex) {
			Logger.getLogger( ClientTicTacToe.class.getName()).log(Level.SEVERE, null, ex);
		}
		return true;
	}

	private void doMove() {
        if (finish)
            return;

		arrayList.add(i);
		yourTurn = false;
		turnLabel.setText("Opponent turn ...");

		ch[i] = "*";
		setImage( "/img/X.png", b[i]);
		try {
			if (socket != null) {
				output.writeUTF("" + i);
				FiLo.add(i);
				FiLo.display(FiLo);

				if (isWon()) {
				    output.writeUTF("9");
				    win++;
					winLabel.setText("Wins: " + win);
					finish = true;
					yourTurn = false;
					turnLabel.setText("Well done");
					Next_Previews = true;
					BackButton.setVisible( true );
					BackForwardButton.setVisible( true );
                    RematchButton.setVisible( true );
					JOptionPane.showMessageDialog(null, "You Win!");
				}

				if (isDraw()) {
					BackButton.setVisible( true );
					BackForwardButton.setVisible( true );
                    RematchButton.setVisible( true );
					JOptionPane.showMessageDialog(null, "Draw!");
				}


			}
		} catch (IOException ex) {
		}

	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
		Thread o = new Thread(() -> {
			if (e.getKeyCode() == KeyEvent.VK_1) {
				i = 0;
				label: if (socket != null) {
					if (!arrayList.contains(0) && yourTurn) {
						turnLabel.setText("Your turn ! ");
						doMove();
						playSound();
					} else {
						break label;
					}
				} else {
					JOptionPane.showMessageDialog(null, "there is a problem please call your friend to make connection!");
				}

			}
			if (e.getKeyCode() == KeyEvent.VK_2) {

				i = 1;
				label: if (socket != null) {
					if (!arrayList.contains(1) && yourTurn) {
						turnLabel.setText("Your turn ! ");
						doMove();
					} else {
						break label;
					}
				} else {
					JOptionPane.showMessageDialog(null, "there is a problem please call your friend to make connection!");
				}
			}

			if (e.getKeyCode() == KeyEvent.VK_3) {

				i = 2;
				label: if (socket != null) {
					if (!arrayList.contains(2) && yourTurn) {
						turnLabel.setText("Your turn ! ");
						doMove();
					} else {
						break label;
					}
				} else {
					JOptionPane.showMessageDialog(null, "there is a problem please call your friend to make connection!");
				}

			}

			if (e.getKeyCode() == KeyEvent.VK_4) {

				i = 3;
				label: if (socket != null) {
					if (!arrayList.contains(3) && yourTurn) {
						turnLabel.setText("Your turn ! ");
						doMove();
					} else {
						break label;
					}
				} else {
					JOptionPane.showMessageDialog(null, "there is a problem please call your friend to make connection!");
				}

			}

			if (e.getKeyCode() == KeyEvent.VK_5) {

				i = 4;
				label: if (socket != null) {
					if (!arrayList.contains(4) && yourTurn) {
						turnLabel.setText("Your turn ! ");
						doMove();
					} else {
						break label;
					}
				} else {
					JOptionPane.showMessageDialog(null, "there is a problem please call your friend to make connection!");
				}

			}

			if (e.getKeyCode() == KeyEvent.VK_6) {

				i = 5;
				label: if (socket != null) {
					if (!arrayList.contains(5) && yourTurn) {
						turnLabel.setText("Your turn ! ");
						doMove();
					} else {
						break label;
					}
				} else {
					JOptionPane.showMessageDialog(null, "there is a problem please call your friend to make connection!");
				}

			}

			if (e.getKeyCode() == KeyEvent.VK_7) {

				i = 6;
				label: if (socket != null) {
					if (!arrayList.contains(6) && yourTurn) {
						turnLabel.setText("Your turn ! ");
						doMove();
					} else {
						break label;
					}
				} else {
					JOptionPane.showMessageDialog(null, "there is a problem please call your friend to make connection!");
				}

			}

			if (e.getKeyCode() == KeyEvent.VK_8) {

				i = 7;
				label: if (socket != null) {
					if (!arrayList.contains(7) && yourTurn) {
						turnLabel.setText("Your turn ! ");
						doMove();
					} else {
						break label;
					}
				} else {
					JOptionPane.showMessageDialog(null, "there is a problem please call your friend to make connection!");
				}

			}

			if (e.getKeyCode() == KeyEvent.VK_9) {

				i = 8;
				label: if (socket != null) {
					if (!arrayList.contains(8) && yourTurn) {
						turnLabel.setText("Your turn ! ");
						doMove();
					} else {
						break label;
					}
				} else {
					JOptionPane.showMessageDialog(null, "there is a problem please call your friend to make connection!");
				}

			}

			try {
				Thread.sleep(1000);
			} catch (InterruptedException ex) {
				Logger.getLogger( ServerTicTacToe.class.getName()).log(Level.SEVERE, null, ex);
			}
		});
		o.start();
	}

	private void playSound() {
		try {
			AudioInputStream audioIn = AudioSystem
					.getAudioInputStream( ServerTicTacToe.class.getResource("/audio/move.wav"));
			Clip clip = AudioSystem.getClip();
			clip.open(audioIn);
			clip.start();

		} catch (Exception e) {
		}
	}


	private void setImage(String name, JButton b) {
		Image ImageName = new ImageIcon(this.getClass().getResource(name)).getImage();
		b.setIcon(new ImageIcon(ImageName));
	}

	private void setImageLabel(String name, JLabel b, int x, int y) {
		Image ImageName = new ImageIcon(this.getClass().getResource(name)).getImage().getScaledInstance(x, y,
				Image.SCALE_DEFAULT);

		b.setIcon(new ImageIcon(ImageName));
	}

	private void doReload() {

		BackButton.setVisible( false );
		BackForwardButton.setVisible( false );
        RematchButton.setVisible( false );
		for (int j = 0; j < 9; j++) {
			ch[j] = "";
			b[j].setBackground(Color.white);
			b[j].setIcon(null);
		}
		yourTurn = true;
		turnLabel.setText("Your turn ! ");
		finish = false;
		arrayList = new ArrayList<Integer>();
		FiLo = new Stack();
		LiFo = new Stack();
		id2 = null;
	}

	public static boolean isNumeric(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	void makeSocket() {

		try {
			socket = new Socket("localhost", 9999);
			input = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(socket.getOutputStream());
			pos = 0;
			while (true) {
                System.out.println( "Client " +pos );
				String str = null;
				str = input.readUTF().trim();
                String a = "";
				if (!isNumeric(str)) {
                    for (int j = 1; j < str.length(); j++) {
                        a += str.charAt( j );
                    }
                }else{
					pos = Byte.parseByte(str);
					if (pos == 20) {
						System.exit(0);
					}
					if (pos < 9) {
						FiLo.add(pos);
						FiLo.display(FiLo);
						arrayList.add(pos);
					}
					if (pos == 11) {
						BackButton.setVisible( true );
						BackForwardButton.setVisible( true );
                        RematchButton.setVisible( true );
						doReload();
					}

					if (pos == 10) {
						int reply = JOptionPane.showConfirmDialog(null, "Do You Want Rematch?", "Question",
								JOptionPane.YES_NO_OPTION);
						if (reply == JOptionPane.YES_OPTION) {
                            output.writeUTF("11");
							doReload();
						}
					}

					if (pos == 9) {

                        finish = true;
						yourTurn = false;
						turnLabel.setText("Better luck next time");
						BackButton.setVisible( true );
						BackForwardButton.setVisible( true );
                        RematchButton.setVisible( true );
						loss++;
						lossLabel.setText("Loss: " + loss);
						JOptionPane.showMessageDialog(null, "You Loss!");

					} else {
						yourTurn = true;
						turnLabel.setText("Your turn ! ");
					}

					if (pos == 15) {
						finish = true;
						BackButton.setVisible( true );
						BackForwardButton.setVisible( true );
                        RematchButton.setVisible( true );
                        output.writeUTF("b" + Login.username);
                        JOptionPane.showMessageDialog(null, "Draw!");
					}
					if (pos < 9) {
						ch[pos] = "0";
						setImage( "/img/O.png", b[pos]);

					}
				}
			}
		} catch (Exception e) {
		}
	}
}
