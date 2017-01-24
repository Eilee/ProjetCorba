package files;

import org.omg.CORBA.*;
import java.lang.*;



public class regular_fileImpl extends regular_filePOA{

	private String name;
	private String content;
	private int offset;
	private boolean ferme;
	private boolean write;
	private boolean read;

	public regular_fileImpl(String n){
		name = n;
		content = "";
		offset = 0;
		ferme = false;
		write = false;
		read = false;
	}

	public String name(){
		return this.name;
	}

	public void open(mode m){
		if(ferme){
			ferme = false;
			if(m.equals(mode.read_only)){
				read = true;
				write = false;
			}else if(m.equals(mode.write_append)){
				write = true;
				read = false;
			}else if(m.equals(mode.write_trunc)){
				write = true;
				read = false;
			}else if(m.equals(mode.read_write)){
				write = true;
				read = true;
			}		
		}
	}

	public int read(int size, StringHolder data)throws end_of_file,invalid_operation{
		if(ferme)return -1;
		if(false)throw new invalid_operation();
		if((size+offset) > contenu.length() || size < 0)throw new end_of_file();
		data.value = contenu.substring(offset,offset+size);
		offset = offset+size;
		return size;
		
	}

    	public int write(int size, String data)throws invalid_operation{
		if(ferme)return -1;
		if(false)throw new invalid_operation();
		contenu += data;
		return size;
	}

    	public void seek(int new_offset)throws invalid_offset,invalid_operation{
		if(!ferme){
			if(false)throw new invalid_operation();
			if(new_offset > contenu.length())throw new invalid_offset();
			offset = new_offset;
		}
	}

    	public void close(){
		ferme = true;
	}

}
