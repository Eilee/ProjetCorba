package files;

import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import java.io.*;
import java.lang.*;
import java.util.*;


public class Serveur {
    public static void main(String[] args) throws IOException {
        
        try {
            //init ORB
            ORB orb = ORB.init(args, null);

            //init POA
            POA poa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            poa.the_POAManager().activate();

            ////////////////////////////////////////////////////////////////
	    directoryImpl dirImpl = new directoryImpl(poa,"racine");
            org.omg.CORBA.Object dir = poa.servant_to_reference(dirImpl);

	    try {
                String gestion_ref = orb.object_to_string(dir);
                String refFile = "gestion.ref";
                PrintWriter out = new PrintWriter(new FileOutputStream(refFile));
                out.println(gestion_ref);
                out.close();
            } catch (IOException ex) {
                System.err.println(
                    "Impossible d'ecrire la reference dans gestion.ref");
                System.exit(1);
            }

            System.out.println("Le serveur est pret ");

            orb.run();

            System.exit(0);
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}
