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
	int nb, num;
	String name, numString, contenu;
	directory current = dir;

	file_listHolder flH = new file_listHolder();
	directory_entryHolder deH = new directory_entryHolder();
	deH.value = new directory_entry();
	deH.value.type = file_type.regular_file_type;
	directoryHolder dH = new directoryHolder();
	regular_fileHolder rfH = new regular_fileHolder();
	StringHolder sH = new StringHolder();
	sH.value = new String("");

	try{
	    while(!exit) {	
		System.out.println("Répertoire courant : " + current.name());
		nb = current.list_files(flH);
		if(nb > 0){
		    while(flH.value.next_one(deH)){
			System.out.print(deH.value.name);
			if(deH.value.type == file_type.directory_type)System.out.print("|_|");
			System.out.print("    ");
		    }
		}

		//Menu affiché par le client
		System.out.println("\n\nQue faire : 1- Créer un fichier");
		System.out.println("            2- Créer un répertoire");
       	 	System.out.println("            3- lire un fichier");
       	 	System.out.println("            4- écrire dans un fichier");
       	 	System.out.println("            5- accéder à un répertoire");
       	 	System.out.println("            6- supprimer un fichier ou un répertoire");
       	 	System.out.println("            7- revenir au répertoire racine");
       	 	System.out.println("            8- quitter");

		numString = sc.nextLine();
		char numChar = numString.charAt(0);
		if(numString.length() >1)numChar = '*';

		//Liste des actions possibles en fonction du choix du client
		switch(numChar){

		    //Création d'un fichier
		    case '1':
			name = "";
		    	while(name.length()<5 || !name.substring(name.length()-4,name.length()).equals(".txt")){
		    	    System.out.print("Nom du fichier (*.txt) : ");
		    	    name = sc.nextLine();
		    	}
		    	current.create_regular_file(rfH,name);
			break;

		    //Création d'un répertoire
		    case '2':
			 System.out.print("Nom du répertoire : ");
		    	name = sc.nextLine();
		    	current.create_directory(dH,name);
			break;

		    //Lecture d'un fichier
		    case '3':
			System.out.print("Nom du fichier : ");
		    	name = sc.nextLine();
		    	current.open_regular_file(rfH,name,mode.read_only);
		    	nb = rfH.value.read(256, sH);
		    	System.out.println(nb+" caractères lues : "+sH.value);
			break;

		    //Ecriture dans un fichier
		    case '4':
			System.out.print("Nom du fichier : ");
		    	name = sc.nextLine();
		    	System.out.print("Texte à écrire dans le fichier : ");
		    	contenu = sc.nextLine();
		    	String reponse = "";
		    	while(!reponse.equals("Y") && !reponse.equals("N")){
		    	    System.out.print("Voulez écraser le contenu du fichier(Y/N) ? Dans le cas contraire le texte sera écris à la suite : ");
			    reponse = sc.nextLine();
		    	}
		    	if(reponse.equals("Y")){
			    current.open_regular_file(rfH,name,mode.write_trunc);
		    	}else{
			    current.open_regular_file(rfH,name,mode.write_append);
		    	}
		    	nb = rfH.value.write(256,contenu);
		    	System.out.println(nb+" caractères écrits : "+contenu);
			break;

		    //Accès au dossier
		    case '5':
			System.out.print("Nom du dossier : ");
		    	name = sc.nextLine();
		    	current.open_directory(dH,name);
		    	current = dH.value;
		    	current.init();
			break;

		    //Suppression d'un fichier ou d'un dossier
		    case '6':
			System.out.print("Nom du fichier ou du dossier à supprimer : ");
		    	name = sc.nextLine();
		    	current.delete_file(name);
			break;

		    //Accès au dossier racine
		    case '7':
			current = dir;
			break;

		    //Arrêt
		    case '8':
		    	exit = true;
			break;

		    //Relance du menu
		    default:
			break;
		}
	    }
	}catch(end_of_file e){
		System.out.println("Erreur : end_of_file");
	}catch(invalid_operation e){
		System.out.println("Erreur : invalid_operation");
	}catch(invalid_type_file e){
		System.out.println("Erreur : invalid_type_file");
	}catch(no_such_file e){
		System.out.println("Erreur : no_such_file");
	}catch(already_exist e){
		System.out.println("Erreur : already_exist");
	}
    }
}

