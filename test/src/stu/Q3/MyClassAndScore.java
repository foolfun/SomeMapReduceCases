package stu.Q3;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

public class MyClassAndScore  implements WritableComparable<MyClassAndScore> {
	   private String classname;
	   private int sum;
	    public String getClassname() {
	        return classname;
	    }
	    public int getSum() {
	        return sum;
	    }
	    public void setClassname(String classname) {
	        this.classname = classname;
	    }
	    public void setSum(int sum) {
	        this.sum = sum;
	    }
	    public MyClassAndScore() {
	        super();
	    }
	    @Override
	    public String toString() {
	        return classname+"\t"+sum;
	    }
	 
	    public MyClassAndScore(String classname, int sum) {
	        this.classname = classname;
	        this.sum = sum;
	    }
	    @Override
	    public void write(DataOutput out) throws IOException {
	     out.writeUTF(classname);
	     out.writeInt(sum);
	    }
	 
	    @Override
	    public void readFields(DataInput in) throws IOException {
	     this.classname=in.readUTF();
	     this.sum=in.readInt();
	    }
	    @Override
	    public int compareTo(MyClassAndScore o) {
	        int temp=this.getClassname().compareTo(o.getClassname());
	        if(temp==0){
	            temp=o.getSum()-this.getSum();
	        }
	        return temp;
	    }
	}