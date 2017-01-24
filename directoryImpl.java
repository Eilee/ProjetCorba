package files;

import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import java.lang.*;
import java.util.*;

public class directoryImpl extends directoryPOA
{
    protected POA poa_;
    private int number_of_file;
    String name;
    ArrayList<regular_fileImpl> alFile;
    ArrayList<directoryImpl> alDir;

    public directoryImpl(String n, POA poa){
        this.poa_ = poa;
        this.name = n;
        alFile =  new ArrayList<regular_fileImpl>();
        alDir = new ArrayList<directoryImpl>();
    }

    public String name(){
	   return this.name;
    }

    public int number_of_file(){
	   return this.number_of_file;
    }

    public void open_regular_file(regular_fileHolder r, String name, mode m) throws invalid_type_file, no_such_file{

    }

    public void open_directory(directoryHolder f, String name) throws invalid_type_file, no_such_file{

    }

    public void create_regular_file(regular_fileHolder r, String name) throws already_exist{
        try{
            regular_fileImpl newFile = new regular_fileImpl(name);
            if(alFile.contains(newFile)) throw new already_exist();
            org.omg.CORBA.Object alloc = poa_.servant_to_reference(newFile);
            r.value = regular_fileHelper.narrow(alloc);

        }catch(Exception e){
            System.out.println(e);
        }
    }

    public void create_directory(directoryHolder f, String name) throws already_exist{
        try{
            directoryImpl newDir = new directoryImpl(name,poa_);
            if(alDir.contains(newDir)){
                throw new already_exist();
            }
            org.omg.CORBA.Object alloc = poa_.servant_to_reference(newDir);
            f.value = directoryHelper.narrow(alloc);
        }catch(Exception e){
            System.out.println(e);
        } 
    }

    public void delete_file(String name) throws no_such_file{

    }

    public int list_files(file_listHolder l){
	   return 0;
    }

}
