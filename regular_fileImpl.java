package files;

import org.omg.CORBA.*;
import java.lang.*;


public class regular_fileImpl extends regular_filePOA{

	private String name;
	private int offset;

	public regular_fileImpl(String n){
		name = n;
		offset = 0;

	}

	public String name(){
		return this.name;
	}

	public void open(mode m){
		
	}

	public int read(int size, StringHolder data)throws end_of_file,invalid_operation{
		return 0;
	}

    public int write(int size, String data)throws invalid_operation{
    	return 0;
	}

    public void seek(int new_offset)throws invalid_offset,invalid_operation{

	}

    public void close(){

	}

}
