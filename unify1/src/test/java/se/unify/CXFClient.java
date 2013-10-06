package se.unify;
/*
 * @author Egil Sonesson, egil.sonesson@evitecunify.se
 *
 */

/*
 * Class used for sending content to a specified Http URL
 */
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class CXFClient {

    public static void main(String[] args) {
        System.out.println("CXFClient called with params:");
        for (int i = 0; i < args.length; i++) {
            System.out.println("Params[" + i + "] = \"" + args[i] + "\"");
        }
        if (args.length < 3) {
            System.out.println("Usage: CXFClient endpoint filename httpmethod contentType(default application/xml) encoding(may be null)");
        } else {
            String endpoint    = args[0];
            String filename    = args[1];
            String httpmethod  = args[2];
            String contentType = args.length >= 4 ? args[3] : "application/xml";
            String encoding    = args.length >= 5 ? args[4] : null;
            CXFClient cxfc = new CXFClient();
            try {
                System.out.println("Returned: "
                        + cxfc.sendRequest(endpoint, filename, httpmethod, contentType, encoding));
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }

    public String sendRequest(String endpoint, String fileName, String httpMethod, String contentType, String encoding ) throws Exception {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(endpoint);
            // Setup the connection properties
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(httpMethod);
            connection.setRequestProperty("Content-Type",contentType);

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            // Collect the file contents into sb
            StringBuffer sb = new StringBuffer();
            BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
           
            String actualContent = sb.toString();
            if( encoding != null ) {
                actualContent = URLEncoder.encode(actualContent,encoding);
            }
            System.out.println("File contents: " + actualContent);

            // Adjust the content length
            connection.setRequestProperty("Content-Length", Integer.toString(actualContent.length()));

            // Send request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(actualContent);
            wr.flush();
            wr.close();

            // Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

    }

}