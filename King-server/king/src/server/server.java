package server;

import java.io.*;
import java.net.*;
public class server {
public static final int PORT = 8080; // porta al di fuori del range 1-1024 !
static int RandomNumero=0;
static String CardByPlayer1= "";
static String cardGenerated = "";
static int randomPlayer=0;
static int initialPlayer=0;
static boolean cartaUscita[]=new boolean[53];
static double player1[]=new double[13];
static double player2[]=new double[13];
static double player3[]=new double[13];
static double player4[]=new double[13];
static boolean giocata1[]=new boolean[53];
static boolean giocata2[]=new boolean[53];
static boolean giocata3[]=new boolean[53];
static boolean giocata4[]=new boolean[53];
static int resultPlayer1=0;
static int resultPlayer2=0;
static int resultPlayer3=0;
static int resultPlayer4=0;
static double result=0.0;
static int count=0;
static int hands=0;
static String lose="";
static int penalizzante=0;

public static void main(String[] args) throws IOException {
	 ServerSocket serverSocket = new ServerSocket(PORT);
	 System.out.println("EchoServer: started ");
	 System.out.println("Server Socket: " + serverSocket);
	 Socket clientSocket=null;
	 BufferedReader in=null;
	 String s="";
	 Boolean haveDone=false;
	 try {
		 while(!lose.equals("lose")) {
			 clientSocket = serverSocket.accept();
			 System.out.println("Connection accepted: "+ clientSocket);
			 hands=0;
			 
			 while(hands<2) {
	
				 for(int k=0; k<cartaUscita.length;k++) {
					 cartaUscita[k]=false;
				 }
	
			     s="";    
				 generateCard(player1);
				 generateCard(player2);
				 generateCard(player3);
				 generateCard(player4);
		
				 for(int k=0; k<player1.length;k++) {
					 s+=player1[k]+",";
				 }
		
		
		         try {
		             in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		         } catch (IOException e) {
		             e.printStackTrace();
		         }
		         
		         OutputStream output = null;
		         try {
		             output = clientSocket.getOutputStream();
		         } catch (IOException e) {
		             e.printStackTrace();
		         }
		         PrintWriter writer = new PrintWriter(output, true);
		         writer.println(s);
		         if(hands==0) {
		        	 randomPlayer = (int) (Math.random() * 4) + 1;
		        	 writer.println(randomPlayer);
		         }
		         initialPlayer=randomPlayer;
		         int a=1;
		         while(a<14) {
		        	 haveDone=false;
		        	 cardGenerated="";
		    		 for(int k=0; k<player1.length;k++) {
		    			 s+=player1[k]+",";
		    		 }
		    		 
			         if(randomPlayer==2) {
			        	 playCard(player2,giocata2);
			             writer.println(cardGenerated);
			             writer.println(cardGenerated);
			             CardByPlayer1= in.readLine();
		
			         }
			         else if(randomPlayer==3) {
			        	 playCard(player3,giocata3);
			             writer.println(cardGenerated);
			             writer.println(cardGenerated);
			             CardByPlayer1= in.readLine();
		
			         }
			         else if(randomPlayer==4) {
			        	 playCard(player4,giocata4);
			             writer.println(cardGenerated);
			             writer.println(cardGenerated);
			             CardByPlayer1= in.readLine();
		
			         }
			         else if(randomPlayer==1) {
			        	 if(haveDone==false && a==1) {
			        		 writer.println("your turn");
			        		 haveDone=true;
			        	 }
			             CardByPlayer1= in.readLine();
			        	 playCard(player1,giocata1);
		
			             writer.println(cardGenerated);
			         }
			         setResult();
			        	 
			         writer.println(resultPlayer1);
			         writer.println(resultPlayer2);
			         writer.println(resultPlayer3);
			         writer.println(resultPlayer4);
			         
			         writer.println(randomPlayer);
			         
			         
			         if(a==13) {
			        	 randomPlayer = initialPlayer+1;
			        	 if(randomPlayer==5)
			        		 randomPlayer=1;
			        	 writer.println(randomPlayer);
			         }
		
		
			         cardGenerated="";
			         CardByPlayer1="";
			         penalizzante=0;
			         a++;
		         }
		         resultPlayer1=0;
		         resultPlayer2=0;
		         resultPlayer3=0;
		         resultPlayer4=0;
		         hands++;
			 }
			 lose=in.readLine();
			 
		 }
	 }
	 catch (IOException e) {
		 System.err.println("Accept failed");
		 System.exit(1);
	 }
	 // chiusura di stream e socket
	 System.out.println("EchoServer: closing...");
	 in.close();
	 clientSocket.close();
	 serverSocket.close();
	
}


public static void generateCard(double player[]) {

	int i=0;
	 while (i < 13) {
           RandomNumero = (int) (Math.random() * 52) + 1;
           if (!cartaUscita[RandomNumero]) {
           	cartaUscita[RandomNumero] = true;
           	player[i]=RandomNumero;
               i++;
           }
	 }
	
}
	
