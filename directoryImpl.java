package files;

import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import java.lang.*;
import java.util.*;
import java.io.*;

public class directoryImpl extends directoryPOA
{
    protected POA poa_;
    private int number_of_file;
    File currentDir;
    String path = "";
    String name = "";
    ArrayList<regular_file> alFile;
    ArrayList<directory> alDir;

    /*
     *  Constructeur d'un nouveau directory avec en parametre le nom du directory, la POA et le chemin avec lequel le directory sera créé
    */
    public directoryImpl(String n, POA poa,String p){
        this.name = n;
        this.path = p+"/"+this.name;
        currentDir = new File(this.path);
        //if(!currentDir.mkdir())System.out.println("Problème mkdir");
        this.poa_ = poa;
        this.name = n;
        alFile =  new ArrayList<regular_file>();
        alDir = new ArrayList<directory>();
        
    }

    public String name(){
	   return this.name;
    }
    public String path(){
        return this.path;
    }

    public int number_of_file(){
	   return this.number_of_file;
    }

    public void init(){
	String[] listeContent = currentDir.list();
	for(int i=0;i<listeContent.length;i++){
	    try{ 
 	    	if(listeContent[i].endsWith(".txt")==true){ 
		    if(!regular_fileExist(name)){
		    	regular_fileImpl newFile = new regular_fileImpl(listeContent[i],this.path);
	    	    	org.omg.CORBA.Object alloc = poa_.servant_to_reference(newFile);
	    	    	regular_file rf = regular_fileHelper.narrow(alloc);
	    	    	alFile.add(rf);
		    }
	    	}else{
		    if(!directoryExist(name)){
		    	directoryImpl newDir = new directoryImpl(listeContent[i],poa_,this.path);
            	    	org.omg.CORBA.Object alloc = poa_.servant_to_reference(newDir);
            	    	directory d = directoryHelper.narrow(alloc);
	    	    	alDir.add(d);
	    	    }
		}
	    }catch(Exception e){
		System.out.println(e);
	    }
	}
    }
    
    public void open_regular_file(regular_fileHolder r, String name, mode m) throws invalid_type_file, no_such_file{
        if(regular_fileExist(name)){
            Iterator<regular_file> iter = alFile.iterator();
            while(iter.hasNext()){
                regular_file rfTmp = iter.next();
                if(rfTmp.name().equals(name)){
                    r.value = rfTmp;
                    r.value.open(m);
                }
            }
        }else if(directoryExist(name)){
            throw new invalid_type_file();
        }else{
            throw new no_such_file();
        }
    }

    public void open_directory(directoryHolder f, String name) throws invalid_type_file, no_such_file{
        if(directoryExist(name)){
            Iterator<directory> iter = alDir.iterator();
            while(iter.hasNext()){
                directory dirTmp = iter.next();
                if(dirTmp.name().equals(name)){
                    f.value = dirTmp;
                }
            }
        }else if(regular_fileExist(name)){
            throw new invalid_type_file();
        }else{
            throw new no_such_file();
        }
    }

    /*
     *  Parcour la liste de regular_fileImpl pour verifier si le fichier existe
    */
    public boolean regular_fileExist(String name){
        Iterator<regular_file> iter = alFile.iterator();
        while(iter.hasNext()){
            if(iter.next().name().equals(name)){
                return true;
            }
        }
        return false;
    }

    /*
     *  Parcour la liste de directoryImpl pour verifier si le directory existe
    */
    public boolean directoryExist(String name){
        Iterator<directory> iter = alDir.iterator();
        while(iter.hasNext()){
            if(iter.next().name().equals(name)){
                return true;
            }
        }
        return false;
    }

    public void create_regular_file(regular_fileHolder r, String name) throws already_exist{
        try{
          
            if(regular_fileExist(name)){ throw new already_exist();}
            if(directoryExist(name)){ throw new already_exist();}
            /*
             *  Création et allocation du nouveau fichier.
            */
            regular_fileImpl newFile = new regular_fileImpl(name,this.path);
            org.omg.CORBA.Object alloc = poa_.servant_to_reference(newFile);
            regular_file rf = regular_fileHelper.narrow(alloc);
	    r.value = rf;
	    alFile.add(rf);

        }catch(Exception e){
            System.out.println(e);
        }
    }

    public void create_directory(directoryHolder f, String name) throws already_exist{
        try{
            if(directoryExist(name)){ throw new already_exist();}
            if(regular_fileExist(name)){ throw new already_exist();}
            /*
             *  Création et allocation du nouveau directory.
            */
            directoryImpl newDir = new directoryImpl(name,poa_,this.path);
            org.omg.CORBA.Object alloc = poa_.servant_to_reference(newDir);
            directory d = directoryHelper.narrow(alloc);
	    f.value = d;
	    alDir.add(d);
        }catch(Exception e){
            System.out.println(e);
        } 
    }

    public void delete_file(String name) throws no_such_file{
        if(regular_fileExist(name)){
            Iterator<regular_file> iter = alFile.iterator();
            while(iter.hasNext()){
                regular_file rfTmp = iter.next();
                if(rfTmp.name().equals(name)){
                    //rfTmp.delete();
                }
            }
        }else if(directoryExist(name)){
            Iterator<directory> iter = alDir.iterator();
            while(iter.hasNext()){
                directory dirTmp = iter.next();
                if(dirTmp.name().equals(name)){
                   // dirTmp.delete();
                }
            }
        }else{
            throw new no_such_file();
        }
    }

    public int list_files(file_listHolder l){
	try{
	    file_listImpl flTmp = new file_listImpl(alFile,alDir);
	    org.omg.CORBA.Object fli = poa_.servant_to_reference(flTmp);
	    l.value = file_listHelper.narrow(fli);
	}catch(Exception e){
            System.out.println(e);
        } 
	return l.value.size();
    }

}
