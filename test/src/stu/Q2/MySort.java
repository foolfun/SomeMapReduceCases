package stu.Q2;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

public class MySort implements WritableComparable<MySort> {
    private  String classname;
    private  String course;

   public MySort() {
       super();
   }

   public MySort(String classname, String course) {
       this.classname = classname;
       this.course = course;

   }

   public String getClassname() {
       return classname;
   }

   public String getCourse() {
       return course;
   }

   @Override
   public String toString() {
       return classname+"\t"+course;
   }

   public void setClassname(String classname) {
       this.classname = classname;
   }

   public void setCourse(String course) {
       this.course = course;
   }

   @Override
   public void write(DataOutput out) throws IOException {
      out.writeUTF(classname);
      out.writeUTF(course);
   }

   @Override
   public void readFields(DataInput in) throws IOException {
      this.classname=in.readUTF();
      this.course=in.readUTF();
   }
   @Override
   public int compareTo(MySort o) {
      int temp=this.getClassname().compareTo(o.getClassname());
      if(temp==0){
          temp=this.getCourse().compareTo(o.getCourse());
      }
      return temp;
   }
}