	public static void playCard(double player[], boolean giocata[]) {

        int RandomCard=0;
        double randomPlayer1 = 0;
        double cardExtracted=0;
        while(true) {
			if(randomPlayer!=1) {
				RandomCard = (int) (Math.random() * (13));
				if(player[RandomCard]!=0) {
					cardGenerated+= player[RandomCard]+",";
					cardExtracted=player[RandomCard];
	        		player[RandomCard]=0;
	        		break;
				}
			}
			else {
				randomPlayer1= Double.parseDouble(CardByPlayer1);
				break;
			}
        }

        if(randomPlayer==2) {
        	setOtherPlayers(cardExtracted,player3,giocata3);
        	setOtherPlayers(cardExtracted,player4,giocata4);
        }
        else if(randomPlayer==3) {
        	setOtherPlayers(cardExtracted,player2,giocata2);
        	setOtherPlayers(cardExtracted,player4,giocata4);
        }
        else if(randomPlayer==4) {
        	setOtherPlayers(cardExtracted,player2,giocata2);
        	setOtherPlayers(cardExtracted,player3,giocata3);
        }
        else if(randomPlayer==1) {
        	setOtherPlayers(randomPlayer1,player2,giocata2);
        	setOtherPlayers(randomPlayer1,player3,giocata3);
        	setOtherPlayers(randomPlayer1,player4,giocata4);
        }
	}
	
	public static void setOtherPlayers(double number, double player[], boolean giocata[]) {
		double min=0;
		double max=0;
		if(number<14) {
			min=0;
			max=14;
		}
		else if(number>13 && number<27) {
			min=13;
			max=27;
		}
		else if(number>26 && number<40) {
			min=26;
			max=40;
		}

		else if(number>39) {
			min=39;
			max=53;
		}
		
        int RandomCard=0;
        boolean tick[]=new boolean [13];
        boolean alpha=false;
        boolean beta=false;
        while(!alpha) {
        	RandomCard = (int) (Math.random() * (13));
        	if(player[RandomCard]<max && player[RandomCard]>min) {
        		cardGenerated+=player[RandomCard]+",";
        		player[RandomCard]=0;
        		count=0;
        		alpha=true;
        	}
        	else if(count<12 && tick[RandomCard]==false) {
        			tick[RandomCard]=true;
        			count++;
        	}
        	else if(count>=12){
        		while(!beta) {
	            	RandomCard = (int) (Math.random() * (13));
	            	if(player[RandomCard]!=0) {
	            		cardGenerated+=player[RandomCard]+",";
	            		player[RandomCard]=0;
	            		count=0;
	            		beta=true;
	            	}
        		}
        		alpha=true;
        	}
        		
        }
			
	}
	
	public static void setResult() {
        double[] internal=new double[13];
        String[] arrOfStr = cardGenerated.split(",");
        for(int j=0; j<arrOfStr.length;j++)
            internal[j]=Double.parseDouble(arrOfStr[j]);

        for (int k = 0; k < internal.length; k++) {
            if (internal[k] == 1.0) 
                internal[k]=13.5;            
            else if (internal[k] == 14.0) 
                internal[k]=26.5;            
            else if (internal[k] == 27.0) 
                internal[k]=39.5;            
            else if (internal[k] == 40.0) 
                internal[k]=52.5;            
        }
        
        if (CardByPlayer1.equals("1.0")) 
        	CardByPlayer1 = "13.5";        
        else if (CardByPlayer1.equals("14.0")) 
        	CardByPlayer1="26.5";        
        else if (CardByPlayer1.equals("27.0")) 
        	CardByPlayer1="39.5";        
        else if (CardByPlayer1.equals("40.0")) 
        	CardByPlayer1="52.5";        
        	
        
        
			if(randomPlayer==1) 
				setResultAux1(Double.parseDouble(CardByPlayer1),internal,hands);
			else
				setResultAux1(internal[0],internal, hands);
        
	}
	
