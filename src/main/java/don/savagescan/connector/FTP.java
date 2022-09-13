package don.savagescan.connector;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;


@Component
public class FTP {

    public void open() {
        String ftpUrl = "";

        try {
            URLConnection ftpConnection = new URL(ftpUrl).openConnection();
            ftpConnection.setConnectTimeout(10000);
            ftpConnection.setReadTimeout(10000);

            InputStream inputStream = ftpConnection.getInputStream();

            System.out.println("connected");
            inputStream.close();


        } catch (IOException e) {
            System.out.println(e.getMessage());
        }


    }

}