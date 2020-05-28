package stu.Q2;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class MyClassMapReduce {
    public static class MyMapper extends Mapper<LongWritable,Text,MySort,IntWritable>{
        MySort ms=new MySort();
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] fields = value.toString().split("\t");
            int score=0;
            ms.setClassname(fields[0]);
              for(int i=3;i<fields.length;i++){
                  if(i==3){
                  ms.setCourse("����");
                  score=Integer.parseInt(fields[3]);
                  context.write(ms,new IntWritable(score));
                  }
                  if(i==4){
                      ms.setCourse("��ѧ");
                      score=Integer.parseInt(fields[4]);
                      context.write(ms,new IntWritable(score));
                  }else{
                      ms.setCourse("Ӣ��");
                      score=Integer.parseInt(fields[5]);
                      context.write(ms,new IntWritable(score));
                  }
              }
        }
    }
    public static class MyReducer extends Reducer<MySort,IntWritable,MySort,Text>{
        @Override
        protected void reduce(MySort key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum=0;int count=0;
          for(IntWritable v: values){
              sum+=v.get();
              count++;
          }
          context.write(key,new Text(""+1.0*sum/count));
          System.out.println(key.toString()+"---------"+1.0*sum/count);
 
 
             }
    }
    public static void main(String[] args) {
        Configuration conf =new Configuration();
        System.setProperty("HADOOP_USER_NAME", "zsl");//�������Լ����û���
        conf .set( "fs.defaultFS" , "hdfs://localhost:9000" );//�������Լ��Ľӿ�
        try {
            Job job=Job.getInstance(conf);
            job.setJarByClass(MyClassMapReduce.class);
            job.setMapperClass(MyMapper.class);
            job.setReducerClass(MyReducer.class);
 
            job.setMapOutputKeyClass(MySort.class);
            job.setMapOutputValueClass(IntWritable.class);
 
            job.setOutputKeyClass(MySort.class);
            job.setOutputValueClass(Text.class);
 
            job.setPartitionerClass(MyPartition.class);
            job.setGroupingComparatorClass(MyGrouping.class);
            job.setNumReduceTasks(5);
 
            //ָ����Ҫͳ�Ƶ��ļ�����·��
            Path inpath=new Path("/student/score.txt");//�������Լ��ļ���ַ
            FileInputFormat.addInputPath(job, inpath);
 
            //ָ�����Ŀ¼ ���·�����ܴ��ڣ�����ͻᱨ�� Ĭ���Ǹ���ʽ�����
            Path outpath=new Path("/student/result02");
            if(outpath.getFileSystem(conf).exists(outpath)){
                outpath.getFileSystem(conf).delete(outpath,true);
            }
            FileOutputFormat.setOutputPath(job, outpath);
 
            job.waitForCompletion(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
