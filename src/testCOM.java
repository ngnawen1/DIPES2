    import javax.comm.*;
    import java.util.*;
    import com.sun.comm.Win32Driver;
    import java.io.*;
    public class testCOM implements SerialPortEventListener {
        private String portCOM;
        private CommPortIdentifier portID = null; //identifiant du port
        private SerialPort serialPort; //le port série
        private BufferedReader fluxLecture; //flux de lecture du port
    /*
     * Methode qui initialise le port série en evenementiel
     */
    public void ModeEvenement(String portCOM) {
     //récupération de l'identifiant du port
     try {
      portID = CommPortIdentifier.getPortIdentifier(portCOM);
     } catch (NoSuchPortException e) {
     }
     //ouverture du port
     try {
      serialPort = (SerialPort) portID.open("ModeEvenement", 2000);
     } catch (PortInUseException e) {
     }
     //récupération du flux
     try {
      fluxLecture =
       new BufferedReader(
        new InputStreamReader(serialPort.getInputStream()));
     } catch (IOException e) {}
     //ajout du listener
     try {
      serialPort.addEventListener(this);
     } catch (TooManyListenersException e) {
     }
     //paramétrage du port
     serialPort.notifyOnDataAvailable(true);
     try {
      serialPort.setSerialPortParams(
       4800,
       SerialPort.DATABITS_8,
       SerialPort.STOPBITS_1,
       SerialPort.PARITY_NONE);
     } catch (UnsupportedCommOperationException e) {}
     System.out.println("port ouvert, attente de lecture" );
    }
       
        /*
         * Methode qui effectue la lecture de 7 trames sur le port série
         * Une fois la lecture faite on ferme le flux de lecture et le port COM
         */
        public void ReadSerialPort(){
         int i=7;
         String reponse = new String();
     try {
      System.out.println("i="+i);
      while(i!=0){
       System.out.println("On lit sur le port COM\n" );
       reponse = (String) fluxLecture.readLine();
       System.out.println(reponse);
       i--;
       System.out.println("i="+i);
      }
     } catch (IOException e) {}
     //fermeture du flux de lecture
     try {
      fluxLecture.close();
     } catch (IOException e) {}
     //fermeture du port COM
     serialPort.close();
    }
    public void serialEvent(SerialPortEvent event) {
     //gestion des événements sur le port :
     //on ne fait rien sauf quand les données sont disponibles
       switch (event.getEventType()) {
      case SerialPortEvent.DATA_AVAILABLE :
       this.ReadSerialPort();//si data dispo on lance la lecture
       break;
      default:
       break;//on ne fait rien pour les autres evenements
     }
    }
       
        /*
         * Methode qui scanne tous les ports COM et test si des données viennent du port COm scanné
         */
    public void listPort(){
     Enumeration listePorts = CommPortIdentifier.getPortIdentifiers();
     int typePort;
     String GPSPortCOM;
     while (listePorts.hasMoreElements()){
      portID = (CommPortIdentifier) (CommPortIdentifier) listePorts.nextElement();
      if(portID.getPortType()==CommPortIdentifier.PORT_SERIAL){   
          System.out.println("Nom du PORT :"+portID.getName());
          System.out.println("User :"+portID.getCurrentOwner());
          System.out.println("Use ? :"+portID.isCurrentlyOwned());
          System.out.println("Type du PORT :"+portID.getPortType());
          // On lance la gestion des évènements sur portID
       this.ModeEvenement(portID.getName());
      }
     }
    }
    public static void main(String[] args) {
     //initialisation du driver
     Win32Driver w32Driver = new Win32Driver();
     w32Driver.initialize();
     testCOM test = new testCOM();
     test.listPort();
    }
    }