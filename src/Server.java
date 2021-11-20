import java.io.*;
import java.net.*;

public class Server {
    private ServerSocket ss;
    private int numPlayers;
   
    private ServerSideConnection player1;
    private ServerSideConnection player2;
    private int turns;
    private int maxRounds;
    private int p1Num;
    private int p2Num;
    private int rounds;



    public Server(){
        System.out.println("Server Status");
        System.out.println("Please wait while all players connect.");
        numPlayers = 0;
        turns = 0;
        maxRounds = 4;
        rounds = 0;


        try {
            ss = new ServerSocket(7621);
        } catch (IOException e) {
           System.out.println("Exception from Server: ");
           e.printStackTrace();
        }
    }
    	
    public class ServerSideConnection implements Runnable{
    	private Socket socket;
    	private DataInputStream dataIn;
    	private DataOutputStream dataOut;
    	private int playerId;
    	
    	public ServerSideConnection(Socket s, int id) {
    		socket = s;
    		playerId = id;
    		
    		try {
    			dataIn = new DataInputStream(socket.getInputStream());
    			dataOut = new DataOutputStream(socket.getOutputStream());
    		}catch(IOException e) {
    			e.printStackTrace();
    		}
    	}
    	
    	
    	public void run() {
    		try {
    			dataOut.writeInt(playerId);
                dataOut.write(maxRounds);
    			dataOut.flush();
    			
    			while (turns < 5) {
                  
    				if(playerId ==1){
                        p1Num = dataIn.readInt();
                        System.out.println("Player 1 clicked button #" + p1Num);
                        turns++;
                        System.out.println(turns);
                        player2.sendClick(p1Num);
                     
    				}
                    if(playerId == 2) {
                        p2Num = dataIn.readInt();
                        System.out.println("Player 2 clicked button #" + p2Num);
                        turns++;
                        System.out.println(turns);
                        player1.sendClick(p2Num);
                        if(turns % 2 == 0){
                            if(p1Num == p2Num){
                          
                                System.out.println("player 2 won has won this round");
                                rounds++;
                            }
                            else {
                               
                            	System.out.println("player 1 won has won this round");
                            	rounds++;
                            }
                        }
                    }

                    
    			}
    		}catch(IOException e) {
    			
    		}
    	}
        public void sendClick(int n ){
            try{
                dataOut.writeInt(n);
                dataOut.flush();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }


    
    public void connectServer() {
 
    	
        try {
            while(numPlayers < 2){
                Socket s = ss.accept();
                numPlayers++;
                System.out.println("Player " + numPlayers + " connected. Hello ");
                ServerSideConnection ssc = new ServerSideConnection(s, numPlayers);

                if(numPlayers == 1){
                    player1= ssc;
                }else {
                    player2 = ssc;
                }
                Thread t = new Thread(ssc);
                t.start();
            }
            System.out.println("We now have the 2 players. The game will now begin.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        Server gameServer = new Server();
        gameServer.connectServer();
    }

}
