package ChatClient;

import DailyAdvice.DailyAdviceClient;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Настя on 25.03.2017.
 */
public class Client {
    public static void main(String[] args) throws Exception{
        Client client=new Client();
        client.go();
    }
    Socket socket;
    JTextField out;
    JTextArea in;
    BufferedReader reader;
    PrintWriter writer;
    boolean exit=false;
    public void go() {
        JFrame frame=new JFrame("Simple chat client");
        JPanel mainPanel=new JPanel();
        in=new JTextArea(15,30);
        in.setLineWrap(true);
        in.setWrapStyleWord(true);
        in.setEditable(false);
        //in.setText("String");
        JScrollPane scroller=new JScrollPane(in);

        out=new JTextField(20);
        JButton plusButton=new JButton("+");
        JButton multButton=new JButton("*");
        JButton exitButton=new JButton("exit");
        //out.setText("kk");
        plusButton.addActionListener(new Client.ButPlusListener());
        multButton.addActionListener(new Client.ButMultListener());
        exitButton.addActionListener(new ButExitListener());

        mainPanel.add(scroller);
        mainPanel.add(out);
        mainPanel.add(plusButton);
        mainPanel.add(multButton);
        mainPanel.add(exitButton);

        setUpNetwork();

        Thread threadReader=new Thread(new Runnable(){
            @Override
            public void run(){
                String message;
                try{
                message=reader.readLine();
                    //Todo : fix
                    while((message!=null) && (exit==false)){
                        //if((message=(reader.readLine()))!=null) {
                            System.out.println("Message: " + message);
                            in.append(message);
                            in.append(System.lineSeparator());
                        //}
                        message=reader.readLine();
                    }

                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
        threadReader.start();

        frame.getContentPane().add(mainPanel);
        frame.setSize(400,500);
        frame.setVisible(true);
    }
    private void setUpNetwork(){
        try {
            socket = new Socket("127.0.0.1", 34711);
            InputStreamReader readerStream=new InputStreamReader(socket.getInputStream());
            reader=new BufferedReader(readerStream);
            writer=new PrintWriter(socket.getOutputStream());
        } catch(IOException e){
            e.printStackTrace();
        }
    }
    /*class incomeReader implements Runnable{
        @Override
        public void run(){
            String message;
            try{
                while((message=(reader.readLine()))!=null){
                    System.out.println("Message: " + message);
                    in.setText(message);
                    in.setText("kkk");
                    //in.append(message);
                    //in.append("\n");
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }*/
    class ButPlusListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event){
            try{
                String res=out.getText();
                res+=" 1";
                System.out.println(res);
                writer.println(res);
                writer.flush();
            }catch (Exception e){
                e.printStackTrace();
            }
            out.setText("");
            out.requestFocus();
        }
    }
    class ButExitListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event){
            try{
                exit=true;
            }catch (Exception e){
                e.printStackTrace();
            }
            out.setText("");
            out.requestFocus();
        }
    }
    class ButMultListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event){
            try{
                String res=out.getText();
                res+=" 0";
                System.out.println(res);
                writer.println(res);
                writer.flush();
            }catch (Exception e){
                e.printStackTrace();
            }
            out.setText("");
            out.requestFocus();
        }
    }
}
