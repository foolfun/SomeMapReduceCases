package stu.Q1;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

public  class  StudentScore  implements  WritableComparable<StudentScore> {
    private String name;
    private int sum;
    private double avg;
    public StudentScore(){
        super();
    }
    public StudentScore(String name,int sum,Double avg){
        this.name=name;
        this.sum=sum;
        this.avg=avg;
    }
    public int getSum() {
        return sum;
    }
    public double getAvg() {
        return avg;
    }
    public void setSum(int sum) {
        this.sum = sum;
    }
    public void setAvg(double avg) {
        this.avg = avg;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return this.name+"\t"+this.sum+"\t"+this.avg;
    }
 
    @Override
    public void write(DataOutput out) throws IOException {
      out.writeUTF(name);
      out.writeInt(sum);
      out.writeDouble(avg);
    }
 
    @Override
    public void readFields(DataInput in) throws IOException {
       this.name=in.readUTF();
       this.sum=in.readInt();
       this.avg=in.readDouble();
 
    }
    @Override
    public int compareTo(StudentScore o) {
        return o.getSum()-this.getSum(); //根据总分进行倒序排序
    }
}
