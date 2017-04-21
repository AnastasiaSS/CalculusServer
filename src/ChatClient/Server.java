package ChatClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Настя on 25.03.2017.
 */
public class Server {
    public static void main(String[] args) throws Exception{
        Server server=new Server();
        server.work();
    }
    public class ClientHandler implements Runnable{
        BufferedReader reader;
        PrintWriter writer;
        Socket socket;
        public ClientHandler(Socket socket){
            try {
                this.socket = socket;
                InputStreamReader isReader =new InputStreamReader(this.socket.getInputStream());
                reader=new BufferedReader(isReader);
                writer=new PrintWriter(this.socket.getOutputStream());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        @Override
        public void run(){
            String message;
            try {
                while ((message = reader.readLine()) != null) {
                    String result=analCalc(message);
                    System.out.println("Read" + message);
                    System.out.println("Read" + result);
                    writer.println(result);
                    writer.flush();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public void work() {
        try {
            ServerSocket server = new ServerSocket(34711);
            while (true){
                Socket sock=server.accept();

                Thread thread=new Thread(new ClientHandler(sock));
                thread.start();
                System.out.println("got a connection");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private String analCalc(String s){
        String res="";
        String[] arr=s.split("  *");
        for(String ee: arr) {
            System.out.print(ee);
        }
        int oper=Integer.parseInt(arr[arr.length-1]);
        ArrayList<Float> arrNum=new ArrayList<Float>();
        for(int i=0; i<arr.length-1; i++){
            try{
                float temp=Float.parseFloat(arr[i]);
                arrNum.add(temp);
            }catch(Exception e){
                break;
            }
        }
        if(arrNum.size() < arr.length-1){
            StringBuilder str=new StringBuilder("");
            if(oper==1) {
                for (int i = 0; i < arr.length-1; i++)
                    str.append(arr[i]);
                res=str.toString();
            }
            else
                res="ERROR";
        }
        else{
            float temp=0.0f;
            if (oper==1) {
                for (int i = 0; i < arrNum.size(); i++)
                    temp += arrNum.get(i);
            }
            else{
                temp=1.0f;
                for (int i = 0; i < arrNum.size(); i++)
                    temp *= arrNum.get(i);
            }
            res+=temp;
        }
        return res;
    }
}
