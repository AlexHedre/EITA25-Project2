package client;

import javax.net.ssl.*;
import javax.security.cert.X509Certificate;
import java.io.*;
import java.security.KeyStore;

public class Client {

    /**
     * The class responsible for the client side of
     * the TLS communication.
     * @param args
     * @throws Exception
     */

    public static void main(String [] args) throws Exception {
        String host = null;
        int port = -1;
        for (int i = 0; i < args.length; i++) {
            System.out.println("args[" + i + "] = " + args[i]);
        }
        if (args.length < 2) {
            System.out.println("USAGE: java client host port");
            System.exit(-1);
        }
        try { /* get input parameters */
            host = args[0];
            port = Integer.parseInt(args[1]);
        } catch (IllegalArgumentException e) {
            System.out.println("USAGE: java client host port");
            System.exit(-1);
        }

        String[] msg = new String[2];
        BufferedReader read = new BufferedReader(new InputStreamReader(System.in));

        try { /* set up a key manager for client authentication */
            SSLSocketFactory factory = null;
            KeyStore ks = KeyStore.getInstance("JKS");
            KeyStore ts = KeyStore.getInstance("JKS");
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            SSLContext ctx = SSLContext.getInstance("TLS");

            while (true) {
                try {
                    System.out.print("Enter Username:");
                    msg[0] = read.readLine();

                    Console console = System.console();
                    if (console != null) {
                        char[] pass = console.readPassword("Enter password:");
                        String password = "";
                        for (char c : pass) {
                            password += c;
                        }
                        msg[1] = password;
                    } else {
                        System.out.print("Enter Password:");
                        msg[1] = read.readLine();
                    }

                    char[] password = msg[1].toCharArray();

                    if (msg[0].equals("GovernmentKS")) {
                        ks.load(new FileInputStream("Certificates/GovernmentStore/" + msg[0]), password);  //keystore
                        ts.load(new FileInputStream("Certificates/GovernmentStore/governmenttruststore"), "password".toCharArray()); //truststore
                    } else {
                        ks.load(new FileInputStream("Certificates/ClientStore/" + msg[0]), password);  //keystore
                        ts.load(new FileInputStream("Certificates/ClientStore/clienttruststore"), "password".toCharArray()); //truststore
                    }

                    kmf.init(ks, password); // user password (keypass)
                    tmf.init(ts); // keystore can be used as truststore here
                    ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
                    factory = ctx.getSocketFactory();
                    break;
                } catch (Exception e) {
                    System.out.println("Invalid username or password");
                    continue;
                }
            }

            SSLSocket socket = (SSLSocket)factory.createSocket(host, port);
            System.out.println("\nsocket before handshake:\n" + socket + "\n");

            /*
             * send http request
             *
             * See SSLSocketClient.java for more information about why
             * there is a forced handshake here when using PrintWriters.
             */
            socket.startHandshake();

            SSLSession session = socket.getSession();
            X509Certificate cert = (X509Certificate)session.getPeerCertificateChain()[0];
            String subject = cert.getSubjectDN().getName();
            System.out.println("certificate name (subject DN field) on certificate received from server:\n" + subject + "\n");
            System.out.println("socket after handshake:\n" + socket + "\n");
            System.out.println("secure connection established\n\n");

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            System.out.println("Sending username...\n");
            out.println(msg[0]);
            while (socket.isConnected()) {
                String input;
                while (!(input = in.readLine()).equals("ENDOFMSG")) {
                    System.out.println(input);
                }
                System.out.print(">");
                msg[0] = read.readLine();
                out.println(msg[0]);
                out.flush();
                if (msg[0].equalsIgnoreCase("quit")) {
                    break;
                }
            }
            in.close();
            out.close();
            read.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
