package Client;


import java.net.Socket;

public class ClientLogIn {
    private String userName;
    private String passWord;
    private boolean loginStatus = false;
    public void execute(){
        try {
            Socket socket = new Socket("localhost", 7000);

            System.out.println("Connect to server");

            new ReadThread(socket,this).start();
            new WriteThread(socket,this).start();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public String getUserName(){
        return this.userName;
    }

    public String getPassWord(){
        return this.passWord;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public void setPassWord(String passWord){
        this.passWord = passWord;
    }

    void setLoginStatus(Boolean a){
        this.loginStatus = a;
    }

    boolean getLoginStatus(){
        return this.loginStatus;
    }

    public static void main(String[] args){

        ClientLogIn Client = new ClientLogIn();
        Client.execute();
    }
}