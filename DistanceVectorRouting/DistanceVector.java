package DistanceVectorRouting;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class DistanceVector {

    BufferedReader br = null;
    Router r;
    int rno;
    int portNo;
    int portr0;
    int portr1;
    int portr2;
    int portr3;
    ServerSocket ss;
    String addr;
    String addr0;
    String addr1;
    String addr2;
    String addr3;
    

    private DistanceVector() {
        r = new Router();
        br = new BufferedReader(new InputStreamReader(System.in));
    }

    public static void main(String[] args) throws Exception {
        DistanceVector st = new DistanceVector();
        BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
        
        st.Initialization();

        while (true) {
            System.out.println("");
            System.out.print("Enter your choice : (1 -> Server/ 2-> Client) : ");
            int x = Integer.parseInt(br1.readLine());
            switch (x) {
                case 1:
                    st.startServer();
                    break;
                case 2:
                    st.startSender();
                    break;

                default:
                    System.out.println("Invalid Choice..");
            }
        }
    }

    private void startServer() throws Exception {
        System.out.println("Current Routing table at Router R" + rno + " :");
        printall(r);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Waiting for connection..");
        Socket s = ss.accept();
        System.out.println("..Connection Established..");
        ObjectInputStream is = new ObjectInputStream(s.getInputStream());
        ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
        Router rin = (Router) is.readObject();
        System.out.println("..Routing Table Recieved from other Router..");
        System.out.println("Routing Table of Router R" + rin.getTrno() + " : ");
        printall(rin);
        objectComparison(rin);
        System.out.println("..Routing table updated..");
        System.out.println("New Routing table at Router R" + rno + " :");
        printall(r);
        os.writeObject(r);
        System.out.println("..Routing table sent to other Router..");
        System.out.println("..Closing Connection..");
        s.close();

    }

    private void startSender() throws Exception {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Enter the router number to connect : ");
        int RouterToConnect = Integer.parseInt(br.readLine());
        int x = 0;
        String y="";
        switch (RouterToConnect) {
            case 0:
                y = addr0;
                x = portr0;
                break;
            case 1:
                y = addr1;
                x = portr1;
                break;
            case 2:
                y = addr2;
                x = portr2;
                break;
            case 3:
                y = addr3;
                x = portr3;
                break;

        }
        //int x = Integer.parseInt(br.readLine());
        Socket s = new Socket(y, x);
        System.out.println("Current Routing table : ");
        printall(r);
        ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
        os.writeObject(r);
        System.out.println("..Routing table sent to the other router..");
        System.out.println("..Waiting for Reply from Other Router..");
        ObjectInputStream is = new ObjectInputStream(s.getInputStream());
        Router rin = (Router) is.readObject();
        System.out.println("..Routing Table Recieved from other router..");
        System.out.println("Routing table of Router R" + rin.getTrno() + " : ");
        printall(rin);
        objectComparison(rin);
        System.out.println("..Routing table updated..");
        System.out.println("New Routing table : ");
        printall(r);
        System.out.println("..Closing Connection..");
        s.close();
    }

    private void Initialization() throws Exception {

        int[] x = new int[5];
        int[] a = new int[5];
        int[] b = new int[5];
        int[] c = new int[5];

        System.out.print("Enter the Router Number : ");
        rno = Integer.parseInt(br.readLine());
        portInitialization();
        ss = new ServerSocket(portNo);
        x[0] = rno;
        r.setTrno(rno);
        System.out.println("..INFO FOR THIS ROUTER :: Router Number : " + rno + " Port Number : " + portNo + "..");
        System.out.println("Please Enter Routing Table Entries of R" + rno + " : ");
        for (int i = 1; i < 5; i++) {
            System.out.print("Distance from R" + rno + " to R" + (i - 1) + " : ");
            x[i] = Integer.parseInt(br.readLine());
        }

        switch (rno) {
            case 0:
                a = SubInitialization(1);
                b = SubInitialization(2);
                c = SubInitialization(3);
                r.setR0(x);
                r.setR1(a);
                r.setR2(b);
                r.setR3(c);
                break;
            case 1:
                a = SubInitialization(0);
                b = SubInitialization(2);
                c = SubInitialization(3);
                r.setR0(a);
                r.setR1(x);
                r.setR2(b);
                r.setR3(c);
                break;
            case 2:
                a = SubInitialization(0);
                b = SubInitialization(1);
                c = SubInitialization(3);
                r.setR0(a);
                r.setR1(b);
                r.setR2(x);
                r.setR3(c);
                break;
            case 3:
                a = SubInitialization(0);
                b = SubInitialization(1);
                c = SubInitialization(2);
                r.setR0(a);
                r.setR1(b);
                r.setR2(c);
                r.setR3(x);
                break;

        }

    }

    private int[] SubInitialization(int no) throws Exception {
        int b[] = new int[5];
        b[0] = no;
        for (int i = 1; i < 5; i++) {
            b[i] = 16;
        }
        return b;
    }

    private void printe(int[] a) {
        for (int no : a) {
            System.out.print(no + "\t");
        }
        System.out.println();
    }

    private void printall(Router rx) {
        int[] a = new int[5];
        int[] b = new int[5];
        int[] c = new int[5];
        int[] d = new int[5];
        a = rx.getR0();
        b = rx.getR1();
        c = rx.getR2();
        d = rx.getR3();
        System.out.println("");
        System.out.println("RNo\tR0\tR1\tR2\tR3");
        printe(a);
        printe(b);
        printe(c);
        printe(d);
        System.out.println("");
    }

    private void objectComparison(Router rin) {
        //identify incoming router
        //int rcurrent = r.getTrno();
        int rNoOfIncoming = rin.getTrno();
        int[] currentR = null;
        int[] incomingR = null;

        //incoming object to array initialization
        int[] ain = new int[5];
        int[] bin = new int[5];
        int[] cin = new int[5];
        int[] din = new int[5];
        ain = rin.getR0();
        bin = rin.getR1();
        cin = rin.getR2();
        din = rin.getR3();
        //determining incoming array
        switch (rNoOfIncoming) {
            case 0:
                incomingR = ain;
                break;
            case 1:
                incomingR = bin;
                break;
            case 2:
                incomingR = cin;
                break;
            case 3:
                incomingR = din;
                break;
        }
        //current object to array initialization
        int[] a = new int[5];
        int[] b = new int[5];
        int[] c = new int[5];
        int[] d = new int[5];
        a = r.getR0();
        b = r.getR1();
        c = r.getR2();
        d = r.getR3();
        switch (rno) {
            case 0:
                currentR = a;
                break;
            case 1:
                currentR = b;
                break;
            case 2:
                currentR = c;
                break;
            case 3:
                currentR = d;
                break;
        }
        //comparing both objects(routing table of both routers)
        a = arrayComparison(a, ain);
        b = arrayComparison(b, bin);
        c = arrayComparison(c, cin);
        d = arrayComparison(d, din);

        /*System.out.println("Checking Table : ");
        printe(a);
        printe(b);
        printe(c);
        printe(d);*/
        //setting value of the new routing information
        r.setR0(a);
        r.setR1(b);
        r.setR2(c);
        r.setR3(d);
        currentR = algo(rNoOfIncoming, currentR, incomingR);
        switch (rno) {
            case 0:
                r.setR0(currentR);
                break;
            case 1:
                r.setR1(currentR);
                break;
            case 2:
                r.setR2(currentR);
                break;
            case 3:
                r.setR3(currentR);
                break;
        }

    }

    private int[] arrayComparison(int[] currentArray, int[] incomingArray) {
        //int flag=0;
        //int arrayNo;
        if (currentArray[0] == rno) {
            //    arrayNo= flag;
            //    flag++;
        } else {
            for (int i = 1; i < 5; i++) {
                if (currentArray[i] == 0) {
                    continue;
                } else if (currentArray[i] > incomingArray[i]) {
                    currentArray[i] = incomingArray[i];
                }
            }
            //  flag++;
        }
        return currentArray;
    }

    private int[] algo(int incomingRno, int[] currentRT, int[] otherRT) {
        int salt = currentRT[incomingRno + 1];
        for (int i = 1; i < 5; i++) {
            int value = otherRT[i] + salt;
            if (currentRT[i] > value) {
                currentRT[i] = value;
            }
        }
        return currentRT;
    }

    private void portInitialization() throws Exception {
        System.out.print("Enter Address of R0 : ");
        addr0 = br.readLine();
        System.out.print("Enter Port Number of R0 : ");
        portr0 = Integer.parseInt(br.readLine());
        System.out.print("Enter Address of R1 : ");
        addr1 = br.readLine();
        System.out.print("Enter Port Number of R1 : ");
        portr1 = Integer.parseInt(br.readLine());
        System.out.print("Enter Address of R2 : ");
        addr2 = br.readLine();
        System.out.print("Enter Port Number of R2 :  ");
        portr2 = Integer.parseInt(br.readLine());
        System.out.print("Enter Address of R3 : ");
        addr3 = br.readLine();
        System.out.print("Enter Port Number of R3 : ");
        portr3 = Integer.parseInt(br.readLine());
        switch (rno) {
            case 0:
                addr = addr0;
                portNo = portr0;
                break;
            case 1:
                addr = addr1;
                portNo = portr1;
                break;
            case 2:
                addr = addr2;
                portNo = portr2;
                break;
            case 3:
                addr = addr3;
                portNo = portr3;
                break;

        }

    }

}