	public static void setResultAux1(double card,double [] internal,int hands) {
		double max=0;
		if(randomPlayer==1)
			max= Double.parseDouble(CardByPlayer1);
		else
			max= internal[0];
        double sogliaMin=0.0;
        double sogliaMax=0.0;
		if (card<14.0)
    		sogliaMax=14.0;
    	else if (card<27.0 && card>13.5) {
    		sogliaMin=13.5;
    		sogliaMax=27.0;
    	}
    	else if (card<40.0 && card>26.5) {
    		sogliaMin=26.5;
    		sogliaMax=40.0;
    	}
    	else {
    		sogliaMin=39.5;
        	sogliaMax=54.0;
        }
		if(hands==0) {
	        for (int ktr = 0; ktr < internal.length; ktr++) {
	        	if(internal[ktr]==11.0 || internal[ktr]==13.0 || internal[ktr]==24.0 || internal[ktr]==26.0 || internal[ktr]==37.0 || internal[ktr]==39.0 || internal[ktr]==50.0 || internal[ktr]==52.0)
	        		penalizzante++;
	        }
		}
		else if(hands==1) {
	        for (int ktr = 0; ktr < internal.length; ktr++) {
	        	if(internal[ktr]==12.0 || internal[ktr]==25.0 || internal[ktr]==38.0 || internal[ktr]==51.0)
	        		penalizzante++;
	        }
		}
        for (int ktr = 0; ktr < internal.length; ktr++) {
            if (internal[ktr] > max && internal[ktr]>sogliaMin && internal[ktr]<sogliaMax) {
                max = internal[ktr];
            }
        }
        if(hands==0) {
        	if(Double.parseDouble(CardByPlayer1)==11.0 || Double.parseDouble(CardByPlayer1)==13.0 || Double.parseDouble(CardByPlayer1)==24.0 || Double.parseDouble(CardByPlayer1)==26.0 || Double.parseDouble(CardByPlayer1)==37.0 || Double.parseDouble(CardByPlayer1)==39.0 || Double.parseDouble(CardByPlayer1)==50.0 || Double.parseDouble(CardByPlayer1)==52.0)
        		penalizzante++;
        }
        	
        else if(hands==1) {
	        if(Double.parseDouble(CardByPlayer1)==12.0 || Double.parseDouble(CardByPlayer1)==25.0 || Double.parseDouble(CardByPlayer1)==38.0 || Double.parseDouble(CardByPlayer1)==51.0)
	        	penalizzante++;
        }
        
        if(Double.parseDouble(CardByPlayer1)>max && Double.parseDouble(CardByPlayer1)>sogliaMin && Double.parseDouble(CardByPlayer1)<sogliaMax)
        	result= Double.parseDouble(CardByPlayer1);
        else
        	result= max;
        
        if(randomPlayer==1 || randomPlayer==2) {
	        if(result==Double.parseDouble(CardByPlayer1)) {
	        	if(hands==0)
	       	 		resultPlayer1=resultPlayer1-(2*penalizzante);
	        	else if(hands==1)
	        		resultPlayer1=resultPlayer1-(3*penalizzante);
	       	 	randomPlayer=1;
	       	}
	        else if(result==internal[0]) {
	        	if(hands==0)
	       	 		resultPlayer2=resultPlayer2-(2*penalizzante);
	        	else if(hands==1)
	        		resultPlayer2=resultPlayer2-(3*penalizzante);
	       	 	randomPlayer=2;
	       	}
	        else if(result==internal[1]) {
	        	if(hands==0)
	       	 		resultPlayer3=resultPlayer3-(2*penalizzante);
	        	else if(hands==1)
	        		resultPlayer3=resultPlayer3-(3*penalizzante);
	       	 	randomPlayer=3;
	       	}
	        else {
	        	if(hands==0)
	       	 		resultPlayer4=resultPlayer4-(2*penalizzante);
	        	else if(hands==1)
	        		resultPlayer4=resultPlayer4-(3*penalizzante);
	       	 	randomPlayer=4;
	        }
        }
        
        else if(randomPlayer==3) {
	        if(result==Double.parseDouble(CardByPlayer1)) {
	        	if(hands==0)
	       	 		resultPlayer1=resultPlayer1-(2*penalizzante);
	        	else if(hands==1)
	        		resultPlayer1=resultPlayer1-(3*penalizzante);
		        randomPlayer=1;
	        }
	        else if(result==internal[0]) {
	        	if(hands==0)
	       	 		resultPlayer3=resultPlayer3-(2*penalizzante);
	        	else if(hands==1)
	        		resultPlayer3=resultPlayer3-(3*penalizzante);
		        randomPlayer=3;
	        }
	        else if(result==internal[1]) {
	        	if(hands==0)
	       	 		resultPlayer2=resultPlayer2-(2*penalizzante);
	        	else if(hands==1)
	        		resultPlayer2=resultPlayer2-(3*penalizzante);
		        randomPlayer=2;
	        }
	        else {
	        	if(hands==0)
	       	 		resultPlayer4=resultPlayer4-(2*penalizzante);
	        	else if(hands==1)
	        		resultPlayer4=resultPlayer4-(3*penalizzante);
		        randomPlayer=4;
	        }
        }
        
        else if(randomPlayer==4) {
	        if(result==Double.parseDouble(CardByPlayer1)) {
	        	if(hands==0)
	       	 		resultPlayer1=resultPlayer1-(2*penalizzante);
	        	else if(hands==1)
	        		resultPlayer1=resultPlayer1-(3*penalizzante);
	       	 	randomPlayer=1;
	        }
	        else if(result==internal[0]) {
	        	if(hands==0)
	       	 		resultPlayer4=resultPlayer4-(2*penalizzante);
	        	else if(hands==1)
	        		resultPlayer4=resultPlayer4-(3*penalizzante);
		        randomPlayer=4;
	        }
	        else if(result==internal[1]) {
	        	if(hands==0)
	       	 		resultPlayer2=resultPlayer2-(2*penalizzante);
	        	else if(hands==1)
	        		resultPlayer2=resultPlayer2-(3*penalizzante);
		        randomPlayer=2;
	        }
	        else {
	        	if(hands==0)
	       	 		resultPlayer3=resultPlayer3-(2*penalizzante);
	        	else if(hands==1)
	        		resultPlayer3=resultPlayer3-(3*penalizzante);
		        randomPlayer=3;
	       	}
        }
        
	}
	
}