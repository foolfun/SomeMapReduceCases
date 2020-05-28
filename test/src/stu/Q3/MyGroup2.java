package stu.Q3;


import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;
 
public class MyGroup2 extends WritableComparator {
    public MyGroup2(){
        super(MyClassAndScore.class,true);
    }
 
    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        MyClassAndScore aa=(MyClassAndScore)a;
        MyClassAndScore bb=(MyClassAndScore)b;
        return aa.getClassname().compareTo(bb.getClassname());
    }
}