package stu.Q2;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class MyGrouping extends WritableComparator {
    public MyGrouping(){
        super(MySort.class,true);
    }
    @Override
    public int compare(WritableComparable a, WritableComparable b) {
         MySort aa=(MySort)a;
         MySort bb=(MySort)b;
        int  i= aa.getClassname().compareTo(bb.getClassname());
        if(i==0){
            return aa.getCourse().compareTo(bb.getCourse());
        }
        return i;
    }
}