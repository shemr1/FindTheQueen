import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;


public class PlayerFrame extends JFrame{
    private int width;
    private int height;
    private Container screenPane;
    private JTextArea message;
    private JButton btn1;
    private JButton btn2;
    private JButton btn3;
    private int playerId;
    private int otherPlayer;
    private int maxRnds = 1;
    private int turns;
    private int myWins= 0;
    private int myChoice;
    private int oppChoice;
    boolean enabled;
    private int oppWins =0;
    private String name;
    private int rand;

    private ClientSideConnection clientSideConnection;
	


    public PlayerFrame(String string){
    	name = string;
        width = 500;
        height = 350;
        screenPane = this.getContentPane();
        message = new JTextArea();
        btn1 = new JButton("1");
        btn2 = new JButton("2");
        btn3= new JButton("3");
    }

    public void initGUI(String s) {
        this.setSize(width,height);
        this.setTitle("Find the Queen, " + s );
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        screenPane.setLayout(new GridLayout(5,5));
        screenPane.add(message);
        message.setText("Lets have some fun");
        message.setWrapStyleWord(true);
        message.setLineWrap(true);
        message.setEditable(false);
        screenPane.add(btn1);
        screenPane.add(btn2);
        screenPane.add(btn3);
        rand = (int) Math.random();
        
        if (rand % 2== 0) {
        	if(playerId == 1){
                message.setText("You are the dealer. You go first.");
                otherPlayer = 2;
                enabled =true;
            }else{
                message.setText("You are the spotter. Wait your turn");
                otherPlayer = 1;
                enabled = false;
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        updateTurn();
                    }
                });
                t.start();
            }

        }else {
        	if(playerId == 2){
                message.setText("You are the dealer. You go first.");
                otherPlayer = 1;
                enabled =true;
            }else{
                message.setText("You are the spotter. Wait your turn");
                otherPlayer = 2;
                enabled = false;
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        updateTurn();
                    }
        });
                }}
        
        
        toggle();
        this.setVisible(true);
    }



    public void connectServer(String username){
        clientSideConnection = new ClientSideConnection();
    }

    public void setButtons(){
        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JButton button  = (JButton) e.getSource();
                int buttonNum = Integer.parseInt(button.getText());


                System.out.println("Round: " +maxRnds);
                enabled = false;
                toggle();

                message.setText("You clicked button " +buttonNum+ ". Wait for your opponent.");
                turns++;
               
                myChoice = buttonNum;
                clientSideConnection.sendButtonClick(buttonNum);
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        updateTurn();
                    }
                });
                t.start();


            }

        };

        btn1.addActionListener(actionListener);
        btn2.addActionListener(actionListener);
        btn3.addActionListener(actionListener);
    }

    public void toggle(){
        btn1.setEnabled(enabled);
        btn2.setEnabled(enabled);
        btn3.setEnabled(enabled);
    }

    public void updateTurn(){
        int n = clientSideConnection.receiveOppClick();
        oppChoice = n;
        message.setText("Your opponent has made his choice");
        if (turns % 2 == 1) {
        	if(enabled == false) {
        		enabled = true;
                toggle();
        	}
        	else {
        		enabled = false;
        		toggle();
        	}
        	  
        }else {
        	   enabled = true;
        toggle();
        }
     
    }

    public void checkSwitch(){
        if(playerId == 2){

        }
    }



    //Client Connection
    private class ClientSideConnection{
        private Socket socket;
        private DataInputStream dataInputStream;
        private DataOutputStream dataOutputStream;

        public ClientSideConnection() {
            System.out.println("Client " + name);
            try {
                socket = new Socket("localhost", 7621);

                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());

                playerId = dataInputStream.readInt();
                System.out.println(playerId);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public void sendButtonClick(int n){
            try {
                dataOutputStream.writeInt(n);
                dataOutputStream.flush();
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        public int receiveOppClick(){
            int x = 0;
            try{
                x = dataInputStream.readInt();
                System.out.println("Your opponent has made their choice");
                oppChoice = x;
            }catch(IOException e){
                e.printStackTrace();
            }
            return x;
        }

        public String receiveServerString(){
            String x = null;
            try{
                x = dataInputStream.readUTF();
                System.out.println("Your opponent has made their choice");
            }catch(IOException e){
                e.printStackTrace();
            }
            return x;
        }
    }

    public static void main(String string){
    	
        PlayerFrame pf = new PlayerFrame(string);
    	 pf.connectServer(string);
         pf.initGUI(string);
         pf.setButtons();



    }

}






