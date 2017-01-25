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
	boolean exit = false;
	String name,num;
	directory current = dir;
	file_listHolder flH = new file_listHolder();
	directory_entryHolder deH = new directory_entryHolder();
	regular_fileHolder rflH = new regular_fileHolder();

	try{
	    while(!exit) {	
		System.out.println("Répertoire courant : " + current.name());
		/*current.list_files(flH);
		while(flH.value.next_one(deH)){
			System.out.print(deH.value.name);
			if(deH.value.type == file_type.directory_type)System.out.print("|_|");
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
		num = sc.nextLine();

		if(num.equals("1")){
		    name = "";
		    while(name.length()<5 || !name.substring(name.length()-4,name.length()).equals(".txt")){
		    	System.out.print("Nom du fichier (*.txt) : ");
		    	name = sc.nextLine();
		    }
		    current.create_regular_file(rflH,name);
		}else if(num.equals("2")){
		    
		}else if(num.equals("3")){
		    
		}else if(num.equals("4")){
		    
		}else if(num.equals("5")){
		    
		}else if(num.equals("6")){
		    
		}else if(num.equals("7")){
		    
		}else if(num.equals("8")){
		    
		}else if(num.equals("9")){
		    exit = true;
		}
	    }
	/*}catch(no_such_file e){
		System.out.println("Erreur : no_such_file");
	}catch(end_of_file e){
		System.out.println("Erreur : end_of_file");
	}catch(invalid_offset e){
		System.out.println("Erreur : invalid_offset");
	}catch(invalid_type_file e){
		System.out.println("Erreur : invalid_type_file");
	}catch(invalid_operation e){
		System.out.println("Erreur : invalid_operation");*/
	}catch(already_exist e){
		System.out.println("Erreur : already_exist");
	}
    }
}

