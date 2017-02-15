package files;

import org.omg.CORBA.*;
import java.lang.*;
import java.io.*;


public class regular_fileImpl extends regular_filePOA{

	private String name;
	private String path;
	private int offset;
	private File f;
	private FileInputStream fis;
	private FileOutputStream fos;
	private boolean open;

	public regular_fileImpl(String n, String p){
		name = n;
		path = p;
		offset = 0;
		open = false;
		try{
		    f = new File(path+"/"+name);
		    f.createNewFile();
		}catch(IOException e){
		    e.printStackTrace();
		}
	}

	public String name(){
		return this.name;
	}

	public int offset(){
		return this.offset;
	}

	public void open(mode m){
		if(!open){
			try{
				open = true;
				if(m.equals(mode.read_only)){
					fis = new FileInputStream(f);
					offset = 0;
				}else if(m.equals(mode.write_append)){
					fos = new FileOutputStream(f,true);
					offset = (int)f.length();
				}else if(m.equals(mode.write_trunc)){
					fos = new FileOutputStream(f);
					PrintWriter printwriter = new PrintWriter(fos);
					printwriter.close();
					fos = new FileOutputStream(f);
					offset = 0;
				}else if(m.equals(mode.read_write)){
					fis = new FileInputStream(f);
					fos = new FileOutputStream(f);
					offset = 0;
				}	
			}catch(IOException e){
				e.printStackTrace();
			}	
		}
	}

	public int read(int size, StringHolder data)throws end_of_file,invalid_operation{
		if(fis == null)throw new invalid_operation();
		byte[] buf = new byte[1];
		int nbRead = 0;
		int n = 0;
		data.value = "";
		try{
			while((n = fis.read(buf)) >= 0 && nbRead < size){
				data.value += (char) buf[0];
				nbRead++;
				offset++;
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		
		open = false;
		return nbRead;
	}

    	public int write(int size, String data)throws invalid_operation{
		if(fos == null)throw new invalid_operation();
		byte[] buf = data.getBytes();

		try{
			fos.write(buf);
		}catch(IOException e){
			e.printStackTrace();
		}
		
		open = false;
    		return data.length();
	}

    	public void seek(int new_offset)throws invalid_offset,invalid_operation{
		if(new_offset<0 || new_offset > f.length())throw new invalid_offset();
		offset = new_offset;
	}

    	public void close(){
		try{
			if(fis != null)
				fis.close();
		}catch(IOException e){
			e.printStackTrace();
		}
		try{
			if(fos != null)
				fos.close();	
		}catch(IOException e){
			e.printStackTrace();
		}	
	}

    	public void delete(){
		close();
		f.delete();
    	}

}
