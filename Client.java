package files;

import org.omg.CORBA.*;
import java.io.*;
import java.lang.*;
import java.util.*;


public class Client {
    public static void main(String[] args) throws IOException {
        ////////////////////////////////////////////////////
        // On initialise l'ORB
        ////////////////////////////////////////////////////
        // initialize the ORB.
        ORB orb = ORB.init(args, null);

        if(args.length!=0)
            {
            System.err.println("utilisation : pas de parametre ");
            System.exit(1);
            }

        ////////////////////////////////////////////////////
        // Recuperation de la reference d'objet du serveur
        // Dans cet exemple, la reference est stockee sous
        // la forme d'une chaine de caracteres (IOR) dans un
        // fichier. A ce stade, il est bien sur possible 
        // d'invoquer un service de nommage.
        ////////////////////////////////////////////////////
        String ior = null;

        try {
            String ref = "gestion.ref";
            FileInputStream file = new FileInputStream(ref);
            BufferedReader in = new BufferedReader(new InputStreamReader(file));
            ior = in.readLine();
            file.close();
        } catch (IOException ex) {
            System.err.println("Impossible de lire fichier : `" +
                ex.getMessage() + "'");
            System.exit(1);
        }

        ////////////////////////////////////////////////////
        // Construction d'une reference d'objet, non typee d'abord,
        // puis "cast" en une reference sur l'interface 
        // "calcul"  avec narrow ("cast" dynamique)
        ////////////////////////////////////////////////////
        org.omg.CORBA.Object obj = orb.string_to_object(ior);

        if (obj == null) {
            System.err.println("Erreur sur string_to_object() ");
            throw new RuntimeException();
        }

        directory dir = directoryHelper.narrow(obj);

        if (dir== null) {
            System.err.println("Erreur sur narrow() ");
            throw new RuntimeException();
        }

        ////////////////////////////////////////////////////
        // Invocation du serveur
        ////////////////////////////////////////////////////

	Scanner sc = new Scanner(System.in);
	String name;
	int num;
	directory current = dir;
	file_listHolder flH;
	directory_entryHolder deH;

	while(true) {	
		System.out.println("Répertoire courant : " + current.name());
		/*current.list_files(flH);
		while(flH.next_one(deH)){
			System.out.print(deh.name);
			if(deh.type == file_type.directory_type)System.out.print("|_|");
			System.out.print("    ");
		}*/
		System.out.println(" Que faire : 1- Créer un fichier");
        	System.out.println("             2- Créer un répertoire");
       	 	System.out.println("             3- lire un fichier");
       	 	System.out.println("             4- écrire dans un fichier");
       	 	System.out.println("             5- accéder à un dossier");
       	 	System.out.println("             6- supprimer un fichier");
       	 	System.out.println("             7- supprimer un dossier");
       	 	System.out.println("             8- revenir au dossier racine");
       	 	System.out.println("             9- quitter");
		num = Integer.parseInt(sc.nextLine());

		switch(num){
		    case 1:
			name = "";
			while(name.length()<5 || !name.substring(name.length()-4,name.length()).equals(".txt")){
			    System.out.print("Nom du fichier (*.txt) : ");
			    name = sc.nextLine();
			}
			//current.create_regular_file(regular_fileHolder r, String name)
			break;
		    case 2:
			break;
		    case 3:
			break;
		    case 4:
			break;
		    case 5:
			break;
		    case 6:
			break;
		    case 7:
			break;
		    case 8:
			break;
		    case 9:
			break;
		    default:
			break;
		}
	}
    }
}

