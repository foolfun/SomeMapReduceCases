package stu.Q2;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Partitioner;

public class MyPartition  extends Partitioner<MySort, IntWritable> {
    @Override
    public int getPartition(MySort key, IntWritable arg1, int arg2) {
        if(key.getClassname().equals("1303")){
            return 0;
        }
        if(key.getClassname().equals("1304")){
            return 1;
        }
        if(key.getClassname().equals("1305")){
            return 2;
        }
        if(key.getClassname().equals("1306")){
            return 3;
        }else{
            return 4;
        }
    }
}
