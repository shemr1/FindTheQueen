//Shemar Brown-Wright
//20/11/2021

import java.io.*;
import java.net.*;


//Server class 
public class Server {
    private ServerSocket ss;
    private int numPlayers;
   //declaration of ssc for each user
    private ServerSideConnection player1;
    private ServerSideConnection player2;
    
    // declare counters
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

        // creation of socket at specific port
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
    	
        
        // denoting each user by id 
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
            
            //sending the id and initial round to the client
    		try {
    			dataOut.writeInt(playerId);
                dataOut.write(maxRounds);
    			dataOut.flush();
    			
                
                //game specific instructions
                
                //loop 
    			while (turns < 5) {
                  //check if user has implemented an imput click
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
                        // check criteria for round win based off passed round counter
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
        //send click value
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
 
    	//connect server side 
        //accept client request as long as total number of players is less than 2
        try {
            while(numPlayers < 2){
                Socket s = ss.accept();
                numPlayers++;
                System.out.println("Player " + numPlayers + " connected. Hello ");
                ServerSideConnection ssc = new ServerSideConnection(s, numPlayers);
                //denote which player has what connection
                if(numPlayers == 1){
                    player1= ssc;
                }else {
                    player2 = ssc;
                }
                //implement thread
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
//--SBW--
