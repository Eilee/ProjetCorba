package files;

import org.omg.CORBA.*;
import java.lang.*;
import java.util.*;


public class file_listImpl extends file_listPOA{

	private ArrayList<directory_entry> list_of_directory_entry;
	private int pos;

	public file_listImpl(ArrayList<regular_file> files, ArrayList<directory> directories){
		list_of_directory_entry = new ArrayList<directory_entry>();
		pos = 0;
		
		Iterator<regular_file> iter = files.iterator();
		while(iter.hasNext()){
			regular_file file = iter.next();
			directory_entry de = new directory_entry();
			de.name = file.name();
			de.type = file_type.regular_file_type;
			list_of_directory_entry.add(de);
		}

		Iterator<directory> iter2 = directories.iterator();
		while(iter2.hasNext()){
			directory directory = iter2.next();
			directory_entry de = new directory_entry();
			de.name = directory.name();
			de.type = file_type.directory_type;
			list_of_directory_entry.add(de);
		}
	}

	public boolean next_one(directory_entryHolder e){
		if(pos>=list_of_directory_entry.size())return false;
		e.value = list_of_directory_entry.get(pos);
		pos++;
		return true;
	}

	public int size(){
		return list_of_directory_entry.size();
	}

}
