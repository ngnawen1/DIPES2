/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sipee;

/**
 *
 * @author admine
 */
import javax.comm.*;
import java.util.*;
import java.io.*;
import org.smslib.*;

/**
Description:
This class works as a sms modem.
It supplies 5 interfaces for outside usage.
(1) public static boolean startModem(); complete starting and configuring sms modem ;
(2) public static boolean stopModem(); stop the sms modem;
(3) public static boolean isReady(); return the sms modem's work status(true for working,false for stopping);
(4) public static void addWarning(ArrayList<String> numberList,String content);
(5) public static void addTask(ArrayList<String> numberList,String content);
If any questions, please contact JinLi at 13811220655.
 */
public class smsModem {

    private static LinkedList<COutgoingMessage> msgList = new LinkedList<>();
    private static String com = "";
    private static int baudRate = 0;
    private static CService srv;
    private static boolean isSending = false;

    private static void testAndSetCom(){ /**
    Try to find out which com port is used by modem and the work baudrate.
     */
    
        CommPortIdentifier portId;
        Enumeration portList;
        int bauds[] = {9600};
        // int bauds[] = { 9600, 19200, 38400, 115200 };
        //int bauds[] = {38400, 115200};
        portList = CommPortIdentifier.getPortIdentifiers();
        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            System.out.println("PORTID:" + portId);
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                System.out.println("Found port: " + portId.getName());
                for (int i = 0; i < bauds.length; i++) {
                    System.out.print("	Trying at " + bauds[i] + "...");
                    try {
                        SerialPort serialPort;
                        InputStream inStream;
                        OutputStream outStream;
                        int c;
                        String response;

                        serialPort = (SerialPort) portId.open("SMSLibCommTester", 1971);
                        serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN);
                        serialPort.setSerialPortParams(bauds[i], SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                        inStream = serialPort.getInputStream();
                        outStream = serialPort.getOutputStream();
                        serialPort.enableReceiveTimeout(2000);
                        c = inStream.read();
                        while (c != -1) {
                            c = inStream.read();
                        }
                        outStream.write('A');
                        outStream.write('T');
                        outStream.write('\r');
                        outStream.write('A');
                        outStream.write('T');
                        outStream.write('\r');
                        outStream.write('A');
                        outStream.write('T');
                        outStream.write('\r');
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        response = "";
                        c = inStream.read();
                        while (c != -1) {
                            response += (char) c;
                            c = inStream.read();
                        }
                        serialPort.close();
                        if (response.indexOf("OK") >= 0) {
                            setCom(portId.getName());
                            setBaud(bauds[i]);
                            System.out.print("  Getting Info...");
                            startSrv();
                            System.out.println("  Found: " + srv.getDeviceInfo().getModel());
                            stopSrv();
                        } else {
                            System.out.println("  Nobody here!");
                        }
                    } catch (Exception e) {
                        System.out.println("  Nobody here!");
                    }
                }
            }
        }
        if ("".equals(com) && baudRate == 0) {
            System.out.println("Didn't find any useable com port");
        }
    }

    private static void showModemInformation() /**
    Get the modem information.
     */
    {
        System.out.println("Getting modem information ...");
        if ("".equals(com) && baudRate == 0) {
            System.out.println("Haven't got any useable com port yet! Run testAndSetCom() at first. ");
            System.exit(1);
        }
        //sendTest.srv = new CService(com, baudRate, "wavecom", "");
        startSrv();
        System.out.println();
        System.out.println("ReadMessages: Synchronous Reading.");
        System.out.println("  Using " + CService._name + " " + CService._version);
        System.out.println();

        //srv.setSimPin("0000");
        // OK, let connect and see what happens... Exceptions may be thrown
        // here!
        // Lets get info about the GSM device...
        System.out.println("Mobile Device Information: ");
        System.out.println("	Manufacturer  : " + srv.getDeviceInfo().getManufacturer());
        System.out.println("	Model         : " + srv.getDeviceInfo().getModel());
        System.out.println("	Serial No     : " + srv.getDeviceInfo().getSerialNo());
        System.out.println("	IMSI          : " + srv.getDeviceInfo().getImsi());
        System.out.println("	S/W Version   : " + srv.getDeviceInfo().getSwVersion());
        System.out.println("	Battery Level : " + srv.getDeviceInfo().getBatteryLevel() + "%");
        System.out.println("	Signal Level  : " + srv.getDeviceInfo().getSignalLevel() + "%");
        System.out.println("	GPRS Status   : " + (srv.getDeviceInfo().getGprsStatus() ? "Enabled" : "Disabled"));
        System.out.println("");
        // Write your test calls here.
        // ...
        // ...
        stopSrv();
        //System.exit(0);
    }

    private static String getCom() /**
    get the com port.
     */
    {
        return com;
    }

    private static int getBaud() /**
    get the baudrate.
     */
    {
        return baudRate;
    }

    private static void setCom(String comport) /**
    set com port manually.
     */
    {
        com = comport;
    }

    private static void setBaud(int baud) /**
    set baudrate manually.
     */
    {
        baudRate = baud;
    }

    private static void startSrv(){ /**
    start the CService
     */
    
        srv = new CService(getCom(), getBaud(),"NOKIA", "3110c");
        try {
            srv.connect();
            System.out.println(" srv starts now... ");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static void stopSrv(){ /**
    stop the CService
     */
    
        try {
            srv.disconnect();
            System.out.println("srv stops now...");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    /**
    For outside usage.
    Start the sms modem.
     */
    public static boolean startModem() {
        testAndSetCom();
        showModemInformation();
        sendInitial();
        return isReady();
    }

    /**
    For outside usage.
    Stop the sms modem.
     */
    public static boolean stopModem() {
        try {
            srv.disconnect();
            System.out.println("srv stops now...");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
        return !(isReady());
    }

    /**
    For outside usage.
    Return the status (connected or not ) of sms modem.
     */
    public static boolean isReady() {
        return srv.getConnected();
    }

    private static synchronized void sendingLock() {
        isSending = true;
    }

    private static synchronized void sendingUnlock() {
        isSending = false;
    }

    private static synchronized boolean isSending() {
        return isSending;
    }

    /**
    For outside usage.
    Add warning message list.
     */
    public static void addWarning(ArrayList<String> numberList, String content) {
        for (int i = 0; i < numberList.size(); i++) {
            COutgoingMessage msg = new COutgoingMessage(numberList.get(i), content);
            InsertWarningMsgToList(msg);
        }
        Thread send = new Thread(new Runnable() {

            public void run() {
                if (!(isSending())) {
                    sendingLock();
                    System.out.println("Processing");
                    startSending();
                    sendingUnlock();
                } else {
                    System.out.println("Have started processing yet, so return now.");
                    return;
                }
            }
        });
        send.setDaemon(true);
        send.start();
    }

    /**
    For outside usage.
    Add task message list.
     */
    public static void addTask(ArrayList<String> numberList, String content) {
        for (int i = 0; i < numberList.size(); i++) {
            COutgoingMessage msg = new COutgoingMessage(numberList.get(i), content);
            InsertTaskMsgToList(msg);

        }
        Thread send = new Thread(new Runnable() {

            public void run() {
                if (!(isSending())) {
                    sendingLock();
                    System.out.println("Processing");
                    startSending();
                    sendingUnlock();
                } else {
                    System.out.println("Have started processing yet, so return now.");
                    return;
                }
            }
        });
        send.setDaemon(true);
        send.start();
    }

    private static void startSending() {
        while (msgSize() > 0) {
            sendMsg(getMessage());
        }
    }

    private static synchronized void InsertWarningMsgToList(COutgoingMessage msg) /** Insert outgoing msg to msg list
     */
    {
        msgList.add(0, msg);
    }

    private static synchronized boolean InsertTaskMsgToList(COutgoingMessage msg) /** Insert outgoing msg to msg list
     */
    {
        return msgList.add(msg);
    }

    public static synchronized COutgoingMessage getMessage() {
        return msgList.poll();
    }


    private static synchronized int msgSize() {
        return msgList.size();
    }

    private static boolean msgEmpty() {
        return msgSize() == 0;
    }

    private static void sendInitial() {
        System.out.println("Initialization for sending message ...");
        try {
            // If the GSM device is PIN protected, enter the PIN here.
            // PIN information will be used only when the GSM device reports
            // that it needs a PIN in order to continue.
            srv.setSimPin("0000");
            // Some modems may require a SIM PIN 2 to unlock their full functionality.
            // Like the Vodafone 3G/GPRS PCMCIA card.
            // If you have such a modem, you should also define the SIM PIN 2.
            srv.setSimPin2("0000");
            // Normally, you would want to set the SMSC number to blank. GSM
            // devices get the SMSC number information from their SIM card.
            srv.setSmscNumber("");
            srv.setProtocol(0);
            startSrv();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
    This function is used for sending message. Before using it ,you must run sendInitial() at first,and do not forget close() the srv after sending message.
     */
    private static void sendMsg(COutgoingMessage msg) {
        System.out.println("Sending Message ...");


        // Lets get info about the GSM device...
        //System.out.println("Mobile Device Information: ");
        //System.out.println("	Manufacturer  : " + srv.getDeviceInfo().getManufacturer());
        //System.out.println("	Model         : " + srv.getDeviceInfo().getModel());
        //System.out.println("	Serial No     : " + srv.getDeviceInfo().getSerialNo());
        //System.out.println("	IMSI          : " + srv.getDeviceInfo().getImsi());
        //System.out.println("	S/W Version   : " + srv.getDeviceInfo().getSwVersion());
        //System.out.println("	Battery Level : " + srv.getDeviceInfo().getBatteryLevel() + "%");
        //System.out.println("	Signal Level  : " + srv.getDeviceInfo().getSignalLevel() + "%");

        // Lets create a message for dispatch.
        // A message needs the recipient's number and the text. Recipient's
        // number should always be defined in international format.

        //COutgoingMessage msg = new COutgoingMessage("+306948494037", "Message from SMSLib for Java.");

        // Set the message encoding.
        // We can use 7bit, 8bit and Unicode. 7bit should be enough for most
        // cases. Unicode is necessary for Far-East countries.
        msg.setMessageEncoding(CMessage.MessageEncoding.EncUcs2);

        // Do we require a Delivery Status Report?


        msg.setStatusReport(true);

        //// We can also define the validity period.
        //// Validity period is always defined in hours.
        //// The following statement sets the validity period to 8 hours.
        //msg.setValidityPeriod(8);

        // Do we require a flash SMS? A flash SMS appears immediately on
        // recipient's phone.
        // Sometimes its called a forced SMS. Its kind of rude, so be
        // careful!
        // Keep in mind that flash messages are not supported by all
        // handsets.
        // msg.setFlashSms(true);

        // Some special applications are "listening" for messages on
        // specific ports.
        // The following statements set the Source and Destination port.
        // They should always be used in pairs!!!
        // Source and Destination ports are defined as 16bit ints in the
        // message header.
        // msg.setSourcePort(10000);
        // msg.setDestinationPort(11000);

        // Ok, finished with the message parameters, now send it!
        // If we have many messages to send, we could also construct a
        // LinkedList with many COutgoingMessage objects and pass it to
        // srv.sendMessage().
        try {
           // srv.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //testAndSetCom();
        //showModemInformation();
        //setCom("COM1");
        //setBaud(115200);
        //srv=new CService(getCom(), getBaud(), "", "");
        //System.out.println("com:"+sendTest.getCom());
        //System.out.print("BaudRate: ");
        //System.out.println(sendTest.getBaud());
        //sendTest.makeAndInsertMsg("13811220655","Hi,how are  you!!! Happy every day!");
        //makeAndInsertMsg("10086","ye");
        //System.out.println(sendTest.msgSize());
        //sendInitial();
        //sendMsg(sendTest.msgList.remove(0));
        //if (isReady()) {System.out.println("connected ...");}
        startModem();
        ArrayList<String> numberlist = new ArrayList<>();
        numberlist.add("13811220655");  // ���
        //numberlist.add("13811419467");  // �볯ϼ
        //numberlist.add("13811525489");  //�ռ�ƽ
        //numberlist.add("13488715330");  //��͡
        //numberlist.add("15901559534");  //��Ө
        //numberlist.add("13466657538");  //Ҧ��
        //numberlist.add("13810927163");  //��ΰ
        //numberlist.add("13811328726");  //����
        String content1 = "Hello. Test LinkedList.Message type: Task message.";
        String content2 = "Hello. Test LinkedList.Message type: Warning message.";
        addTask(numberlist, content1);
        addWarning(numberlist, content2);

        try {
            Thread.sleep(300000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        stopModem();
        //if (!isReady()) {System.out.println("disconnected ...");}
        //sendTest.msgList.remove(0);
        //System.out.println(sendTest.msgSize());
    }
}
