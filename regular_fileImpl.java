import org.omg.CORBA.*;
import java.lang.*;


public class regular_fileImpl extends regular_filePOA
{
	int current_offset;
	String name;
	
	public regular_fileImpl(String n){
		this.name = n;
	}
	public int read(int size, StringHolder data) throws end_of_file,invalid_operation{

	}

    public int write(int size,String data) throws invalid_operation{

    }

    public void seek(int new_offset)throws invalid_offset,invalid_operation{

    }

    public void close(){

    }

}
