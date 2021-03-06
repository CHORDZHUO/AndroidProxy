package cn.wsgwz.gravity.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.content.Context;

import org.json.JSONException;

import cn.wsgwz.gravity.config.Config;
import cn.wsgwz.gravity.util.ShellUtil;

/**
 * Created by Jeremy Wang on 2016/12/20.
 */
public class SocketServer extends Thread{
    public static final short PORT = 12888;
    private Config config;
    private Context context;
    private ServerSocket serverSocket = new ServerSocket(PORT);;
    private ExecutorService executorService = Executors.newCachedThreadPool();
    private boolean isCapture;

    public SocketServer(Context context,boolean isCapture) throws IOException, JSONException {
        this.config = ShellUtil.getConfig(context,true);;
        this.context = context;
        this.isCapture = isCapture;
    }
    @Override
    public void run() {
        try {
            while (true) {
                executorService.execute(new RequestHandler(serverSocket.accept(),config,isCapture));
            }
        }
        catch (IOException e) {
            //e.printStackTrace();
        }
    }

    public void releasePort(){
        if(serverSocket!=null){
            try {
                serverSocket.close();
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }
        if(executorService!=null){
            executorService.shutdownNow();
        }


    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }
}
