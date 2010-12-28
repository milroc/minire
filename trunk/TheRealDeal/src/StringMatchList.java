import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * 
 * @author Irfan Somani
 *
 */
public class StringMatchList {
	ArrayList<String> matches;
	ArrayList<Integer> indexes;
	ArrayList<Integer> lines;
	ArrayList<String> fileNames;
	

	
	public StringMatchList(ArrayList<String> m, ArrayList<Integer> i, ArrayList<Integer> l) throws Exception{
		matches = m;
		indexes = i;
		lines = l;
	}
	
	public StringMatchList() throws Exception{
		matches = new ArrayList<String>();
		indexes = new ArrayList<Integer>();
		lines = new ArrayList<Integer>();
		fileNames = new ArrayList<String>();
	}
	
	public void add(String m, Integer i, Integer l, String f){
		matches.add(m);
		indexes.add(i);
		lines.add(l);
		fileNames.add(f);
	}
	
	/**
	 * @author Miles McCrocklin
	 * @param var
	 * @return
	 * @throws Exception
	 */
	public StringMatchList union(NodeVal var) throws Exception {
		 StringMatchList result = new StringMatchList();
		 StringMatchList o = var.r;
		 result = this;
		 
			for(int i = 0; i < o.fileNames.size(); i++)
			{
				if(!(result.fileNames.contains(o.fileNames.get(i)) &&
						result.indexes.contains(o.indexes.get(i)) &&
						result.lines.contains(o.lines.get(i)) &&
						result.matches.contains(o.matches.get(i))) )
				{
					result.add(o.matches.get(i), o.indexes.get(i), o.lines.get(i), o.fileNames.get(i));
				}
			}
			return result;

	 }
	
	/**
	  * @author Miles McCrocklin
	  * @param var
	  * @return
	  * @throws Exception
	  */
	 public StringMatchList inters(NodeVal var) throws Exception {

		 StringMatchList result = new StringMatchList();
		 StringMatchList o = var.r;
			for(int i = 0; i < o.fileNames.size(); i++)
			{				
				if((this.fileNames.contains(o.fileNames.get(i)) &&
						this.indexes.contains(o.indexes.get(i)) &&
						this.lines.contains(o.lines.get(i)) &&
						this.matches.contains(o.matches.get(i))) )
				{
					result.add(o.matches.get(i), o.indexes.get(i), o.lines.get(i), o.fileNames.get(i));
				}
				
			}
			return result;
	 }
	 
	 /**
	  * @author Miles McCrocklin
	  * @param var
	  * @return
	  * @throws Exception
	  */
	 public boolean equals(NodeVal var) throws Exception {
		 boolean result = true;
		 StringMatchList o = var.r;
		 if(this.indexes.size() != o.indexes.size()) result = false;
		 for(int i = 0; i < o.fileNames.size(); i++)
		 {				
			 if(!(this.fileNames.contains(o.fileNames.get(i)) &&
					 this.indexes.contains(o.indexes.get(i)) &&
					 this.lines.contains(o.lines.get(i)) &&
					 this.matches.contains(o.matches.get(i))) )
			{
				result = false;
			}
			
		}
			return result;
	 }
	
	/**
	 * @author Miles McCrocklin
	 * @param var
	 * @return
	 * @throws Exception
	 */	
	public String toString(){
		String ret = "";
		for (int x=0; x<this.indexes.size(); x++){
			ret+="{";
			ret+=this.matches.get(x)+",";
			ret+=this.fileNames.get(x)+",";
			ret+=this.lines.get(x)+",";
			ret+=this.indexes.get(x);
			ret+="}";
		}
		
		
		return ret;
	}

	/**
	 * @author Miles McCrocklin
	 * @param o
	 * @return
	 */
	public boolean equals(StringMatchList o)
	{
		boolean val = true;
		for(int i = 0; i < this.fileNames.size(); i++)
		{
			if(!(this.fileNames.get(i).equals(o.fileNames.get(i)) &&
					this.indexes.get(i) == o.indexes.get(i) &&
					this.lines.get(i) == o.lines.get(i) &&
					this.matches.get(i).equals(o.matches.get(i))))
				val = false;
		}
		return val;
	}
	
}
